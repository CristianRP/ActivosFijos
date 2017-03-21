package com.gruporosul.activosfijos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Activo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 27/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ActivoEscaneadoAdapter
        extends RecyclerView.Adapter<ActivoEscaneadoAdapter.ViewHolder> {

    private Context mContext;
    private List<Activo> mListActivo;

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

    public ActivoEscaneadoAdapter(List<Activo> ubicacion, Context context) {
        this.mListActivo = ubicacion;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Bind(R.id.txtActivo)
        TextView txtActivo;
        @Bind(R.id.txtUbicacion)
        TextView txtUbicacion;
        @Bind(R.id.txtIdActivo)
        TextView txtIdActivo;

        private ActivoEscaneadoAdapter parent = null;

        public ViewHolder(View v, ActivoEscaneadoAdapter parent) {
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
        return mListActivo.get(position).getCodUbicacion();
    }


    @Override
    public int getItemCount() {
        return mListActivo.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_mis_activos, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Activo mActivo = mListActivo.get(position);
        holder.txtIdActivo.setText(String.valueOf(mActivo.getIdActivo()));
        holder.txtActivo.setText(mActivo.getDescripcion());
        holder.txtUbicacion.setText(mActivo.getUbicacion());
    }
}
