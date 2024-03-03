package com.APlus.Schodule.ui.apuntes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Documento;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.util.ArrayList;

public class RecyclerAdapter_Docs extends RecyclerView.Adapter<RecyclerAdapter_Docs.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";

    public Context ct;
    int[] ext = MainActivity.ext;
    String[] tipoD = MainActivity.tipoDoc;
    String[] terms = MainActivity.term;
    String name;
    private ArrayList<String> documentos;
    private Documento doc;
    private Materia materia;
    private Activity activity;

    public RecyclerAdapter_Docs(Materia materia, Activity ct, ArrayList<String> documentos) {
        this.materia = materia;
        this.ct = ct;
        this.documentos = documentos;
        this.activity = ct;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleradapter_docs, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        this.doc = Utils.getDocumentDirect(ct, materia, documentos.get(position));

        try {
            String ext_doc = MainActivity.term[doc.getExt()];
            Drawable[] dr = {ct.getResources().getDrawable(R.drawable.grad_png), ct.getResources().getDrawable(R.drawable.grad_gif),
                    ct.getResources().getDrawable(R.drawable.grad_jav), ct.getResources().getDrawable(R.drawable.grad_pyt),
                    ct.getResources().getDrawable(R.drawable.grad_ppt), ct.getResources().getDrawable(R.drawable.grad_pdf),
                    ct.getResources().getDrawable(R.drawable.grad_txt), ct.getResources().getDrawable(R.drawable.grad_doc),
                    ct.getResources().getDrawable(R.drawable.grad_xls), ct.getResources().getDrawable(R.drawable.grad_obj),
                    ct.getResources().getDrawable(R.drawable.grad_mp4), ct.getResources().getDrawable(R.drawable.grad_mp3),
                    ct.getResources().getDrawable(R.drawable.grad_jpg)
            };
            //holder.rowCountTextView.setText(MainActivity.term[doc.getExt()]);
            holder.imageView.setImageResource(ext[doc.getExt()]);
            holder.cl.setBackgroundDrawable(dr[doc.getExt()]);

        } catch (Exception e) {
            Toast.makeText(ct, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        holder.textView.setText(documentos.get(position));
    }

    public int getItemCount() {
        return documentos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView rowCountTextView;
        TextView textView;
        ConstraintLayout cl;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.textView = itemView.findViewById(R.id.textView);
            this.rowCountTextView = itemView.findViewById(R.id.rowCountTextView);
            this.cl = itemView.findViewById(R.id.cl);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener((v) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ct, R.style.AlertD));
                builder.setTitle("Borrar");
                builder.setMessage("¿Seguro que desea eliminar el documento?");
                builder.setPositiveButton("Aceptar", (d, w) -> {
                    Utils.deleteDocumentDirect(ct, materia, documentos.get(getAdapterPosition()));
                    documentos.remove(getAdapterPosition());
                    //documentos = Utils.getDocumentsList(ct, materia);
                    Toast.makeText(ct, "Borrado", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(getAdapterPosition());
                });
                builder.setNegativeButton("Cancelar", null);
                builder.create().show();
                return true;
            });
        }

        public void onClick(final View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
            builder.setTitle("Opciones");
            builder.setMessage("¿Qué deseas hacer?");
            builder.setNegativeButton("Cancelar", null);
            builder.setPositiveButton("Ver", (dialog, which) -> {
                String type = "";
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    /////Checking if its right
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    doc = Utils.getDocumentDirect(ct, materia, documentos.get(getAdapterPosition()));
                    Utils.deleteDir(ct, doc.getName());
                    Uri uri = FileProvider.getUriForFile(ct, ct.getApplicationContext().getPackageName() + ".provider", Utils.writeFile(doc.getData(), ct, doc.getName()));
                    int poc = doc.getExt();

                    if (terms[poc].equalsIgnoreCase("PNG") || (terms[poc].equalsIgnoreCase("JPG") || terms[poc].equalsIgnoreCase("GIF")))
                        type = tipoD[0];
                    else if (terms[poc].equalsIgnoreCase("MP4"))
                        type = tipoD[1];
                    else if (terms[poc].equalsIgnoreCase("MP3"))
                        type = tipoD[2];
                    else if (terms[poc].equalsIgnoreCase("TXT"))
                        type = tipoD[3];
                    else if (terms[poc].equalsIgnoreCase("PDF"))
                        type = tipoD[4];
                    else
                        type = tipoD[5];

                    intent.setDataAndType(uri, type);
                    ct.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ct, "Error inesperado", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNeutralButton("Editar", (dialog, which) -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertD));
                builder1.setTitle("Editar Espacio");
                builder1.setMessage("¿Qué deseas editar?");
                builder1.setPositiveButton("Cancelar", null);
                builder1.setNegativeButton("Título", (dialog1, which1) -> editar_nomb(ViewHolder.this.getPosition()));

                builder1.setNeutralButton("Ícono", (dialog12, which12) -> {
                    Intent in = new Intent(ct, Activity_Extensiones.class);
                    in.putExtra("materia", materia);
                    in.putExtra("nombredoc", documentos.get(getAdapterPosition()));
                    activity.startActivityForResult(in, 0);
                });
                builder1.create().show();
            });
            builder.create().show();
        }
    }

    public void editar_nomb(final int pos) {
        doc = Utils.getDocumentDirect(ct, materia, documentos.get(pos));
        final String[] nomb = {null};
        View promptsView = LayoutInflater.from(this.ct).inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(ct, R.style.AlertD));
        alertDialogBuilder.setView(promptsView);
        EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setText(doc.getName());
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
            nomb[0] = userInput.getText().toString();
            if (nomb[0].equals("")) {
                Toast.makeText(ct, "Cuidado, no ingresaste nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            String oldname = documentos.get(pos);
            doc.setName(nomb[0]);
            Utils.replaceDocumentDirect(ct, materia, doc, oldname);
            documentos = Utils.getDocumentsList(ct, materia);
            //Utils.replaceDocumentSubjectHash(ct, materia, doc);
            Toast.makeText(ct, "Nombre actualizado correctamente", Toast.LENGTH_SHORT).show();
            notifyItemChanged(pos);
        }).setNegativeButton("Cancel", null);
        alertDialogBuilder.create().show();
    }
}
