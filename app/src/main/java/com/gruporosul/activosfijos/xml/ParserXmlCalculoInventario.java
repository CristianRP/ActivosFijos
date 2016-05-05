package com.gruporosul.activosfijos.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.activosfijos.bean.Diferencia;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 21-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ParserXmlCalculoInventario {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAF_Calculo_Inventario";
    private static final String ETIQUETA_ITEM = "AF_Calculo_Inventario";

    private static final String ETIQUETA_ID_ACTIVO_ESCANEADO = "idActivoEscaneado";
    private static final String ETIQUETA_ID_INVENTARIO = "idInventario";
    private static final String ETIQUETA_ID_ACTIVO = "idActivo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_ESTADO = "estado";
    private static final String ETIQUETA_ID_ESCANER = "escaner";

    public List<Diferencia> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Diferencia> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Diferencia> diferencias = new ArrayList<>();

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

    private Diferencia leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        String idActivoEscaneado = null;
        String idInventario = null;
        String idActivo = null;
        String descripcion = null;
        String estado = null;
        String escaner = null;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_ID_ACTIVO_ESCANEADO:
                    idActivoEscaneado = leerIdActivoEscaneado(pullParser);
                    Log.e("idEscaneado", idActivoEscaneado);
                    break;
                case ETIQUETA_ID_INVENTARIO:
                    idInventario = leerIdInventario(pullParser);
                    Log.e("idInvnetario", idInventario);
                    break;
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(pullParser);
                    Log.e("idActivo", "" + idActivo);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(pullParser);
                    Log.e("descripcion", descripcion);
                    break;
                case ETIQUETA_ESTADO:
                    estado = leerEstado(pullParser);
                    break;
                case ETIQUETA_ID_ESCANER:
                    escaner = leerEscaner(pullParser);
                    Log.e("escaner", escaner);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Diferencia(
                idActivoEscaneado,
                idInventario,
                idActivo,
                descripcion,
                estado,
                escaner);
    }

    private String leerIdActivoEscaneado(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO_ESCANEADO);
        String cod = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO_ESCANEADO);
        return cod;
    }

    private String leerIdInventario(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_INVENTARIO);
        String cod = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_INVENTARIO);
        return cod;
    }


    private String leerIdActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        String cod = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return cod;

    }

    private String leerDescripcion(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String desc = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);
        return desc;

    }

    private String leerEstado(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ESTADO);
        String estado = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ESTADO);
        return estado;

    }

    private String leerEscaner(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ESCANER);
        String cod = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ESCANER);
        return cod;
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
