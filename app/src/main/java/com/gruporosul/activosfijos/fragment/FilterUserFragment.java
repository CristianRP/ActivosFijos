package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.adapter.FilterUserAdapter;
import com.gruporosul.activosfijos.bean.FilterUser;
import com.gruporosul.activosfijos.xml.ParserXmlFilterUser;

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
public class FilterUserFragment extends Fragment
        implements FilterUserAdapter.OnItemClickListener {

    public static final String TAG = FilterUserFragment.class.getSimpleName();
    private static final String  tag_string_req = "filter_user_req";

    private LinearLayoutManager mLayoutManager;
    private FilterUserAdapter mAdaptador;
    private ProgressDialog mProgressDialog;

    private ResumenFragment mResumenFragment;

    private final static String URL =
            "http://168.234.51.176:8070/Servicioclientes.asmx/AF_Lista_Departamento_Usuario?codDepartamento=";

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @BindView(R.id.recyclerFilterUser)
    RecyclerView mRecyclerView;

    @BindString(R.string.title_usuarios)
    String mTitleUsuarios;

    public FilterUserFragment() {
        // Required empty public constructor
    }

    public static FilterUserFragment newInstance(Bundle arguments){
        FilterUserFragment f = new FilterUserFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mFragmentFilterUser = inflater.inflate(R.layout.fragment_filter_user, container, false);

        ButterKnife.bind(this, mFragmentFilterUser);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new FilterUserAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();
        new RequestFilterUsuario().execute(URL + getArguments().getString("codDepartamento"));

        return mFragmentFilterUser;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(mTitleUsuarios);
    }

    @Override
    public void onItemClick(FilterUserAdapter.ViewHolder item, int position) {
        showDialog(position);
    }

    /**
     * Resive la posición del item seleccionado y
     * envia datos al siguiente fragment {@link FilterUserFragment}
     * @param position
     */
    public void filterFragment(int position, String ubicacion) {

        FilterUser mFilterUser = FilterUser.FILTER_USER.get(position);

        Bundle arguments = new Bundle();
        arguments.putString("codColaborador", Integer.toString(mFilterUser.getCodUsuario()));
        arguments.putString("idDepartamento", getArguments().getString("codDepartamento"));
        arguments.putString("colaborador", mFilterUser.getUsuario());
        arguments.putString("departamento", getArguments().getString("departamento"));
        arguments.putString("ubicacion", ubicacion);

        mResumenFragment = ResumenFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mResumenFragment, FilterUserFragment.TAG);
        //mFragmentTransaction.addToBackStack(null); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    /**
     * Petición lista de usuarios {@link FilterUser}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class RequestFilterUsuario extends AsyncTask<String, Void, List<FilterUser>> {

        @Override
        protected List<FilterUser> doInBackground(String... urls) {
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
        protected void onPostExecute(List<FilterUser> items) {

            FilterUser.FILTER_USER = items;

            mAdaptador.notifyDataSetChanged();
            mProgressDialog.dismiss();

        }

        private List<FilterUser> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlFilterUser mParserXmlFilterUser = new ParserXmlFilterUser();
            List<FilterUser> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlFilterUser.parsear(mInputStream);

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

    public void showDialog(final int position) {
        FilterUser mFilterUser = FilterUser.FILTER_USER.get(position);
        new MaterialDialog.Builder(getActivity())
                .title(R.string.titulo_nuevo)
                .content(getString(R.string.content_nuevo, mFilterUser.getUsuario()))
                .positiveText(R.string.crear)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialogUbicacion(position);
                    }
                })
                .show();
    }

    public void showDialogUbicacion(final int position) {

        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_ubicacion)
                .items(R.array.ubicaciones)
                .positiveText(R.string.elegir)
                .negativeText(R.string.cancel)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        filterFragment(position, text.toString());
                        return false;
                    }
                })
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
    }

    public void clear() {
        FilterUser.FILTER_USER.clear();
    }

}
