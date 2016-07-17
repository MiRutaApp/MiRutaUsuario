package com.tesis.ulima.miruta;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appeaser.sublimenavigationviewlibrary.OnNavigationMenuEventListener;
import com.appeaser.sublimenavigationviewlibrary.SublimeBaseMenuItem;
import com.appeaser.sublimenavigationviewlibrary.SublimeNavigationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MiRutaContract.FetchRutas, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "MapsActivity";
    private static GoogleMap mMap;
    private static Polyline polylineFinal;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationRequest locationRequest;
    private LatLng latLng;
    private MiRutaContract.Request mRequest;
    private int finalRequest = 0;
    private boolean terminadoBuses;
    private boolean terminadoPoly;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private static final LatLngBounds PERU = new LatLngBounds(
            new LatLng(-12.5, -78), new LatLng(-11, -76));

    private boolean found = false;

    private LinearLayout linear_perfil;
    private TextView linear_perfil_name;

    @BindView(R.id.autocomplete_places)
    AutoCompleteTextView autocomplete_places;
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
                Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
                Log.d(TAG, "linear_perfil clicked");
            }
        });
        linear_perfil_name = (TextView) snv.getHeaderView().findViewById(R.id.tvNamePlate);
        linear_perfil_name.setText(ParseUser.getCurrentUser().getUsername());
        snv.setNavigationMenuEventListener(new OnNavigationMenuEventListener() {
            @Override
            public boolean onNavigationMenuEvent(OnNavigationMenuEventListener.Event event,
                                                 SublimeBaseMenuItem menuItem) {
                switch (event) {
                    case CHECKED:
                        if (R.id.checkbox_item_1 == menuItem.getItemId()) {
                            for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
                                ruta.getValue().setState(true);
                            }
                        }
                        populateMap();
                        Log.i(TAG, "Item checked");
                        break;
                    case UNCHECKED:
                        if (R.id.checkbox_item_1 == menuItem.getItemId()) {
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
                        intent = new Intent(MapsActivity.this, MisRutasActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.text_item_2:
                        intent = new Intent(MapsActivity.this, EmpresasActivity.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

    }

    private AdapterView.OnItemClickListener autocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            autocomplete_places.requestFocus();
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(autocomplete_places.getWindowToken(), 0);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Dirigiendolo hacia: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private ResultCallback<PlaceBuffer> updatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17);
            mMap.moveCamera(cameraUpdate);
            Snackbar.make(findViewById(R.id.map), "Escoge un bus o paradero para seguir la ruta", Snackbar.LENGTH_LONG).setAction("Deshacer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "setUpMap onClick");
                }
            }).show();
            places.release();
        }
    };

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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (Utils.rutasParaderos.containsKey(marker)) {
                    Intent intent = new Intent(MapsActivity.this, SeguirRutaActivity.class);
                    intent.putExtra("id", Utils.rutasParaderos.get(marker));
                    Utils.mMarker = marker;
                    startActivity(intent);
                }
            }
        });

        autocomplete_places.setOnItemClickListener(autocompleteClickListener);
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).enableAutoManage(this, 0, this)
                .addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in millisecond
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17);
                mMap.moveCamera(cameraUpdate);
            }
        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            final String message = "Por favor, habilite su servicio de GPS";

            builder.setMessage(message)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    startActivity(new Intent(action));
                                    d.dismiss();
                                }
                            })
                    .setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    d.cancel();
                                    finish();
                                }
                            });
            builder.create().show();
        }

    }

    @Override
    public void fetchRutas(List<ParseObject> rutas) {
        String last = "";
        Log.d(TAG, "rutas size: " + rutas.size());
        for (ParseObject parseObject : rutas) {
            Ruta ruta = new Ruta();
            mRequest.requestBuses(parseObject);
            ruta.setNombre(parseObject.getString("nombre"));
            ruta.setCamino((ArrayList<ParseGeoPoint>) parseObject.get("camino"));
            ruta.setObjectId(parseObject.getObjectId());
            String first = "";
            try {
                first = parseObject.getParseObject("empresa").fetchIfNeeded().getString("nombre");
                ruta.setEmpresaId(parseObject.getParseObject("empresa").fetchIfNeeded().getObjectId());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (first.equals(last)) {
                Utils.empresaRutas.get(last).add(ruta);
            } else {
                Utils.nombreEmpresas.add(first);
                List<Ruta> dummy = new ArrayList<>();
                dummy.add(ruta);
                Utils.empresaRutas.put(first, dummy);
                Utils.empresaRutas.put(first, dummy);
                last = first;
            }
            Utils.rutas.put(parseObject.getObjectId(), ruta);
            fetchSnapRoad((ArrayList<ParseGeoPoint>) parseObject.get("camino"),
                    parseObject.getObjectId());
        }
    }

    @Override
    public void fetchBuses(List<ParseObject> buses, String rutaId) {
        List<Unidad> unidadList = new ArrayList<>();
        for (ParseObject parseObject : buses) {
            Unidad unidad = new Unidad();
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
        Utils.rutasUnidad.put(rutaId, unidadList);
        if (Utils.rutas.size() == Utils.rutasUnidad.size()) {
            terminadoBuses = true;
            if (terminadoPoly) {
                populateMap();
            }
        }
    }

    @Override
    public void fetchEmpresas(List<ParseObject> empresas) {
        Log.d(TAG, "empresas size: " + empresas.size());
        for (ParseObject parseObject : empresas) {
            Empresa empresa = new Empresa();
            empresa.setObjectId(parseObject.getObjectId());
            empresa.setNombre(parseObject.getString("nombre"));
            Utils.empresas.put(parseObject.getObjectId(), empresa);
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
                            if (finalRequest == Utils.rutas.size()) {
                                terminadoPoly = true;
                                if (terminadoBuses) {
                                    populateMap();
                                }
                            }
                            Log.d(TAG, "count: " + finalRequest);
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
        mMap.clear();
        int max = Utils.rutas.size();
        float flo = (float) 360 / (max + 1);
        float flo2 = (float) 360 / (max + 1);
        for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
            if (ruta.getValue().isState()) {
                for (ParseGeoPoint parseGeoPoint : ruta.getValue().getCamino()) {
                    LatLng latLng = new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busstop));
                    markerOptions.title(ruta.getValue().getNombre());
                    Utils.rutasParaderos.put(mMap.addMarker(markerOptions), ruta.getValue().getObjectId());
                }
                for (Unidad unidad : Utils.rutasUnidad.get(ruta.getValue().getObjectId())) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(unidad.getPosicion().getLatitude(), unidad.getPosicion().getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                    markerOptions.title(ruta.getValue().getNombre());
                    markerOptions.snippet("Capacidad de: " + String.valueOf(unidad.getMaxCapacidad()));
                    Utils.rutasParaderos.put(mMap.addMarker(markerOptions), ruta.getValue().getObjectId());
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
        Log.d(TAG, "onResume");

    }

    @Override
    public void showRequestError(String error) {
        Log.e(TAG, error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, PERU,
                    null);
            autocomplete_places.setAdapter(placeAutocompleteAdapter);

            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location == null) {
                try {
                    int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
                    }
                } catch (Exception e) {
                    Log.e("Mapaerror", e.getMessage());
                }
            } else if (Utils.mMarker != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(Utils.mMarker.getPosition(), 17);
                mMap.moveCamera(cameraUpdate);
                Utils.mMarker = null;
            } else {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17);
                mMap.moveCamera(cameraUpdate);
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
