package com.tesis.ulima.miruta;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by gonzalo on 12/23/2015.
 */
public class GetGeoFromLocation extends AsyncTask<LatLng, Void, Void> {
    Callback callback;
    Geocoder geocoder;
    List<Address> matches = null;

    public GetGeoFromLocation(Geocoder geocoder,Callback callback){
        this.geocoder =  geocoder;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(LatLng... latlng) {
        try {
            matches = geocoder.getFromLocation(latlng[0].latitude, latlng[0].longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        callback.onResult(matches);
    }
    public interface Callback{
        void onResult(List<Address> matches);
    }
}