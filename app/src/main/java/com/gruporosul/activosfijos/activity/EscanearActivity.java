package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.adapter.ActivoEscaneadoAdapter;
import com.gruporosul.activosfijos.bean.Activo;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.bean.Encabezado;
import com.gruporosul.activosfijos.util.Utils;
import com.gruporosul.activosfijos.volley.AppController;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class EscanearActivity extends AppCompatActivity
        implements ActivoEscaneadoAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //@Bind(R.id.recyclerActivosEscaneados)
    RecyclerView recyclerActivosEscaneados;
    @BindView(R.id.content_escanear)
    RelativeLayout contentEscanear;
    @BindView(R.id.fab_enviar_inventario)
    FloatingActionButton fabEnviarInventario;
    @BindView(R.id.fab_escanear)
    FloatingActionButton fabEscanear;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    private ActivoEscaneadoAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager mLayoutManager;
    private List<Activo> activoList = new ArrayList<>();
    private int cod_activo;
    private int idMovimiento = 0;
    private LayerDrawable icon;

    private static String get_activos =
            "http://168.234.51.176:8070/Servicioclientes.asmx/af_get_activo_escaneado?idActivo=";
    private static String insert_activo_inventario =
            "http://168.234.51.176:8070/Servicioclientes.asmx/insert_activo_inventario";
    private static String cambiar_estado_inventario =
            "http://168.234.51.176:8070/Servicioclientes.asmx/cambiar_estado_inventario";
    private static String get_id_encabezado =
            "http://168.234.51.176:8070/Servicioclientes.asmx/get_afget_id_encabezado?codFicha=";

    public static EscanearActivity escanearActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear);
        ButterKnife.bind(this);

        setToolbar();

        escanearActivity = this;

        recyclerActivosEscaneados = (RecyclerView) findViewById(R.id.recyclerActivosEscaneados);
        recyclerActivosEscaneados.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerActivosEscaneados.setLayoutManager(mLayoutManager);

        recyclerActivosEscaneados.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );
        recyclerActivosEscaneados.setItemAnimator(new DefaultItemAnimator());

        fabEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearActivo();
            }
        });

        if (activoList.size() == 0) {
            fabEnviarInventario.setEnabled(false);
        }

        fabEnviarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Quieres enviar el inventario?");
            }
        });

        new get_id_encabezado().execute(get_id_encabezado + getIntent().getIntExtra("cod_ficha",0));

    }


    private void showDialog(String message) {
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

    private void envioActivos() {
        for (Activo activo : activoList) {
            insertarActivo(ParserXmlEncabezadoId.ID_ENCABEZADO, activo.getIdActivo());
        }
        activoList.clear();
        mAdapter.notifyDataSetChanged();
        recyclerActivosEscaneados.setVisibility(GONE);
        emptyView.setVisibility(View.VISIBLE);
        validarInventario(ParserXmlEncabezadoId.ID_ENCABEZADO, "VALIDAR");
    }

    @Override
    public void onItemClick(ActivoEscaneadoAdapter.ViewHolder item, int position) {

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        setTitle("Fichas colaboradores");
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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

                emptyView.setVisibility(
                        GONE
                );
                recyclerActivosEscaneados.setVisibility(
                        View.VISIBLE
                );

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
        if(getApplicationContext() != null && toast != null) {
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
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(EscanearActivity.this);
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mProgressDialog.dismiss();
        }

        @Override
        protected void onPostExecute(List<Activo> items) {
            for (Activo activo: items) {
                activoList.add(activo);
            }
            emptyView.setVisibility(GONE);
            recyclerActivosEscaneados.setVisibility(View.VISIBLE);
            fabEnviarInventario.setEnabled(true);
            mAdapter = new ActivoEscaneadoAdapter(activoList, EscanearActivity.this);
            mAdapter.setHasStableIds(true);
            mAdapter.setOnItemClickListener(EscanearActivity.this);
            recyclerActivosEscaneados.setAdapter(mAdapter);
            if ((mProgressDialog != null) && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
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
        activo_inventario.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(activo_inventario);
    }

    private void validarInventario(final int idMovimiento, final String estado) {
        StringRequest validar_inventario = new StringRequest(
                Request.Method.POST,
                cambiar_estado_inventario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent validar = new Intent(EscanearActivity.this, AprobarInventario.class);
                        validar.putExtra("idMovimiento", ParserXmlEncabezadoId.ID_ENCABEZADO);
                        startActivity(validar);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)  {
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
        validar_inventario.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(validar_inventario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.badge_menu, menu);
        MenuItem item = menu.findItem(R.id.action_total);
        icon = (LayerDrawable) item.getIcon();
        Utils.setBadgeCount(EscanearActivity.this, icon, ParserXmlEncabezadoId.ID_ENCABEZADO);
        return true;
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if ((mProgressDialog != null) && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    /**
     * Petición lista de usuarios {@link ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class get_id_encabezado extends AsyncTask<String, Void, List<Encabezado>> {

        @Override
        protected List<Encabezado> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Encabezado> items) {
            Encabezado.ID_ENCABEZADO = items;
            if ((mProgressDialog != null) && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            invalidateOptionsMenu();
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(EscanearActivity.this);
            mProgressDialog.setMessage("Obteniendo último inventario...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        private List<Encabezado> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {
            InputStream mInputStream = null;
            ParserXmlEncabezadoId mParserXmlEncabezadoId = new ParserXmlEncabezadoId();
            List<Encabezado> items = null;
            try {
                mInputStream = descargarContenido(urlString);
                items = mParserXmlEncabezadoId.parsear(mInputStream);
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

}
