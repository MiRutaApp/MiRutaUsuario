package com.tesis.ulima.miruta.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Christian on 6/22/2016.
 */
public class Ruta{
    String nombre, empresaId, objectId;
    ArrayList<ParseGeoPoint> camino;
    String encodedLine;
    boolean state =true;

    public String getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(String encodedLine) {
        this.encodedLine = encodedLine;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public ArrayList<ParseGeoPoint> getCamino() {
        return camino;
    }

    public void setCamino(ArrayList<ParseGeoPoint> camino) {
        this.camino = camino;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
