package com.tesis.ulima.miruta;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.tesis.ulima.miruta.model.Ruta;

import java.util.List;

/**
 * Created by Christian on 6/11/2016.
 */
public class MiRutaContract {
    public interface Request{
        void requestParseUser(String username, String password);
        void requestEmpresas();
        void requestRutas();
        void requestBuses(ParseObject ruta);
    }

    public interface FetchUser{
        void fetchUser(ParseUser parseUser);

        void showRequestError(String error);
    }

    public interface FetchRutas{
        void fetchRutas(List<ParseObject> rutas);
        void fetchBuses(List<ParseObject> buses,String rutaId);
        void fetchEmpresas(List<ParseObject> empresas);

        void showRequestError(String error);
    }


}
