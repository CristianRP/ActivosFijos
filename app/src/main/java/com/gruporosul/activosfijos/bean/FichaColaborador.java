package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 18/10/2016.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */


public class FichaColaborador {

    private int codFicha;
    private String nFicha;
    private String usuario;
    private String email;
    private String idDepartamento;
    private String descDepartamento;

    public static List<FichaColaborador> FICHAS_COLABORADORES = new ArrayList<>();

    public static FichaColaborador getFichasColaboradores(int codFicha) {
        for (FichaColaborador fichaColaborador : FICHAS_COLABORADORES) {
            if (fichaColaborador.getCodFicha() == codFicha) {
                return fichaColaborador;
            }
        }
        return null;
    }

    public FichaColaborador() {
    }

    public FichaColaborador(int codFicha, String nFicha, String usuario,
                            String email, String idDepartamento, String descDepartamento) {
        this.codFicha = codFicha;
        this.nFicha = nFicha;
        this.usuario = usuario;
        this.email = email;
        this.idDepartamento = idDepartamento;
        this.descDepartamento = descDepartamento;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getDescDepartamento() {
        return descDepartamento;
    }

    public void setDescDepartamento(String descDepartamento) {
        this.descDepartamento = descDepartamento;
    }
}
