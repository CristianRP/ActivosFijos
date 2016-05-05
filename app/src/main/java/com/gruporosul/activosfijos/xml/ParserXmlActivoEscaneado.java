package com.gruporosul.activosfijos.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.activosfijos.bean.ActivoEscaneado;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ParserXmlActivoEscaneado {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAF_Resultado_Escaneado";
    private static final String ETIQUETA_ITEM = "AF_Resultado_Escaneado";


    private static final String ETIQUETA_COD_USUARIO = "codColaborador";
    private static final String ETIQUETA_USUARIO = "colaborador";
    private static final String ETIQUETA_ID_ACTIVO = "idActivo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_ESTADO = "estado";
    private static final String ETIQUETA_COD_DEPARTAMENTO = "codDepartamento";
    private static final String ETIQUETA_DEPARTAMENTO = "departamento";

    public static String NOMBRE_COLABORADOR = null;
    public static int TOTAL_ACTIVOS = 0;

    public List<ActivoEscaneado> parsear(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            xmlPullParser.setInput(in, null);
            xmlPullParser.nextTag();
            return leerItems(xmlPullParser);
        } finally {
            in.close();
        }

    }

    private List<ActivoEscaneado> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<ActivoEscaneado> mActivo = new ArrayList<>();

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {

            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = xmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                mActivo.add(leerItem(xmlPullParser));
            } else {
                saltarEtiqueta(xmlPullParser);
            }

        }

        return mActivo;

    }

    private ActivoEscaneado leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int codUsuario = 0;
        String usuario = null;
        int idActivo = 0;
        String descripcion = null;
        String estado = null;
        String codDepartamento = null;
        String departamento = null;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_COD_USUARIO:
                    codUsuario = leerCodUsuario(pullParser);
                    Log.e("codUsuario", "" + codUsuario);
                    break;
                case ETIQUETA_USUARIO:
                    usuario = leerUsuario(pullParser);
                    NOMBRE_COLABORADOR = usuario;
                    Log.e("usuario", usuario);
                    break;
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(pullParser);
                    Log.e("idActivo", " " + idActivo);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(pullParser);
                    Log.e("descripcion", descripcion);
                    break;
                case ETIQUETA_ESTADO:
                    estado = leerEstado(pullParser);
                    Log.e("estado", estado);
                    break;
                case ETIQUETA_COD_DEPARTAMENTO:
                    codDepartamento = leerCodDepartamento(pullParser);
                    Log.e("codDepartamento", codDepartamento);
                    break;
                case ETIQUETA_DEPARTAMENTO:
                    departamento = leerDepartamento(pullParser);
                    Log.e("departamento", departamento);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new ActivoEscaneado(
                codUsuario,
                usuario,
                idActivo,
                descripcion,
                estado,
                codDepartamento,
                departamento);
    }


    private int leerCodUsuario(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_USUARIO);
        int codUsuario = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_USUARIO);
        return codUsuario;

    }

    private String leerUsuario(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO);
        String usuario = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO);
        return usuario;

    }

    private int leerIdActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        int idActivo = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return idActivo;

    }

    private String leerDescripcion(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String descripcion = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);

        return descripcion;

    }

    private String leerEstado(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ESTADO);
        String estado = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ESTADO);

        return estado;

    }

    private String leerCodDepartamento(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_DEPARTAMENTO);
        String codDepa = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_DEPARTAMENTO);
        return codDepa;

    }

    private String leerDepartamento(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DEPARTAMENTO);
        String depa = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DEPARTAMENTO);
        return depa;

    }

    private String obtenerTexto(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        String resultado = "";

        if (pullParser.next() == XmlPullParser.TEXT) {
            resultado = pullParser.getText();
            pullParser.nextTag();
        }

        return resultado;

    }

    private void saltarEtiqueta(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        if (pullParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (pullParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }

    }

}
