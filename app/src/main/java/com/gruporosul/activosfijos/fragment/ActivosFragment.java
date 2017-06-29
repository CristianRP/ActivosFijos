package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DetailActivity;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.adapter.ActivoFijoAdapter;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.util.Utils;
import com.gruporosul.activosfijos.xml.ParserXmlListaActivo;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.BindString;
import butterknife.ButterKnife;

import static com.gruporosul.activosfijos.bean.ActivoFijo.ACTIVOS_FIJOS;

/**
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 *
 * A simple {@link Fragment} subclass.
 */
public class ActivosFragment extends Fragment
        implements ActivoFijoAdapter.OnItemClickListener {

    public static final String TAG = ActivosFragment.class.getSimpleName();
    private static final String  tag_string_req = "activos_req";

    private static final String URL_INVENTARIO = "http://168.234.51.176:8070/Servicioclientes.asmx/Escanear_Activo";

    private LinearLayoutManager mLayoutManager;
    private ActivoFijoAdapter mAdaptador;
    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;

    private BlankFragment mBlankFragment;

    private final static String URL =
            "http://168.234.51.176:8070/Servicioclientes.asmx/AF_Lista_Usuario_Activos?codColaborador=";

    private static String idActivo;
    private static String descripcion;
    private static String estado;

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @BindView(R.id.recyclerListadoActivos)
    RecyclerView mRecyclerView;

    @BindString(R.string.hint_scan)
    String mHintScan;

    @BindView(R.id.empty_view)
    TextView empty_view;

    public ActivosFragment() {
        // Required empty public constructor
    }

    public static ActivosFragment newInstance(Bundle arguments){
        ActivosFragment f = new ActivosFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mActivosFragment = inflater.inflate(R.layout.fragment_activos, container, false);

        ButterKnife.bind(this, mActivosFragment);

        setHasOptionsMenu(true);

        mPrefManager = new PrefManager(getActivity());

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new ActivoFijoAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        new RequestListadoActivos().execute(URL + getArguments().getString("codColaborador"));
        //getActivity().setTitle(getString(R.string.title_listado, getArguments().getString("colaborador")))

        return mActivosFragment;
    }

    @Override
    public void onItemClick(ActivoFijoAdapter.ViewHolder item, int position) {
        //Toast.makeText(getActivity(), "Detalle", Toast.LENGTH_SHORT).show();
        ActivoFijo activo = ActivoFijo.ACTIVOS_FIJOS.get(position);
        Intent activos = new Intent(getActivity(), DetailActivity.class);
        activos.putExtra("idActivo", activo.getIdActivo());
        startActivity(activos);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.badge_menu, menu);

        MenuItem item = menu.findItem(R.id.action_total);

        // Obtener drawable del item
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        Log.e("total", "" + mAdaptador.getItemCount() + ParserXmlListaActivo.TOTAL_ACTIVOS);
        // Actualizar el contador
        Utils.setBadgeCount(getActivity(), icon, ParserXmlListaActivo.TOTAL_ACTIVOS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_total:
                showSnackBar("Total de activos asignados: " + ParserXmlListaActivo.TOTAL_ACTIVOS);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Snackbar prefabricada
     */
    public void showSnackBar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Petición lista de usuarios {@link com.gruporosul.activosfijos.bean.ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class RequestListadoActivos extends AsyncTask<String, Void, List<ActivoFijo>> {

        @Override
        protected List<ActivoFijo> doInBackground(String... urls) {
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
            listaVacia();
        }

        @Override
        protected void onPostExecute(List<ActivoFijo> items) {

            try {
                if (items.size() <= 0) {
                    listaVacia();
                    getActivity().setTitle(getString(R.string.app_name));
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    empty_view.setVisibility(View.GONE);

                    ACTIVOS_FIJOS = items;

                    mAdaptador.notifyDataSetChanged();
                    mProgressDialog.dismiss();

                    getActivity().invalidateOptionsMenu();
                    getActivity().setTitle(getString(R.string.title_listado, ACTIVOS_FIJOS.get(0).getColaborador()));
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                listaVacia();
                getActivity().setTitle(getString(R.string.app_name));
            }

           /* try {
                if (items.size() <= 0) {
                    listaVacia();
                    getActivity().setTitle(getString(R.string.app_name));
                } else {
                    ActivoFijo.ACTIVOS_FIJOS = items;

                    mAdaptador.notifyDataSetChanged();
                    mProgressDialog.dismiss();

                    getActivity().invalidateOptionsMenu();
                    getActivity().setTitle(getString(R.string.title_listado, ActivoFijo.ACTIVOS_FIJOS.get(0).getColaborador()));
                }

                if (dataset.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

            } catch (NullPointerException ex) {
                ex.printStackTrace();
                listaVacia();
                getActivity().setTitle(getString(R.string.app_name));
            }*/

        }

        private List<ActivoFijo> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlListaActivo mParserXmlListaActivo = new ParserXmlListaActivo();
            List<ActivoFijo> items = null;

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

            java.net.URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            return connection.getInputStream();

        }

    }

    /**
     * Si la lista esta vacia cambia de fragment
     */
    public void listaVacia() {

        mProgressDialog.dismiss();

        Bundle arguments = new Bundle();

        arguments.putString("ficha", ActivosFragment.TAG);

        mBlankFragment = BlankFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mBlankFragment, BlankFragment.TAG);
        //mFragmentTransaction.addToBackStack(FragmentDepartamento.TAG); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear(ACTIVOS_FIJOS);
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

                toast = "Código escaneado: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    public void clear(List<ActivoFijo> agrupadors) {
        agrupadors.clear();
        mAdaptador.notifyDataSetChanged();
    }

    public void escanearActivo(int position) {
        ActivoFijo mActivo = ACTIVOS_FIJOS.get(position);
        descripcion = mActivo.getDescripcion();
        estado = mActivo.getEstado();
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(mHintScan);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    public void showDialog(final int position) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_dialog)
                .items(R.array.list_options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                escanearActivo(position);
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "Detalle", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .show();
    }

}
