package com.gruporosul.activosfijos.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.adapter.MovimientoAdapter;
import com.gruporosul.activosfijos.bean.Movimiento;
import com.gruporosul.activosfijos.xml.ParserXmlMovimiento;

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
 * Activities that contain this fragment must implement the
 * {@link MovimientoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MovimientoFragment extends Fragment
                implements MovimientoAdapter.OnItemClickListener {

    public static final String TAG = FragmentDepartamento.class.getSimpleName();

    private static final String  tag_string_req = "";

    private LinearLayoutManager mLayoutManager;
    private MovimientoAdapter mAdaptador;
    private ProgressDialog mProgressDialog;

    private FilterUserFragment mFilterUserFragment;
    private ActivosFragment mActivosFragment;

    private final static String URL_MOVIMIENTOS =
            "http://200.30.160.117:8070/ServicioClientes.asmx/AF_Movimientos?idColaborador=";

    /**
     * Bindings de view y string con la libreria {@link ButterKnife}
     */
    @Bind(R.id.recyclerMovimiento)
    RecyclerView mRecyclerView;

    @BindString(R.string.nav_departamento)
    String mTitleDepartamento;

    @BindString(R.string.hint_scan)
    String mHintScan;

    @Bind(R.id.empty_view)
    TextView empty_view;

    private OnFragmentInteractionListener mListener;

    public MovimientoFragment() {
        // Required empty public constructor
    }

    public static MovimientoFragment newInstance(Bundle arguments){
        MovimientoFragment f = new MovimientoFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mMovimientoFragment = inflater.inflate(R.layout.fragment_movimiento, container, false);

        ButterKnife.bind(this, mMovimientoFragment);

        setHasOptionsMenu(true);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdaptador = new MovimientoAdapter();
        mAdaptador.setHasStableIds(true);
        mAdaptador.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdaptador);
        //mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdaptador));

        mProgressDialog = new ProgressDialog(getActivity());

        getActivity().setTitle(R.string.movimientos_item);

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.show();
        new RequestListaMovimiento().execute(URL_MOVIMIENTOS + getArguments().getString("codColaborador"));

        return mMovimientoFragment;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onItemClick(MovimientoAdapter.ViewHolder item, int position) {
        
    }

    /**
     * Petición lista de {@link Movimiento}
     * y seteado de datos en el {@link RecyclerView}
     */
    private class RequestListaMovimiento extends AsyncTask<String, Void, List<Movimiento>> {

        @Override
        protected List<Movimiento> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Movimiento> items) {

            if (items.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                empty_view.setVisibility(View.VISIBLE);
                mProgressDialog.dismiss();
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                empty_view.setVisibility(View.GONE);

                Movimiento.MOVIMIENTOS = items;

                mAdaptador.notifyDataSetChanged();
                mProgressDialog.dismiss();

            }

        }

        /**
         * Obtiene la lista de {@link com.gruporosul.activosfijos.bean.Movimiento} en formato xml y
         * se envia a la clase ParserXmlDepartamento
         *
         * @param urlString espera la url del web-service
         * @return
         * @throws XmlPullParserException
         * @throws IOException
         */
        private List<Movimiento> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlMovimiento mParserXmlMovimiento = new ParserXmlMovimiento();
            List<Movimiento> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlMovimiento.parsear(mInputStream);

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
    public void onDestroy() {
        super.onDestroy();
        clear();
    }

    public void clear() {
        Movimiento.MOVIMIENTOS.clear();
    }
}
