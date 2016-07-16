package com.tesis.ulima.miruta;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.tesis.ulima.miruta.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmpresasActivity extends AppCompatActivity {
    @BindView(R.id.empresas_recycler)
    RecyclerView empresas_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null) {
            ActionBar ab=getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
        }
        EmpresasAdapter adapter= new EmpresasAdapter(EmpresasActivity.this);
        empresas_recycler.setLayoutManager(new LinearLayoutManager(this));
        empresas_recycler.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.fin){
        finish();
            Utils.fin=false;
        }
    }
}
