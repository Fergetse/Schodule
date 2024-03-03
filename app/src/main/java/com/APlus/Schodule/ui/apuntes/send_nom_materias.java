package com.APlus.Schodule.ui.apuntes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Materia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class send_nom_materias extends AppCompatActivity implements Serializable {
    private ArrayAdapter<String> adaptador;
    public boolean fisttime = false;
    public ArrayList<Materia> materias = new ArrayList<>();
    RecyclerAdapter_Send recyclerAdapter;
    RecyclerView recyclerViewS;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton tap_b_vm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_send_nom_materias);
        setTitle("Ingresar o Editar Materia");
        tap_b_vm = findViewById(R.id.tap_b_vm);

        actualizador();
        if (getFirstTimeRun(this) == 0) {
            setTitle("--Ingresa tus Materias--");
            this.fisttime = true;
        }

        if(fisttime) {
            tap_b_vm.setVisibility(View.VISIBLE);
            tap_b_vm.setClickable(true);
        }

        ///todo -> Using lamba expresion
        findViewById(R.id.agregar_mat).setOnClickListener((View v) -> {
                final String[] nomb = {null};
                View promptsView = LayoutInflater.from(send_nom_materias.this).inflate(R.layout.add_subject, (ViewGroup) null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(send_nom_materias.this, R.style.AlertD));
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder.setCancelable(false).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        nomb[0] = userInput.getText().toString();
                        File dir = getExternalFilesDir("Materias");
                        Materia materia = new Materia(nomb[0]);

                        if(nomb[0].isEmpty()){
                            Toast.makeText(send_nom_materias.this, "Ingresa un nombre válido", Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            new FileOutputStream(new File(dir + "/" + "Materias")).close();
                            Intent intent = new Intent(send_nom_materias.this, Activity_Icons.class);
                            intent.putExtra("materia", materia);
                            startActivityForResult(intent, 0);

                        } catch (IOException e2) {
                            Toast.makeText(send_nom_materias.this, "Error creando la materia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel",null);
                alertDialogBuilder.create().show();
        });

        tap_b_vm.setOnClickListener((v) -> {
            if (!fisttime) {
                finish();
            } else if (!send_nom_materias.this.getExternalFilesDir("Materias").isDirectory() || send_nom_materias.this.getExternalFilesDir("Materias").list().length != 0) {
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(send_nom_materias.this);
                builder.setTitle((CharSequence) "Importante");
                builder.setMessage((CharSequence) "No agregaste materias, ¿Deseas continuar?");
                builder.setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        send_nom_materias.this.finish();
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                builder.create().show();
            }
        });

        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout = swipeRefreshLayout2;
        swipeRefreshLayout2.setOnRefreshListener(() -> {
            actualizador();
            recyclerAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        actualizador();
    }

    public static int getFirstTimeRun(@NotNull Context contexto) {
        int result;
        SharedPreferences sp = contexto.getSharedPreferences("MYAAPP", 0);
        int lastVersionCode = sp.getInt("FIRSTTIMERUNi", -1);
        if (lastVersionCode == -1) {
            result = 0;
        } else {
            result = lastVersionCode == 1 ? 1 : 2;
        }
        sp.edit().putInt("FIRSTTIMERUNi", 1).apply();
        return result;
    }

    public void actualizador() {
        try {
            if (!new File(getExternalFilesDir("Materias") + "/" + "ListaMaterias").exists()) {
                return;
            }
            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(getExternalFilesDir("Materias") + "/" + "ListaMaterias")));
            materias = (ArrayList<Materia>) entrada.readObject();
            entrada.close();

            this.recyclerViewS = (RecyclerView) findViewById(R.id.recyclerViewS);
            RecyclerAdapter_Send recyclerAdapter_Send = new RecyclerAdapter_Send(materias, this);
            this.recyclerAdapter = recyclerAdapter_Send;
            this.recyclerViewS.setAdapter(recyclerAdapter_Send);
            //this.recyclerViewS.addItemDecoration(new DividerItemDecoration(this, 1));
        }catch (Exception e){

        }
    }
}
