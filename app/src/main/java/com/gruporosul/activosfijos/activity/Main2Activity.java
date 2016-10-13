package com.gruporosul.activosfijos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gruporosul.activosfijos.R;
import com.gruporosul.activosfijos.bean.Activo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {

    @Bind(R.id.content_main2)
    LinearLayout contentMain2;
    @BindString(R.string.motivo)
    String motivo;
    @BindString(R.string.responsable)
    String responsable;
    @BindString(R.string.nueva_ubicacion)
    String nuevaUbicacion;
    @BindString(R.string.is_provitional)
    String isProvitional;
    @BindString(R.string.nuevo_responsable)
    String nuevoResponsable;
    @BindString(R.string.seleccionar_ubicacion)
    String seleccionarUbicacion;

    private static float gravity = 17f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent tipo = getIntent();
        Intent activos = getIntent();
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (5 * scale + 0.5f);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        Activo mActivo = Activo.MIS_ACTIVOS.get(0);
        if (tipo.getStringExtra("tipo").equals("ubicacion")) {
            TextView txtFecha = createTextViews("Fecha: " + dateFormat.format(date), dpAsPixels);
            TextInputLayout txtMotivo = createTextInputLayout(motivo);
            TextView tvResponsable = createTextViews(responsable, dpAsPixels);
            TextView tvNombre = createTextViews(mActivo.getnFicha(), dpAsPixels);
            TextView tvCodFicha = createTextViews(Integer.toString(mActivo.getCodFicha()), dpAsPixels);
            TextView tvNuevaUbicacion = createTextViews(nuevaUbicacion, dpAsPixels);
            CheckBox checkBoxProvisional = createCheckBoxes(isProvitional, dpAsPixels);
            Button btnUbicacion = createButtons(seleccionarUbicacion, dpAsPixels);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.END;
            txtFecha.setLayoutParams(params);
            tvCodFicha.setLayoutParams(params);
            contentMain2.addView(txtFecha);
            contentMain2.addView(tvResponsable);
            contentMain2.addView(tvCodFicha);
            contentMain2.addView(tvNombre);
            contentMain2.addView(txtMotivo);
            contentMain2.addView(tvNuevaUbicacion);
            contentMain2.addView(btnUbicacion);
            contentMain2.addView(checkBoxProvisional);
        }

    }

    private TextInputLayout createTextInputLayout(String content) {
        TextInputLayout tipLayout = new TextInputLayout(this);
        tipLayout.addView(createTextInputEditTexts(content));
        return tipLayout;
    }

    private TextInputEditText createTextInputEditTexts(String content) {
        TextInputEditText tipEditText = new TextInputEditText(this);
        tipEditText.setHint(content);
        tipEditText.setTextSize(gravity);
        tipEditText.setTag(content);
        return tipEditText;
    }

    private TextView createTextViews(String content, int padding) {
        TextView textView = new TextView(this);
        textView.setText(content);
        textView.setTextSize(gravity);
        textView.setTag(content);
        textView.setPadding(0, padding, 0, padding);
        return textView;
    }

    private Button createButtons(String content, int padding) {
        Button button = new Button(this);
        button.setText(content);
        button.setPadding(0, 0, 0, padding);
        return button;
    }

    private CheckBox createCheckBoxes(String content, int padding) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(content);
        checkBox.setTag(content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END;
        checkBox.setLayoutParams(params);
        checkBox.setTextSize(gravity);
        checkBox.setPadding(0, padding, 0, 0);
        return checkBox;
    }
}
