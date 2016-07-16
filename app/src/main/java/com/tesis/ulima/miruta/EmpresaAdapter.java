package com.tesis.ulima.miruta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesis.ulima.miruta.model.Empresa;
import com.tesis.ulima.miruta.model.Ruta;
import com.tesis.ulima.miruta.utils.Utils;

import java.util.Map;

/**
 * Created by Christian on 7/10/2016.
 */
public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.ViewHolder> {
    private Context mContext;
    private String mEmpresa;

    public EmpresaAdapter(Context context, String empresa) {
        mContext = context;
        mEmpresa = empresa;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate((R.layout.empresa_route_view), null);
        final ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   AnimationSet animationSet = new AnimationSet(false);
                   Animation animation = new AlphaAnimation(0.7f, 1.0f);
                   animation.setDuration(100);
                   Animation animation1 = new ScaleAnimation(0.95f, 1, 0.95f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                   animation1.setDuration(100);
                   animationSet.addAnimation(animation);
                   animationSet.addAnimation(animation1);
                   viewHolder.itemView.startAnimation(animationSet);
                   for (Map.Entry<String, Ruta> ruta : Utils.rutas.entrySet()) {
                       ruta.getValue().setState(false);
                   }
                   Utils.empresaRutas.get(mEmpresa).get(viewHolder.getAdapterPosition()).setState(true);
                   ((Activity)mContext).finish();
                   Utils.fin=true;
                   MapsActivity.populateMap();
                   return true;
               }
           }
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.route_name.setText(Utils.empresaRutas.get(mEmpresa).get(position).getNombre());
        String route = "https://maps.googleapis.com/maps/api/staticmap?size=600x600&path=weight:5%7Ccolor:blue%7Cenc:" +
                Utils.empresaRutas.get(mEmpresa).get(position).getEncodedLine();
        Picasso.with(mContext).load(route).into(holder.route_image);
    }

    @Override
    public int getItemCount() {
        return Utils.empresaRutas.get(mEmpresa).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView route_name;
        public ImageView route_image;

        public ViewHolder(View itemView) {
            super(itemView);
            route_name = (TextView) itemView.findViewById(R.id.route_name);
            route_image = (ImageView) itemView.findViewById(R.id.route_image);
        }
    }
}
