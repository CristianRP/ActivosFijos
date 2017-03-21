package com.gruporosul.activosfijos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.preferences.PrefManager;
import com.gruporosul.activosfijos.volley.AppController;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private static final String url_login = "http://200.30.160.117:8070/Servicioclientes.asmx/LoginUsuario";
    private static final String  tag_string_req = "login_req";
    private PrefManager mPrefManager;


    /**
     * Bindings de view's (Libreria  ButterKnife)
     */
    @Bind(R.id.txtLogin)
    TextView tituloLogin;
    @Bind(R.id.textUser)
    EditText textUser;
    @Bind(R.id.textPassword)
    EditText textPassword;
    @Bind(R.id.fabLogin)
    FloatingActionButton mFabLogin;
    @Bind(R.id.coordinatorLogin)
    CoordinatorLayout mCoodinatorLogin;
    @Bind(R.id.imgLogo)
    ImageView mImgLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPrefManager = new PrefManager(getApplicationContext());

        if (mPrefManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, InventarioActivity.class));
            finish();
        }

        getDeviceResolution();

        tituloLogin.setText(Html.fromHtml(getString(R.string.content_login)));

        mFabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Verifica la densidad de pixeles de pantalla y setea una imagen
     * diferente
     * @return
     */
    private String getDeviceResolution()
    {
        int density = getApplicationContext().getResources().getDisplayMetrics().densityDpi;
        switch (density)
        {
            case DisplayMetrics.DENSITY_MEDIUM:
                tituloLogin.setText("");
                mImgLogo.setImageResource(0);
                mImgLogo.setImageResource(R.drawable.logo_grupo_rosul_hdpi);
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                mImgLogo.setImageResource(0);
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }

    /**
     * Verifica que el formulario este lleno y realiza la petición
     */
    private void attemptLogin() {

        textUser.setError(null);
        textPassword.setError(null);

        String username = textUser.getText().toString();
        String password = textPassword.getText().toString();
        String idDispositivo =
                Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            textPassword.setError(getString(R.string.error_password_field_required));
            focusView = textPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            textUser.setError(getString(R.string.error_user_field_required));
            focusView = textUser;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgressDialog();
            LoginRequest(username, password, idDispositivo);
        }

        Log.e("ID-DEVICE", idDispositivo);

    }

    /**
     * ProgresDialog prefabricado para su uso posterior
     */
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage(getString(R.string.action_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    /**
     * Realiza petición a travez de volley, para consultar el metodo LoginUsuario
     * del web-service SOAP, el cual espera los siguientes parametros:
     * @param username
     * @param password
     * @param idDispositivo
     */
    public void LoginRequest(final String username, final String password, final String idDispositivo) {

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url_login,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        mProgressDialog.dismiss();
                        if (!TextUtils.isEmpty(response) && !response.contains("error")) {
                            isEncargado(username, password, idDispositivo);
                            mPrefManager.createLoginSession(username, password, idDispositivo, false);
                            startActivity(new Intent(LoginActivity.this, InventarioActivity.class));
                            finish();
                        } else {
                            showSnackbar(mCoodinatorLogin, getString(R.string.login_error));
                            textUser.setText("");
                            textPassword.setText("");
                            textUser.requestFocus();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", username);
                params.put("password", password);
                params.put("id", idDispositivo);
                return params;
            }

        };

        // Se añade la petición a la cola de peticiones de volley
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    /**
     * Snackbar prefabricada para un uso rapido y eficiente
     */
    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void isEncargado(final String username, final String password, final String idDispositivo) {
        StringRequest isEncardadoRequest = new StringRequest(
                Request.Method.POST,
                "http://200.30.160.117:8070/Servicioclientes.asmx/get_is_encargado_activos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {
                            mPrefManager.createLoginSession(username, password, idDispositivo, true);
                            startActivity(new Intent(LoginActivity.this, InventarioActivity.class));
                            finish();
                        } else {
                            mPrefManager.createLoginSession(username, password, idDispositivo, false);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("value", username);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(isEncardadoRequest);
    }

}
