package com.APlus.Schodule.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.ui.tareas.Done_Homework;
import com.APlus.Schodule.utils.Horario;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_Home extends RecyclerView.Adapter<RecyclerAdapter_Home.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "RecyclerAdapter";
    int[] Images = MainActivity.Images;

    public Context ct;
    private ItemTouchHelper itemth;
    public String dia;
    ArrayList<Materia> lista_horario = new ArrayList<>();
    private Fragment fg;
    private int request;

    public RecyclerAdapter_Home(ArrayList<Materia> lista_horario2, Context ct, String dia2, Fragment fg, int request) {
        this.lista_horario = lista_horario2;
        this.ct = ct;
        this.dia = dia2;
        this.fg = fg;
        this.request = request;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycleradapter_home, parent, false));
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.itemth = touchHelper;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Materia mat = lista_horario.get(position);
        ArrayList<Materia> materias = Utils.getMaterias(ct);
        for(int i = 0; i < materias.size(); i++){
            if(materias.get(i).getName().equalsIgnoreCase(mat.getName())){
                if(mat.isAnimatedIco())
                    mat.setIcoAnimated(materias.get(i).getIcoAnimated());
                else
                    mat.setIco(materias.get(i).getIco());
                break;
            }
        }

        if(mat.isAnimatedIco()) {
            if (mat.getIcoAnimated() != -1) {
                holder.imageView.setImageResource(Images[mat.getIcoAnimated()]);
            } else {
                holder.imageView.setImageResource(R.mipmap.ic_default_foreground);
            }
        }else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(mat.getIco().getImg(), 0, mat.getIco().getImg().length));
        }
        holder.rowCountTextView.setText(mat.getHora_inicio() + " : " + mat.getMinuto_inicio() + "  -  " + mat.getHora_final() + " : " + mat.getMinuto_final());
        holder.textView.setText(mat.getName());
    }

    public int getItemCount() {
        return lista_horario.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        try {
           Materia frommat = lista_horario.get(fromPosition);
            lista_horario.remove(frommat);
            lista_horario.add(toPosition, frommat);

            ArrayList<Horario> hor = Utils.getHorarios(ct);
            Horario h = null;
            int poc = 0;
            for(int i = 0; i < hor.size(); i++){
                if(hor.get(i).getDia().equalsIgnoreCase(dia)){
                    h = hor.get(i);
                    hor.remove(i);
                    poc = i;
                    break;
                }
            }
            h.setMaterias(lista_horario);
            hor.add(poc, h);
            Utils.createHorarios(ct, hor);
            notifyItemMoved(fromPosition, toPosition);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnTouchListener, GestureDetector.OnGestureListener {
        ImageView imageView;
        TextView rowCountTextView;
        TextView textView;
        GestureDetector mGestureDetector;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.rowCountTextView = (TextView) itemView.findViewById(R.id.rowCountTextView);
            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return true;
                }
            });
        }

        public void onClick(final View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
            builder.setTitle( "Opciones");
            builder.setMessage( "¿Qué deseas hacer?");
            builder.setPositiveButton( "Editar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                    builder.setTitle( "Editar Espacio");
                    builder.setMessage( "¿Deseas editar este espacio?");
                    builder.setPositiveButton( "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent in = new Intent(RecyclerAdapter_Home.this.ct, AddSubject.class);
                            in.putExtra("materia", lista_horario.get(getAdapterPosition()));
                            in.putExtra("dia", RecyclerAdapter_Home.this.dia);
                            fg.startActivityForResult(in, request);
                        }
                    });
                    builder.setNegativeButton( "Cancelar", (DialogInterface.OnClickListener) null);
                    builder.create().show();
                }
            });
            builder.setNegativeButton( "Cancelar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setNeutralButton( "Borrar", (dialog, wich) -> {
                AlertDialog.Builder build = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                build.setTitle("Borrar");
                build.setMessage("¿Seguro que desea eliminar este espacio?");
                build.setPositiveButton("Aceptar", (DialogInterface.OnClickListener) (d, w) -> {
                    lista_horario.remove(getAdapterPosition());
                    ArrayList<Horario> horarios = Utils.getHorarios(ct);
                    Horario hor = null;
                    int poc = 0;
                    for (int i = 0; i < horarios.size(); i++) {
                        if (horarios.get(i).getDia().equalsIgnoreCase(dia)) {
                            hor = horarios.get(i);
                            poc = i;
                            break;
                        }
                    }
                    hor.getMaterias().remove(getAdapterPosition());
                    horarios.remove(poc);
                    horarios.add(poc, hor);
                    Utils.createHorarios(ct, horarios);
                    notifyItemRemoved(getAdapterPosition());
                });
                build.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                build.create().show();
            });
            builder.create().show();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //mOnNoteListener.onNoteClick(getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            itemth.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
