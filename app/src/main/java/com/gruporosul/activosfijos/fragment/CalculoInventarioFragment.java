package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.adapter.CalculoAdapter;
import com.gruporosul.activosfijos.bean.Diferencia;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.volley.AppController;
import com.gruporosul.activosfijos.xml.ParserXmlCalculoInventario;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculoInventarioFragment extends Fragment
                implements CalculoAdapter.OnItemClickListener {

    public static final String TAG = CalculoInventarioFragment.class.getSimpleName();
    private static final String tag_actualizar = "actualizar_inventario";

    private static final String URL_CALCULO = "http://168.234.51.176:8070/Servicioclientes.asmx/Calcular_Inventario?idInventario=";

    private LinearLayoutManager mLayoutManager;
    private CalculoAdapter mAdaptador;
    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;

    private BlankFragment mBlankFragment;
    private FragmentDepartamento mFragmentDepartamento;

    private final static String URL_UPDATE_INVENTARIO =
            "http://168.234.51.176:8070/ServicioClientes.asmx/Actualizar_Estado_Inventario";

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @BindView(R.id.recyclerCalculoInventario)
    RecyclerView mRecyclerView;

    @BindString(R.string.hint_scan)
    String mHintScan;

    public CalculoInventarioFragment() {
        // Required empty public constructor
    }

    public static CalculoInventarioFragment newInstance(Bundle arguments) {
        CalculoInventarioFragment f = new CalculoInventarioFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mCaluloInventarioFragment = inflater.inflate(R.layout.fragment_calculo_inventario, container, false);

        ButterKnife.bind(this, mCaluloInventarioFragment);

        setHasOptionsMenu(true);

        mPrefManager = new PrefManager(getActivity());

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new CalculoAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new RequestListadoActivos().execute(URL_CALCULO + getArguments().getString("idInventario"));
        //getActivity().setTitle(getString(R.string.title_listado, getArguments().getString("colaborador")))

        return mCaluloInventarioFragment;
    }

    @Override
    public void onItemClick(CalculoAdapter.ViewHolder item, int position) {

    }


    @OnClick(R.id.btnContinuar)
    void continuarInventario(){
        getActivity().getSupportFragmentManager().popBackStack(EscanearActivosFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @OnClick(R.id.btnEnviarInventario)
    void finalizarInventario() {
        actualizarInventario();
    }

    /**
     * Petición lista de usuarios {@link com.gruporosul.activosfijos.bean.ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class RequestListadoActivos extends AsyncTask<String, Void, List<Diferencia>> {

        @Override
        protected List<Diferencia> doInBackground(String... urls) {
            try {
                Log.e("url", urls[0]);
                return parsearXmlDeUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mProgressDialog.dismiss();
            //listaVacia();
        }

        @Override
        protected void onPostExecute(List<Diferencia> items) {

            try {
                if (items.size() <= 0) {
                    showDialog();
                } else {
                    Diferencia.LISTADO_DIFERENCIA = items;

                    mAdaptador.notifyDataSetChanged();
                    mProgressDialog.dismiss();

                    getActivity().invalidateOptionsMenu();
                    getActivity().setTitle(getString(R.string.titulo_calculo, getArguments().getString("idInventario")));
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                showDialog();
            }

        }

        private List<Diferencia> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlCalculoInventario mParserXmlCalculoInventario = new ParserXmlCalculoInventario();
            List<Diferencia> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlCalculoInventario.parsear(mInputStream);

            } finally {
                if (mInputStream != null) {
                    mInputStream.close();
                }
            }

            return items;

        }

        private InputStream descargarContenido(String urlString)
                throws IOException {

            java.net.URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            return connection.getInputStream();

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear(Diferencia.LISTADO_DIFERENCIA);
    }

    public void clear(List<Diferencia> agrupadors) {
        agrupadors.clear();
        mAdaptador.notifyDataSetChanged();
    }

    public void showDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.titulo_dialog_calculo)
                .content(R.string.content_calculo)
                .positiveText(R.string.send_inventario)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // Enviar Inventario
                        actualizarInventario();
                    }
                })
                .show();
    }

    public void actualizarInventario() {

        StringRequest actualizarInventario = new StringRequest(
                Request.Method.POST,
                URL_UPDATE_INVENTARIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getActivity(), "Enviado con exito", Toast.LENGTH_SHORT).show();
                        inicio();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("idInventario", getArguments().getString("idInventario"));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(actualizarInventario, tag_actualizar);

    }

    public void inicio() {

        Bundle arguments = new Bundle();

        mFragmentDepartamento = FragmentDepartamento.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mFragmentDepartamento, FragmentDepartamento.TAG);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();
    }

}
