package com.APlus.Schodule.ui.apuntes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.FileUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Documento;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public Context ct;
    private int[] Images = MainActivity.Images;
    private ArrayList<Materia> materias;
    private Materia materia;
    private Fragment fg;

    public MyAdapter(Fragment fg, ArrayList<Materia> materias) {
        this.ct = fg.getContext();
        this.materias = materias;
        this.fg = fg;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        materia = materias.get(position);

        if(materia.isAnimatedIco()){
            if (materia.getIcoAnimated() != -1) {
                try {
                    holder.myimg.setImageResource(Images[materia.getIcoAnimated()]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                holder.myimg.setImageResource(R.mipmap.ic_default_foreground);
        }else
            holder.myimg.setImageBitmap(BitmapFactory.decodeByteArray(materia.getIco().getImg(), 0, materia.getIco().getImg().length));

        holder.tv.setText(materia.getName());
    }

    public int getItemCount() {
        return materias.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myimg;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.myimg = itemView.findViewById(R.id.myimage);
            this.tv = itemView.findViewById(R.id.tv);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener( view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ct, R.style.AlertD));
                builder.setTitle("Selección de Ícono");
                builder.setMessage("¿Deseas cambiar el ícono?");
                builder.setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent = new Intent(ct, Activity_Icons.class);
                    intent.putExtra("materia", materias.get(getAdapterPosition()));
                    intent.putExtra("edit", true);
                    fg.startActivityForResult(intent, 0);
                });
                builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                builder.create().show();
                return true;
            });
        }

        public void onClick(final View view) {
            try {
                Intent intent = new Intent(ct, send_apunte_01.class);
                intent.putExtra("materia", materias.get(getAdapterPosition()));
                fg.startActivityForResult(intent, 0);
            }catch (Exception e){
                Toast.makeText(ct, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
