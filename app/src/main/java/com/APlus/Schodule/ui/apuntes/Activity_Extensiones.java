package com.APlus.Schodule.ui.apuntes;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Documento;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Activity_Extensiones extends AppCompatActivity {

    public Materia materia;
    private Documento doc;
    private String nombre;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_extensiones);
        materia = (Materia) getIntent().getExtras().get("materia");
        nombre = getIntent().getExtras().get("nombredoc").toString();

        setTitle("Extensión para " + materia.getName());
        GridView mgridView = (GridView) findViewById(R.id.gridview);
        mgridView.setAdapter(new GridViewCustomAdapter(this, 2));
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                doc = Utils.getDocumentDirect(Activity_Extensiones.this, materia, nombre);
                //doc = Utils.getDocumentsHash(Activity_Extensiones.this, materia).get(nombredoc);
                doc.setExt(position);
                Utils.replaceDocumentDirect(Activity_Extensiones.this, materia, doc, null);
                //Utils.replaceDocumentSubject(Activity_Extensiones.this, materia, doc);
                finish();
            }
        });
    }
}