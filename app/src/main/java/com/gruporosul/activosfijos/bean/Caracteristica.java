package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22/08/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Caracteristica {

    private int idActivo;
    private int idCaracteristica;
    private String desCaracteristica;
    private String valor;

    public static List<Caracteristica> CARACTERISTICAS = new ArrayList<>();

    public static Caracteristica getItem(int idActivo) {
        for (Caracteristica item : CARACTERISTICAS) {
            if (item.idActivo == idActivo) {
                return item;
            }
        }
        return null;
    }


    public Caracteristica() {
    }

    public Caracteristica(int idActivo, int idCaracteristica, String desCaracteristica, String valor) {
        this.idActivo = idActivo;
        this.idCaracteristica = idCaracteristica;
        this.desCaracteristica = desCaracteristica;
        this.valor = valor;
    }

    public int getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(int idActivo) {
        this.idActivo = idActivo;
    }

    public int getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(int idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public String getDesCaracteristica() {
        return desCaracteristica;
    }

    public void setDesCaracteristica(String desCaracteristica) {
        this.desCaracteristica = desCaracteristica;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
