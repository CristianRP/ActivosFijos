package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Caracteristica;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.xml.ParserXmlActivoDescripcion;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static String[] TEST_CARACTERISTICAS = {
            "COLOR: NEGRO", "RUEDAS: SI"
    };
    @BindView(R.id.content_detail)
    LinearLayout contentDetail;

    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;
    private static final String get_activos_detail = "http://168.234.51.176:8070/Servicioclientes.asmx/GET_CARACTERISTICAS_ACTIVO?idActivo=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Caracteristicas del activo");


        mPrefManager = new PrefManager(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Obteniendo caracteristicas...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        //LinearLayout linearLayout = new LinearLayout(this);
        //setContentView(linearLayout);
        //linearLayout.setOrientation(LinearLayout.VERTICAL);
        /*for( String texto : TEST_CARACTERISTICAS)
        {
            TextView textView = new TextView(this);
            textView.setText(texto);
            textView.setTextSize(24);
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Large);
            } else {
                textView.setTextAppearance(android.R.style.TextAppearance_Material_Large);
            }
            contentDetail.setOrientation(LinearLayout.VERTICAL);
            contentDetail.addView(textView);
        }*/

        new GET_ACTIVOS_DETAIL().execute(get_activos_detail + getIntent().getIntExtra("idActivo",0));

    }

    private TextView createTextViews(final String caracteristica) {
        TextView newText = new TextView(this);
        newText.setText(caracteristica);
        return newText;
    }

    private class GET_ACTIVOS_DETAIL extends AsyncTask<String, Void, List<Caracteristica>> {

        @Override
        protected List<Caracteristica> doInBackground(String... urls) {

            try {
                //Log.e("DXMLA-101", urls[0]);
                return parsearXmlDeUrl(urls[0]);
            } catch (IOException e) {
                //Log.e("DirectorioA-97", e.toString());
                e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                //Log.e("DirectorioA-101", e.toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Caracteristica> result) {

            Caracteristica.CARACTERISTICAS = result;

            for( Caracteristica texto : Caracteristica.CARACTERISTICAS)
            {
                TextView textView = new TextView(DetailActivity.this);
                textView.setText(getString(R.string.detail_string, texto.getDesCaracteristica(), texto.getValor()));
                textView.setTextSize(24);
                if (Build.VERSION.SDK_INT < 23) {
                    textView.setTextAppearance(DetailActivity.this, android.R.style.TextAppearance_Material_Large);
                } else {
                    textView.setTextAppearance(android.R.style.TextAppearance_Material_Large);
                }
                contentDetail.setOrientation(LinearLayout.VERTICAL);
                contentDetail.addView(textView);
            }

            mProgressDialog.dismiss();

        }


        private List<Caracteristica> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            // Log.e("parsearXMl-127", urlString);+

            InputStream stream = null;
            ParserXmlActivoDescripcion mParserActivoDesc = new ParserXmlActivoDescripcion();
            List<Caracteristica> profileList = null;

            try {
                stream = descargarContenido(urlString);
                profileList = mParserActivoDesc.parsear(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return profileList;

        }

        private InputStream descargarContenido(String urlString)
                throws IOException {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Iniciar la petición
            conn.connect();
            Log.e("connect", urlString);
            return conn.getInputStream();

        }

    }

}
