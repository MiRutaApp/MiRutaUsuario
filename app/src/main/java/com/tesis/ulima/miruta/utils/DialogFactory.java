package com.tesis.ulima.miruta.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.tesis.ulima.miruta.R;


/**
 * Created by Christian on 5/21/2016.
 */
public class DialogFactory {
    public static Snackbar showErrorSnackBar(Activity mContext, View rootView, String error){
        String message= "Ocurrio un error";
        if(error!=null){
            message= error;
        }
        String output = message.substring(0, 1).toUpperCase() + message.substring(1);
        Snackbar snackbar_error= Snackbar.make(rootView,output, Snackbar.LENGTH_LONG);
        View view= snackbar_error.getView();
        TextView tv=(TextView)view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.material_red));

        return snackbar_error;
    }
}
