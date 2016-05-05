package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 19-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class Identificador {

    private String identificador;

    public static List<Identificador> ID = new ArrayList<>();

    public static Identificador getItem(String cod) {
        for (Identificador item : ID) {
            if (item.getIdentificador().equals(cod)) {
                return item;
            }
        }
        return null;
    }

    public Identificador() {
    }

    public Identificador(String identificador) {
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
