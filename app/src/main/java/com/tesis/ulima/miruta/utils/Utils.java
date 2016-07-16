package com.tesis.ulima.miruta.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.tesis.ulima.miruta.model.Empresa;
import com.tesis.ulima.miruta.model.Ruta;
import com.tesis.ulima.miruta.model.Unidad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Christian on 6/29/2016.
 */
public class Utils {
    public static HashMap<String,Empresa>empresas=new HashMap<>();                 //<empresaId,Empresa>
    public static List<String> nombreEmpresas =new ArrayList<>();
    public static HashMap<String,List<Ruta>> empresaRutas =new HashMap<>();  //<nombre,lista<Ruta>>
    public static HashMap<String,Ruta> rutas=new HashMap<>();               //<rutaId,Ruta>
    public static HashMap<String,List<Unidad>> rutasUnidad =new HashMap<>();  //<rutaid,list<Unidad>>
    public static HashMap<Marker,String> rutasParaderos=new HashMap<>();      //Marker,rutaId
    public static boolean fin;
}
