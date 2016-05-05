package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.ActivoEscaneado;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.xml.ParserXmlActivoEscaneado;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannActivo extends Fragment {

    public static final String TAG = ActivosFragment.class.getSimpleName();

    private static final String URL_SCANN_ACTIVO = "http://200.30.160.117:8070/Servicioclientes.asmx/AF_Scann_Activo?idActivo=";

    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;

    private static String idActivo;
    private static String descripcion;
    private static String estado;

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @Bind(R.id.relativeScann)
    RelativeLayout mRelativeScann;

    @BindString(R.string.hint_scan)
    String mHintScan;

    @Bind(R.id.empty_view)
    TextView empty_view;

    @Bind(R.id.txtIdColaborador)
    TextView mIdColaborador;

    @Bind(R.id.txtNombre)
    TextView mNombre;

    @Bind(R.id.txtIdActivo)
    TextView mIdActivo;

    @Bind(R.id.txtDescripcion)
    TextView mDescripcion;

    @Bind(R.id.txtEstado)
    TextView mEstado;

    @Bind(R.id.txtUbicacion)
    TextView mUbicacion;

    public static ScannActivo newInstance(Bundle arguments){
        ScannActivo f = new ScannActivo();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }


    public ScannActivo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mScannActivo = inflater.inflate(R.layout.fragment_scann_activo, container, false);

        ButterKnife.bind(this, mScannActivo);

        setHasOptionsMenu(true);

        mPrefManager = new PrefManager(getActivity());

        mProgressDialog = new ProgressDialog(getActivity());

        escanearCodigo();

        return mScannActivo;
    }

    void escanearCodigo() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
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
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelado";
            /*} else if (result.getContents().length() > 4) {
                toast = "El código escaneado es incorrecto.";*/
            } else {
                mProgressDialog.setMessage("Cargando...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                new RequestActivoEscaneado().execute(URL_SCANN_ACTIVO + result.getContents());
                toast = "Código escaneado: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    /**
     * Petición lista de usuarios {@link com.gruporosul.activosfijos.bean.ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class RequestActivoEscaneado extends AsyncTask<String, Void, List<ActivoEscaneado>> {

        @Override
        protected List<ActivoEscaneado> doInBackground(String... urls) {
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
            mRelativeScann.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
        }

        @Override
        protected void onPostExecute(List<ActivoEscaneado> items) {

            try {
                if (items.isEmpty()) {
                    isEmpty();
                } else {
                    mRelativeScann.setVisibility(View.VISIBLE);
                    empty_view.setVisibility(View.GONE);

                    ActivoEscaneado.ACTIVO_ESCANEADO = items;

                    mIdColaborador.setText(Html.fromHtml(getString(R.string.id_colaborador, items.get(0).getCodColaborador())));
                    mNombre.setText(Html.fromHtml(getString(R.string.nombre_colaborador, items.get(0).getColaborador())));
                    mIdActivo.setText(Html.fromHtml(getString(R.string.id_activo, items.get(0).getIdActivo())));
                    mDescripcion.setText(Html.fromHtml(getString(R.string.descripcion_activo, items.get(0).getDescripcion())));
                    mEstado.setText(Html.fromHtml(getString(R.string.estado_activo, items.get(0).getEstado())));
                    mUbicacion.setText(Html.fromHtml(getString(R.string.ubicacion_activo, items.get(0).getDepartamento())));

                    mProgressDialog.dismiss();

                    getActivity().invalidateOptionsMenu();

                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                isEmpty();
            }




        }

        public void isEmpty() {
            mRelativeScann.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
        }

        private List<ActivoEscaneado> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlActivoEscaneado mParserXmlActivoEscaneado = new ParserXmlActivoEscaneado();
            List<ActivoEscaneado> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlActivoEscaneado.parsear(mInputStream);

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

}
