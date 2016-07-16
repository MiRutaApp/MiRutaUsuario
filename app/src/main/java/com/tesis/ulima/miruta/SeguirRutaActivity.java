package com.tesis.ulima.miruta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesis.ulima.miruta.model.Empresa;
import com.tesis.ulima.miruta.model.Ruta;
import com.tesis.ulima.miruta.utils.Utils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeguirRutaActivity extends AppCompatActivity {
    @BindView(R.id.profile_pic_imageview)
    ImageView profile_pic_imageview;
    @BindView(R.id.profile_empresa_name)
    TextView profile_empresa_name;
    @BindView(R.id.ruta_name)
    TextView ruta_name;
    @BindView(R.id.buses_cantidad)
    TextView buses_cantidad;
    @BindView(R.id.route_image)
    ImageView route_image;
    @BindView(R.id.button)
    Button button;
    String rutaId;
    Ruta ruta;
    Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguir_ruta);
        ButterKnife.bind(this);
        rutaId=getIntent().getStringExtra("id");
        ruta= Utils.rutas.get(rutaId);
        empresa=Utils.empresas.get(ruta.getEmpresaId());
        profile_empresa_name.setText(empresa.getNombre());
        ruta_name.setText(ruta.getNombre());
        buses_cantidad.setText(String.valueOf("Buses totales: "+Utils.rutasUnidad.get(rutaId).size()));
        String route = "https://maps.googleapis.com/maps/api/staticmap?size=600x600&path=weight:5%7Ccolor:blue%7Cenc:" +
                Utils.rutas.get(rutaId).getEncodedLine();
        Picasso.with(SeguirRutaActivity.this).load(route).into(route_image);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
                    ruta.getValue().setState(false);
                }
                Utils.rutas.get(rutaId).setState(true);
                MapsActivity.populateMap();
                finish();
            }
        });

    }
}
