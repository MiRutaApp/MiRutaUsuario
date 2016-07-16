package com.tesis.ulima.miruta.prod;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tesis.ulima.miruta.data.MiRutaApiService;

import java.util.List;

/**
 * Created by Christian on 6/11/2016.
 */
public class MiRutaApiClient implements MiRutaApiService{
    static MiRutaApiClient mApiClient;
    public static MiRutaApiClient getInstance(){
        if(mApiClient==null){
            mApiClient= new MiRutaApiClient();
        }
        return mApiClient;
    }

    @Override
    public void fetchUserLogin(final FetchUserLoginApiCallback<ParseUser> callback, String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    if(user!=null){
                        callback.onLoaded(user);
                    }else callback.onError("User is null");
                }else callback.onError(e.getMessage());
            }
        });
    }

    @Override
    public void fetchRutas(final FetchRutasApiCallback callback) {
        ParseQuery<ParseObject> rutasQuery= ParseQuery.getQuery("Ruta");
        rutasQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    callback.onLoaded(objects);
                }else {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void fetchEmpresas(final FetchEmpresasApiCallback callback) {
        ParseQuery<ParseObject> empresasQuery=ParseQuery.getQuery("Empresa");
        empresasQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    callback.onLoaded(objects);
                }else callback.onError(e.getMessage());
            }
        });
    }

    @Override
    public void fetchBuses(final FetchBusesApiCallback callback, final ParseObject ruta) {
        ParseQuery<ParseObject> busesQuery= ParseQuery.getQuery("Unidad");
        busesQuery.whereEqualTo("ruta",ruta);
        busesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    callback.onLoaded(objects,ruta.getObjectId());
                }else {
                    callback.onError(e.getMessage());
                }
            }
        });
    }
}
