package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22-Jan-16.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class Movimiento {

    private String idMovimiento;
    private String tipoMovimiento;
    private String fecha;
    private String motivo;
    private String codColaboradorOrigen;
    private String nombreColaboradorOrigen;
    private String codColaboradorDestino;
    private String nombreColaboradorDestino;
    private String idActivo;

    public static List<Movimiento> MOVIMIENTOS = new ArrayList<>();

    public static Movimiento getItem(String cod) {
        for (Movimiento item : MOVIMIENTOS) {
            if (item.getIdMovimiento().equals(cod)) {
                return item;
            }
        }
        return null;
    }


    public Movimiento() {
    }

    public Movimiento(String idMovimiento, String tipoMovimiento, String fecha, String motivo,
                      String codColaboradorOrigen, String nombreColaboradorOrigen,
                      String codColaboradorDestino, String nombreColaboradorDestino,
                      String idActivo) {
        this.idMovimiento = idMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.motivo = motivo;
        this.codColaboradorOrigen = codColaboradorOrigen;
        this.nombreColaboradorOrigen = nombreColaboradorOrigen;
        this.codColaboradorDestino = codColaboradorDestino;
        this.nombreColaboradorDestino = nombreColaboradorDestino;
        this.idActivo = idActivo;
    }


    public String getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(String idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCodColaboradorOrigen() {
        return codColaboradorOrigen;
    }

    public void setCodColaboradorOrigen(String codColaboradorOrigen) {
        this.codColaboradorOrigen = codColaboradorOrigen;
    }

    public String getNombreColaboradorOrigen() {
        return nombreColaboradorOrigen;
    }

    public void setNombreColaboradorOrigen(String nombreColaboradorOrigen) {
        this.nombreColaboradorOrigen = nombreColaboradorOrigen;
    }

    public String getCodColaboradorDestino() {
        return codColaboradorDestino;
    }

    public void setCodColaboradorDestino(String codColaboradorDestino) {
        this.codColaboradorDestino = codColaboradorDestino;
    }

    public String getNombreColaboradorDestino() {
        return nombreColaboradorDestino;
    }

    public void setNombreColaboradorDestino(String nombreColaboradorDestino) {
        this.nombreColaboradorDestino = nombreColaboradorDestino;
    }

    public String getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(String idActivo) {
        this.idActivo = idActivo;
    }
}
