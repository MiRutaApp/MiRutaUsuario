package com.tesis.ulima.miruta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appeaser.sublimenavigationviewlibrary.OnNavigationMenuEventListener;
import com.appeaser.sublimenavigationviewlibrary.SublimeBaseMenuItem;
import com.appeaser.sublimenavigationviewlibrary.SublimeNavigationView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.tesis.ulima.miruta.model.Empresa;
import com.tesis.ulima.miruta.model.Ruta;
import com.tesis.ulima.miruta.model.Unidad;
import com.tesis.ulima.miruta.prod.Injection;
import com.tesis.ulima.miruta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MiRutaContract.FetchRutas {
    public static final String TAG = "MapsActivity";
    private static GoogleMap mMap;
    private static Polyline polylineFinal;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng latLng;
    private MiRutaContract.Request mRequest;
    private int finalRequest = 0;
    private boolean terminadoBuses;
    private boolean terminadoPoly;

    private LinearLayout linear_perfil;
    private TextView linear_perfil_name;

    @BindView(R.id.navigation_view)
    SublimeNavigationView snv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mRequest == null) {
            mRequest = new MiRutaPresenter(this, Injection.provideMiRutaRepository());
        }
        mRequest.requestRutas();
        mRequest.requestEmpresas();
        linear_perfil = (LinearLayout) snv.getHeaderView().findViewById(R.id.linear_perfil);
        linear_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapsActivity.this,ProfileActivity.class);
                startActivity(intent);
                Log.d(TAG,"linear_perfil clicked");
            }
        });
        linear_perfil_name=(TextView)snv.getHeaderView().findViewById(R.id.tvNamePlate);
        linear_perfil_name.setText(ParseUser.getCurrentUser().getUsername());
        snv.setNavigationMenuEventListener(new OnNavigationMenuEventListener() {
            @Override
            public boolean onNavigationMenuEvent(OnNavigationMenuEventListener.Event event,
                                                 SublimeBaseMenuItem menuItem) {
                switch (event) {
                    case CHECKED:
                        if(R.id.checkbox_item_1==menuItem.getItemId()){
                            for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
                                ruta.getValue().setState(true);
                            }
                        }
                        populateMap();
                        Log.i(TAG, "Item checked");
                        break;
                    case UNCHECKED:
                        if(R.id.checkbox_item_1==menuItem.getItemId()){
                            for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
                                ruta.getValue().setState(false);
                            }
                        }
                        populateMap();
                        Log.i(TAG, "Item unchecked");
                        break;
                    case GROUP_EXPANDED:
                        Log.i(TAG, "Group expanded");
                        break;
                    case GROUP_COLLAPSED:
                        Log.i(TAG, "Group collapsed");
                        break;
                    /*default:
                        menuItem.setChecked(!menuItem.isChecked());
                        break;*/
                }
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.text_item_1:
                        intent= new Intent(MapsActivity.this,MisRutasActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.text_item_2:
                        intent= new Intent(MapsActivity.this,EmpresasActivity.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //noinspection MissingPermission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        mMap.setMyLocationEnabled(true);

        //noinspection MissingPermission
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d(TAG, "getLastKnownLocation " + location.getLatitude() + " " + location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        mMap.moveCamera(cameraUpdate);
        //noinspection MissingPermission
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(Utils.rutasParaderos.containsKey(marker)) {
                    Intent intent = new Intent(MapsActivity.this, SeguirRutaActivity.class);
                    intent.putExtra("id", Utils.rutasParaderos.get(marker));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void fetchRutas(List<ParseObject> rutas) {
        String last="";
        Log.d(TAG,"rutas size: "+rutas.size());
        for (ParseObject parseObject : rutas) {
            Ruta ruta = new Ruta();
            mRequest.requestBuses(parseObject);
            ruta.setNombre(parseObject.getString("nombre"));
            ruta.setCamino((ArrayList<ParseGeoPoint>) parseObject.get("camino"));
            ruta.setObjectId(parseObject.getObjectId());
            String first="";
            try {
                first=parseObject.getParseObject("empresa").fetchIfNeeded().getString("nombre");
                ruta.setEmpresaId(parseObject.getParseObject("empresa").fetchIfNeeded().getObjectId());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(first.equals(last)){
                Utils.empresaRutas.get(last).add(ruta);
            }else {
                Utils.nombreEmpresas.add(first);
                List<Ruta> dummy=new ArrayList<>();
                dummy.add(ruta);
                Utils.empresaRutas.put(first,dummy);
                Utils.empresaRutas.put(first,dummy);
                last=first;
            }
            Utils.rutas.put(parseObject.getObjectId(), ruta);
            fetchSnapRoad((ArrayList<ParseGeoPoint>) parseObject.get("camino"),
                    parseObject.getObjectId());
        }
    }

    @Override
    public void fetchBuses(List<ParseObject> buses, String rutaId) {
        List<Unidad> unidadList=new ArrayList<>();
        for (ParseObject parseObject : buses) {
            Unidad unidad=new Unidad();
            unidad.setPosicion((ParseGeoPoint) parseObject.get("posicion"));
            unidad.setNombre(parseObject.getString("nombre"));
            unidad.setCapacidad(parseObject.getInt("capacidad"));
            unidad.setEstado(parseObject.getInt("estado"));
            unidad.setMaxCapacidad(parseObject.getInt("maxcapacidad"));
            unidad.setObjectId(parseObject.getObjectId());
            unidad.setRutaId(rutaId);
            try {
                unidad.setChoferId(parseObject.getParseObject("chofer").fetchIfNeeded().getObjectId());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            unidadList.add(unidad);
        }
        Utils.rutasUnidad.put(rutaId,unidadList);
        if(Utils.rutas.size()==Utils.rutasUnidad.size()){
            terminadoBuses=true;
            if(terminadoPoly){
                populateMap();
            }
        }
    }

    @Override
    public void fetchEmpresas(List<ParseObject> empresas) {
        Log.d(TAG,"empresas size: "+empresas.size());
        for (ParseObject parseObject:empresas){
            Empresa empresa= new Empresa();
            empresa.setObjectId(parseObject.getObjectId());
            empresa.setNombre(parseObject.getString("nombre"));
            Utils.empresas.put(parseObject.getObjectId(),empresa);
        }
    }

    private void fetchSnapRoad(final ArrayList<ParseGeoPoint> points, final String objectId) {
        Log.d("fetchSnapRoad", "Making the call");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String puntos = "";
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                puntos = puntos.concat("origin=" + points.get(0).getLatitude() + "," + points.get(0).getLongitude() + "&waypoints=");
            } else if (i == points.size() - 1) {
                puntos = puntos.concat("&destination=" +
                        points.get(i).getLatitude() + "," + points.get(i).getLongitude());
            } else if (i == points.size() - 2) {
                puntos = puntos.concat(points.get(i).getLatitude() + "," + points.get(i).getLongitude());
            } else
                puntos = puntos.concat(points.get(i).getLatitude() + "," + points.get(i).getLongitude() + "|");
        }
        Log.d("puntos", puntos);
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + puntos +
                "&sensor=false&units=metric";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String encodedLine = response.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");
                            Utils.rutas.get(objectId).setEncodedLine(encodedLine);
                            finalRequest++;
                            if(finalRequest==Utils.rutas.size()){
                                terminadoPoly=true;
                                if(terminadoBuses){
                                    populateMap();
                                }
                            }
                            Log.d(TAG,"count: "+finalRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });
        requestQueue.add(jsObjRequest);
    }


    public static void populateMap() {
        Log.d("populateMap", "making before call");
        /*if(polylineFinal!=null){
            polylineFinal.remove();
            Log.d(TAG, "removing polylines");
        }*/
        mMap.clear();
        int max= Utils.rutas.size();
        float flo=(float)360/(max+1);
        float flo2=(float)360/(max+1);
        for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
            if(ruta.getValue().isState()) {
                for (ParseGeoPoint parseGeoPoint : ruta.getValue().getCamino()) {
                    LatLng latLng = new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop));
                    markerOptions.title(ruta.getValue().getNombre());
                    Utils.rutasParaderos.put(mMap.addMarker(markerOptions),ruta.getValue().getObjectId());
                }
                for(Unidad unidad:Utils.rutasUnidad.get(ruta.getValue().getObjectId())){
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng=new LatLng(unidad.getPosicion().getLatitude(),unidad.getPosicion().getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                    markerOptions.title(ruta.getValue().getNombre());
                    markerOptions.snippet("Capacidad de: "+String.valueOf(unidad.getMaxCapacidad()));
                    Utils.rutasParaderos.put(mMap.addMarker(markerOptions),ruta.getValue().getObjectId());
                }
                PolylineOptions polylineOptions = new PolylineOptions().geodesic(true);
                polylineOptions.color(Color.HSVToColor(190, new float[]{flo, 0.7f, 0.8f})).width(15);
                List<LatLng> list = PolyUtil.decode(ruta.getValue().getEncodedLine());
                polylineOptions.addAll(list);
                mMap.addPolyline(polylineOptions);
                flo = flo + flo2;
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (locationManager != null) {
            //noinspection MissingPermission
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }*/
    }

    @Override
    public void showRequestError(String error) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
