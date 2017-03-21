package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 02/11/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Encabezado {

    private int idEncabezado;

    public Encabezado() {
    }

    public Encabezado(int idEncabezado) {
        this.idEncabezado = idEncabezado;
    }

    public static List<Encabezado> ID_ENCABEZADO = new ArrayList<>();

    public Encabezado getEncabezado(int idEncabezado) {
        for (Encabezado encabezado:ID_ENCABEZADO) {
            if (encabezado.getIdEncabezado() == idEncabezado) {
                return encabezado;
            }
        }
        return null;
    }

    public int getIdEncabezado() {
        return idEncabezado;
    }

    public void setIdEncabezado(int idEncabezado) {
        this.idEncabezado = idEncabezado;
    }
}
