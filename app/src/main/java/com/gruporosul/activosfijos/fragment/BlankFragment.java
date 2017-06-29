package com.gruporosul.activosfijos.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gruporosul.activosfijos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cristian Ramírez on 17-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 *
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    public static final String TAG = BlankFragment.class.getSimpleName();

    private FichaColaboradorFragment mFichaColaboradorFragment;

    @BindView(R.id.btnInicio)
    Button btnInicio;

    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance(Bundle arguments) {
        BlankFragment f = new BlankFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mBlankFragment = inflater.inflate(R.layout.fragment_blank, container, false);

        ButterKnife.bind(this, mBlankFragment);

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });

        return mBlankFragment;
    }

    public void inicio() {

        Bundle arguments = new Bundle();

        mFichaColaboradorFragment = FichaColaboradorFragment.newInstance(arguments);
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_content, mFichaColaboradorFragment, FichaColaboradorFragment.TAG);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE); //Agrega una transición al fragment

        mFragmentTransaction.commit();
    }

}
