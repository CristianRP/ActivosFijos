package com.gruporosul.activosfijos.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.activosfijos.bean.Usuario;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ParserXmlUsuario {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAF_Lista_Usuario";
    private static final String ETIQUETA_ITEM = "AF_Lista_Usuario";

    private static final String ETIQUETA_COD_USUARIO = "codUsuario";
    private static final String ETIQUETA_USUARIO = "usuario";

    public List<Usuario> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Usuario> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Usuario> usuarios = new ArrayList<>();

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

    private Usuario leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int codUsuario = 0;
        String usuario = null;

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
                    Log.e("usuario", usuario);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Usuario(
                codUsuario,
                usuario);
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
