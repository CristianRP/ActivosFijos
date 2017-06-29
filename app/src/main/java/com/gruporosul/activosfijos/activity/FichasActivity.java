package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.adapter.FichasAdapter;
import com.gruporosul.activosfijos.bean.FichaColaborador;
import com.gruporosul.activosfijos.xml.ParserXmlFichasColaboradores;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FichasActivity extends AppCompatActivity
        implements FichasAdapter.OnItemClickListener {

    private FichasAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerViewFichas;
    private SearchView mSearView;
    private static final String url_get_fichas =
            "http://168.234.51.176:8070/Servicioclientes.asmx/af_get_ficha_colaboradores";

    public static int OPCION = 0;
    List<FichaColaborador> fichaColaboradors;
    private boolean busqueda = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichas);

        setToolbar();

        mRecyclerViewFichas = (RecyclerView) findViewById(R.id.recyclerFichas);
        mSearView = (SearchView) findViewById(R.id.search_fichas);

        mRecyclerViewFichas.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewFichas.setLayoutManager(mLayoutManager);

        mRecyclerViewFichas.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );
        mRecyclerViewFichas.setItemAnimator(new DefaultItemAnimator());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new RequestFichasList().execute(url_get_fichas);

        mSearView.setOnQueryTextListener(listener);

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

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            newText = newText.toLowerCase();
            fichaColaboradors = new ArrayList<>();
            for (int i = 0; i < FichaColaborador.FICHAS_COLABORADORES.size(); i ++) {
                final String text = FichaColaborador.FICHAS_COLABORADORES.get(i).getnFicha().toLowerCase();
                if (text.contains(newText)) {
                    fichaColaboradors.add(FichaColaborador.FICHAS_COLABORADORES.get(i));
                }
            }
            mRecyclerViewFichas.setLayoutManager(new LinearLayoutManager(FichasActivity.this));
            mAdapter = new FichasAdapter(fichaColaboradors, FichasActivity.this);
            mAdapter.setOnItemClickListener(FichasActivity.this);
            mRecyclerViewFichas.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            busqueda = true;
            return true;
        }
    };

    @Override
    public void onItemClick(FichasAdapter.ViewHolder item, int position) {
        if (OPCION != 0) {
            if (!busqueda) {
                FichaColaborador fichaColaborador = FichaColaborador.FICHAS_COLABORADORES.get(position);
                Intent ficha = new Intent();
                ficha.putExtra("cod_ficha", fichaColaborador.getCodFicha());
                setResult(RESULT_OK, ficha);
                finish();
            } else {
                FichaColaborador fichaColaborador = fichaColaboradors.get(position);
                Intent ficha = new Intent();
                ficha.putExtra("cod_ficha", fichaColaborador.getCodFicha());
                setResult(RESULT_OK, ficha);
                finish();
            }
        }
    }

    /**
     * Request de la Lista de {@link FichaColaborador}
     */
    private class RequestFichasList extends AsyncTask<String, Void, List<FichaColaborador>> {

        @Override
        protected List<FichaColaborador> doInBackground(String... params) {
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
        protected void onPostExecute(List<FichaColaborador> fichaColaboradors) {
            FichaColaborador.FICHAS_COLABORADORES = fichaColaboradors;
            mAdapter = new FichasAdapter(FichaColaborador.FICHAS_COLABORADORES, FichasActivity.this);
            mAdapter.setHasStableIds(true);
            mAdapter.setOnItemClickListener(FichasActivity.this);
            mRecyclerViewFichas.setAdapter(mAdapter);
            mProgressDialog.dismiss();

        }

        private List<FichaColaborador> parsearXmlDeUrl(String url)
            throws XmlPullParserException, IOException {

            InputStream mInput = null;
            ParserXmlFichasColaboradores mParserFichas = new ParserXmlFichasColaboradores();
            List<FichaColaborador> fichaColaboradors = null;

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

}
