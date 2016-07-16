package com.tesis.ulima.miruta.data;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.tesis.ulima.miruta.model.Ruta;

import java.util.List;

/**
 * Created by Christian on 6/11/2016.
 */
public interface MiRutaRepository {

    interface FetchUserLoginCallback{
        void onSuccess(ParseUser parseUser);
        void onError(String error);
    }
    void fetchUserLogin(FetchUserLoginCallback callback,String username,String password);

    interface FetchRutasCallback{
        void onSuccess(List<ParseObject> rutas);
        void onError(String error);
    }

    void fetchRutas(FetchRutasCallback callback);

    interface FetchEmpresasCallback{
        void onSuccess(List<ParseObject> empresas);
        void onError(String error);
    }
    void fetchEmpresas(FetchEmpresasCallback callback);

    interface FetchBusesCallback{
        void onSuccess(List<ParseObject> buses, String objectId);
        void onError(String error);
    }
    void fetchBuses(FetchBusesCallback callback, ParseObject ruta);
}
