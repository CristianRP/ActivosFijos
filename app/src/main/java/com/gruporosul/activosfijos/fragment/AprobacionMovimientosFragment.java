package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.adapter.AprobarActivoAdapter;
import com.gruporosul.activosfijos.bean.Movimientos;
import com.gruporosul.activosfijos.xml.ParserXmlListadoMovimientos;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AprobacionMovimientosFragment extends Fragment
        implements AprobarActivoAdapter.OnItemClickListener {


    @Bind(R.id.recyclerAprobarMovimientos)
    RecyclerView mRecyclerAprobarMovimientos;
    private ProgressDialog mProgressDialog;
    private AprobarActivoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private static String get_listado_movimientos = "http://168.234.51.176:8070/Servicioclientes.asmx/af_get_lista_movimientos";


    private AprobarMovimientoFragment mAprobarMovimientoFragment;
    public static final String TAG = AprobacionMovimientosFragment.class.getSimpleName();

    public AprobacionMovimientosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aprobacion_movimientos, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
        mRecyclerAprobarMovimientos.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerAprobarMovimientos.setLayoutManager(mLayoutManager);
        /*mRecyclerAprobarMovimientos.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );*/
        mRecyclerAprobarMovimientos.setItemAnimator(new DefaultItemAnimator());


        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Obteniendo lista...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new get_list_movimientos().execute(get_listado_movimientos);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AprobarActivoAdapter.ViewHolder item, int position) {
        Movimientos movimientos = Movimientos.LISTADO_MOVIMIENTOS.get(position);

        mAprobarMovimientoFragment = AprobarMovimientoFragment.newInstance(0,movimientos.getIdMovimiento());
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mAprobarMovimientoFragment, AprobarMovimientoFragment.TAG);
        mFragmentTransaction.addToBackStack(AprobacionMovimientosFragment.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();
    }

    /**
     * Petición lista de usuarios {@link com.gruporosul.activosfijos.bean.Movimientos}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class get_list_movimientos extends AsyncTask<String, Void, List<Movimientos>> {

        @Override
        protected List<Movimientos> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Movimientos> items) {
            Movimientos.LISTADO_MOVIMIENTOS = items;
            mAdapter = new AprobarActivoAdapter(Movimientos.LISTADO_MOVIMIENTOS, getActivity());
            mAdapter.setHasStableIds(true);
            mAdapter.setOnItemClickListener(AprobacionMovimientosFragment.this);
            mRecyclerAprobarMovimientos.setAdapter(mAdapter);
            mProgressDialog.dismiss();
        }

        private List<Movimientos> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlListadoMovimientos mParserListadoMovimientos = new ParserXmlListadoMovimientos();
            List<Movimientos> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserListadoMovimientos.parsear(mInputStream);

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
