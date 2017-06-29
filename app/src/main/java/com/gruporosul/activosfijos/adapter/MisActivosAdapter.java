package com.gruporosul.activosfijos.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Activo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 27/09/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class MisActivosAdapter extends MultiChoiceAdapter<MisActivosAdapter.ViewHolder> {

    private final Context mContext;

    public MisActivosAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()
        ).inflate(R.layout.layout_item_mis_activos, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);

        Activo mActivo = Activo.MIS_ACTIVOS.get(position);

        holder.txtIdActivo.setText(String.valueOf(mActivo.getIdActivo()));
        holder.txtActivo.setText(mActivo.getDescripcion());
        holder.txtUbicacion.setText(mActivo.getUbicacion());

    }

    @Override
    public int getItemCount() {
        return Activo.MIS_ACTIVOS.size();
    }

    @Override
    protected void setActive(View view, boolean state) {
        //ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.container_item);
        if (state) {
            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackgroundLight));
            //imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        } else {
            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            //imageView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(ViewHolder holder,
                                                                final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on item " + position, Toast.LENGTH_SHORT).show();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtActivo)
        TextView txtActivo;
        @BindView(R.id.txtUbicacion)
        TextView txtUbicacion;
        @BindView(R.id.txtIdActivo)
        TextView txtIdActivo;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
