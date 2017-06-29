package com.gruporosul.activosfijos.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.fragment.EscanearActivosFragment;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 28-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ActivoFijoAdapter extends RecyclerView.Adapter<ActivoFijoAdapter.ViewHolder> {

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
        @BindColor(R.color.red)
        int red;


        private ActivoFijoAdapter parent = null;

        public ViewHolder(View v, ActivoFijoAdapter parent) {
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
        return ActivoFijo.ACTIVOS_FIJOS.get(position).getIdActivo();
    }

    @Override
    public int getItemCount() {
        Log.e("HA-79", ActivoFijo.ACTIVOS_FIJOS.size() + "");
        return ActivoFijo.ACTIVOS_FIJOS.size();
    }

    public ActivoFijoAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listado_activos, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ActivoFijo mActivo = ActivoFijo.ACTIVOS_FIJOS.get(position);

        holder.txtBarCode.setText("");
        holder.txtIdActivo.setText("ID: " + mActivo.getIdActivo());
        holder.txtDescActivo.setText(mActivo.getDescripcion());
        holder.txtEstadoActivo.setText(mActivo.getEstado());
        if (EscanearActivosFragment.escaneado) {
            if (EscanearActivosFragment.POSICION == position) {
                //holder.txtEstadoActivo.setTextColor(R.color.bootstrap_brand_info);
                EscanearActivosFragment.escaneado = false;
            }
        } else {
            holder.txtEstadoActivo.setTextColor(holder.red);
        }


    }

}
