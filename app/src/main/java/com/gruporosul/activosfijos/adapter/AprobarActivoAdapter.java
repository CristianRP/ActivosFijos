package com.gruporosul.activosfijos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Movimientos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 23/03/2017.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class AprobarActivoAdapter extends RecyclerView.Adapter<AprobarActivoAdapter.ViewHolder> {

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
    private List<Movimientos> mListMovimientos;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.txtIdMovimiento)
        TextView mIdMovimiento;
        @BindView(R.id.txtDescripcionActivo)
        TextView mDescripcionActivo;
        @BindView(R.id.tipoMovimiento)
        TextView mTipoMovimiento;
        @BindView(R.id.txtUsuarioSolicito)
        TextView mUsuarioSolicito;
        @BindView(R.id.txtDescUsuarioNuevo)
        TextView mDescUsuarioNuevo;

        private AprobarActivoAdapter parent = null;

        public ViewHolder(View v, AprobarActivoAdapter parent) {
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
        return Integer.parseInt(mListMovimientos.get(position).getIdMovimiento());
    }

    public AprobarActivoAdapter(List<Movimientos> activos, Context mContext) {
        this.mListMovimientos = activos;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return mListMovimientos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listado_movimientos, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movimientos movimientos = mListMovimientos.get(position);
        String idMovimiento = String.format("ID Movimiento: %1$s", movimientos.getIdMovimiento());
        holder.mIdMovimiento.setText(idMovimiento);
        //String tipoMovimiento = String.format("")
        holder.mTipoMovimiento.setText(movimientos.getTipoMovimiento());
        holder.mDescripcionActivo.setText(movimientos.getDescripcionActivo());
        holder.mUsuarioSolicito.setText(movimientos.getDescripcionFichaResponsable());
        holder.mDescUsuarioNuevo.setText(movimientos.getDescripcionFichaNuevoResponsable());
    }
}