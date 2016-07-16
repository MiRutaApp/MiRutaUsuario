package com.tesis.ulima.miruta.data;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Christian on 6/11/2016.
 */
public class MiRutaRepositoryImpl implements MiRutaRepository {
    MiRutaApiService mApiService;

    public MiRutaRepositoryImpl(MiRutaApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public void fetchUserLogin(final FetchUserLoginCallback callback,String username,String password) {
        mApiService.fetchUserLogin(new MiRutaApiService.FetchUserLoginApiCallback<ParseUser>() {
            @Override
            public void onLoaded(ParseUser parseUser) {
                callback.onSuccess(parseUser);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        },username,password);
    }

    @Override
    public void fetchRutas(final FetchRutasCallback callback) {
        mApiService.fetchRutas(new MiRutaApiService.FetchRutasApiCallback() {
            @Override
            public void onLoaded(List<ParseObject> rutas) {
                callback.onSuccess(rutas);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void fetchEmpresas(final FetchEmpresasCallback callback) {
        mApiService.fetchEmpresas(new MiRutaApiService.FetchEmpresasApiCallback() {
            @Override
            public void onLoaded(List<ParseObject> empresas) {
                callback.onSuccess(empresas);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void fetchBuses(final FetchBusesCallback callback, ParseObject ruta) {
        mApiService.fetchBuses(new MiRutaApiService.FetchBusesApiCallback() {
            @Override
            public void onLoaded(List<ParseObject> buses, String objectId) {
              callback.onSuccess(buses,objectId);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        },ruta);
    }
}
