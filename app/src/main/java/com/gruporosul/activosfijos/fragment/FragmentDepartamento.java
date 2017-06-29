package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.adapter.AdapterDepartamento;
import com.gruporosul.activosfijos.bean.Departamento;
import com.gruporosul.activosfijos.xml.ParserXmlDepartamento;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class FragmentDepartamento extends Fragment
        implements AdapterDepartamento.OnItemClickListener {

    public static final String TAG = FragmentDepartamento.class.getSimpleName();
    private static final String  tag_string_req = "departamento_req";

    private LinearLayoutManager mLayoutManager;
    private AdapterDepartamento mAdaptador;
    private ProgressDialog mProgressDialog;

    private FilterUserFragment mFilterUserFragment;
    private ActivosFragment mActivosFragment;

    private final static String URL =
            "http://168.234.51.176:8070/Servicioclientes.asmx/AF_Lista_Departamento";

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @BindView(R.id.recyclerDepartamento)
    RecyclerView mRecyclerView;

    @BindString(R.string.nav_departamento)
    String mTitleDepartamento;

    @BindString(R.string.hint_scan)
    String mHintScan;

    public FragmentDepartamento() {
        // Required empty public constructor
    }

    public static FragmentDepartamento newInstance(Bundle arguments){
        FragmentDepartamento f = new FragmentDepartamento();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mFragmentDepartamento = inflater.inflate(R.layout.fragment_departamento, container, false);

        ButterKnife.bind(this, mFragmentDepartamento);

        setHasOptionsMenu(true);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new AdapterDepartamento();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        if (Departamento.DEPARTAMENTOS.isEmpty()) {
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.show();
            new RequestListaDepartamento().execute(URL);
        }

        return mFragmentDepartamento;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(mTitleDepartamento);
    }

    /**
     * Onclick del ViewHolder, segun el item seleccionado
     * @param item
     * @param position
     */
    @Override
    public void onItemClick(AdapterDepartamento.ViewHolder item, int position) {
        filterFragment(position);
    }

    /**
     * Resive la posición del item seleccionado y
     * envia datos al siguiente fragment {@link FilterUserFragment}
     * @param position
     */
    public void filterFragment(int position) {

        Departamento departamento = Departamento.DEPARTAMENTOS.get(position);

        Bundle arguments = new Bundle();
        arguments.putString("codDepartamento", departamento.getCodDepartamento());
        arguments.putString("departamento", departamento.getDepartamento());

        mFilterUserFragment = FilterUserFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mFilterUserFragment, FilterUserFragment.TAG);
        mFragmentTransaction.addToBackStack(FragmentDepartamento.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt(mHintScan);
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                toast = "Escaneo cancelado";
            } else {
                scannUser(result);
                toast = "Código escaneado: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    /**
     * Resive la posición del item seleccionado y
     * envia datos al siguiente fragment {@link FilterUserFragment}
     * @param result
     */
    public void scannUser(IntentResult result) {

        Bundle arguments = new Bundle();
        arguments.putString("codColaborador", result.getContents());

        mActivosFragment = ActivosFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mActivosFragment, ActivosFragment.TAG);
        mFragmentTransaction.addToBackStack(FragmentDepartamento.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    /**
     * Petición lista de {@link Departamento}
     * y seteado de datos en el {@link RecyclerView}
     */
    private class RequestListaDepartamento extends AsyncTask<String, Void, List<Departamento>> {

        @Override
        protected List<Departamento> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Departamento> items) {

            Departamento.DEPARTAMENTOS = items;

            mAdaptador.notifyDataSetChanged();
            mProgressDialog.dismiss();

        }

        /**
         * Obtiene la lista de {@link Departamento} en formato xml y
         * se envia a la clase ParserXmlDepartamento
         *
         * @param urlString espera la url del web-service
         * @return
         * @throws XmlPullParserException
         * @throws IOException
         */
        private List<Departamento> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlDepartamento mParserXmlDepartamento = new ParserXmlDepartamento();
            List<Departamento> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlDepartamento.parsear(mInputStream);

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
