package com.tesis.ulima.miruta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.tesis.ulima.miruta.prod.Injection;
import com.tesis.ulima.miruta.utils.DialogFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements MiRutaContract.FetchUser {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.login_username)
    AppCompatEditText login_username;
    @BindView(R.id.login_password)
    AppCompatEditText login_password;
    @BindView(R.id.login_ingresar)
    AppCompatButton login_ingresar;

    LoginActivity mContext;

    MiRutaContract.Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d(TAG,"onCreate");
        mContext=LoginActivity.this;
        if(mRequest==null){
            mRequest=new MiRutaPresenter(this, Injection.provideMiRutaRepository());
        }
        if(ParseUser.getCurrentUser()!=null) {
            Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(i);
        }
        login_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequest.requestParseUser(login_username.getText().toString(), login_password.getText().toString());
            }
        });

    }

    @Override
    public void fetchUser(ParseUser parseUser) {
        Intent i = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    public void showRequestError(String error) {
        DialogFactory.showErrorSnackBar(mContext,mainLinear,error).show();
        Log.e(TAG,error);
    }

}
