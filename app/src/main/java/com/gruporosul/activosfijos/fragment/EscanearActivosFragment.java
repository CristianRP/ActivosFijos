package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.adapter.ActivoFijoAdapter;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.util.Utils;
import com.gruporosul.activosfijos.volley.AppController;
import com.gruporosul.activosfijos.xml.ParserXmlListaActivo;

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
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 *
 * A simple {@link Fragment} subclass.
 */
public class EscanearActivosFragment extends Fragment
            implements ActivoFijoAdapter.OnItemClickListener {

    public static final String TAG = EscanearActivosFragment.class.getSimpleName();
    private static final String  tag_request_escanear = "escanear_activo";

    private LinearLayoutManager mLayoutManager;
    private ActivoFijoAdapter mAdaptador;
    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;

    private static String idActivo;
    private static String descripcion;
    private static String estado;

    private static int count;

    private BlankFragment mBlankFragment;
    private CalculoInventarioFragment mCalculoInventarioFragment;

    private final static String URL_ESCANEAR_ACTIVO =
            "http://168.234.51.176:8070/Servicioclientes.asmx/Escanear_Activo";

    private final static String URL_LISTA_ACTIVOS =
            "http://168.234.51.176:8070/Servicioclientes.asmx/AF_Lista_Usuario_Activos?codColaborador=";

    public static Boolean escaneado = false;
    public static int POSICION = -1;


    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @BindView(R.id.recyclerEscanearActivos)
    RecyclerView mRecyclerView;

    @BindString(R.string.hint_scan)
    String mHintScan;


    public EscanearActivosFragment() {
        // Required empty public constructor
    }


    public static EscanearActivosFragment newInstance(Bundle arguments){
        EscanearActivosFragment f = new EscanearActivosFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mEscanearActivos = inflater.inflate(R.layout.fragment_escanear_activos, container, false);

        ButterKnife.bind(this, mEscanearActivos);

        setHasOptionsMenu(true);

        mPrefManager = new PrefManager(getActivity());

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new ActivoFijoAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new RequestListadoActivos().execute(URL_LISTA_ACTIVOS + getArguments().getString("codColaborador"));
        //getActivity().setTitle(getString(R.string.title_listado, getArguments().getString("colaborador")))

        return mEscanearActivos;
    }

    @Override
    public void onItemClick(ActivoFijoAdapter.ViewHolder item, int position) {

        /*Drawable background = item.itemView.;
        int color = Color.TRANSPARENT;
        if (background instanceof ColorDrawable) {
            color = ((ColorDrawable) background).getColor();
        }*/
        ColorDrawable buttonColor = (ColorDrawable) item.itemView.getBackground();
        int colorId = buttonColor.getColor();

        if (colorId == R.color.bootstrap_brand_info) {
            showReDialog(position);
        } else {
            showDialog(position);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.badge_menu_scann, menu);

        MenuItem item = menu.findItem(R.id.action_total);

        // Obtener drawable del item
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        Log.e("total", "" + mAdaptador.getItemCount() + ParserXmlListaActivo.TOTAL_ACTIVOS);
        // Actualizar el contador
        Utils.setBadgeCount(getActivity(), icon, ParserXmlListaActivo.TOTAL_ACTIVOS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_total:
                showSnackBar("Total de activos asignados: " + ParserXmlListaActivo.TOTAL_ACTIVOS);
                return true;
            case R.id.action_calcular:
                confirmarCalculo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Snackbar prefabricada
     */
    public void showSnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Petición lista de usuarios {@link com.gruporosul.activosfijos.bean.ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class RequestListadoActivos extends AsyncTask<String, Void, List<ActivoFijo>> {

        @Override
        protected List<ActivoFijo> doInBackground(String... urls) {
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
            listaVacia();
        }

        @Override
        protected void onPostExecute(List<ActivoFijo> items) {

            try {
                if (items.size() <= 0) {
                    listaVacia();
                    getActivity().setTitle(getString(R.string.app_name));
                } else {
                    ActivoFijo.ACTIVOS_FIJOS = items;

                    mAdaptador.notifyDataSetChanged();
                    mProgressDialog.dismiss();

                    getActivity().invalidateOptionsMenu();
                    getActivity().setTitle(getString(R.string.title_listado, ActivoFijo.ACTIVOS_FIJOS.get(0).getColaborador()));
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                listaVacia();
                getActivity().setTitle(getString(R.string.app_name));
            }

        }

        private List<ActivoFijo> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlListaActivo mParserXmlListaActivo = new ParserXmlListaActivo();
            List<ActivoFijo> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlListaActivo.parsear(mInputStream);

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

    /**
     * Si la lista esta vacia cambia de fragment
     */
    public void listaVacia() {

        mProgressDialog.dismiss();

        Bundle arguments = new Bundle();

        mBlankFragment = BlankFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mBlankFragment, BlankFragment.TAG);
        mFragmentTransaction.addToBackStack(FragmentDepartamento.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear(ActivoFijo.ACTIVOS_FIJOS);
    }

    private String toast;

    /**
     * Toast prefabricado, para un uso mas eficiente, en diferentes partes de codigo.
     */
    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        toast = "" + count;
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Escaneo cancelado";
            } else if (descripcion.isEmpty() && estado.isEmpty()) {
                insertEscanearActivo(getArguments().getString("idInventario"),
                        result.getContents(),
                        descripcion, estado, "EXTRA");
                toast = "Código escaneado: " + result.getContents();
                mAdaptador.notifyDataSetChanged();
            } else if (count > 0) {
                insertEscanearActivo(getArguments().getString("idInventario"),
                        result.getContents(),
                        descripcion, estado, "EXTRA");
                toast = "Código escaneado: " + result.getContents();
                mAdaptador.notifyDataSetChanged();
            } else {
                insertEscanearActivo(getArguments().getString("idInventario"),
                        result.getContents(),
                        descripcion, estado, "OK");
                toast = "Código escaneado: " + result.getContents();
                mAdaptador.notifyDataSetChanged();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    public void clear(List<ActivoFijo> agrupadors) {
        agrupadors.clear();
        mAdaptador.notifyDataSetChanged();
    }

    public void escanearActivo(int position) {
        ActivoFijo mActivo = ActivoFijo.ACTIVOS_FIJOS.get(position);
        idActivo = Integer.toString(mActivo.getIdActivo());
        descripcion = mActivo.getDescripcion();
        estado = mActivo.getEstado();
        escaneado = true;
        POSICION = position;

        functionScann();

    }

    public void showDialog(final int position) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_dialog)
                .items(R.array.opciones_scann)
                .negativeText(R.string.cancel)
                .positiveText(R.string.elegir)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (text.toString().equals(getString(R.string.escanear_op))) {
                            escanearActivo(position);
                        } else if (text.toString().equals(getString(R.string.re_escanear_op))) {
                            showReDialog(position);
                        } else  if (text.toString().equals(getString(R.string.reportar_op))) {
                            ActivoFijo mActivo = ActivoFijo.ACTIVOS_FIJOS.get(position);
                            insertEscanearActivo(getArguments().getString("idInventario"),
                                    Integer.toString(mActivo.getIdActivo()), mActivo.getDescripcion()
                                    , mActivo.getEstado(), "FALSE");
                        }
                        return false;
                    }
                })
                .show();

    }

    public void showReDialog(final int position) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_re_dialog)
                .content(R.string.content_re)
                .positiveText(R.string.re_escanear)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        escanearActivo(position);
                    }
                })
                .show();
    }

    public void insertEscanearActivo(final String idInventario, final String idActivo, final String descripcion,
                               final String estado, final String escaner) {

        StringRequest escanearActivo = new StringRequest(
                Request.Method.POST,
                URL_ESCANEAR_ACTIVO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Volley-Succesful", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley-error", error.getMessage());
                        VolleyLog.e("Volley-error", error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("idInventario", idInventario);
                params.put("idActivo", idActivo);
                params.put("descripcion", descripcion);
                params.put("estado", estado);
                params.put("escaner", escaner);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(escanearActivo, tag_request_escanear);

    }

    public void calcularInventario() {

        Bundle arguments = new Bundle();

        arguments.putString("idInventario", getArguments().getString("idInventario"));
        arguments.putString("idColaborador", getArguments().getString("codColaborador"));

        mCalculoInventarioFragment = CalculoInventarioFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mCalculoInventarioFragment, CalculoInventarioFragment.TAG);
        mFragmentTransaction.addToBackStack(EscanearActivosFragment.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();
    }

    public void confirmarCalculo() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.calcular_title)
                .content(R.string.calcular_content)
                .positiveText(R.string.calcular)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        calcularInventario();
                    }
                })
                .show();
    }

    @OnClick(R.id.fabAdd)
    void addActivo() {

        showExtraDialog();

    }

    public void showExtraDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_add_new)
                .content(R.string.content_add)
                .positiveText(R.string.add_new)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        count++;
                        functionScann();
                    }
                })
                .show();
    }


    public void functionScann() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(mHintScan);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

}
