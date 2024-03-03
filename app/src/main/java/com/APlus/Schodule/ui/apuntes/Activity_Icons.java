package com.APlus.Schodule.ui.apuntes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Ico;
import com.APlus.Schodule.utils.Materia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Activity_Icons extends AppCompatActivity {
    private File dir;
    private ArrayList<Materia> materias = new ArrayList<>();

    private int f176nm;
    private Materia materia;
    private RecyclerAdapter_Send recyclerAdapter;
    private Context root;
    private String rutaIMG;
    private Uri uriIMG;
    private FileOutputStream write_mat;
    private boolean editar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity__icons);

        materia = (Materia) getIntent().getExtras().get("materia");
        try{
            editar = getIntent().getExtras().getBoolean("edit");
        }catch (Exception e){
            editar = false;
        }

        setTitle("Ícono para " + materia.getName());
        GridView mgridView = (GridView) findViewById(R.id.gridview);
        mgridView.setAdapter(new GridViewCustomAdapter(this, 0));


        mgridView.setOnItemClickListener((adapterView, view, position, id) -> {
            if (position != MainActivity.Images.length-1) {
                escribir_poc_ico(position, true);
                finish();
                return;
            }
            selecFoto();
        });
    }

    public void escribir_poc_ico(int position, boolean animated) {
        if(animated)
            materia.setIcoAnimated(position);
        try {
            if(new File(getExternalFilesDir("Materias") + "/" + "ListaMaterias").exists()){
                ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(new File(getExternalFilesDir("Materias") + "/" + "ListaMaterias")));
                materias = (ArrayList<Materia>) entrada.readObject();
                entrada.close();
            }
            if(editar){
                for(int i = 0; i < materias.size(); i++){
                    if(materias.get(i).getName().equals(materia.getName())) {
                        materias.remove(i);
                        materias.add(i, materia);
                        break;
                    }
                }
            }else{
                materias.add(materia);
            }
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(new File(getExternalFilesDir("Materias") + "/" + "ListaMaterias")));
            salida.writeObject(materias);
            salida.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    public void selecFoto() {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/jpeg");
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try{
                InputStream is = getContentResolver().openInputStream(data.getData());
                byte[] buffer = new byte[1024];
                int i = 0;
                do{
                    i = is.read(buffer);
                    if(i != -1){
                        baos.write(buffer, 0, i);
                    }else break;
                }while (true);

                byte[] img = baos.toByteArray();
                if(img.length > 100000){
                    Toast.makeText(this, "Imagen demasiado grande", Toast.LENGTH_LONG).show();
                    return;
                }
                materia.setIco(new Ico(img));
                escribir_poc_ico(0, false);
            }catch (Exception e){
                Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
