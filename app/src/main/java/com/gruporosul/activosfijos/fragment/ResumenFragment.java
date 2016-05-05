package com.gruporosul.activosfijos.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.morphingbutton.MorphingButton;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Identificador;
import com.gruporosul.activosfijos.volley.AppController;
import com.gruporosul.activosfijos.xml.ParserXmlNoInventario;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumenFragment extends Fragment {

    private static final String URL_IDENTIFICADOR_INVENTARIO = "http://200.30.160.117:8070/Servicioclientes.asmx/Identificador_Inventario";
    private static final String URL_NUEVO_INVENTARIO = "http://200.30.160.117:8070/Servicioclientes.asmx/Inventario";
    private static final String insert_inventario = "insert_inventario";
    private static final String TAG = ResumenFragment.class.getSimpleName();

    private EscanearActivosFragment mEscanearActivosFragment;

    private ProgressDialog mProgressDialog;

    @Bind(R.id.mbIniciar)
    MorphingButton mbIniciar;

    @Bind(R.id.txtTituloInventario)
    TextView mTxtTitulo;

    @Bind(R.id.txtFechaValue)
    TextView mTxtFecha;

    @Bind(R.id.txtUsuarioValue)
    TextView mTxtUsuario;

    @Bind(R.id.txtEstadoValue)
    TextView mTxtEstado;

    @Bind(R.id.txtUbicacionValue)
    TextView mTxtUbicacion;

    public ResumenFragment() {
        // Required empty public constructor
    }

    public static ResumenFragment newInstance(Bundle arguments){
        ResumenFragment f = new ResumenFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        ButterKnife.bind(this, view);

        getActivity().setTitle(getString(R.string.resumen_inventario));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.action_loading));
        mProgressDialog.show();

        new RequestIdentificadorInventario().execute(URL_IDENTIFICADOR_INVENTARIO);

        DateFormat dateFormat = SimpleDateFormat.getDateInstance();  //SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        mTxtFecha.setText(dateFormat.format(date));
        mTxtEstado.setText(getString(R.string.estado_value));
        mTxtUbicacion.setText(getArguments().getString("ubicacion"));
        mTxtUsuario.setText(getArguments().getString("colaborador"));

    }

    @OnClick(R.id.mbIniciar)
    void iniciarInventario() {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(getResources().getDimensionPixelSize(R.dimen.mb_height_56)) // 56 dp
                .width(getResources().getDimensionPixelSize(R.dimen.mb_height_56)) // 56 dp
                .height(getResources().getDimensionPixelSize(R.dimen.mb_height_56)) // 56 dp
                .color(getResources().getColor(R.color.mb_blue_dark)) // normal state color
                .colorPressed(getResources().getColor(R.color.mb_gray)) // pressed state color
                .icon(R.drawable.ic_done); // icon
        mbIniciar.morph(circle);
       /* Log.e("null?", mTxtUsuario.getText().toString()+
                mTxtUbicacion.getText().toString()+ mTxtEstado.getText().toString());*/
        insertInventario(mTxtUsuario.getText().toString(),
                         mTxtUbicacion.getText().toString(), mTxtEstado.getText().toString());
    }

    public void insertInventario(final String usuario, final String ubicacion,
                                 final String estado) {

        StringRequest insertInventario = new StringRequest(
                Request.Method.POST,
                URL_NUEVO_INVENTARIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Volley-Successful", "Inventario: \n" + response);
                        //mProgressDialog.dismiss();
                        escanActivos();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley-Error", "Inventario: \n" + error);
                        //mProgressDialog.dismiss();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("usuario", usuario);
                params.put("ubicacion", ubicacion);
                params.put("estado", estado);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(insertInventario, insert_inventario);

    }

    /**
     * Peticion del identificador del nuevo inventario
     * La función del metodo del web-service hace un conteo de los inventarios
     * que hay, luego le suma +1 para dar el id del nuevo inventario
     */
    private class RequestIdentificadorInventario extends AsyncTask<String, Void, List<Identificador>> {

        @Override
        protected List<Identificador> doInBackground(String... urls) {
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
        protected void onPostExecute(List<Identificador> items) {

            Identificador.ID = items;

            mTxtTitulo.setText(getString(R.string.titulo_inventario, Identificador.ID.get(0).getIdentificador()));

            mProgressDialog.dismiss();

        }

        private List<Identificador> parsearXmlDeUrl(String urlString)
                throws XmlPullParserException, IOException {

            InputStream mInputStream = null;
            ParserXmlNoInventario mParserXmlNoInventario = new ParserXmlNoInventario();
            List<Identificador> items = null;

            try {

                mInputStream = descargarContenido(urlString);
                items = mParserXmlNoInventario.parsear(mInputStream);

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

    public void escanActivos() {

        Bundle arguments = new Bundle();
        arguments.putString("idInventario", Identificador.ID.get(0).getIdentificador());
        arguments.putString("codColaborador", getArguments().getString("codColaborador"));

        mEscanearActivosFragment = EscanearActivosFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mEscanearActivosFragment, ResumenFragment.TAG);
        //mFragmentTransaction.addToBackStack(null); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}
