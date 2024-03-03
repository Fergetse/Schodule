package com.APlus.Schodule.ui.tareas;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Horario;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Tarea;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public ArrayList<Tarea> Homework;
    int[] Images = MainActivity.Images;
    private Context f168ct;
    private Fragment fg;

    public RecyclerAdapter(ArrayList<Tarea> tareas, Fragment fg) {
        this.Homework = tareas;
        this.f168ct = fg.getActivity();
        this.fg = fg;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Materia mat = Utils.getMateria(f168ct, Homework.get(position).getMateria());
        Tarea tarea = Homework.get(position);

        if(mat.isAnimatedIco()) {
            if (mat.getIcoAnimated() != -1) {
                holder.imageView.setImageResource(Images[mat.getIcoAnimated()]);
            } else {
                holder.imageView.setImageResource(R.mipmap.ic_default_foreground);
            }
        }else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(mat.getIco().getImg(), 0, mat.getIco().getImg().length));
        }
        holder.rowCountTextView.setText(tarea.getMateria().getName());
        holder.textView.setText(tarea.getFecha());
    }

    public int getItemCount() {
        return Homework.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView rowCountTextView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.rowCountTextView = (TextView) itemView.findViewById(R.id.rowCountTextView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                    builder.setTitle((CharSequence) "Borrar");
                    builder.setMessage((CharSequence) "¿Seguro que desea eliminar la tarea?");
                    builder.setPositiveButton((CharSequence) "Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.removeTarea(f168ct, Homework.get(getAdapterPosition()));
                            Homework.remove(getAdapterPosition());
                            //Homework = Utils.getNotDoneTareas(f168ct);
                            notifyItemRemoved(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                    builder.create().show();
                    return true;
                }
            });
        }

        public void onClick(final View view) {
            final int position = getPosition();
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
            alertdialogbuilder.setTitle("Tarea:");
            alertdialogbuilder.setMessage(Homework.get(getAdapterPosition()).getContenido());
            alertdialogbuilder.setCancelable(false);
            alertdialogbuilder.setPositiveButton((CharSequence) "Ok", null);

            alertdialogbuilder.setNeutralButton((CharSequence) "Tarea Hecha", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                    builder.setTitle((CharSequence) "Tarea Realizada");
                    builder.setMessage((CharSequence) "¿Haz finalizado esta tarea?");
                    builder.setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Homework.get(position).setDone(true);
                            Utils.replaceTarea(f168ct, Homework.get(position));
                            //Homework = Utils.getNotDoneTareas(f168ct);
                            Homework.remove(position);
                            notifyItemRemoved(position);
                            //notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                    builder.create().show();
                }
            });
            alertdialogbuilder.setNegativeButton((CharSequence) "Editar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View promptsView = LayoutInflater.from(view.getContext()).inflate(R.layout.write_homework, (ViewGroup) null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                    alertDialogBuilder.setView(promptsView);
                    EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                    userInput.setText(Homework.get(getAdapterPosition()).getContenido());
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Homework.get(position).setContenido(userInput.getText().toString());
                            Utils.replaceTarea(f168ct, Homework.get(position));
                            //Homework = Utils.getNotDoneTareas(f168ct);
                            notifyItemChanged(position);
                            //notifyDataSetChanged();
                            Toast.makeText(f168ct, "Tarea Actualizada", Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton((CharSequence) "Cancel", null);
                    alertDialogBuilder.create().show();
                }
            });
            alertdialogbuilder.create().show();
        }
    }
}