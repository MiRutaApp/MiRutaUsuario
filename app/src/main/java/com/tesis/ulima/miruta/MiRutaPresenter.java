package com.tesis.ulima.miruta;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.tesis.ulima.miruta.data.MiRutaRepository;
import com.tesis.ulima.miruta.model.Ruta;

import java.util.List;

/**
 * Created by Christian on 6/11/2016.
 */
public class MiRutaPresenter implements MiRutaContract.Request {
    private MiRutaRepository mMiRutaRepository;
    private MiRutaContract.FetchUser mFetchUser;
    private MiRutaContract.FetchRutas mFetchRutas;
    public MiRutaPresenter(MiRutaContract.FetchUser fetchUser,MiRutaRepository miRutaRepository) {
        mFetchUser = fetchUser;
        mMiRutaRepository=miRutaRepository;
    }

    public MiRutaPresenter(MiRutaContract.FetchRutas fetchRutas,MiRutaRepository miRutaRepository) {
        mFetchRutas = fetchRutas;
        mMiRutaRepository=miRutaRepository;
    }

    @Override
    public void requestParseUser(String username, String password) {
        mMiRutaRepository.fetchUserLogin(new MiRutaRepository.FetchUserLoginCallback() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                mFetchUser.fetchUser(parseUser);
            }

            @Override
            public void onError(String error) {
                mFetchUser.showRequestError(error);
            }
        },username,password);
    }

    @Override
    public void requestEmpresas() {
        mMiRutaRepository.fetchEmpresas(new MiRutaRepository.FetchEmpresasCallback() {
            @Override
            public void onSuccess(List<ParseObject> empresas) {
                mFetchRutas.fetchEmpresas(empresas);
            }

            @Override
            public void onError(String error) {
                mFetchRutas.showRequestError(error);
            }
        });
    }

    @Override
    public void requestRutas() {
        mMiRutaRepository.fetchRutas(new MiRutaRepository.FetchRutasCallback() {
            @Override
            public void onSuccess(List<ParseObject> rutas) {
                mFetchRutas.fetchRutas(rutas);
            }

            @Override
            public void onError(String error) {
                mFetchRutas.showRequestError(error);
            }
        });
    }

    @Override
    public void requestBuses(final ParseObject ruta) {
        mMiRutaRepository.fetchBuses(new MiRutaRepository.FetchBusesCallback() {
            @Override
            public void onSuccess(List<ParseObject> buses, String objectId) {
                mFetchRutas.fetchBuses(buses,objectId);
            }

            @Override
            public void onError(String error) {

            }
        },ruta);
    }
}
