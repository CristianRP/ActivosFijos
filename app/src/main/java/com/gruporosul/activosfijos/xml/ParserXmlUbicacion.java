package com.gruporosul.activosfijos.xml;

import android.util.Xml;

import com.gruporosul.activosfijos.bean.Ubicacion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 13/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlUbicacion {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfUbicaciones";
    private static final String ETIQUETA_ITEM = "Ubicaciones";

    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_COD_UBICACION = "cod_ubicacion";
    private static final String ETIQUETA_COD_UBICACION_PADRE = "cod_ubicacion_padre";
    private static final String ETIQUETA_NUBICACION_PADRE = "nubicacion_padre";

    public List<Ubicacion> parsear(InputStream in)
        throws XmlPullParserException, IOException {
        try {
            XmlPullParser mXmlPullParser = Xml.newPullParser();
            mXmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            mXmlPullParser.setInput(in, null);
            mXmlPullParser.nextTag();
            return leerItems(mXmlPullParser);
        } finally {
            in.close();
        }
    }

    private List<Ubicacion> leerItems(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        List<Ubicacion> ubicacions = new ArrayList<>();
        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);
        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {
            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = mXmlPullParser.getName();
            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                ubicacions.add(leerItem(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }
        }
        return ubicacions;
    }

    private Ubicacion leerItem(XmlPullParser xmlPullParser)
        throws  XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);
        String descripcion = null;
        int codUbicacion = 0;
        int codUbicacionPadre = 0;
        String nUbicacionPadre = null;

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = xmlPullParser.getName();
            switch (name) {
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(xmlPullParser);
                    break;
                case ETIQUETA_COD_UBICACION:
                    codUbicacion = leerCodUbicacion(xmlPullParser);
                    break;
                case ETIQUETA_COD_UBICACION_PADRE:
                    codUbicacionPadre = leerCodUbicacionPadre(xmlPullParser);
                    break;
                case ETIQUETA_NUBICACION_PADRE:
                    nUbicacionPadre = leerNUbicacionPadre(xmlPullParser);
                    break;
                default:
                    break;
            }
        }
        return new Ubicacion(
                codUbicacion,
                descripcion,
                codUbicacionPadre,
                nUbicacionPadre
        );
    }

    private String leerDescripcion(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String descripcion = getText(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);

        return descripcion;
    }

    private int leerCodUbicacion(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_UBICACION);
        int codUbicacion = Integer.parseInt(getText(xmlPullParser));
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_UBICACION);
        return codUbicacion;
    }

    private int leerCodUbicacionPadre(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_UBICACION_PADRE);
        int codUbicacionPadre = Integer.parseInt(getText(xmlPullParser));
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_UBICACION_PADRE);
        return codUbicacionPadre;
    }

    private String leerNUbicacionPadre(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NUBICACION_PADRE);
        String nUbicacioPadre = getText(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NUBICACION_PADRE);
        return nUbicacioPadre;
    }

    private String getText(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        String resultado = "";
        if (xmlPullParser.next() == XmlPullParser.TEXT) {
            resultado = xmlPullParser.getText();
            xmlPullParser.nextTag();
        }
        return resultado;
    }

    private void saltarEtiqueta(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (xmlPullParser.next()) {
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
