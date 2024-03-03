package com.APlus.Schodule.ui.apuntes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Materia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RecyclerAdapter_Send extends RecyclerView.Adapter<RecyclerAdapter_Send.ViewHolder> implements Serializable {
    private int[] Images = MainActivity.Images;

    Context ct;
    Activity activity;
    ArrayList<Materia> materias;
    Materia materia;

    public RecyclerAdapter_Send(ArrayList<Materia> materias, Activity activity) {
        this.materias = materias;
        this.ct = activity;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_send, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        materia = materias.get(position);
        
        holder.textView.setText(materia.getName());

        if(materia.isAnimatedIco()) {
            if (materia.getIcoAnimated() != -1) {
                holder.imageView.setImageResource(Images[materia.getIcoAnimated()]);
            } else {
                holder.imageView.setImageResource(R.mipmap.ic_default_foreground);
            }
        }else {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(materia.getIco().getImg(), 0, materia.getIco().getImg().length));
        }
    }

    public int getItemCount() {
        return materias.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.cb);
            
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(view -> true);
        }

        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
            builder.setTitle("Acciones");
            builder.setMessage("¿Qué deseas hacer?");

            builder.setPositiveButton("Cambiar ícono", (dialog, which) -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                builder1.setTitle("Selección de Ícono");
                builder1.setMessage("¿Deseas cambiar el ícono?");
                builder1.setPositiveButton("Aceptar", (dialog1, which1) -> {
                    Intent intent = new Intent(ct, Activity_Icons.class);
                    intent.putExtra("materia", materias.get(getAdapterPosition()));
                    intent.putExtra("edit", true);
                    activity.startActivityForResult(intent, 0);
                });
                builder1.setNegativeButton("Cancelar", null);
                builder1.create().show();
            });

            builder.setNegativeButton("Cancelar", null);

            builder.setNeutralButton("Borrar Materia", (dialog, which) -> {
                AlertDialog.Builder builder12 = new AlertDialog.Builder(new ContextThemeWrapper(ct, R.style.AlertD));
                builder12.setTitle("Borra Materia");
                builder12.setMessage("¿Deseas eliminar esta materia?");

                builder12.setPositiveButton("Aceptar", (dialog12, which12) -> {
                    Toast.makeText(ct, "Materia Borrada", Toast.LENGTH_SHORT).show();
                    materias.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    updateListDocument();
                });
                builder12.setNegativeButton("Cancelar", null);
                builder12.create().show();
            });
            builder.create().show();
        }
    }

    private void updateListDocument(){
        try{
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(ct.getExternalFilesDir("Materias") + "/" + "ListaMaterias")));
            salida.writeObject(materias);
            salida.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
