package com.APlus.Schodule.utils;

import java.io.Serializable;

public class Tarea implements Serializable {
    private String fecha, contenido, codigo;
    private Materia materia;
    private boolean done;

    private static final long serialVersionUID = 07L;

    public Tarea(String fecha, String contenido, Materia materia){
        this.fecha = fecha;
        this.contenido = contenido;
        this. materia = materia;
        this.done = false;
        this.codigo = (fecha+contenido+materia.getCodigo()+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
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

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = (fecha+contenido+materia.getCodigo()+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }

    @Override
    public String toString(){
        return fecha + ", " + materia;
    }
}
