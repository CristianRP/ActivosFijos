package com.gruporosul.activosfijos.xml;

import android.util.Xml;

import com.gruporosul.activosfijos.bean.Aprobacion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 04/11/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlAprobacionInventario {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAprobarInventario";
    private static final String ETIQUETA_ITEM = "AprobarInventario";
    private static final String ETIQUETA_ID_MOVIMIENTO = "idMovimiento";
    private static final String ETIQUETA_ID_ACTIVO = "idActivo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_ESTADO = "estado";

    public List<Aprobacion> parsear(InputStream inputStream)
            throws XmlPullParserException, IOException {
        try {
            XmlPullParser mXmlPullParser = Xml.newPullParser();
            mXmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            mXmlPullParser.setInput(inputStream, null);
            mXmlPullParser.nextTag();
            return leerItems(mXmlPullParser);
        } finally {
            inputStream.close();
        }
    }

    private List<Aprobacion> leerItems(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        List<Aprobacion> fichaColaboradors = new ArrayList<>();

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {
            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = mXmlPullParser.getName();
            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                fichaColaboradors.add(leerItem(mXmlPullParser));
            } else {
                saltarEtiqueta(mXmlPullParser);
            }
        }
        return fichaColaboradors;
    }

    private Aprobacion leerItem(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int idMovimiento = 0;
        int idActivo = 0;
        String descripcion = null;
        String estado = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {
            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mXmlPullParser.getName();
            switch (name) {
                case ETIQUETA_ID_MOVIMIENTO:
                    idMovimiento = leerIdMovimiento(mXmlPullParser);
                    break;
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(mXmlPullParser);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(mXmlPullParser);
                    break;
                case ETIQUETA_ESTADO:
                    estado = leerEstado(mXmlPullParser);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;
            }
        }

        return new Aprobacion(
                idMovimiento,
                idActivo,
                descripcion,
                estado
        );
    }
    private int leerIdMovimiento(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_MOVIMIENTO);
        int codFicha = Integer.parseInt(obtenerTexto(xmlPullParser));
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_MOVIMIENTO);
        return codFicha;
    }

    private int leerIdActivo(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        int codFicha = Integer.parseInt(obtenerTexto(xmlPullParser));
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return codFicha;
    }

    private String leerDescripcion(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String codFicha = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);
        return codFicha;
    }

    private String leerEstado(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ESTADO);
        String codFicha = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ESTADO);
        return codFicha;
    }

    private String obtenerTexto(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {

        String result = "";
        if (mXmlPullParser.next() == XmlPullParser.TEXT) {
            result = mXmlPullParser.getText();
            mXmlPullParser.nextTag();
        }

        return result;
    }

    private void saltarEtiqueta(XmlPullParser mXmlPullParser)
            throws XmlPullParserException, IOException {
        if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (mXmlPullParser.next()) {
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
