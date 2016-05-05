package com.gruporosul.activosfijos.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Diferencia;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 21-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class CalculoAdapter extends RecyclerView.Adapter<CalculoAdapter.ViewHolder> {

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

        @Bind(R.id.txtCalculoDesc)
        TextView txtCalculoDesc;
        @Bind(R.id.txtCalculoEstado)
        TextView txtCalculoEstado;
        @Bind(R.id.rlCalculo)
        RelativeLayout rlCalculo;

        private CalculoAdapter parent = null;

        public ViewHolder(View v, CalculoAdapter parent) {
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
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        Log.e("CA-75", Diferencia.LISTADO_DIFERENCIA.size() + "");
        return Diferencia.LISTADO_DIFERENCIA.size();
    }

    public CalculoAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_calculo_inventario, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Diferencia mDiferencia = Diferencia.LISTADO_DIFERENCIA.get(position);

        holder.txtCalculoDesc.setText(mDiferencia.getDescripcion().replaceFirst(" ", "\n"));
        holder.txtCalculoEstado.setText(mDiferencia.getEstado());

        if (mDiferencia.getEscaner().equals("FALSE")) {
            holder.rlCalculo.setBackgroundResource(R.color.bootstrap_brand_danger);
        } else if (mDiferencia.getEscaner().equals("EXTRA")) {
            holder.rlCalculo.setBackgroundResource(R.color.bootstrap_brand_warning);
        } else {
            holder.rlCalculo.setBackgroundResource(R.color.windowBackground);
        }

    }


}
