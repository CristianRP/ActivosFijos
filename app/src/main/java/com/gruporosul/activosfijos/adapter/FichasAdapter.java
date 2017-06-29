package com.gruporosul.activosfijos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.FichaColaborador;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 18/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class FichasAdapter
        extends RecyclerView.Adapter<FichasAdapter.ViewHolder> {

    TextView tvDepto;
    private Context mContext;
    private List<FichaColaborador> mListFichas;

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

    public FichasAdapter(List<FichaColaborador> fichas, Context mContext) {
        this.mListFichas = fichas;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tvCodFicha)
        TextView tvCodFicha;
        @BindView(R.id.tvNFicha)
        TextView tvNFicha;
        @BindView(R.id.tvUsuario)
        TextView tvUsuario;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.tvDepto)
        TextView tvDepto;

        private FichasAdapter viewParent = null;

        public ViewHolder(View v, FichasAdapter parent) {
            super(v);
            v.setOnClickListener(this);
            this.viewParent = parent;
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = viewParent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return mListFichas.get(position).getCodFicha();
    }

    @Override
    public int getItemCount() {
        return mListFichas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_fichas_view, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FichaColaborador fichas = mListFichas.get(position);
        holder.tvCodFicha.setText(mContext.getString(R.string.codFicha, fichas.getCodFicha()));
        holder.tvNFicha.setText(fichas.getnFicha());
        holder.tvUsuario.setText(fichas.getUsuario());
        holder.tvEmail.setText(fichas.getEmail());
        holder.tvDepto.setText(mContext.getString(R.string.depto, fichas.getDescDepartamento()));
    }
}
