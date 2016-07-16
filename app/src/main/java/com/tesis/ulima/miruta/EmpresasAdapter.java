package com.tesis.ulima.miruta;

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
import android.widget.TextView;

import com.tesis.ulima.miruta.utils.Utils;

/**
 * Created by Christian on 7/10/2016.
 */
public class EmpresasAdapter extends RecyclerView.Adapter<EmpresasAdapter.ViewHolder> {
    private Context mContext;
    public EmpresasAdapter(Context context){
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate((R.layout.simple_list_empresa_item), null);
        final ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(false);
                Animation animation = new AlphaAnimation(0.7f, 1.0f);
                animation.setDuration(100);
                Animation animation1 = new ScaleAnimation(0.95f, 1, 0.95f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation1.setDuration(100);
                animationSet.addAnimation(animation);
                animationSet.addAnimation(animation1);
                viewHolder.itemView.startAnimation(animationSet);
                Intent intent = new Intent(mContext, EmpresaActivity.class);
                intent.putExtra("nombre", Utils.nombreEmpresas.get(viewHolder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.route_name.setText(Utils.nombreEmpresas.get(position));
    }

    @Override
    public int getItemCount() {
        return Utils.nombreEmpresas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView route_name;
        public ViewHolder(View itemView) {
            super(itemView);
            route_name=(TextView)itemView.findViewById(R.id.empresa_name);
        }
    }
}
