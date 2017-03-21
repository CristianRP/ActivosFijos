package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.adapter.AprobacionAdapter;
import com.gruporosul.activosfijos.bean.Activo;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.bean.Aprobacion;
import com.gruporosul.activosfijos.volley.AppController;
import com.gruporosul.activosfijos.xml.ParserXmlAprobacionInventario;
import com.gruporosul.activosfijos.xml.ParserXmlEncabezadoId;
import com.gruporosul.activosfijos.xml.ParserXmlMisActivos;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AprobarInventario extends AppCompatActivity
        implements AprobacionAdapter.OnItemClickListener {

    @Bind(R.id.emptyView)
    LinearLayout emptyView;
    @Bind(R.id.fab_procesar)
    FloatingActionButton fabProcesar;
    @Bind(R.id.fab_escanear)
    FloatingActionButton fabEscanear;
    @Bind(R.id.fab_enviar_inventario)
    FloatingActionButton fabEnviarInventario;

    private List<Activo> activoList = new ArrayList<>();
    private int cod_activo;
    private AprobacionAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerViewAprobacion;
    private static final String url_get_inventario = "http://200.30.160.117:8070/Servicioclientes.asmx/get_diferencia_inventario?";
    private static String get_activos =
            "http://200.30.160.117:8070/Servicioclientes.asmx/af_get_activo_escaneado?idActivo=";
    private static String cambiar_estado_inventario =
            "http://200.30.160.117:8070/Servicioclientes.asmx/cambiar_estado_inventario";
    private static String insert_activo_inventario =
            "http://200.30.160.117:8070/Servicioclientes.asmx/insert_activo_inventario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprobar_inventario);
        ButterKnife.bind(this);

        setToolbar();

        mRecyclerViewAprobacion = (RecyclerView) findViewById(R.id.recyclerAprobacionInventario);
        mRecyclerViewAprobacion.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewAprobacion.setLayoutManager(mLayoutManager);
        mRecyclerViewAprobacion.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );
        mRecyclerViewAprobacion.setItemAnimator(new DefaultItemAnimator());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Obteniendo diferencias...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        Intent aprobar = getIntent();
        final int idMovimiento = aprobar.getIntExtra("idMovimiento", 0);

        new RequestDiferenciaInventario().execute(url_get_inventario + "idMovimiento=" + idMovimiento);


        fabProcesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarInventarioDialog("¿Procesar inventario? Se enviara con las diferencias encontradas", idMovimiento);
            }
        });

        fabEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearActivo();
            }
        });

        fabEnviarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Quieres enviar el inventario?", idMovimiento);
            }
        });
    }

    private void showDialog(String message, final int idMovimiento) {
        new MaterialDialog.Builder(this)
                .title("Enviar Inventario")
                .content(message)
                .positiveText("Enviar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        envioActivos();
                    }
                })
                .negativeText("Cancelar")
                .show();
    }

    private void procesarInventarioDialog(String message, final int idMovimiento) {
        new MaterialDialog.Builder(this)
                .title("Procesaro Inventario")
                .content(message)
                .positiveText("Procesar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        procesarInventario(idMovimiento, "APLICADO");
                        //ParserXmlEncabezadoId.ID_ENCABEZADO = 0;
                        Aprobacion.INVENTARIO.clear();
                        activoList.clear();
                    }
                })
                .negativeText("Cancelar")
                .show();
    }

    private void envioActivos() {
        for (Activo activo : activoList) {
            insertarActivo(ParserXmlEncabezadoId.ID_ENCABEZADO, activo.getIdActivo());
        }
        validarInventario(getIntent().getIntExtra("idMovimiento", 0), "VALIDAR");
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Fichas colaboradores");
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void insertarActivo(final int idMovimiento, final int idActivo) {
        StringRequest activo_inventario = new StringRequest(
                Request.Method.POST,
                insert_activo_inventario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("volley-request", "Ingresado con exito");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley-error", "No ingresado :(");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idMovimiento", Integer.toString(idMovimiento));
                params.put("idActivo", Integer.toString(idActivo));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(activo_inventario);
    }

    @Override
    public void onItemClick(AprobacionAdapter.ViewHolder item, int position) {

    }

    /**
     * Request de la Lista de {@link Aprobacion}
     */
    private class RequestDiferenciaInventario extends AsyncTask<String, Void, List<Aprobacion>> {

        @Override
        protected List<Aprobacion> doInBackground(String... params) {
            try {
                return parsearXmlDeUrl(params[0]);
            } catch (IOException io) {
                io.printStackTrace();
                return null;
            } catch (XmlPullParserException xpe) {
                xpe.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Aprobacion> fichaColaboradors) {
            Aprobacion.INVENTARIO = fichaColaboradors;
            emptyView.setVisibility(View.GONE);
            mRecyclerViewAprobacion.setVisibility(View.VISIBLE);
            mAdapter = new AprobacionAdapter(Aprobacion.INVENTARIO, AprobarInventario.this);
            mAdapter.setHasStableIds(true);
            mAdapter.setOnItemClickListener(AprobarInventario.this);
            mRecyclerViewAprobacion.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mProgressDialog.dismiss();
        }

        private List<Aprobacion> parsearXmlDeUrl(String url)
                throws XmlPullParserException, IOException {

            InputStream mInput = null;
            ParserXmlAprobacionInventario mParserFichas = new ParserXmlAprobacionInventario();
            List<Aprobacion> fichaColaboradors = null;

            try {
                mInput = descargarContenido(url);
                fichaColaboradors = mParserFichas.parsear(mInput);
            } finally {
                if (mInput != null) {
                    mInput.close();
                }
            }

            return fichaColaboradors;
        }

        private InputStream descargarContenido(String urlString)
                throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            return connection.getInputStream();
        }
    }

    private void escanearActivo() {
        launchScanActivity();
    }

    void launchScanActivity() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Escanear activo");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelado";
            } else {
                toast = "Código escaneado: " + result.getContents();
                cod_activo = Integer.parseInt(result.getContents());

                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Cargando...");
                mProgressDialog.show();

                new get_list_of_activos().execute(get_activos + cod_activo);


            }
            displayToast();
        }
    }

    /**
     * Toast prefabricado, para un uso mas eficiente, en diferentes partes de codigo.
     */
    private String toast;

    private void displayToast() {
        if (getApplicationContext() != null && toast != null) {
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    /**
     * Petición lista de usuarios {@link ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class get_list_of_activos extends AsyncTask<String, Void, List<Activo>> {

        @Override
        protected List<Activo> doInBackground(String... urls) {
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
        }

        @Override
        protected void onPostExecute(List<Activo> items) {
            for (Activo activo : items) {
                activoList.add(activo);
            }
            mProgressDialog.dismiss();
            Toast.makeText(AprobarInventario.this, "Escaneado con éxito!", Toast.LENGTH_SHORT).show();
        }

        private List<Activo> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {
            InputStream mInputStream = null;
            ParserXmlMisActivos mParserXmlListaActivo = new ParserXmlMisActivos();
            List<Activo> items = null;
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
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();
            return connection.getInputStream();
        }

    }

    private void validarInventario(final int idMovimiento, final String estado) {
        StringRequest validar_inventario = new StringRequest(
                Request.Method.POST,
                cambiar_estado_inventario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent validar = new Intent(AprobarInventario.this, AprobarInventario.class);
                        validar.putExtra("idMovimiento", ParserXmlEncabezadoId.ID_ENCABEZADO);
                        Aprobacion.INVENTARIO.clear();
                        activoList.clear();
                        startActivity(validar);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley-error", "No cambiado :(");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idMovimiento", Integer.toString(idMovimiento));
                params.put("estado", estado);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(validar_inventario);
    }

    private void cambiarPendiente(final int idMovimiento, final String estado) {
        StringRequest validar_inventario = new StringRequest(
                Request.Method.POST,
                cambiar_estado_inventario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley-error", "No cambiado :(");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idMovimiento", Integer.toString(idMovimiento));
                params.put("estado", estado);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(validar_inventario);
    }

    private void procesarInventario(final int idMovimiento, final String estado) {
        StringRequest validar_inventario = new StringRequest(
                Request.Method.POST,
                cambiar_estado_inventario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent main = new Intent(AprobarInventario.this, MainActivity.class);
                        startActivity(main);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley-error", "No cambiado :(");
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idMovimiento", Integer.toString(idMovimiento));
                params.put("estado", estado);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(validar_inventario);
    }

}