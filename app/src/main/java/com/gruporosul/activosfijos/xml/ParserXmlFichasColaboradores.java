package com.gruporosul.activosfijos.xml;

import android.util.Xml;

import com.gruporosul.activosfijos.bean.FichaColaborador;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 18/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class ParserXmlFichasColaboradores {

    private static final String ns = null;
    private static final String ETIQUETA_ARRAY = "ArrayOfFichaColaborador";
    private static final String ETIQUETA_ITEM = "FichaColaborador";

    private static final String ETIQUETA_COD_FICHA = "cod_ficha";
    private static final String ETIQUETA_NFICHA = "nficha";
    private static final String ETIQUETA_USUARIO = "usuario";
    private static final String ETIQUETA_EMAIL = "email";
    private static final String ETIQUETA_ID_DEPARTAMENTO = "id_departamento";
    private static final String ETIQUETA_DESC_DEPARTAMENTO = "desc_departamento";

    public List<FichaColaborador> parsear(InputStream inputStream)
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

    private List<FichaColaborador> leerItems(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        List<FichaColaborador> fichaColaboradors = new ArrayList<>();

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

    private FichaColaborador leerItem(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {

        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);

        int codFicha = 0;
        String nFicha = null;
        String usuario = null;
        String email = null;
        String idDepartamento = null;
        String descDepartamento = null;

        while (mXmlPullParser.next() != XmlPullParser.END_TAG) {
            if (mXmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = mXmlPullParser.getName();
            switch (name) {
                case ETIQUETA_COD_FICHA:
                    codFicha = leerCodFicha(mXmlPullParser);
                    break;
                case ETIQUETA_NFICHA:
                    nFicha = leerNFicha(mXmlPullParser);
                    break;
                case ETIQUETA_USUARIO:
                    usuario = leerUsuario(mXmlPullParser);
                    break;
                case ETIQUETA_EMAIL:
                    email = leerEmail(mXmlPullParser);
                    break;
                case ETIQUETA_ID_DEPARTAMENTO:
                    idDepartamento = leerIdDepartamento(mXmlPullParser);
                    break;
                case ETIQUETA_DESC_DEPARTAMENTO:
                    descDepartamento = leerDescDepartamento(mXmlPullParser);
                    break;
                default:
                    saltarEtiqueta(mXmlPullParser);
                    break;
            }
        }

        return new FichaColaborador(
                codFicha,
                nFicha,
                usuario,
                email,
                idDepartamento,
                descDepartamento
        );
    }

    private int leerCodFicha(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COD_FICHA);
        int codFicha = Integer.parseInt(obtenerTexto(xmlPullParser));
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COD_FICHA);
        return codFicha;
    }

    private String leerNFicha(XmlPullParser xmlPullParser)
        throws XmlPullParserException, IOException {
        xmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NFICHA);
        String nficha = obtenerTexto(xmlPullParser);
        xmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NFICHA);
        return nficha;
    }

    private String leerUsuario(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {
        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_USUARIO);
        String usuario = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_USUARIO);
        return usuario;
    }

    private String leerEmail(XmlPullParser mXmlPullParse)
        throws XmlPullParserException, IOException {
        mXmlPullParse.require(XmlPullParser.START_TAG, ns, ETIQUETA_EMAIL);
        String email = obtenerTexto(mXmlPullParse);
        mXmlPullParse.require(XmlPullParser.END_TAG, ns, ETIQUETA_EMAIL);
        return email;
    }

    private String leerIdDepartamento(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {
        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ID_DEPARTAMENTO);
        String idDepartamento = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_ID_DEPARTAMENTO);
        return idDepartamento;
    }

    private String leerDescDepartamento(XmlPullParser mXmlPullParser)
        throws XmlPullParserException, IOException {
        mXmlPullParser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESC_DEPARTAMENTO);
        String descDepartamento = obtenerTexto(mXmlPullParser);
        mXmlPullParser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESC_DEPARTAMENTO);
        return descDepartamento;
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
