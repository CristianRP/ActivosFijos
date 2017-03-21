package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 04/11/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Aprobacion {

    private int idMovimiento;
    private int idActivo;
    private String descripcion;
    private String estado;

    public static List<Aprobacion> INVENTARIO = new ArrayList<>();

    private Aprobacion getListadoAprobar(int idMovimiento) {
        for (Aprobacion activos:
             INVENTARIO) {
            if (activos.getIdMovimiento() == idMovimiento) {
                return activos;
            }
        }
        return null;
    }

    public Aprobacion() {}

    public Aprobacion(int idMovimiento, int idActivo, String descripcion, String estado) {
        this.idMovimiento = idMovimiento;
        this.idActivo = idActivo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(int idActivo) {
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
}
