package com.gruporosul.activosfijos.xml;

import android.util.Xml;

import com.gruporosul.activosfijos.bean.Movimientos;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/03/2017.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlListadoMovimientos {
    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfMovimiento";
    private static final String ETIQUETA_ITEM = "Movimiento";

    private static final String ETIQUETA_ID_MOVIMIENTO = "idMovimiento";
    private static final String ETIQUETA_ID_ACTIVO = "idActivo";
    private static final String ETIQUETA_DESCRIPCION_ACTIVO = "descripcionActivo";
    private static final String ETIQUETA_TIPO_MOVIMIENTO = "tipoMovimiento";
    private static final String ETIQUETA_UBICACION_ACTUAL = "ubicacionActual";
    private static final String ETIQUETA_DESC_UBICACION_ACTUAL = "descripcionUbicacionActual";
    private static final String ETIQUETA_NUEVA_UBICACION = "ubicacionNueva";
    private static final String ETIQUETA_DESC_UBICACION_NUEVA = "descripcionUbicacionNueva";
    private static final String ETIQUETA_FECHA_SOLICITO = "fechaSolicito";
    private static final String ETIQUETA_FECHA_PLANIFICADO = "fechaPlanificado";
    private static final String ETIQUETA_DIAS = "dias";
    private static final String ETIQUETA_FICHA_RESPONSABLE = "fichaResponsable";
    private static final String ETIQUETA_DESC_RESPONSABLE = "descripcionFichaResponsable";
    private static final String ETIQUETA_FICHA_NUEVO_RESPONSABLE = "fichaNuevoResponsable";
    private static final String ETIQUETA_DESC_NUEVO_RESPONSABLE = "descripcionFichaNuevoResponsable";
    private static final String ETIQUETA_USUARIO_SOLICITO = "usuarioSolicito";
    private static final String ETIQUETA_USUARIO__ACEPTO = "usuarioAcepto";
    private static final String ETIQUETA_USUARIO_VALIDO = "usuarioValido";
    private static final String ETIQUETA_MOTIVO = "motivo";
    private static final String ETIQUETA_USUARIO_AUTORIZA = "usuarioAutoriza";
    private static final String ETIQUETA_F_SOLICITO = "fSolicito";
    private static final String ETIQUETA_DIAS_SOLICITO = "diasDesdeQueSolicito";

    public List<Movimientos> parsear(InputStream in) throws XmlPullParserException, IOException {

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

    private List<Movimientos> leerItems(XmlPullParser xmlPullParser)
            throws XmlPullParserException, IOException {

        List<Movimientos> mMovimientos = new ArrayList<>();

        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ARRAY);

        while (xmlPullParser.next() != XmlPullParser.END_TAG) {

            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nombreEtiqueta = xmlPullParser.getName();

            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                mMovimientos.add(leerItem(xmlPullParser));
            } else {
                saltarEtiqueta(xmlPullParser);
            }

        }

        return mMovimientos;

    }

    private Movimientos leerItem(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {

        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        String idMovimiento = null;
        String idActivo = null;
        String descripcionActivo = null;
        String tipoMovimiento = null;
        String ubicacionActual = null;
        String descripcionUbicacionActual = null;
        String ubicacionNueva = null;
        String descripcionUbicacionNueva = null;
        String fechaSolicito = null;
        String fechaPlanificado = null;
        String dias = null;
        String fichaResponsable = null;
        String descripcionFichaResponsable = null;
        String fichaNuevoResponsable = null;
        String descripcionFichaNuevoResponsable = null;
        String usuarioSolicito = null;
        String usuarioAcepto = null;
        String usuarioValido = null;
        String motivo = null;
        String usuarioAutoriza = null;
        String fSolicito = null;
        String diasDesdeQueSolicito = null;


        while (pullParser.next() != XmlPullParser.END_TAG) {

            if (pullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = pullParser.getName();

            switch (name) {
                case ETIQUETA_ID_MOVIMIENTO:
                    idMovimiento = leerIdMovimiento(pullParser);
                    break;
                case ETIQUETA_ID_ACTIVO:
                    idActivo = leerIdActivo(pullParser);
                    break;
                case ETIQUETA_DESCRIPCION_ACTIVO:
                    descripcionActivo = leerDescripcionActivo(pullParser);
                    break;
                case ETIQUETA_TIPO_MOVIMIENTO:
                    tipoMovimiento = leerTipoMovimiento(pullParser);
                    break;
                case ETIQUETA_UBICACION_ACTUAL:
                    ubicacionActual = leerUbicacionActual(pullParser);
                    break;
                case ETIQUETA_DESC_UBICACION_ACTUAL:
                    descripcionUbicacionActual = leerDescUbicacionActual(pullParser);
                    break;
                case ETIQUETA_NUEVA_UBICACION:
                    ubicacionNueva = leerUbicacionNueva(pullParser);
                    break;
                case ETIQUETA_DESC_UBICACION_NUEVA:
                    descripcionUbicacionNueva = leerDescUbicacionNueva(pullParser);
                    break;
                case ETIQUETA_FECHA_SOLICITO:
                    fechaSolicito = leerFechaSolicito(pullParser);
                    break;
                case ETIQUETA_FECHA_PLANIFICADO:
                    fechaPlanificado = leerFechaPlanificado(pullParser);
                    break;
                case ETIQUETA_DIAS:
                    dias = leerDias(pullParser);
                    break;
                case ETIQUETA_FICHA_RESPONSABLE:
                    fichaResponsable = leerFichaResponsable(pullParser);
                    break;
                case ETIQUETA_DESC_RESPONSABLE:
                    descripcionFichaResponsable = leerDescResponsable(pullParser);
                    break;
                case ETIQUETA_FICHA_NUEVO_RESPONSABLE:
                    fichaNuevoResponsable = leerFichaNuevoResponsable(pullParser);
                    break;
                case ETIQUETA_DESC_NUEVO_RESPONSABLE:
                    descripcionFichaNuevoResponsable = leerDesNuevoResponsable(pullParser);
                    break;
                case ETIQUETA_USUARIO_SOLICITO:
                    usuarioSolicito = leerUsuarioSolicito(pullParser);
                    break;
                case ETIQUETA_USUARIO__ACEPTO:
                    usuarioAcepto = leerUsuarioAcepto(pullParser);
                    break;
                case ETIQUETA_USUARIO_VALIDO:
                    usuarioValido = leerUsuarioValido(pullParser);
                    break;
                case ETIQUETA_MOTIVO:
                    motivo = leerMotivo(pullParser);
                    break;
                case ETIQUETA_USUARIO_AUTORIZA:
                    usuarioAutoriza = leerUsuarioAutoriza(pullParser);
                    break;
                case ETIQUETA_F_SOLICITO:
                    fSolicito = leerFSolicito(pullParser);
                    break;
                case ETIQUETA_DIAS_SOLICITO:
                    diasDesdeQueSolicito = leerDiasDesdeQueSolicito(pullParser);
                    break;
                default:
                    saltarEtiqueta(pullParser);
                    break;
            }

        }

        return new Movimientos(
                idMovimiento,
                idActivo,
                descripcionActivo,
                tipoMovimiento,
                ubicacionActual,
                descripcionUbicacionActual,
                ubicacionNueva,
                descripcionUbicacionNueva,
                fechaSolicito,
                fechaPlanificado,
                dias,
                fichaResponsable,
                descripcionFichaResponsable,
                fichaNuevoResponsable,
                descripcionFichaNuevoResponsable,
                usuarioSolicito,
                usuarioAcepto,
                usuarioValido,
                motivo,
                usuarioAutoriza,
                fSolicito,
                diasDesdeQueSolicito
        );
    }

    private String leerIdMovimiento(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_MOVIMIENTO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_MOVIMIENTO);
        return value;
    }

    private String leerIdActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_ACTIVO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_ACTIVO);
        return value;
    }

    private String leerDescripcionActivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION_ACTIVO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION_ACTIVO);
        return value;
    }

    private String leerTipoMovimiento(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPO_MOVIMIENTO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TIPO_MOVIMIENTO);
        return value;
    }

    private String leerUbicacionActual(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_UBICACION_ACTUAL);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_UBICACION_ACTUAL);
        return value;
    }

    private String leerDescUbicacionActual(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESC_UBICACION_ACTUAL);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESC_UBICACION_ACTUAL);
        return value;
    }

    private String leerUbicacionNueva(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NUEVA_UBICACION);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NUEVA_UBICACION);
        return value;
    }

    private String leerDescUbicacionNueva(XmlPullParser pullParser)
        throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESC_UBICACION_NUEVA);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESC_UBICACION_NUEVA);
        return value;
    }

    private String leerFechaSolicito(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_SOLICITO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_SOLICITO);
        return value;
    }

    private String leerFechaPlanificado(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_PLANIFICADO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_PLANIFICADO);
        return value;
    }

    private String leerDias(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DIAS);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DIAS);
        return value;
    }

    private String leerFichaResponsable(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FICHA_RESPONSABLE);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FICHA_RESPONSABLE);
        return value;
    }

    private String leerDescResponsable(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESC_RESPONSABLE);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESC_RESPONSABLE);
        return value;
    }

    private String leerFichaNuevoResponsable(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FICHA_NUEVO_RESPONSABLE);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FICHA_NUEVO_RESPONSABLE);
        return value;
    }

    private String leerDesNuevoResponsable(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESC_NUEVO_RESPONSABLE);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESC_NUEVO_RESPONSABLE);
        return value;
    }

    private String leerUsuarioSolicito(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO_SOLICITO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO_SOLICITO);
        return value;
    }

    private String leerUsuarioAcepto(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO__ACEPTO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO__ACEPTO);
        return value;
    }

    private String leerUsuarioValido(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO_VALIDO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO_VALIDO);
        return value;
    }

    private String leerMotivo(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_MOTIVO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_MOTIVO);
        return value;
    }

    private String leerUsuarioAutoriza(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO_AUTORIZA);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO_AUTORIZA);
        return value;
    }

    private String leerFSolicito(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_F_SOLICITO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_F_SOLICITO);
        return value;
    }

    private String leerDiasDesdeQueSolicito(XmlPullParser pullParser)
            throws XmlPullParserException, IOException {
        pullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DIAS_SOLICITO);
        String value = obtenerTexto(pullParser);
        pullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DIAS_SOLICITO);
        return value;
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
