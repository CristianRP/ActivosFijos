package com.gruporosul.activosfijos.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.FilterUser;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 23-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class FilterUserAdapter extends RecyclerView.Adapter<FilterUserAdapter.ViewHolder> {

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

        @Bind(R.id.txtDepartamento)
        TextView txtFilterUser;

        private FilterUserAdapter parent = null;

        public ViewHolder(View v, FilterUserAdapter parent) {
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
        return FilterUser.FILTER_USER.get(position).getCodUsuario();
    }

    public FilterUserAdapter() {
    }

    @Override
    public int getItemCount() {
        Log.e("HA-79", FilterUser.FILTER_USER.size() + "");
        return FilterUser.FILTER_USER.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_card, parent, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FilterUser filterUser = FilterUser.FILTER_USER.get(position);

        holder.txtFilterUser.setText(filterUser.getUsuario());
    }


}
