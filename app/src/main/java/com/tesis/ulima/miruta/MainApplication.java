package com.tesis.ulima.miruta;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Christian on 6/9/2016.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("449e9382040418fff7bd75dfae5c6a7260abbc69")
                .server("http://miruta.frikicorp.com/parse/")
                .build());
    }
}
