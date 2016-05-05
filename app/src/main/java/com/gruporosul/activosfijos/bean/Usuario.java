package com.gruporosul.activosfijos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Ramírez on 22-Dec-15.
 * Grupo Rosul
 * cristianramirezgt@gmail.com
 */
public class Usuario {

    private int codUsuario;
    private String usuario;

    public static List<Usuario> USUARIOS = new ArrayList<>();

    public static Usuario getItem(int cod) {
        for (Usuario item : USUARIOS) {
            if (item.getCodUsuario() == cod) {
                return item;
            }
        }
        return null;
    }

    public Usuario() {
    }

    public Usuario(int codUsuario, String usuario) {
        this.codUsuario = codUsuario;
        this.usuario = usuario;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
