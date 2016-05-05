package com.gruporosul.activosfijos.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.activosfijos.bean.FilterUser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 23-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ParserXmlFilterUser {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAF_Lista_Departamento_Usuario";
    private static final String ETIQUETA_ITEM = "AF_Lista_Departamento_Usuario";

    private static final String ETIQUETA_COD_DEPARTAMENTO = "codDepartamento";
    private static final String ETIQUETA_DEPARTAMENTO = "departamento";
    private static final String ETIQUETA_COD_USUARIO = "codUsuario";
    private static final String ETIQUETA_USUARIO = "usuario";

    public List<FilterUser> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<FilterUser> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<FilterUser> usuarios = new ArrayList<>();

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {

            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = xmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                usuarios.add(leerItem(xmlPullParser));
            } else {
                saltarEtiqueta(xmlPullParser);
            }

        }

        return usuarios;

    }

    private FilterUser leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        String codDepartamento = null;
        String departamento = null;
        int codUsuario = 0;
        String usuario = null;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_COD_DEPARTAMENTO:
                    codDepartamento = leerCodDepartamento(pullParser);
                    Log.e("codDepartamento", codDepartamento);
                    break;
                case ETIQUETA_DEPARTAMENTO:
                    departamento = leerDepartamento(pullParser);
                    Log.e("departamento", departamento);
                    break;
                case ETIQUETA_COD_USUARIO:
                    codUsuario = leerCodUsuario(pullParser);
                    Log.e("codUsuario", "" + codUsuario);
                    break;
                case ETIQUETA_USUARIO:
                    usuario = leerUsuario(pullParser);
                    Log.e("usuario", usuario);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new FilterUser(
                codDepartamento,
                departamento,
                codUsuario,
                usuario);
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

    private int leerCodUsuario(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_USUARIO);
        int cod = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_USUARIO);
        return cod;

    }

    private String leerUsuario(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO);
        String depa = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO);
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
