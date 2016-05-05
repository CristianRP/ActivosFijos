package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 21-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class Diferencia {

    private String idActivoEscaneado;
    private String idInventario;
    private String idActivo;
    private String descripcion;
    private String estado;
    private String escaner;

    public static List<Diferencia> LISTADO_DIFERENCIA = new ArrayList<>();

    public static Diferencia getItem(String nombre) {
        for (Diferencia item : LISTADO_DIFERENCIA) {
            if (item.getIdActivo().equals(nombre)) {
                return item;
            }
        }
        return null;
    }

    public Diferencia() {
    }

    public Diferencia(String idActivoEscaneado, String idInventario, String idActivo, String descripcion, String estado, String escaner) {
        this.idActivoEscaneado = idActivoEscaneado;
        this.idInventario = idInventario;
        this.idActivo = idActivo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.escaner = escaner;
    }

    public String getIdActivoEscaneado() {
        return idActivoEscaneado;
    }

    public void setIdActivoEscaneado(String idActivoEscaneado) {
        this.idActivoEscaneado = idActivoEscaneado;
    }

    public String getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(String idInventario) {
        this.idInventario = idInventario;
    }

    public String getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(String idActivo) {
        this.idActivo = idActivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEscaner() {
        return escaner;
    }

    public void setEscaner(String escaner) {
        this.escaner = escaner;
    }
}
