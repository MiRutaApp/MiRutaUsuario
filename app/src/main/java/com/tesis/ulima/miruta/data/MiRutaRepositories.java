package com.tesis.ulima.miruta.data;

import android.support.annotation.NonNull;

/**
 * Created by Christian on 6/11/2016.
 */
public class MiRutaRepositories {

    public static MiRutaRepository sMiRutaRepository = null;
    public static MiRutaRepository getMiRutaRepository(@NonNull MiRutaApiService apiService){
        if(sMiRutaRepository==null){
            sMiRutaRepository= new MiRutaRepositoryImpl(apiService);
        }
        return sMiRutaRepository;
    }

}
