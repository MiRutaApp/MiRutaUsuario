package com.tesis.ulima.miruta.model;

import com.parse.ParseGeoPoint;

/**
 * Created by Christian on 7/13/2016.
 */
public class Unidad {
    String objectId, nombre,choferId,rutaId;
    int capacidad,maxCapacidad,estado;
    ParseGeoPoint posicion;

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

    public String getChoferId() {
        return choferId;
    }

    public void setChoferId(String choferId) {
        this.choferId = choferId;
    }

    public String getRutaId() {
        return rutaId;
    }

    public void setRutaId(String rutaId) {
        this.rutaId = rutaId;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getMaxCapacidad() {
        return maxCapacidad;
    }

    public void setMaxCapacidad(int maxCapacidad) {
        this.maxCapacidad = maxCapacidad;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public ParseGeoPoint getPosicion() {
        return posicion;
    }

    public void setPosicion(ParseGeoPoint posicion) {
        this.posicion = posicion;
    }
}
