package com.tesis.ulima.miruta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(ParseUser.getCurrentUser()!=null){
            Log.i("SplashActivity","ParseUser not null");
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        else {Log.i("SplashActivity","ParseUser null");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();

    }
}
