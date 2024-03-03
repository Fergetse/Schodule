package com.APlus.Schodule.ui.tareas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.lifecycle.ViewModelProvider;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Tarea;
import com.APlus.Schodule.utils.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FloatingActionButton Bot_nvaTarea;
    private FloatingActionButton Bt_historial;

    private Animation f166FB;
    private RecyclerView ListaTareas;
    private RecyclerAdapter ListaTareasObjeto;

    private Animation f167TB;
    private FloatingActionButton T_B_REC;
    private List<String> adaptador;
    private FloatingActionButton bt_1;
    private boolean click = false;
    private ImageButton def_bt;

    public File dir_doc;
    public File doc;
    public FileOutputStream doc_txt;
    public Button fecha_selected;

    public Spinner mat_tar;
    private Animation rotC;
    private Animation rotO;

    public SharedPreferences settings;
    public SwipeRefreshLayout swipeRefreshLayout;
    public TextView textView5;

    private ArrayList<Tarea> tareas;
    private View root;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gallery, container, false);

        this.bt_1 = (FloatingActionButton) root.findViewById(R.id.bt_1);
        this.Bot_nvaTarea = (FloatingActionButton) root.findViewById(R.id.Bot_nvaTarea);
        this.Bt_historial = (FloatingActionButton) root.findViewById(R.id.Bt_historial);
        this.T_B_REC = (FloatingActionButton) root.findViewById(R.id.T_B_REC);
        this.def_bt = (ImageButton) root.findViewById(R.id.def_bt);
        this.rotO = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_open_anim);
        this.rotC = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_close_anim);
        this.f167TB = AnimationUtils.loadAnimation(getActivity(), R.anim.to_bottom_anim);
        this.f166FB = AnimationUtils.loadAnimation(getActivity(), R.anim.from_bottom_anim);

        getActivity().setTitle("Tareas");

        this.textView5 = (TextView) root.findViewById(R.id.textView5);
        this.ListaTareas = (RecyclerView) root.findViewById(R.id.ListaTareas);
        this.fecha_selected = (Button) root.findViewById(R.id.fecha_selected);
        this.mat_tar = (Spinner) root.findViewById(R.id.mat_tar);
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout = swipeRefreshLayout2;

        actualizador();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        this.settings = sharedPreferences;
        String hour = sharedPreferences.getString("hour", "");
        String minute = this.settings.getString("minute", "");
        if (!(hour == "" || minute == "")) {
            TextView textView = this.textView5;
            textView.setText("Recordatorio: " + hour + ":" + minute);
        }

        ////Active animation
        this.bt_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alclick();
            }
        });

        /////Check for done homework
        root.findViewById(R.id.Bt_historial).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(getContext(), Done_Homework.class));
                }catch (Exception e){System.out.println(e.getMessage());}
            }
        });

        /////Select a date
        root.findViewById(R.id.bot_fecha).setOnClickListener(v -> {
            Calendar calendarioT = Calendar.getInstance();
            new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
                Button access$300 = fecha_selected;
                access$300.setText(String.valueOf(dayOfMonth) + " - " + String.valueOf(month + 1) + " - " + String.valueOf(year));
            }, calendarioT.get(1), calendarioT.get(2), calendarioT.get(5)).show();
        });

        //In swipeRefresher
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });

        //Fill spinner with subjects
        ArrayList<Materia> materias = Utils.getMaterias(root.getContext());
        mat_tar.setAdapter(new ArrayAdapter(getActivity(), R.layout.spinner_custom_item, materias));

        ///Create new homework
        root.findViewById(R.id.Bot_nvaTarea).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (GalleryFragment.this.fecha_selected.getText().toString().equalsIgnoreCase("") || GalleryFragment.this.mat_tar.getSelectedItem().toString().equalsIgnoreCase("Selecciona una materia")) {
                    Toast.makeText(getActivity(), "Primero selecciona una fecha y materia", Toast.LENGTH_LONG).show();
                    return;
                }

                alclick();
                final String[] nomb = {null};
                View promptsView = LayoutInflater.from(GalleryFragment.this.getActivity()).inflate(R.layout.write_homework, (ViewGroup) null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertD));
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nomb[0] = userInput.getText().toString();
                        if(nomb[0].equals("")){
                            Toast.makeText(root.getContext(), "Ingresa una tarea", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Tarea tarea = new Tarea(fecha_selected.getText().toString(), nomb[0], Utils.getMateria(root.getContext(), (Materia) mat_tar.getSelectedItem()));
                        tareas = Utils.getHomework(root.getContext());
                        tareas.add(tarea);
                        Utils.createTareas(root.getContext(), tareas);
                        actualizador();
                    }
                }).setNegativeButton((CharSequence) "Cancelar", null).create();
                alertDialogBuilder.show();
            }
        });

        /////For timer
        root.findViewById(R.id.T_B_REC).setOnClickListener(v -> {
            //Calendar mcurrentTime = Calendar.getInstance();
            MaterialTimePicker tp = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build();
            tp.addOnPositiveButtonClickListener(view -> {
                int selectedHour = tp.getHour();
                int selectedMinute = tp.getMinute();

                String finalHour = "" + selectedHour;
                String finalMinute = "" + selectedMinute;
                if (selectedHour < 10) {
                    finalHour = "0" + selectedHour;
                }
                if (selectedMinute < 10) {
                    finalMinute = "0" + selectedMinute;
                }
                Calendar today = Calendar.getInstance();
                today.set(11, selectedHour);
                today.set(12, selectedMinute);
                today.set(13, 0);
                SharedPreferences.Editor edit = GalleryFragment.this.settings.edit();
                edit.putString("hour", finalHour);
                edit.putString("minute", finalMinute);
                edit.putInt("alarmIDA", 1);
                edit.putLong("alarmTimeA", today.getTimeInMillis());
                edit.commit();
                Toast.makeText(GalleryFragment.this.getActivity(), GalleryFragment.this.getString(R.string.changed_to, finalHour + ":" + finalMinute), Toast.LENGTH_LONG).show();
                //Utils.setAlarm(1, Long.valueOf(today.getTimeInMillis()), GalleryFragment.this.getActivity());
                GalleryFragment.this.textView5.setText("Recordatorio: " + finalHour + ":" + finalMinute);
            });
            tp.show(getActivity().getSupportFragmentManager(), "");

            /*
            TimePickerDialog mTimePicker = new TimePickerDialog(GalleryFragment.this.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String finalHour = "" + selectedHour;
                    String finalMinute = "" + selectedMinute;
                    if (selectedHour < 10) {
                        finalHour = "0" + selectedHour;
                    }
                    if (selectedMinute < 10) {
                        finalMinute = "0" + selectedMinute;
                    }
                    Calendar today = Calendar.getInstance();
                    today.set(11, selectedHour);
                    today.set(12, selectedMinute);
                    today.set(13, 0);
                    SharedPreferences.Editor edit = GalleryFragment.this.settings.edit();
                    edit.putString("hour", finalHour);
                    edit.putString("minute", finalMinute);
                    edit.putInt("alarmIDA", 1);
                    edit.putLong("alarmTimeA", today.getTimeInMillis());
                    edit.commit();
                    Toast.makeText(GalleryFragment.this.getActivity(), GalleryFragment.this.getString(R.string.changed_to, finalHour + ":" + finalMinute), Toast.LENGTH_LONG).show();
                    //Utils.setAlarm(1, Long.valueOf(today.getTimeInMillis()), GalleryFragment.this.getActivity());
                    GalleryFragment.this.textView5.setText("Recordatorio: " + finalHour + ":" + finalMinute);
                }
            }, mcurrentTime.get(11), mcurrentTime.get(12), true);
            mTimePicker.setTitle(GalleryFragment.this.getString(R.string.select_time));
            mTimePicker.show();*/
        });
        return root;
    }

    public void actualizador() {
        tareas = Utils.getNotDoneTareas(root.getContext());

        this.ListaTareas = (RecyclerView) root.findViewById(R.id.ListaTareas);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(tareas, this);
        this.ListaTareasObjeto = recyclerAdapter;
        this.ListaTareas.setAdapter(recyclerAdapter);
        //this.ListaTareas.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
    }

    ////////Animation method

    public void alclick() {
        visibilidad(this.click);
        anim(this.click);
        this.click = !this.click;
    }

    private void visibilidad(boolean clicked) {
        if (!clicked) {
            this.Bot_nvaTarea.show();
            this.Bot_nvaTarea.setClickable(true);
            this.Bt_historial.show();
            this.Bt_historial.setClickable(true);
            this.T_B_REC.show();
            this.T_B_REC.setClickable(true);
            this.textView5.setVisibility(View.VISIBLE);
            this.def_bt.setVisibility(View.VISIBLE);
            this.def_bt.setClickable(true);
            return;
        }
        this.Bot_nvaTarea.hide();
        this.Bot_nvaTarea.setClickable(false);
        this.Bt_historial.hide();
        this.Bt_historial.setClickable(false);
        this.T_B_REC.hide();
        this.T_B_REC.setClickable(false);
        this.def_bt.setVisibility(View.INVISIBLE);
        this.def_bt.setClickable(false);
        this.textView5.setVisibility(View.INVISIBLE);
    }

    private void anim(boolean clicked) {
        if (!clicked) {
            this.Bot_nvaTarea.startAnimation(this.f166FB);
            this.Bt_historial.startAnimation(this.f166FB);
            this.T_B_REC.startAnimation(this.f166FB);
            this.textView5.startAnimation(this.f166FB);
            this.def_bt.startAnimation(this.f166FB);
            this.bt_1.startAnimation(this.rotO);
            return;
        }
        this.Bot_nvaTarea.startAnimation(this.f167TB);
        this.Bt_historial.startAnimation(this.f167TB);
        this.T_B_REC.startAnimation(this.f167TB);
        this.textView5.startAnimation(this.f167TB);
        this.def_bt.startAnimation(this.f167TB);
        this.bt_1.startAnimation(this.rotC);
    }
}