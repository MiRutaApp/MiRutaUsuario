package com.tesis.ulima.miruta;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.tesis.ulima.miruta.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MisRutasActivity extends AppCompatActivity {
    private static final String TAG = "MisRutasActivity";
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    private MisRutasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_rutas);
        ButterKnife.bind(this);
        if(getSupportActionBar()!=null) {
            ActionBar ab=getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
        }

        adapter=new MisRutasAdapter(this);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG,"onChildClick: "+childPosition);
                AppCompatCheckedTextView checkedTextView=(AppCompatCheckedTextView) v.findViewById(R.id.lblListItem);
                if(checkedTextView!=null){
                    checkedTextView.toggle();
                    Utils.rutas.get(Utils.empresaRutas.get(Utils.nombreEmpresas.get(groupPosition)).get(
                            childPosition).getObjectId()).setState(checkedTextView.isChecked());
                }
                return false;
            }
        });

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        Log.d(TAG,"onContentChanged");
    }

    @Override
    public void onBackPressed() {
        MapsActivity.populateMap();
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                MapsActivity.populateMap();
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
