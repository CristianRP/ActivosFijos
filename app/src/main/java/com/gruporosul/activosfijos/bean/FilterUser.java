package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class FilterUser {

    private String codDepartamento;
    private String departamento;
    private int codUsuario;
    private String usuario;

    public static List<FilterUser> FILTER_USER = new ArrayList<>();

    public static FilterUser getItem(String cod) {
        for (FilterUser item : FILTER_USER) {
            if (item.getCodDepartamento().equals(cod)) {
                return item;
            }
        }
        return null;
    }

    public FilterUser() {
    }

    public FilterUser(String codDepartamento, String departamento, int codUsuario, String usuario) {
        this.codDepartamento = codDepartamento;
        this.departamento = departamento;
        this.codUsuario = codUsuario;
        this.usuario = usuario;
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

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
