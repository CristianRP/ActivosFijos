package com.gruporosul.activosfijos.xml;

import android.util.Xml;

import com.gruporosul.activosfijos.bean.Caracteristica;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22/08/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlActivoDescripcion {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAF_Caracteristicas_Activo";
    private static final String ETIQUETA_ITEM = "AF_Caracteristicas_Activo";

    private static final String ETIQUETA_ID_ACTIVO = "idActivo";
    private static final String ETIQUETA_ID_CARACTERISTICA = "caracteristica";
    private static final String ETIQUETA_DESC_CARACTERISTICA = "desCaracteristica";
    private static final String ETIQUETA_VALOR = "valor";

    public List<Caracteristica> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Caracteristica> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Caracteristica> diferencias = new ArrayList<>();

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {

            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = xmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                diferencias.add(leerItem(xmlPullParser));
            } else {
                saltarEtiqueta(xmlPullParser);
            }

        }

        return diferencias;

    }

    private Caracteristica leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int idActivo = 0;
        int idCaracteristica = 0;
        String desCaracteristica = null;
        String valor = null;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(pullParser);
                    break;
                case ETIQUETA_ID_CARACTERISTICA:
                    idCaracteristica = leerIdCaracteristica(pullParser);
                    break;
                case ETIQUETA_DESC_CARACTERISTICA:
                    desCaracteristica = leerCaracteristica(pullParser);
                    break;
                case ETIQUETA_VALOR:
                    valor = leerValor(pullParser);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Caracteristica(
                idActivo,
                idCaracteristica,
                desCaracteristica,
                valor
        );
    }

    private int leerIdActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        int cod = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return cod;
    }

    private int leerIdCaracteristica(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_CARACTERISTICA);
        int cod = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_CARACTERISTICA);
        return cod;
    }


    private String leerCaracteristica(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESC_CARACTERISTICA);
        String cod = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESC_CARACTERISTICA);
        return cod;

    }

    private String leerValor(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_VALOR);
        String desc = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_VALOR);
        return desc;

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
