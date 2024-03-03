package com.APlus.Schodule.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Horario;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.util.ArrayList;

class Home_Basic {

    public RecyclerAdapter_Home RA;
    Activity ct;

    private Horario horario;
    private ArrayList<Horario> horarios = new ArrayList<>();
    private ArrayList<Materia> materias = new ArrayList<>();
    public String dia;
    private RecyclerView recyclerView;
    View root;
    private int request;
    private Fragment fg;

    public SwipeRefreshLayout swipeRefreshLayout;

    Home_Basic(Activity ct, String id, View root, int request, Fragment fg) {
        this.dia = id;
        this.root = root;
        this.ct = ct;
        this.request = request;
        this.fg = fg;
    }

    public void generacion() {
        actualizador(root);

        root.findViewById(R.id.importar).setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ct, R.style.AlertD));
            builder.setTitle((CharSequence) "Copiar horario");
            builder.setMessage((CharSequence) "¿Deseas conservar tu horario actual?");
            builder.setPositiveButton((CharSequence) "Sí", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    importar(true);
                }
            });
            builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    importar(false);
                }
            });
            builder.setNeutralButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
            builder.create().show();
        });

        root.findViewById(R.id.add).setOnClickListener((view) -> {
            if (ct.getExternalFilesDir("Materias").list().length != 0) {
                Intent in = new Intent(ct, AddSubject.class);
                in.putExtra("dia", dia);
                fg.startActivityForResult(in, request);
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(ct);
            builder.setTitle((CharSequence) "Cuidado");
            builder.setMessage((CharSequence) "No has ingresado materias, ¿Deseas continuar?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        Intent in = new Intent(ct, AddSubject.class);
                        in.putExtra("dia", dia);
                        fg.startActivityForResult(in, 0);
                    } catch (Exception e) {
                        Toast.makeText(ct, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton((CharSequence) "Cancelar", null);
            builder.create().show();
        });

        SwipeRefreshLayout swipeRefreshLayout2 = root.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout = swipeRefreshLayout2;

        swipeRefreshLayout2.setOnRefreshListener(() -> {
            /*Home_Basic home_Basic = Home_Basic.this;
            horario.getMaterias().clear();
            RA.notifyDataSetChanged();
            actualizador(root);*/

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public void lector() {
        try {
            if (!new File(ct.getExternalFilesDir("Horarios") + "/" + "ListaHorarios").exists()) {
                Utils.createHorarios(ct, horarios);
            }
            boolean exist = false;
            horarios = Utils.getHorarios(ct);
            for (int i = 0; i < horarios.size(); i++) {
                if (horarios.get(i).getDia().equalsIgnoreCase(dia)) {
                    if (exist = true) ;
                    horario = horarios.get(i);
                    break;
                }
            }

            if (!exist) {
                horario = new Horario(dia);
                horarios.add(horario);
                Utils.createHorarios(ct, horarios);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizador(View root) {
        lector();
        recyclerView = root.findViewById(R.id.recyclerView);
        RecyclerAdapter_Home recyclerAdapter_Home = new RecyclerAdapter_Home(horario.getMaterias(), root.getContext(), this.dia, fg, request);
        this.RA = recyclerAdapter_Home;
        //recyclerView.addItemDecoration(new DividerItemDecoration(this.ct, 1));

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(recyclerAdapter_Home);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerAdapter_Home.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(recyclerAdapter_Home);
        lector();
    }

    public void importar(boolean conservar) {
        View promptsView = LayoutInflater.from(ct).inflate(R.layout.copy_schodule, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(ct, R.style.AlertD));
        alertDialogBuilder.setView(promptsView);
        boolean[] dias = MainActivity.dias_horario;

        RadioButton rb[] = new RadioButton[7];//rb1, rb2, rb3, rb4, rb5, rb6, rb7;
        int ids[] = {R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4, R.id.rb5, R.id.rb6, R.id.rb7};

        for(int i = 0; i < dias.length; i++){
            rb[i] = promptsView.findViewById(ids[i]);
            if(!dias[i])
                rb[i].setVisibility(View.INVISIBLE);
        }

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String diasSemana[] = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                String cop_day = "";
                boolean valido = false;

                for(int i = 0; i < diasSemana.length; i++){
                    if(rb[i].isChecked() && !dia.equalsIgnoreCase(diasSemana[i])) {
                        cop_day = diasSemana[i];
                        valido = true;
                    }
                }

                if (!valido) {
                    Toast.makeText(ct, "Error importando", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                try {
                    ArrayList<Horario> hor = Utils.getHorarios(ct);
                    Horario hdia = null;
                    Horario hextra = null;
                    int p = 0;
                    for (int i = 0; i < hor.size(); i++) {
                        if (hor.get(i).getDia().equalsIgnoreCase(dia)) {
                            hdia = hor.get(i);
                            hor.remove(i);
                            p = i;
                            break;
                        }
                    }

                    for (int i = 0; i < hor.size(); i++) {
                        if (hor.get(i).getDia().equalsIgnoreCase(cop_day)) {
                            hextra = hor.get(i);
                            break;
                        }
                    }

                    ArrayList<Materia> m1 = hdia.getMaterias();
                    ArrayList<Materia> m2 = hextra.getMaterias();

                    if (!conservar)
                        m1.clear();

                    for (int i = 0; i < m2.size(); i++) {
                        m1.add(m2.get(i));
                    }

                    hdia.setMaterias(Utils.createCopyList(m1));
                    hor.add(p, hdia);
                    Utils.createHorarios(ct, hor);
                    actualizador(root);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("Cancel", null);
        alertDialogBuilder.create().show();
    }
}