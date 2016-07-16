package com.tesis.ulima.miruta;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesis.ulima.miruta.R;
import com.tesis.ulima.miruta.model.Ruta;
import com.tesis.ulima.miruta.utils.Utils;

import java.util.HashMap;

/**
 * Created by Christian on 7/10/2016.
 */
public class MisRutasAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private Context mContext;

    public MisRutasAdapter(Context context){
        mContext=context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return Utils.nombreEmpresas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Utils.empresaRutas.get(Utils.nombreEmpresas.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return Utils.nombreEmpresas.get(groupPosition);
    }

    @Override
    public Ruta getChild(int groupPosition, int childPosition) {
        return Utils.empresaRutas.get(Utils.nombreEmpresas.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v;
        if(convertView!=null){
            v=convertView;
        }else v=inflater.inflate(R.layout.list_group,parent,false);
        TextView nombreEmpresa=(TextView)v.findViewById(R.id.lblListHeader);
        nombreEmpresa.setText(getGroup(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v;
        if(convertView!=null){
            v=convertView;
        }else v=inflater.inflate(R.layout.list_item,parent,false);
        AppCompatCheckedTextView checkedTextView=(AppCompatCheckedTextView) v.findViewById(R.id.lblListItem);
        checkedTextView.setText(getChild(groupPosition, childPosition).getNombre());
        checkedTextView.setChecked(getChild(groupPosition, childPosition).isState());
        ImageView imageView=(ImageView)v.findViewById(R.id.lblListItem_image);
        String route= "https://maps.googleapis.com/maps/api/staticmap?size=600x600&path=weight:5%7Ccolor:blue%7Cenc:"+
                getChild(groupPosition, childPosition).getEncodedLine();
        Picasso.with(mContext).load(route).into(imageView);
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
