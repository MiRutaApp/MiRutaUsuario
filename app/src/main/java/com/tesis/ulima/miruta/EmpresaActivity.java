package com.tesis.ulima.miruta;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmpresaActivity extends AppCompatActivity {
    @BindView(R.id.empresa_recycler)
    RecyclerView empresa_recycler;
    @BindView(R.id.profile_pic_imageview)
    ImageView profile_pic_imageview;
    @BindView(R.id.profile_empresa_name)
    TextView profile_empresa_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null) {
            ActionBar ab=getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
        }
        String empresa=getIntent().getStringExtra("nombre");
        EmpresaAdapter adapter= new EmpresaAdapter(EmpresaActivity.this,empresa);
        empresa_recycler.setLayoutManager(new LinearLayoutManager(this));
        empresa_recycler.setAdapter(adapter);
        profile_empresa_name.setText(empresa);

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
}
