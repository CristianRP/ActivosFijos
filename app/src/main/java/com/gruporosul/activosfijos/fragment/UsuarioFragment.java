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
import com.gruporosul.activosfijos.adapter.UsuarioAdapter;
import com.gruporosul.activosfijos.bean.Usuario;
import com.gruporosul.activosfijos.xml.ParserXmlUsuario;

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
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 *
 * A simple {@link Fragment} subclass.
 */
public class UsuarioFragment extends Fragment
        implements UsuarioAdapter.OnItemClickListener {

    public static final String TAG = UsuarioFragment.class.getSimpleName();
    private static final String  tag_string_req = "departamento_req";

    private LinearLayoutManager mLayoutManager;
    private UsuarioAdapter mAdaptador;
    private ProgressDialog mProgressDialog;

    private MovimientoFragment mMovimientoFragment;

    private final static String URL =
            "http://200.30.160.117:8070/Servicioclientes.asmx/AF_Lista_Usuario";

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @Bind(R.id.recyclerUsuario)
    RecyclerView mRecyclerView;

    @BindString(R.string.nav_usuario)
    String mTitleUsuario;

    @BindString(R.string.hint_scan)
    String mHintScan;

    public UsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentUsuario = inflater.inflate(R.layout.fragment_usuario, container, false);

        ButterKnife.bind(this, fragmentUsuario);

        setHasOptionsMenu(true);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new UsuarioAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        if (Usuario.USUARIOS.isEmpty()) {
            mProgressDialog.setMessage("Cargando...");
            mProgressDialog.show();
            new RequestListaUsuario().execute(URL);
        }

        return fragmentUsuario;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(mTitleUsuario);
    }

    @Override
    public void onItemClick(UsuarioAdapter.ViewHolder item, int position) {
        filterFragment(position);
    }

    /**
     * Resive la posición del item seleccionado y
     * envia datos al siguiente fragment {@link FilterUserFragment}
     * @param position
     */
    public void filterFragment(int position) {

        Usuario mUsuario = Usuario.USUARIOS.get(position);

        Bundle arguments = new Bundle();
        arguments.putString("codColaborador", Integer.toString(mUsuario.getCodUsuario()));
        arguments.putString("colaborador", mUsuario.getUsuario());

        mMovimientoFragment = MovimientoFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mMovimientoFragment, MovimientoFragment.TAG);
        mFragmentTransaction.addToBackStack(null); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    /**
     * Petición lista de usuarios
     * y seteado de datos en el recyclerview
     */
    private class RequestListaUsuario extends AsyncTask<String, Void, List<Usuario>> {

        @Override
        protected List<Usuario> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Usuario> items) {

            Usuario.USUARIOS = items;

            mAdaptador.notifyDataSetChanged();
            mProgressDialog.dismiss();

        }

        private List<Usuario> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlUsuario mParserXmlUsuario = new ParserXmlUsuario();
            List<Usuario> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlUsuario.parsear(mInputStream);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_main, menu);
    }

    /**
     * Si selecciona action_scan, activa el lector de codigo de barras,
     * el cual obtiene los datos codigo escaneado.
     * @param item
     * @return
     */
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
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
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
                //scannUser(result);
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
    /*public void scannUser(IntentResult result) {

        Bundle arguments = new Bundle();
        arguments.putString("codColaborador", result.getContents());

        mActivosFragment = ActivosFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mActivosFragment, ActivosFragment.TAG);
        mFragmentTransaction.addToBackStack(null); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }*/

}
