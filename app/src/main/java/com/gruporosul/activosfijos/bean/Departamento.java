package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class Departamento {

    private String codDepartamento;
    private String departamento;

    public static List<Departamento> DEPARTAMENTOS = new ArrayList<>();

    public static Departamento getItem(String nombre) {
        for (Departamento item : DEPARTAMENTOS) {
            if (item.getCodDepartamento().equals(nombre)) {
                return item;
            }
        }
        return null;
    }


    public Departamento() {
    }

    public Departamento(String codDepartamento, String departamento) {
        this.codDepartamento = codDepartamento;
        this.departamento = departamento;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
