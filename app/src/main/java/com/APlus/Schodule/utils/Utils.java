package com.APlus.Schodule.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {
    public static ArrayList<Materia> getMaterias(Context ct) {
        ArrayList<Materia> materias = new ArrayList<>();
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaMaterias")));
            materias = (ArrayList<Materia>) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materias;
    }

    public static void createTareas(Context ct, ArrayList<Tarea> tareas) {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaTareas")));
            salida.writeObject(tareas);
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tarea> getNotDoneTareas(Context ct) {
        ArrayList<Tarea> tareas = new ArrayList<>();
        ArrayList<Tarea> temp = new ArrayList<>();
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaTareas")));
            temp = (ArrayList<Tarea>) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < temp.size(); i++) {
            if (!temp.get(i).isDone()) {
                tareas.add(temp.get(i));
            }
        }
        return tareas;
    }

    public static ArrayList<Tarea> getDoneHomework(Context ct) {
        ArrayList<Tarea> tareas = new ArrayList<>();
        ArrayList<Tarea> temp = new ArrayList<>();
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaTareas")));
            temp = (ArrayList<Tarea>) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).isDone()) {
                tareas.add(temp.get(i));
            }
        }
        return tareas;
    }

    public static ArrayList<Tarea> getHomework(Context ct) {
        ArrayList<Tarea> tareas = new ArrayList<>();
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaTareas")));
            tareas = (ArrayList<Tarea>) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tareas;
    }

    public static ArrayList<String> getDocumentsList(Context ct, Materia materia) {
        ArrayList<String> materias = new ArrayList<>();
        String[] list = ct.getExternalFilesDir("Materias" + "/" + "ListaDocsMaterias" + "/" + materia.getName() + "Docs").list();
        for (int i = 0; i < list.length; i++) {
            materias.add(list[i]);
        }
        return materias;
    }

    public static Documento getDocumentDirect(Context ct, Materia materia, String name) {
        Documento doc = null;
        try {
            ObjectInputStream salida = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Materias" + "/" + "ListaDocsMaterias" + "/" + materia.getName() + "Docs") + "/" + name)));
            doc = (Documento) salida.readObject();
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static void replaceDocumentDirect(Context ct, Materia materia, Documento documento, String oldname) {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("Materias" + "/" + "ListaDocsMaterias" + "/" + materia.getName() + "Docs") + "/" + documento.getName())));
            if (oldname != null) {
                new File(ct.getExternalFilesDir("Materias" + "/" + "ListaDocsMaterias" + "/" + materia.getName() + "Docs") + "/" + oldname).delete();
            }
            salida.writeObject(documento);
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDocumentDirect(Context ct, Materia materia, String nombre) {
        try {
            new File(ct.getExternalFilesDir("Materias" + "/" + "ListaDocsMaterias" + "/" + materia.getName() + "Docs") + "/" + nombre).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //// Get Materia from the code todo ready ---------------------
    public static Materia getMateria(Context ct, Materia materia1) {
        String codigo = materia1.getCodigo();
        ArrayList<Materia> materias = getMaterias(ct);
        Materia materia = new Materia(materia1.getName());
        for (int i = 0; i < materias.size(); i++) {
            if (materias.get(i).getCodigo().equals(codigo)) {
                materia = materias.get(i);
                break;
            }
        }
        return materia;
    }


    //// Replace materia from the code todo ready -----------------
    public static void replaceMateria(Context ct, Materia materia) {
        String codigo = materia.getCodigo();
        ArrayList<Materia> materias = getMaterias(ct);
        int poc = 0;
        for (int i = 0; i < materias.size(); i++) {
            if (materias.get(i).getCodigo().equals(codigo)) {
                materias.remove(i);
                poc = i;
                break;
            }
        }
        materias.add(poc, materia);
        createMaterias(ct, materias);
    }

    ///Replace tarea from its code todo ready ---------------
    public static void replaceTarea(Context ct, Tarea tarea) {
        String codigo = tarea.getCodigo();
        ArrayList<Tarea> tareas = getHomework(ct);
        int poc = 0;
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getCodigo().equals(codigo)) {
                tareas.remove(i);
                poc = i;
                break;
            }
        }
        tareas.add(poc, tarea);
        createTareas(ct, tareas);
    }

    ///Remove tarea from its code todo ready ---------------
    public static void removeTarea(Context ct, Tarea tarea) {
        String codigo = tarea.getCodigo();
        ArrayList<Tarea> tareas = getHomework(ct);
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getCodigo().equals(codigo)) {
                tareas.remove(i);
                break;
            }
        }
        createTareas(ct, tareas);
    }

    public static void createMaterias(Context ct, ArrayList<Materia> materias) {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaMaterias")));
            salida.writeObject(materias);
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Horario> getHorarios(Context ct) {
        ArrayList<Horario> horarios = new ArrayList<>();
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Horarios") + "/" + "ListaHorarios")));
            horarios = (ArrayList<Horario>) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return horarios;
    }

    public static <T> ArrayList<T> createCopyList(ArrayList<T> lista) {
        ArrayList<T> nueva = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            nueva.add(lista.get(i));
        }
        return nueva;
    }

    public static void createHorarios(Context ct, ArrayList<Horario> horarios) {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("Horarios") + "/" + "ListaHorarios")));
            salida.writeObject(horarios);
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createEventos(Context ct, ArrayList<EventoN> eventos) {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("LEventos") + "/" + "ListaEventos")));
            salida.writeObject(eventos);
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<EventoN> getEventos(Context ct) {
        ArrayList<EventoN> eventos = new ArrayList<>();
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("LEventos") + "/" + "ListaEventos")));
            eventos = (ArrayList<EventoN>) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventos;
    }

    public static ArrayList<EventoN> getEventosDia(Context ct, String dia) {
        ArrayList<EventoN> eventos = getEventos(ct);
        ArrayList<EventoN> evts = new ArrayList<>();
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getDia().equals(dia)) {
                evts.add(eventos.get(i));
            }
        }
        return evts;
    }

    ///Remove evento from the code todo ready ------------------
    public static void removeEvento(Context ct, EventoN evento) {
        String codigo = evento.getCodigo();
        ArrayList<EventoN> eventos = getEventos(ct);
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getCodigo().equals(codigo)) {
                eventos.remove(i);
                break;
            }
        }
        createEventos(ct, eventos);
    }

    ///Replace evento from the code todo ready ------------------
    public static void replaceEvento(Context ct, EventoN evento) {
        String codigo = evento.getCodigo();
        ArrayList<EventoN> eventos = getEventos(ct);
        int poc = 0;
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getCodigo().equals(codigo)) {
                eventos.remove(i);
                poc = i;
                break;
            }
        }
        eventos.add(poc, evento);
        createEventos(ct, eventos);
    }

    ///////////// To read schedule days

    public static boolean[] getSchedule(Context ct) {
        boolean[] schedule = {true, true, true, true, true, false, false};
        try {
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(ct.getExternalFilesDir("Config") + "/" + "Schedule")));
            schedule = (boolean[]) entrada.readObject();
            entrada.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedule;
    }

    public static void createSchedule(Context ct, boolean[] schedule) {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("Config") + "/" + "Schedule")));
            salida.writeObject(schedule);
            salida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //////////////////////////

    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(new Date());
    }

    ///////////////////////////

    //Write byte[] in doc
    public static File writeFile(byte data[], Context ct, String name) {
        FileOutputStream fos = null;
        File file = new File(ct.getExternalFilesDir("Materias" + "/" + "temps") + "/" + name);
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }



    public static void deleteDir(Context ct, String name) {
        new File(ct.getExternalFilesDir("Materias" + "/" + "temps") + "/" + name).delete();
    }
}