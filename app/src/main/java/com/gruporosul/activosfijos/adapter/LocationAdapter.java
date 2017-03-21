package com.gruporosul.activosfijos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Ubicacion;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 14/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class LocationAdapter
        extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private Context mContext;
    private List<Ubicacion> mListLocation;

    public interface OnItemClickListener {
        void onItemClick(ViewHolder item, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return listener;
    }

    public LocationAdapter(List<Ubicacion> ubicacion, Context context) {
        this.mListLocation = ubicacion;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Bind(R.id.codUbicacion)
        TextView codUbicacion;
        @Bind(R.id.tvDescripcion)
        TextView tvDescripcion;
        @Bind(R.id.tvNUbicacionPadre)
        TextView tvNUbicacionPadre;
        @Bind(R.id.codUbicacionPadre)
        TextView codUbicacionPadre;

        private LocationAdapter parent = null;

        public ViewHolder(View v, LocationAdapter parent) {
            super(v);
            v.setOnClickListener(this);
            this.parent = parent;
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return mListLocation.get(position).getCodUbicacion();
    }


    @Override
    public int getItemCount() {
        return mListLocation.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_location_view, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ubicacion ubicacion = mListLocation.get(position);
        holder.codUbicacion.setText(mContext.getString(R.string.cod_ubicacion,
                Integer.toString(ubicacion.getCodUbicacion())));
        holder.tvDescripcion.setText(mContext.getString(
                R.string.location_description, ubicacion.getDescripcion()));
        holder.tvNUbicacionPadre.setText(mContext.getString(
                R.string.location_parent_description, ubicacion.getnUbicacionPadre()));
        holder.codUbicacionPadre.setText(mContext.getString(
                R.string.cod_ubicacion, Integer.toString(ubicacion.getCodUbicacionPadre())));
    }
}
