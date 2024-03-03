package com.APlus.Schodule.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Materia implements Serializable {
    private String name, hora_inicio, hora_final, minuto_inicio, minuto_final, codigo;
    private Ico ico;
    private int icoAnimated;
    private boolean animatedIco = true;
    private ArrayList<Nota> notas = new ArrayList<>();
    private ArrayList<Tarea> tareas = new ArrayList<>();
    private boolean selected = false;

    private static final long serialVersionUID = 05L;

    public Materia(String name, String hora_inicio, String minuto_inicio, String hora_final, String minuto_final, Ico ico, String codigo){
        this.name = name;
        this.hora_inicio = hora_inicio;
        this.hora_final = hora_final;
        this.minuto_inicio = minuto_inicio;
        this.minuto_final = minuto_final;
        this.ico = ico;
        animatedIco = false;
        this.codigo = codigo;//(name+hora_inicio+hora_final+minuto_inicio+minuto_final+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)).trim().replace("", "");
    }

    public Materia(String name, String hora_inicio, String minuto_inicio, String hora_final, String minuto_final, int ico, String codigo){
        this.name = name;
        this.hora_inicio = hora_inicio;
        this.minuto_inicio = minuto_inicio;
        this.minuto_final = minuto_final;
        this.hora_final = hora_final;
        this.icoAnimated = ico;
        animatedIco = true;
        this.codigo = codigo;//(name+hora_inicio+hora_final+minuto_inicio+minuto_final+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)).trim().replace("", "");
    }

    public Materia(String name){
        this.name = name;
        codigo = (name+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHora_inicio() {
        return timeChanger(hora_inicio);
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_final() {
        return timeChanger(hora_final);
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public Ico getIco() {
        animatedIco = false;
        return ico;
    }

    public int getIcoAnimated() {
        animatedIco = true;
        return icoAnimated;
    }

    public void setIcoAnimated(int icoAnimated) {
        animatedIco = true;
        this.icoAnimated = icoAnimated;
    }

    public boolean isAnimatedIco() {
        return animatedIco;
    }

    public void setAnimatedIco(boolean animatedIco) {
        this.animatedIco = animatedIco;
    }

    public void setIco(Ico ico) {
        this.ico = ico;
        this.animatedIco = false;
    }

    public String getMinuto_inicio() {
        return timeChanger(minuto_inicio);
    }

    public void setMinuto_inicio(String minuto_inicio) {
        this.minuto_inicio = minuto_inicio;
    }

    public String getMinuto_final() {
        return timeChanger(minuto_final);
    }

    public void setMinuto_final(String minuto_final) {
        this.minuto_final = minuto_final;
    }

    public ArrayList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(ArrayList<Tarea> tareas) {
        this.tareas = tareas;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = (name+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }

    @Override
    public String toString(){
        return name;
    }

    public String timeChanger(String time){
        if(Integer.parseInt(time) < 10){
            time = "0" + time;
        }
        return time;
    }
}
