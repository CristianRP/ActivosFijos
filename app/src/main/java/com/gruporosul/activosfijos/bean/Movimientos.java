package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 27/03/2017.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class Movimientos {
    private String idMovimiento;
    private String idActivo;
    private String descripcionActivo;
    private String tipoMovimiento;
    private String ubicacionActual;
    private String descripcionUbicacionActual;
    private String ubicacionNueva;
    private String descripcionUbicacionNueva;
    private String fechaSolicito;
    private String fechaPlanificado;
    private String dias;
    private String fichaResponsable;
    private String descripcionFichaResponsable;
    private String fichaNuevoResponsable;
    private String descripcionFichaNuevoResponsable;
    private String usuarioSolicito;
    private String usuarioAcepto;
    private String usuarioValido;
    private String motivo;
    private String usuarioAutoriza;
    private String fSolicito;
    private String diasDesdeQueSolicito;


    public static List<Movimientos> LISTADO_MOVIMIENTOS = new ArrayList<>();

    public static Movimientos getItem(String idMovimiento) {
        for (Movimientos item : LISTADO_MOVIMIENTOS) {
            if (item.getIdMovimiento().equals(idMovimiento)) {
                return item;
            }
        }
        return null;
    }

    public Movimientos() {
    }

    public Movimientos(String idMovimiento, String idActivo, String descripcionActivo,
                       String tipoMovimiento, String ubicacionActual,
                       String descripcionUbicacionActual, String ubicacionNueva,
                       String descripcionUbicacionNueva, String fechaSolicito,
                       String fechaPlanificado, String dias, String fichaResponsable,
                       String descripcionFichaResponsable, String fichaNuevoResponsable,
                       String descripcionFichaNuevoResponsable, String usuarioSolicito,
                       String usuarioAcepto, String usuarioValido, String motivo,
                       String usuarioAutoriza, String fSolicito, String diasDesdeQueSolicito) {
        this.idMovimiento = idMovimiento;
        this.idActivo = idActivo;
        this.descripcionActivo = descripcionActivo;
        this.tipoMovimiento = tipoMovimiento;
        this.ubicacionActual = ubicacionActual;
        this.descripcionUbicacionActual = descripcionUbicacionActual;
        this.ubicacionNueva = ubicacionNueva;
        this.descripcionUbicacionNueva = descripcionUbicacionNueva;
        this.fechaSolicito = fechaSolicito;
        this.fechaPlanificado = fechaPlanificado;
        this.dias = dias;
        this.fichaResponsable = fichaResponsable;
        this.descripcionFichaResponsable = descripcionFichaResponsable;
        this.fichaNuevoResponsable = fichaNuevoResponsable;
        this.descripcionFichaNuevoResponsable = descripcionFichaNuevoResponsable;
        this.usuarioSolicito = usuarioSolicito;
        this.usuarioAcepto = usuarioAcepto;
        this.usuarioValido = usuarioValido;
        this.motivo = motivo;
        this.usuarioAutoriza = usuarioAutoriza;
        this.fSolicito = fSolicito;
        this.diasDesdeQueSolicito = diasDesdeQueSolicito;
    }

    public String getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(String idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(String idActivo) {
        this.idActivo = idActivo;
    }

    public String getDescripcionActivo() {
        return descripcionActivo;
    }

    public void setDescripcionActivo(String descripcionActivo) {
        this.descripcionActivo = descripcionActivo;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String getDescripcionUbicacionActual() {
        return descripcionUbicacionActual;
    }

    public void setDescripcionUbicacionActual(String descripcionUbicacionActual) {
        this.descripcionUbicacionActual = descripcionUbicacionActual;
    }

    public String getUbicacionNueva() {
        return ubicacionNueva;
    }

    public void setUbicacionNueva(String ubicacionNueva) {
        this.ubicacionNueva = ubicacionNueva;
    }

    public String getDescripcionUbicacionNueva() {
        return descripcionUbicacionNueva;
    }

    public void setDescripcionUbicacionNueva(String descripcionUbicacionNueva) {
        this.descripcionUbicacionNueva = descripcionUbicacionNueva;
    }

    public String getFechaSolicito() {
        return fechaSolicito;
    }

    public void setFechaSolicito(String fechaSolicito) {
        this.fechaSolicito = fechaSolicito;
    }

    public String getFechaPlanificado() {
        return fechaPlanificado;
    }

    public void setFechaPlanificado(String fechaPlanificado) {
        this.fechaPlanificado = fechaPlanificado;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getFichaResponsable() {
        return fichaResponsable;
    }

    public void setFichaResponsable(String fichaResponsable) {
        this.fichaResponsable = fichaResponsable;
    }

    public String getDescripcionFichaResponsable() {
        return descripcionFichaResponsable;
    }

    public void setDescripcionFichaResponsable(String descripcionFichaResponsable) {
        this.descripcionFichaResponsable = descripcionFichaResponsable;
    }

    public String getFichaNuevoResponsable() {
        return fichaNuevoResponsable;
    }

    public void setFichaNuevoResponsable(String fichaNuevoResponsable) {
        this.fichaNuevoResponsable = fichaNuevoResponsable;
    }

    public String getDescripcionFichaNuevoResponsable() {
        return descripcionFichaNuevoResponsable;
    }

    public void setDescripcionFichaNuevoResponsable(String descripcionFichaNuevoResponsable) {
        this.descripcionFichaNuevoResponsable = descripcionFichaNuevoResponsable;
    }

    public String getUsuarioSolicito() {
        return usuarioSolicito;
    }

    public void setUsuarioSolicito(String usuarioSolicito) {
        this.usuarioSolicito = usuarioSolicito;
    }

    public String getUsuarioAcepto() {
        return usuarioAcepto;
    }

    public void setUsuarioAcepto(String usuarioAcepto) {
        this.usuarioAcepto = usuarioAcepto;
    }

    public String getUsuarioValido() {
        return usuarioValido;
    }

    public void setUsuarioValido(String usuarioValido) {
        this.usuarioValido = usuarioValido;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }

    public String getfSolicito() {
        return fSolicito;
    }

    public void setfSolicito(String fSolicito) {
        this.fSolicito = fSolicito;
    }

    public String getDiasDesdeQueSolicito() {
        return diasDesdeQueSolicito;
    }

    public void setDiasDesdeQueSolicito(String diasDesdeQueSolicito) {
        this.diasDesdeQueSolicito = diasDesdeQueSolicito;
    }
}
