package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 13/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Ubicacion {

    private int codUbicacion;
    private String descripcion;
    private int codUbicacionPadre;
    private String nUbicacionPadre;

    public static List<Ubicacion> UBICACIONES = new ArrayList<>();

    public Ubicacion getUbicacion(int codUbicacion) {
        for (Ubicacion ubicacion : UBICACIONES) {
            if (ubicacion.getCodUbicacion() == codUbicacion) {
                return ubicacion;
            }
        }
        return null;
    }

    public Ubicacion() {
    }

    public Ubicacion(int codUbicacion, String descripcion, int codUbicacionPadre, String nUbicacionPadre) {
        this.codUbicacion = codUbicacion;
        this.descripcion = descripcion;
        this.codUbicacionPadre = codUbicacionPadre;
        this.nUbicacionPadre = nUbicacionPadre;
    }

    public int getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(int codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodUbicacionPadre() {
        return codUbicacionPadre;
    }

    public void setCodUbicacionPadre(int codUbicacionPadre) {
        this.codUbicacionPadre = codUbicacionPadre;
    }

    public String getnUbicacionPadre() {
        return nUbicacionPadre;
    }

    public void setnUbicacionPadre(String nUbicacionPadre) {
        this.nUbicacionPadre = nUbicacionPadre;
    }
}
