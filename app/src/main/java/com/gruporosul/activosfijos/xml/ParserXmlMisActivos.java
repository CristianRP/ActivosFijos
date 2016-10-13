package com.gruporosul.activosfijos.xml;

import android.util.Log;
import android.util.Xml;

import com.gruporosul.activosfijos.bean.Activo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/09/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlMisActivos {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfActivoFijo";
    private static final String ETIQUETA_ITEM = "ActivoFijo";

    private static final String ETIQUETA_COD_FICHA = "cod_ficha";
    private static final String ETIQUETA_NFICHA = "nficha";
    private static final String ETIQUETA_ID_ACTIVO = "id_activo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_UBICACION = "ubicacion";
    private static final String ETIQUETA_PROVISIONAL = "es_provicional";
    private static final String ETIQUETA_FECHA_BAJA = "fecha_baja";
    private static final String ETIQUETA_COD_UBICACION = "cod_ubicacion";

    public List<Activo> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Activo> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Activo> mActivo = new ArrayList<>();

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

    private Activo leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int codFicha = 0;
        String nficha = null;
        int idActivo = 0;
        String descripcion = null;
        String ubicacion = null;
        boolean provisional = false;
        String fechaBaja = null;
        int codUbicacion = 0;

        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_COD_FICHA:
                    codFicha = leerCodFicha(pullParser);
                    break;
                case ETIQUETA_NFICHA:
                    nficha = leerNFicha(pullParser);
                    break;
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(pullParser);
                    break;
                case ETIQUETA_UBICACION:
                    ubicacion = leerUbicacion(pullParser);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion = leerDescripcion(pullParser);
                    break;
                case ETIQUETA_PROVISIONAL:
                    provisional = leerProvisional(pullParser);
                    Log.e(":v provisional", provisional + "");
                    break;
                case ETIQUETA_FECHA_BAJA:
                    fechaBaja = leerFechaBaja(pullParser);
                    break;
                case ETIQUETA_COD_UBICACION:
                    codUbicacion = leerCodUbicacion(pullParser);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Activo(
                codFicha,
                nficha,
                idActivo,
                descripcion,
                ubicacion,
                provisional,
                fechaBaja,
                codUbicacion
        );
    }

    private int leerCodFicha(XmlPullParser pullParser)
        throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_FICHA);
        int value = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_FICHA);
        return value;
    }

    private String leerNFicha(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NFICHA);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NFICHA);
        return value;
    }

    private int leerIdActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        int value = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return value;
    }

    private String leerDescripcion(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);
        return value;
    }

    private String leerUbicacion(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_UBICACION);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_UBICACION);
        return value;
    }


    private boolean leerProvisional(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PROVISIONAL);
        boolean codUsuario = Boolean.parseBoolean(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_PROVISIONAL);
        return codUsuario;
    }

    private String leerFechaBaja(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_BAJA);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_BAJA);
        return value;
    }

    private int leerCodUbicacion(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_UBICACION);
        int usuario = Integer.parseInt(obtenerTexto(pullParser));
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_UBICACION);
        return usuario;

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
