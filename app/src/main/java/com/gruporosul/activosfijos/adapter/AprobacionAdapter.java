package com.gruporosul.activosfijos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Aprobacion;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 04/11/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class AprobacionAdapter extends RecyclerView.Adapter<AprobacionAdapter.ViewHolder> {

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

    private Context mContext;
    private List<Aprobacion> mListActivos;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.txtBarCode)
        TextView txtBarCode;
        @BindView(R.id.txtId)
        TextView txtIdActivo;
        @BindView(R.id.txtActivo)
        TextView txtDescActivo;
        @BindView(R.id.txtEstado)
        TextView txtEstadoActivo;
        @BindView(R.id.rlListado)
        RelativeLayout rlListado;
        @BindColor(R.color.bootstrap_brand_danger)
        int red;
        @BindColor(R.color.bootstrap_brand_success)
        int green;


        private AprobacionAdapter parent = null;

        public ViewHolder(View v, AprobacionAdapter parent) {
            super(v);

            v.setOnClickListener(this);
            this.parent = parent;

            ButterKnife.bind(this, v);

        }

        @Override
        public void onClick(View view) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }

    }

    @Override
    public long getItemId(int position) {
        return mListActivos.get(position).getIdActivo();
    }

    @Override
    public int getItemCount() {
        return mListActivos.size();
    }

    public AprobacionAdapter(List<Aprobacion> activos, Context mContext) {
        this.mListActivos = activos;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listado_activos, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Aprobacion mActivo = mListActivos.get(position);

        holder.txtIdActivo.setText(mContext.getString(R.string.cod_ubicacion, Integer.toString(mActivo.getIdActivo())));
        holder.txtDescActivo.setText(mActivo.getDescripcion());
        holder.txtEstadoActivo.setText(mActivo.getEstado());
        if (mActivo.getEstado().equals("FALTANTE")) {
            holder.txtEstadoActivo.setTextColor(holder.red);
        } else if (mActivo.getEstado().equals("OK")) {
            holder.txtEstadoActivo.setTextColor(holder.green);
        }
    }
}
