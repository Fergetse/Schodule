package com.APlus.Schodule.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Horario implements Serializable {
    private String dia;
    private ArrayList<Materia> materias;
    private String codigo;

    private static final long serialVersionUID = 03L;

    public Horario(String dia){
        this.dia = dia;
        materias = new ArrayList<>();
        codigo = (dia+materias.hashCode()+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
    }

    public Horario(String dia, ArrayList<Materia> materias){
        this.dia = dia;
        this.materias = materias;
        //codigo = (dia+materias.hashCode()+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)).trim().replace(" ", "");
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<Materia> materias) {
        this.materias = materias;
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = (dia+materias.hashCode()+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }
}
