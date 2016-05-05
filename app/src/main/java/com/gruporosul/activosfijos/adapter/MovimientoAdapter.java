package com.gruporosul.activosfijos.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Movimiento;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 22-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.ViewHolder> {

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

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Bind(R.id.txtIdMovimiento)
        TextView txtIdMovimiento;

        @Bind(R.id.txtFecha)
        TextView txtFecha;

        @Bind(R.id.txtNombreOrigen)
        TextView txtNombreOrigen;

        @Bind(R.id.txtNombreDestino)
        TextView txtNombreDestino;

        @Bind(R.id.txtMotivo)
        TextView txtMotivo;

        private MovimientoAdapter parent = null;

        public ViewHolder(View v, MovimientoAdapter parent) {
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
        return Integer.parseInt(Movimiento.MOVIMIENTOS.get(position).getIdMovimiento());
    }

    @Override
    public int getItemCount() {
        Log.e("HA-79", Movimiento.MOVIMIENTOS.size() + "");
        return Movimiento.MOVIMIENTOS.size();
    }

    public MovimientoAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_movimiento_item, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Movimiento movimiento = Movimiento.MOVIMIENTOS.get(position);

        holder.txtIdMovimiento.setText(movimiento.getIdMovimiento());
        holder.txtFecha.setText(movimiento.getFecha());
        holder.txtNombreOrigen.setText(movimiento.getNombreColaboradorOrigen().replace(" ", "\n"));
        holder.txtNombreDestino.setText(movimiento.getNombreColaboradorDestino().replace(" ", "\n"));
        holder.txtMotivo.setText(movimiento.getMotivo());

    }


}
