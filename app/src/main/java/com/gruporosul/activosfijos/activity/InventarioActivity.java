package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.bean.Encabezado;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.volley.AppController;
import com.gruporosul.activosfijos.xml.ParserXmlEncabezadoId;

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

public class InventarioActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_inventario)
    RelativeLayout contentInventario;
    @BindView(R.id.fab)
    FloatingActionButton fabNext;
    @BindString(R.string.hint_scan)
    String mHintScan;
    @BindView(R.id.txtColaborador)
    TextView txtColaborador;

    private final static int COLABORADOR_REQUEST_ID = 0;
    private int cod_ficha;
    private ProgressDialog mProgressDialog;
    private static String get_id_encabezado =
            "http://168.234.51.176:8070/Servicioclientes.asmx/get_afget_id_encabezado?codFicha=";
    private static String insert_new_inventario =
            "http://168.234.51.176:8070/Servicioclientes.asmx/insert_activo_encabezado";
    private PrefManager mPrefManager;
    public static InventarioActivity inventarioActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        ButterKnife.bind(this);

        mPrefManager = new PrefManager(this);

        inventarioActivity = this;

        setToolbar();

        fabNext.setEnabled(false);

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        setTitle("Inventario");
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.btnSeleccionarColaborador)
    void onClickColaborador() {
        showDialogOp();
    }

    private void showDialogOp() {
        new MaterialDialog.Builder(this)
                .title("Seleccionar colaborador")
                .items(R.array.op_menu_colaborador)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                Intent fichas = new Intent(InventarioActivity.this, FichasActivity.class);
                                FichasActivity.OPCION = 1;
                                startActivityForResult(fichas,
                                        COLABORADOR_REQUEST_ID);
                                break;
                            case 1:
                                escanearCodigo();
                                break;
                            default:
                                Toast.makeText(InventarioActivity.this, "Selecciona una opción", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .positiveText("Aceptar")
                .negativeText("Cancelar")
                .show();

    }

    @OnClick(R.id.fab)
    void onClickFabNext() {
        showDialogNext(cod_ficha);
    }

    private void showDialogNext(int codFicha) {
        new MaterialDialog.Builder(this)
                .title("Nuevo inventario")
                .content("Crear nuevo inventario para el colaborador " + codFicha)
                .positiveText("Nuevo")
                .negativeText("Cancelar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent escanear = new Intent(InventarioActivity.this, EscanearActivity.class);
                        escanear.putExtra("cod_ficha", cod_ficha);
                        nuevoInventarioRequest(cod_ficha);
                        new get_id_encabezado().execute(get_id_encabezado + cod_ficha);
                        startActivity(escanear);
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            switch (resultCode) {
                case RESULT_OK:
                    cod_ficha = data.getIntExtra("cod_ficha", 0);
                    String ficha = "Ficha seleccionada: " + cod_ficha;
                    txtColaborador.setText(ficha);
                    fabNext.setEnabled(true);
                    break;
            }
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "El código escaneado es incorrecto.";
            } else {
                cod_ficha = Integer.parseInt(result.getContents());
                String ficha = "Ficha seleccionada: " + cod_ficha;
                txtColaborador.setText(ficha);
                fabNext.setEnabled(true);
                toast = "Código escaneado: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }

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
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(InventarioActivity.this);
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

    private void nuevoInventarioRequest(final int codFicha) {
        StringRequest nuevo_inventario = new StringRequest(
                Request.Method.POST,
                insert_new_inventario,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("nuevo_inventario", "Inventario ingresado!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("codFicha", Integer.toString(codFicha));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(nuevo_inventario);
    }

    void escanearCodigo() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(mHintScan);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private String toast;

    /**
     * Toast prefabricado, para un uso mas eficiente, en diferentes partes de codigo.
     */
    private void displayToast() {
        if(toast != null) {
            Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(InventarioActivity.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
