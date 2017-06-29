package com.gruporosul.activosfijos.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gruporosul.activosfijos.R;

import butterknife.BindView;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 *
 * A simple {@link Fragment} subclass.
 */
public class FichaColaboradorFragment extends Fragment {

    private ActivosFragment mActivosFragment;

    public static final String TAG = FichaColaboradorFragment.class.getSimpleName();

    @BindString(R.string.hint_scan)
    String mHintScan;

    @BindView(R.id.txtCodigo)
    EditText mSearchCodigo;

    @BindView(R.id.text_layout_codigo)
    TextInputLayout mTextIL;

    public FichaColaboradorFragment() {
        // Required empty public constructor
    }

    public static FichaColaboradorFragment newInstance(Bundle arguments) {
        FichaColaboradorFragment f = new FichaColaboradorFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ficha_colaborador, container, false);

        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.searchCodigo)
    void buscarCodigo() {
        mSearchCodigo.setError(null);
        if (TextUtils.isEmpty(mSearchCodigo.getText().toString())) {
            mSearchCodigo.setError(getString(R.string.error_codigo));
        } else {
            scannUser(mSearchCodigo.getText().toString());
            mSearchCodigo.setText("");
            hideKeyboard(getView());
        }

    }

    @OnClick(R.id.escanearCodigo)
    void escanearCodigo() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(mHintScan);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
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
                toast = "El código escaneado es incorrecto.";
            } else {
                scannUser(result.getContents());
                toast = "Código escaneado: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    /**
     * Espera el resultado del escaner y transfiere
     * los datos a {@link ActivosFragment}
     * @param result
     */
    public void scannUser(String result) {

        Bundle arguments = new Bundle();
        arguments.putString("codColaborador", result);

        mActivosFragment = ActivosFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mActivosFragment, ActivosFragment.TAG);
        mFragmentTransaction.addToBackStack(null); // Agrega a la pila el fragmento para poder retroceder
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();

    }


    /**
     * Esconde el teclado al momento de presionar el botón buscar
     * @param v
     */
    private void hideKeyboard(View v) {
        InputMethodManager tecladoVirtual =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE );
        tecladoVirtual.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
