package com.APlus.Schodule.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Horario;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddSubject extends AppCompatActivity {

    FloatingActionButton badd;
    public String dia;
    int hour1;
    int hour2;
    private ArrayList<Horario> horarios = new ArrayList<>();
    private Horario horario;
    int min1;
    int min2;

    Spinner nm;
    TimePicker t1;
    TimePicker t2;
    ArrayList<Materia> materias;
    Materia materia = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        this.nm = (Spinner) findViewById(R.id.nm);
        this.t1 = (TimePicker) findViewById(R.id.t1);
        this.t2 = (TimePicker) findViewById(R.id.t2);
        this.badd = (FloatingActionButton) findViewById(R.id.badd);

        try {
            this.dia = getIntent().getExtras().get("dia").toString();
            this.materia  = (Materia) getIntent().getExtras().get("materia");
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setTitle("Añadir clase al " + dia);
        materias = Utils.getMaterias(AddSubject.this);
        horarios = Utils.getHorarios(AddSubject.this);
        nm.setAdapter(new ArrayAdapter(this, R.layout.spinner_custom_item_addsubject, materias));

        if (materia != null) {
            setTitle("Editar " + materia);
            for(int i = 0; i < nm.getAdapter().getCount(); i++){
                if (this.nm.getAdapter().getItem(i).toString().equalsIgnoreCase(materia.getName())) {
                    this.nm.setSelection(i);
                }
            }

            int values[] = {
                    Integer.parseInt(materia.getHora_inicio()),
                Integer.parseInt(materia.getMinuto_inicio()),
                Integer.parseInt(materia.getHora_final()),
                Integer.parseInt(materia.getMinuto_final())
            };


            t1.setHour(values[0]);
            t1.setMinute(values[1]);
            t2.setHour(values[2]);
            t2.setMinute(values[3]);

            hour1 = values[0];
            min1 = values[1];
            hour2 = values[2];
            min2 = values[3];
        }

        t1.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hour1 = hourOfDay;
            min1 = minute;
        });

        t2.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hour2 = hourOfDay;
            min2 = minute;
        });

        this.badd.setOnClickListener((v) -> {
            String nam = "";
            try {
                nam = nm.getSelectedItem().toString();
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            if (materia != null) {
                horarios = Utils.getHorarios(this);
                Horario hor = null;
                int p = 0;
                for(int i = 0; i < horarios.size(); i++){
                    if(horarios.get(i).getDia().equalsIgnoreCase(dia)){
                        hor = horarios.get(i);
                        p = i;
                        break;
                    }
                }
                int poc = 0;
                materias = hor.getMaterias();
                for(int i = 0; i < materias.size(); i++){
                    if(materias.get(i).getName().equalsIgnoreCase(materia.getName())){
                        hor.getMaterias().remove(i);
                        poc = i;
                    }
                }
                if(materia.isAnimatedIco())
                    hor.getMaterias().add(poc, new Materia(nam, hour1+"", min1+"", hour2+"", min2+"", materia.getIcoAnimated(), materia.getCodigo()));
                else
                    hor.getMaterias().add(poc, new Materia(nam, hour1+"", min1+"", hour2+"", min2+"", materia.getIco(), materia.getCodigo()));

                horarios.remove(p);
                horarios.add(p, hor);
                Utils.createHorarios(AddSubject.this, horarios);
                Toast.makeText(this, materia +" actualizado", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            if (hour1 == 0 || hour2 == 0) {
                Toast.makeText(AddSubject.this, "No completaste todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                if (!new File(getExternalFilesDir("Horarios") + "/" + "ListaHorarios").exists()) {
                    Utils.createHorarios(this, horarios);
                }

                boolean exist = false;
                horarios = Utils.getHorarios(this);
                for (int i = 0; i < horarios.size(); i++) {
                    if (horarios.get(i).getDia().equalsIgnoreCase(dia)) {
                        if (exist = true) ;
                        horario = horarios.get(i);
                        horarios.remove(i);
                        break;
                    }
                }

                if (!exist)
                    horario = new Horario(dia);

                Materia m = null;
                for(int i = 0; i < materias.size(); i++){
                    if(materias.get(i).getName().equalsIgnoreCase(nam)){
                        m = materias.get(i);
                        break;
                    }
                }

                if(m.isAnimatedIco())
                    horario.getMaterias().add(new Materia(nam, hour1+"", min1+"", hour2+"", min2+"", m.getIcoAnimated(), m.getCodigo()));
                else
                    horario.getMaterias().add(new Materia(nam, hour1+"", min1+"", hour2+"", min2+"", m.getIco(), m.getCodigo()));
                horarios.add(horario);

                Utils.createHorarios(this, horarios);
                Toast.makeText(AddSubject.this, nm.getSelectedItem().toString() + " agregada con éxito", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                finish();
                Toast.makeText(AddSubject.this, "Error añadiendo", Toast.LENGTH_LONG).show();
            }
            nm.setSelection(0);
        });
    }
}
