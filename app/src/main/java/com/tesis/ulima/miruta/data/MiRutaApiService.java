package com.tesis.ulima.miruta.data;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Christian on 6/11/2016.
 */
public interface MiRutaApiService {

    interface FetchUserLoginApiCallback<T>{
        void onLoaded(T parseUser);
        void onError(String error);
    }
    void fetchUserLogin(FetchUserLoginApiCallback<ParseUser> callback, String username,String password);

    interface FetchRutasApiCallback{
        void onLoaded(List<ParseObject> rutas);
        void onError(String error);
    }

    void fetchRutas(FetchRutasApiCallback callback);

    interface FetchEmpresasApiCallback{
        void onLoaded(List<ParseObject> empresas);
        void onError(String error);
    }
    void fetchEmpresas(FetchEmpresasApiCallback callback);

    interface FetchBusesApiCallback{
        void onLoaded(List<ParseObject> buses, String objectId);
        void onError(String error);
    }
    void fetchBuses(FetchBusesApiCallback callback, ParseObject ruta);
}
