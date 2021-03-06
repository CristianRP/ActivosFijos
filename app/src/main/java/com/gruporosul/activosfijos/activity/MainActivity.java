package com.gruporosul.activosfijos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.ActivoFijo;
import com.gruporosul.activosfijos.fragment.AprobacionMovimientosFragment;
import com.gruporosul.activosfijos.fragment.FichaColaboradorFragment;
import com.gruporosul.activosfijos.fragment.FragmentDepartamento;
import com.gruporosul.activosfijos.fragment.MisActivosFragment;
import com.gruporosul.activosfijos.fragment.ScannActivo;
import com.gruporosul.activosfijos.fragment.UsuarioFragment;
import com.gruporosul.activosfijos.preferences.PrefManager;

import butterknife.BindView;
import butterknife.BindString;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    private PrefManager mPrefManager;

    /**
     * Binding de views con {@link ButterKnife}
     */

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindString(R.string.app_name) String appName;

    /**
     * Instancia del drawer layout
     */
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;

    /**
     * Instancia del navigation view
     */
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    public static MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPrefManager = new PrefManager(this);

        mainActivity = this;

        //setUserName();

        setToolbar(); //Setear toolbar como action bar

        if (mPrefManager.isLoggedIn()) {
            mNavigationView.getMenu().findItem(R.id.configuration_section).setVisible(true);
        }

        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }

        appName = getString(R.string.ficha_item);
        if (savedInstanceState == null) {
            selectedItem(appName);
        }

        //mDrawerLayout.openDrawer(GravityCompat.START);

        //throw new RuntimeException("I'm a cool exception and I crashed the main thread!");

    }

    /**
     * Establece la toolbar como action bar
     */
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(appName);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Establecer icono del drawer toggle
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUserName() {
        View navHeader = LayoutInflater.from(getApplicationContext()).inflate(R.layout.nav_header, mNavigationView);
        CircleImageView circleImageLogo = (CircleImageView) navHeader.findViewById(R.id.circle_image);
        TextView txtColaborador = (TextView) navHeader.findViewById(R.id.colaborador);
        TextView txtDispositivo = (TextView) navHeader.findViewById(R.id.email);
        Log.e("pre", mPrefManager.getKeyUser());
        /*Glide.with(this)
                .load(R.drawable.logo_rosul)
                .into(circleImageLogo);*/
        txtColaborador.setText(mPrefManager.getKeyUser().toUpperCase());
        txtDispositivo.setText(mPrefManager.getKeyIdDispositivo());
    }

    /**
     * Setear el contenido del drawer
     */
    private void setupDrawerContent(NavigationView navigationView) {

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Marcar item presionado
                item.setChecked(true);
                // Crear nuevo fragment
                String title = item.getTitle().toString();
                selectedItem(title);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }*/

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Cambio de fragments, segun el item seleccionado
     * @param title
     */
    private void selectedItem(String title) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_content, selectedFragment(title))
                .commit();

        mDrawerLayout.closeDrawers(); //Cerrar drawer

        setTitle(title); //Setear titulo actual

    }

    public Fragment selectedFragment(String title) {
        Fragment fragment = new Fragment();
        if (title.equals(getString(R.string.nav_departamento))) {
            if (mPrefManager.getKeyUser().equals("lhernandez")) {
                fragment = new FragmentDepartamento();
            } else {
                Toast.makeText(MainActivity.this, "No tienes acceso a esta función!", Toast.LENGTH_SHORT).show();
            }
        } else if (title.equals(getString(R.string.ficha_item))) {
            if (ActivoFijo.ACTIVOS_FIJOS.isEmpty()) {
                fragment = new FichaColaboradorFragment();
            } else {
                ActivoFijo.ACTIVOS_FIJOS.clear();
                fragment = new FichaColaboradorFragment();
            }
        } else if (title.equals(getString(R.string.movimientos_item))) {
            fragment = new UsuarioFragment();
        } else if (title.equals(getString(R.string.nav_activo))) {
            fragment = new ScannActivo();
        } else if (title.equals(getString(R.string.log_out_item))) {
            new MaterialDialog.Builder(this)
                    .title("Activos Fijos")
                    .content("Quieres cerra la sesión actual?")
                    .positiveText("Cerrar sesión")
                    .negativeText("Cancelar")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPrefManager.logout();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();
                        }
                    })
                    .show();
        } else if (title.equals(getString(R.string.mis_activos))) {
            fragment = new MisActivosFragment();
            //startActivity(new Intent(MainActivity.this, LocationActivity.class));
        } else if (title.equals(getString(R.string.inventario_item))) {
            if (mPrefManager.isEncargado()) {
                startActivity(new Intent(MainActivity.this, InventarioActivity.class));
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "No tienes permiso para esta acción!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (title.equals(getString(R.string.aprovacion))) {
            if (mPrefManager.isEncargado()) {
                fragment = new AprobacionMovimientosFragment();    
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "No tienes permiso para esta acción!", Toast.LENGTH_SHORT).show();
                finish();
            }
            
        }

        return fragment;
    }

    private void escanearCodigo() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("123");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    /*public void showExitDialog() {
        new MaterialDialog.Builder(getApplicationContext())
                .title(R.string.titulo_salir)
                .content(R.string.content_salir)
                .positiveText(R.string.btn_salir)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }*/

    /*private void showExitDialog() {

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);

        mDialog.setTitle(R.string.titulo_salir);
        mDialog.setMessage(R.string.content_salir);
        mDialog.setCancelable(false);
        mDialog.setPositiveButton(R.string.btn_salir, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });

        mDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * Nothing
                 *
            }
        });

        mDialog.show();

    }*/

}
