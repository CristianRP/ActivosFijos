package com.gruporosul.activosfijos.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Movimientos;
import com.gruporosul.activosfijos.util.Constants;
import com.gruporosul.activosfijos.volley.AppController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AprobarMovimientoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AprobarMovimientoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = AprobarMovimientoFragment.class.getSimpleName();
    private AprobacionMovimientosFragment mAprobacionMovimientosFragment = new AprobacionMovimientosFragment();
    private static final String VALIDAR_USUARIO_ACEPTO = "El usuario no lo ha aceptado";

    @BindView(R.id.txtIdMovimiento)
    TextView mIdMovimiento;
    @BindView(R.id.txtDescripcionActivo)
    TextView mDescripcionActivo;
    @BindView(R.id.txtTipoMovimiento)
    TextView mTipoMovimiento;
    @BindView(R.id.txtUbicacion)
    TextView mUbicacion;
    @BindView(R.id.txtNuevaUbicacion)
    TextView mNuevaUbicacion;
    @BindView(R.id.layoutUbicacion)
    LinearLayout mLayoutUbicacion;
    @BindView(R.id.txtResponsable)
    TextView mResponsable;
    @BindView(R.id.layoutResponsable)
    LinearLayout mLayoutResponsable;
    @BindView(R.id.txtNuevoResponsable)
    TextView mNuevoResponsable;
    @BindView(R.id.layoutNuevoResponsable)
    LinearLayout mLayoutNuevoResponsable;
    @BindView(R.id.txtUsuarioSolicito)
    TextView mUsuarioSolicito;
    @BindView(R.id.txtUsuarioAcepto)
    TextView mUsuarioAcepto;
    @BindView(R.id.titleNuevaUbicacion)
    TextView titleNuevaUbicacion;
    @BindView(R.id.txtFechaSolicito)
    TextView mFechaSolicito;
    @BindView(R.id.txtDias)
    TextView mDias;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;


    public AprobarMovimientoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AprobarMovimientoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AprobarMovimientoFragment newInstance(int param1, String param2) {
        AprobarMovimientoFragment fragment = new AprobarMovimientoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_aprobar_movimiento, container, false);
        // Inflate the layout for this fragment

        ButterKnife.bind(this, v);

        Movimientos movimiento = Movimientos.getItem(mParam2);
        assert movimiento != null;
        String idMovimiento = String.format(Locale.getDefault(), "ID Movimiento: %1$s",
                movimiento.getIdMovimiento());
        mIdMovimiento.setText(idMovimiento);
        mDescripcionActivo.setText(movimiento.getDescripcionActivo());
        mTipoMovimiento.setText(movimiento.getTipoMovimiento());
        mUbicacion.setText(movimiento.getDescripcionUbicacionActual());
        titleNuevaUbicacion.setVisibility(GONE);
        mNuevaUbicacion.setVisibility(GONE);
        mLayoutNuevoResponsable.setVisibility(GONE);
        mUsuarioSolicito.setText(movimiento.getUsuarioSolicito());
        mUsuarioAcepto.setText(movimiento.getUsuarioAcepto());
        String mov = movimiento.getFechaSolicito().substring(0, 10);
        String dia = mov.split("-")[2];
        String mes = mov.split("-")[1];
        String anio = mov.split("-")[0];
        String fecha_format = dia + "-" + mes + "-" + anio;
        String fecha_solicito = String.format(Locale.getDefault(), "Fecha que solicito: %1$s",
                fecha_format);
        mFechaSolicito.setText(fecha_solicito);
        if (!movimiento.getDias().equals("No registro")) {
            String dias = String.format(Locale.getDefault(), "Días desde que solicitó: %1$s",
                    movimiento.getDias());
            mDias.setText(dias);
        } else {
            mDias.setVisibility(GONE);
        }
        switch (movimiento.getTipoMovimiento()) {
            case "TRASLADOS":
                //motivo resposanble neuvo responsable
                mLayoutNuevoResponsable.setVisibility(VISIBLE);
                mNuevoResponsable.setVisibility(VISIBLE);
                mNuevoResponsable.setText(movimiento.getDescripcionFichaNuevoResponsable());
                break;
            case "TRASLADOS UBICACION Y RESPONSABLE":
                //motivo responsable nuevo responsable nueva ubicacion
                titleNuevaUbicacion.setVisibility(VISIBLE);
                mNuevaUbicacion.setVisibility(VISIBLE);
                mNuevoResponsable.setVisibility(VISIBLE);
                mNuevaUbicacion.setText(movimiento.getDescripcionUbicacionNueva());
                mNuevoResponsable.setText(movimiento.getDescripcionFichaNuevoResponsable());
                break;
            case "CAMBIO UBICACION":
                // motivo responsable nueva ubicacion
                titleNuevaUbicacion.setVisibility(VISIBLE);
                mNuevaUbicacion.setVisibility(VISIBLE);
                mNuevaUbicacion.setText(movimiento.getDescripcionUbicacionNueva());
                break;
            case "SOLICITUD BAJA":
                //motivo responsable
                break;
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }

    @OnClick(R.id.btnValidarMovimiento)
    void validarMovimiento() {
        if (mUsuarioAcepto.getText().equals(VALIDAR_USUARIO_ACEPTO)) {
            Toast.makeText(getActivity(), "Este activo no ha sido aceptado!", Toast.LENGTH_SHORT).show();
        } else {
            new MaterialDialog.Builder(getActivity())
                    .title("Validar movimiento")
                    .content("¿Estás seguro que quieres validar este movimiento?")
                    .positiveText("Validar")
                    .negativeText("Cancelar")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            validarActivoRequest();
                        }
                    })
                    .show();
        }
    }

    private void validarActivoRequest() {
        StringRequest validar_activo = new StringRequest(
                Request.Method.POST,
                Constants.HOST_ADDRESS + "/ServicioClientes.asmx/AF_VALIDAR_MOVIMIENTOS",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("AproMovimientosRequest", response);
                        Toast.makeText(getActivity(), "Validado con éxito!", Toast.LENGTH_SHORT).show();
                        AprobacionMovimientosFragment aprobacionMovimientosFragment = new AprobacionMovimientosFragment();
                        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.main_content, aprobacionMovimientosFragment, AprobarMovimientoFragment.TAG);
                        mFragmentTransaction.addToBackStack(AprobacionMovimientosFragment.TAG); // Agrega a la pila el fragmento para poder retroceder
                        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

                        mFragmentTransaction.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("AproMovimientosRequest", "Hubo un error");
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Movimientos movimiento = Movimientos.getItem(mParam2);
                assert movimiento != null;
                params.put("idMovimiento", movimiento.getIdMovimiento());
                params.put("idActivo", movimiento.getIdActivo());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(validar_activo);
    }

    @OnClick(R.id.btnRegresar)
    void onClickRegresar() {
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mAprobacionMovimientosFragment, AprobarMovimientoFragment.TAG);
        mFragmentTransaction.addToBackStack(AprobacionMovimientosFragment.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();
    }
}
