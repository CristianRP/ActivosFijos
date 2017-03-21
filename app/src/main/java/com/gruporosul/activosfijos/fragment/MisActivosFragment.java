package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceRecyclerView;
import com.davidecirillo.multichoicerecyclerview.listeners.MultiChoiceSelectionListener;
import com.github.clans.fab.FloatingActionButton;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.activity.DividerItemDecoration;
import com.gruporosul.activosfijos.activity.MovementActivity;
import com.gruporosul.activosfijos.adapter.MisActivosAdapter;
import com.gruporosul.activosfijos.bean.Activo;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.xml.ParserXmlMisActivos;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisActivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisActivosFragment extends Fragment {

    @Bind(R.id.multiChoiceRecyclerView)
    public MultiChoiceRecyclerView multiChoiceRecyclerView;
    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.fab_cambio_ubicacion)
    FloatingActionButton fabCambioUbicacion;
    @Bind(R.id.fab_solicitud_baja)
    FloatingActionButton fabSolicitudBaja;
    @Bind(R.id.fab_traslados)
    FloatingActionButton fabTraslados;
    @Bind(R.id.fab_ubicacion_responsable)
    FloatingActionButton fabUbicacionResponsable;
    private String mParam1;
    private String mParam2;
    private ProgressDialog mProgressDialog;
    private PrefManager mPrefManager;
    private MisActivosAdapter mAdapter;
    private static int CAMBIO_UBICACION = 0;
    private static int SOLICITUD_BAJA = 1;
    private static int TRASLADOS = 2;
    private static int TRASLADOS_RESPONSABLE = 3;

    private static String get_activos = "http://200.30.160.117:8070/Servicioclientes.asmx/af_get_activos?user_name=cramirez";


    public MisActivosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisActivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisActivosFragment newInstance(String param1, String param2) {
        MisActivosFragment fragment = new MisActivosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_activos, container, false);
        ButterKnife.bind(this, view);

        getActivity().setTitle("Mis activos");

        mPrefManager = new PrefManager(getActivity());

        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        setUpMultiChoiceRecyclerView();

        new get_list_of_activos().execute(get_activos);

        final List<Activo> activos = new ArrayList<>();
        multiChoiceRecyclerView.setMultiChoiceSelectionListener(new MultiChoiceSelectionListener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {
                Activo activo = Activo.MIS_ACTIVOS.get(selectedPosition);
                activos.add(activo);
                Toast.makeText(getActivity(), "position " + activo.getIdActivo() + " count" + itemSelectedCount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount) {
                Activo activo = Activo.MIS_ACTIVOS.get(deselectedPosition);
                activos.remove(activo);
                Toast.makeText(getActivity(), "position " + activo.getIdActivo() + " count" + itemSelectedCount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSelectAll(int itemSelectedCount, int allItemCount) {

            }

            @Override
            public void OnDeselectAll(int itemSelectedCount, int allItemCount) {

            }
        });

        fabCambioUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Activo actio : activos) {
                    Log.e(":v lista", "holi :v :" + actio.getIdActivo() + " " + actio.getDescripcion());
                }
                Intent tipoMovimiento = new Intent(getActivity(), MovementActivity.class);
                tipoMovimiento.putExtra("tipo", CAMBIO_UBICACION);
                startActivity(tipoMovimiento);
            }
        });

        fabSolicitudBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tipoMovimiento = new Intent(getActivity(), MovementActivity.class);
                tipoMovimiento.putExtra("tipo", SOLICITUD_BAJA);
                startActivity(tipoMovimiento);
            }
        });

        fabTraslados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tipoMovimiento = new Intent(getActivity(), MovementActivity.class);
                tipoMovimiento.putExtra("tipo", TRASLADOS);
                startActivity(tipoMovimiento);
            }
        });

        fabUbicacionResponsable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tipoMovimiento = new Intent(getActivity(), MovementActivity.class);
                tipoMovimiento.putExtra("tipo", TRASLADOS_RESPONSABLE);
                startActivity(tipoMovimiento);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setUpMultiChoiceRecyclerView() {
        multiChoiceRecyclerView.setRecyclerColumnNumber(1);

        multiChoiceRecyclerView.setMultiChoiceToolbar((AppCompatActivity) getActivity(),
                toolbar,
                getString(R.string.app_name),
                "",
                R.color.colorPrimary, R.color.colorPrimaryDark);

        multiChoiceRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST)
        );

        mAdapter = new MisActivosAdapter(getActivity());
        multiChoiceRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Petición lista de usuarios {@link ActivoFijo}
     * y seteado de datos en el recyclerview {@link RecyclerView}
     */
    private class get_list_of_activos extends AsyncTask<String, Void, List<Activo>> {

        @Override
        protected List<Activo> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Activo> items) {
            Activo.MIS_ACTIVOS = items;
            mAdapter.notifyDataSetChanged();
            mProgressDialog.dismiss();
        }

        private List<Activo> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlMisActivos mParserXmlListaActivo = new ParserXmlMisActivos();
            List<Activo> items = null;

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
