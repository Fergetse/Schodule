package com.APlus.Schodule.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

public class Documento implements Serializable {
    private String name, codigo;
    private Materia materia;
    private byte[] data;
    private int ext;

    private static final long serialVersionUID = 02L;

    public Documento(byte[] data, Materia materia, String name){
        //this.data = Base64.getEncoder().encodeToString(data);
        this.data = data;
        this.materia = materia;
        this.name = name;
        this.ext = -1;
        this.codigo = (materia+name+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = (materia+name+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }

    public byte[] getData() {
        return data;//Base64.getDecoder().decode(data);
    }

    public void setData(byte[] data) {
        //this.data = Base64.getEncoder().encodeToString(data);
        this.data = data;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public int getExt() {
        return ext;
    }

    public void setExt(int ext) {
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
