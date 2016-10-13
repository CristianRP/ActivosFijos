package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/09/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Activo {
    private int codFicha;
    private String nFicha;
    private int idActivo;
    private String descripcion;
    private String ubicacion;
    private boolean provisional;
    private String fechaBaja;
    private int codUbicacion;

    public static List<Activo> MIS_ACTIVOS = new ArrayList<>();

    public static Activo getItem(int idActivo) {
        for (Activo item : MIS_ACTIVOS) {
            if (item.getIdActivo() == idActivo) {
                return item;
            }
        }
        return null;
    }

    public Activo() {
    }

    public Activo(int codFicha, String nFicha, int idActivo,
                  String descripcion, String ubicacion,
                  boolean provisional, String fechaBaja, int codUbicacion) {
        this.codFicha = codFicha;
        this.nFicha = nFicha;
        this.idActivo = idActivo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.provisional = provisional;
        this.fechaBaja = fechaBaja;
        this.codUbicacion = codUbicacion;
    }

    public int getCodFicha() {
        return codFicha;
    }

    public void setCodFicha(int codFicha) {
        this.codFicha = codFicha;
    }

    public String getnFicha() {
        return nFicha;
    }

    public void setnFicha(String nFicha) {
        this.nFicha = nFicha;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isProvisional() {
        return provisional;
    }

    public void setProvisional(boolean provisional) {
        this.provisional = provisional;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public int getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(int codUbicacion) {
        this.codUbicacion = codUbicacion;
    }
}
