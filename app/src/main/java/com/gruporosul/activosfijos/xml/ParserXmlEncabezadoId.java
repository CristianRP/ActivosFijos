package com.gruporosul.activosfijos.xml;

import android.util.Xml;

import com.gruporosul.activosfijos.bean.Encabezado;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 18-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ParserXmlEncabezadoId {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfEncabezado";
    private static final String ETIQUETA_ITEM = "Encabezado";

    private static final String ETIQUETA_ID_ENCABEZADO = "idEncabecado";
    public static int ID_ENCABEZADO = 0;

    public List<Encabezado> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Encabezado> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Encabezado> identificadors = new ArrayList<>();

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {

            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = xmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                identificadors.add(leerItem(xmlPullParser));
            } else {
                saltarEtiqueta(xmlPullParser);
            }

        }

        return identificadors;

    }

    private Encabezado leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int identificador = 0;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_ID_ENCABEZADO:
                    identificador = leerId(pullParser);
                    ID_ENCABEZADO = identificador;
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Encabezado(
                identificador
        );
    }

    private int leerId(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ENCABEZADO);
        int id = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ENCABEZADO);
        return id;

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
