package com.APlus.Schodule.utils;

import java.io.Serializable;

public class EventoN implements Serializable {
    private String contenido, dia, codigo;
    private Long fecha;
    private int color;

    private static final long serialVersionUID = 01L;


    public EventoN(String contenido, Long fecha, String dia, int color){
        this.contenido = contenido;
        this.fecha = fecha;
        this.color = color;
        this.dia = dia;
        this.codigo = (contenido+fecha+dia+color+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = (contenido+fecha+dia+color+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }

    @Override
    public String toString(){
        return contenido;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
