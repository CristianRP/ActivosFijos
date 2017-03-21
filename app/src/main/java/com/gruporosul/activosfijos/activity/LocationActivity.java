package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.adapter.LocationAdapter;
import com.gruporosul.activosfijos.bean.Ubicacion;
import com.gruporosul.activosfijos.xml.ParserXmlUbicacion;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity 
        implements LocationAdapter.OnItemClickListener {

    private LocationAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerViewUbicaciones;
    private SearchView searchView;
    private static final String url_get_ubicaciones =
            "http://200.30.160.117:8070/Servicioclientes.asmx/af_get_ubicaciones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setToolbar();

        mRecyclerViewUbicaciones = (RecyclerView) findViewById(R.id.recyclerUbicaciones);
        searchView = (SearchView) findViewById(R.id.search);

        mRecyclerViewUbicaciones.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewUbicaciones.setLayoutManager(mLayoutManager);

        mRecyclerViewUbicaciones.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST)
        );
        mRecyclerViewUbicaciones.setItemAnimator(new DefaultItemAnimator());
        
        /*mAdapter = new LocationAdapter(Ubicacion.UBICACIONES, this);
        mAdapter.setHasStableIds(true);
        mAdapter.setOnItemClickListener(this);
        mRecyclerViewUbicaciones.setAdapter(mAdapter);*/


        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new RequestLocationList().execute(url_get_ubicaciones);

        searchView.setOnQueryTextListener(listener);

    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            newText = newText.toLowerCase();
            final List<Ubicacion> filteredList = new ArrayList<>();
            for (int i = 0; i < Ubicacion.UBICACIONES.size(); i++) {
                final String text = Ubicacion.UBICACIONES.get(i).getDescripcion().toLowerCase();
                if (text.contains(newText)) {
                    filteredList.add(Ubicacion.UBICACIONES.get(i));
                }
            }
            mRecyclerViewUbicaciones.setLayoutManager(new LinearLayoutManager(LocationActivity.this));
            mAdapter = new LocationAdapter(filteredList, LocationActivity.this);
            mAdapter.setOnItemClickListener(LocationActivity.this);
            mRecyclerViewUbicaciones.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return true;
        }
    };

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Solicitar movimiento");
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public void onItemClick(LocationAdapter.ViewHolder item, int position) {
        Toast.makeText(this, ":v", Toast.LENGTH_SHORT).show();
    }

    /**
     * Request de la lista de ubicaciones {@link Ubicacion }
     */
    private class RequestLocationList extends AsyncTask<String, Void, List<Ubicacion>> {

        @Override
        protected List<Ubicacion> doInBackground(String... params) {
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
        protected void onPostExecute(List<Ubicacion> locations) {

            Ubicacion.UBICACIONES = locations;
            mAdapter = new LocationAdapter(Ubicacion.UBICACIONES, LocationActivity.this);
            mAdapter.setHasStableIds(true);
            mAdapter.setOnItemClickListener(LocationActivity.this);
            mRecyclerViewUbicaciones.setAdapter(mAdapter);
            mProgressDialog.dismiss();

        }

        private List<Ubicacion> parsearXmlDeUrl(String url)
                throws XmlPullParserException, IOException {

            InputStream mInput = null;
            ParserXmlUbicacion  mParserUbicacion = new ParserXmlUbicacion();
            List<Ubicacion> items = null;

            try {
                mInput = descargarContenido(url);
                items = mParserUbicacion.parsear(mInput);
            } finally {
                if (mInput != null) {
                    mInput.close();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
