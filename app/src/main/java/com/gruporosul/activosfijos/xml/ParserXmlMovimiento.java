package com.gruporosul.activosfijos.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.activosfijos.bean.Movimiento;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ParserXmlMovimiento {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfAF_MOVIMIENTOS";
    private static final String ETIQUETA_ITEM = "AF_MOVIMIENTOS";


    private static final String ETIQUETA_ID_MOVIMIENTO = "idMovimiento";
    private static final String ETIQUETA_TIPO_MOVIMIENTO = "tipoMovimiento";
    private static final String ETIQUETA_FECHA = "fecha";
    private static final String ETIQUETA_MOTIVO = "motivo";
    private static final String ETIQUETA_COD_COLABORADOR_ORIGEN = "codColaboradorOrigen";
    private static final String ETIQUETA_NOMBRE_COLABORADOR_ORIGEN = "nombreColaboradorOrigen";
    private static final String ETIQUETA_COD_COLABORADOR_DESTINO = "codColaboradorDestino";
    private static final String ETIQUETA_NOMBRE_COLABORADOR_DESTINO = "nombreColaboradorDestino";
    private static final String ETIQUETA_ID_ACTIVO = "idActivo";

    public List<Movimiento> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Movimiento> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Movimiento> mMovimiento = new ArrayList<>();

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {

            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = xmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                mMovimiento.add(leerItem(xmlPullParser));
            } else {
                saltarEtiqueta(xmlPullParser);
            }

        }

        return mMovimiento;

    }

    private Movimiento leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        String idMovimiento = null;
        String tipoMovimiento = null;
        String fecha = null;
        String motivo = null;
        String codColaboradorOrigen = null;
        String nombreColaboradorOrigen = null;
        String codColaboradorDestino = null;
        String nombreColaboradorDestino = null;
        String idActivo = null;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_ID_MOVIMIENTO:
                    idMovimiento = leerMovimiento(pullParser);
                    Log.e("idMovimiento", "" + idMovimiento);
                    break;
                case ETIQUETA_TIPO_MOVIMIENTO:
                    tipoMovimiento = leerTipoMovimiento(pullParser);
                    Log.e("tipoMovimiento", tipoMovimiento);
                    break;
                case ETIQUETA_FECHA:
                    fecha = leerFecha(pullParser);
                    Log.e("fecha", " " + fecha);
                    break;
                case ETIQUETA_MOTIVO:
                    motivo = leerMotivo(pullParser);
                    Log.e("motivo", motivo);
                    break;
                case ETIQUETA_COD_COLABORADOR_ORIGEN:
                    codColaboradorOrigen = leerCodColaboradorOrigen(pullParser);
                    Log.e("codColaboradorOrigen", codColaboradorOrigen);
                    break;
                case ETIQUETA_NOMBRE_COLABORADOR_ORIGEN:
                    nombreColaboradorOrigen = leerNombreColaboradorOrigen(pullParser);
                    Log.e("nombreColaboradorOrigen", " " + nombreColaboradorOrigen);
                    break;
                case ETIQUETA_COD_COLABORADOR_DESTINO:
                    codColaboradorDestino = leerCodColaboradorDestino(pullParser);
                    Log.e("codColaboradorDestino", codColaboradorDestino);
                    break;
                case ETIQUETA_NOMBRE_COLABORADOR_DESTINO:
                    nombreColaboradorDestino = leerNombreColaboradorDestino(pullParser);
                    Log.e("nameColaboradorDestino", nombreColaboradorDestino);
                    break;
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(pullParser);
                    Log.e("idActivo", idActivo);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Movimiento(
                idMovimiento,
                tipoMovimiento,
                fecha,
                motivo,
                codColaboradorOrigen,
                nombreColaboradorOrigen,
                nombreColaboradorDestino,
                codColaboradorDestino,
                idActivo);
    }


    private String leerMovimiento(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_MOVIMIENTO);
        String idMovimiento = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_MOVIMIENTO);
        return idMovimiento;

    }

    private String leerTipoMovimiento(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPO_MOVIMIENTO);
        String tipo = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TIPO_MOVIMIENTO);
        return tipo;

    }

    private String leerFecha(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA);
        String fecha = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA);
        return fecha;

    }

    private String leerMotivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_MOTIVO);
        String motivo = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_MOTIVO);

        return motivo;

    }

    private String leerCodColaboradorOrigen(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_COLABORADOR_ORIGEN);
        String codOrigen = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_COLABORADOR_ORIGEN);

        return codOrigen;

    }

    private String leerNombreColaboradorOrigen(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NOMBRE_COLABORADOR_ORIGEN);
        String nombreOrigen = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NOMBRE_COLABORADOR_ORIGEN);

        return nombreOrigen;

    }

    private String leerNombreColaboradorDestino(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NOMBRE_COLABORADOR_DESTINO);
        String nombreDestino = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NOMBRE_COLABORADOR_DESTINO);
        return nombreDestino;

    }

    private String leerIdActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        String id = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return id;

    }

    private String leerCodColaboradorDestino(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_COLABORADOR_DESTINO);
        String id = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_COLABORADOR_DESTINO);
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
