package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 28-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ActivoFijo {

    private int codColaborador;
    private String colaborador;
    private int idActivo;
    private String descripcion;
    private String estado;
    private int totalActivos;
    private String codDepartamento;
    private String departamento;

    public static List<ActivoFijo> ACTIVOS_FIJOS = new ArrayList<>();

    public static ActivoFijo getItem(int idActivo) {
        for (ActivoFijo item : ACTIVOS_FIJOS) {
            if (item.getIdActivo() == idActivo) {
                return item;
            }
        }
        return null;
    }

    public ActivoFijo() {
    }

    public ActivoFijo(int codColaborador, String colaborador,
                      int idActivo, String descripcion, String estado,
                      int totalActivos, String codDepartamento, String departamento) {
        this.codColaborador = codColaborador;
        this.colaborador = colaborador;
        this.idActivo = idActivo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.totalActivos = totalActivos;
        this.codDepartamento = codDepartamento;
        this.departamento = departamento;
    }

    public int getCodColaborador() {
        return codColaborador;
    }

    public void setCodColaborador(int codColaborador) {
        this.codColaborador = codColaborador;
    }

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
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

    public int getTotalActivos() {
        return totalActivos;
    }

    public void setTotalActivos(int totalActivos) {
        this.totalActivos = totalActivos;
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
