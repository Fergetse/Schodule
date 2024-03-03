package com.APlus.Schodule.ui.tareas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Tarea;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Done_Homework extends AppCompatActivity {

    public String[] Archivos;
    private ListView ListaTareasHechas;
    private ArrayAdapter<Tarea> adaptador;
    private ArrayList<Tarea> tareas;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done__homework);
        ListaTareasHechas = findViewById(R.id.ListaTareasHechas);
        setTitle("Tareas Realizadas");
        setTitleColor(-1);
        actualizador();
        
        ListaTareasHechas.setOnItemClickListener((adapterView, view, position, id) -> {
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(new ContextThemeWrapper(Done_Homework.this, R.style.AlertD));
            alertdialogbuilder.setTitle( "Tarea:");
            alertdialogbuilder.setMessage(tareas.get(position).getContenido());
            alertdialogbuilder.setCancelable(false);
            alertdialogbuilder.setPositiveButton( "Ok", null);

            alertdialogbuilder.setNeutralButton( "Borra Tarea", (dialog, which) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Done_Homework.this, R.style.AlertD));
                builder.setTitle( "Borrar");
                builder.setMessage( "¿Seguro que desea eliminar la tarea?");
                builder.setPositiveButton( "Aceptar", (dialog1, which1) -> {
                    Toast.makeText(Done_Homework.this, "Asignación Borrada", Toast.LENGTH_SHORT).show();
                    Utils.removeTarea(Done_Homework.this, tareas.get(position));
                    tareas.remove(position);
                    actualizador();
                });
                builder.setNegativeButton( "Cancelar", null);
                builder.create().show();
            });
            alertdialogbuilder.create().show();
        });
    }

    public void actualizador() {
        tareas = Utils.getDoneHomework(Done_Homework.this);
        adaptador = new ArrayAdapter<>(this, R.layout.custom_listview_simple, tareas);
        ListView listView = findViewById(R.id.ListaTareasHechas);
        ListaTareasHechas = listView;
        listView.setAdapter(this.adaptador);
    }
}
