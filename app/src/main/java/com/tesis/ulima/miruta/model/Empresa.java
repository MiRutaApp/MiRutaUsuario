package com.tesis.ulima.miruta.model;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Christian on 6/29/2016.
 */
public class Empresa {
    String objectId,nombre, foto,rutaId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRutaId() {
        return rutaId;
    }

    public void setRutaId(String rutaId) {
        this.rutaId = rutaId;
    }
}
