package com.APlus.Schodule.utils;

import java.io.Serializable;

public class Nota implements Serializable {
    private String titulo, fecha, contenido, codigo;
    private int ico;

    private static final long serialVersionUID = 06L;

    public Nota(String fecha, String titulo, String contenido){
        this.fecha = fecha;
        this.titulo = titulo;
        this.contenido = contenido;
        codigo = (titulo+fecha+contenido+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIco() {
        return ico;
    }

    public void setIco(int ico) {
        this.ico = ico;
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = (titulo+fecha+contenido+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }

    @Override
    public String toString(){
        return titulo;
    }
}
