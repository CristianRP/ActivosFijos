package com.gruporosul.activosfijos.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Departamento;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 22-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class AdapterDepartamento extends RecyclerView.Adapter<AdapterDepartamento.ViewHolder> {

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

        @BindView(R.id.txtDepartamento)
        TextView txtDepartamento;

        private AdapterDepartamento parent = null;

        public ViewHolder(View v, AdapterDepartamento parent) {
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

    public AdapterDepartamento() {

    }

    @Override
    public int getItemCount() {
        Log.e("HA-79", Departamento.DEPARTAMENTOS.size() + "");
        return Departamento.DEPARTAMENTOS.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_card, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Departamento departamento = Departamento.DEPARTAMENTOS.get(position);

        holder.txtDepartamento.setText(departamento.getDepartamento());

    }

}
