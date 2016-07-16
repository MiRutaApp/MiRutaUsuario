package com.tesis.ulima.miruta.prod;

import com.tesis.ulima.miruta.data.MiRutaRepositories;
import com.tesis.ulima.miruta.data.MiRutaRepository;

/**
 * Created by Christian on 6/11/2016.
 */
public class Injection {

    public static MiRutaRepository provideMiRutaRepository(){
        return MiRutaRepositories.getMiRutaRepository(MiRutaApiClient.getInstance());
    }
}
