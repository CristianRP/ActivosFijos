package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 28-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class ActivoEscaneado {

    private int codColaborador;
    private String colaborador;
    private int idActivo;
    private String descripcion;
    private String estado;
    private String codDepartamento;
    private String departamento;

    public static List<ActivoEscaneado> ACTIVO_ESCANEADO = new ArrayList<>();

    public static ActivoEscaneado getItem(int idActivo) {
        for (ActivoEscaneado item : ACTIVO_ESCANEADO) {
            if (item.getIdActivo() == idActivo) {
                return item;
            }
        }
        return null;
    }

    public ActivoEscaneado() {
    }

    public ActivoEscaneado(int codColaborador, String colaborador, int idActivo, String descripcion, String estado, String codDepartamento, String departamento) {
        this.codColaborador = codColaborador;
        this.colaborador = colaborador;
        this.idActivo = idActivo;
        this.descripcion = descripcion;
        this.estado = estado;
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
