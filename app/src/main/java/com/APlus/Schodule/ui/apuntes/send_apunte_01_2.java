package com.APlus.Schodule.ui.apuntes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Documento;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;


public class send_apunte_01_2 extends AppCompatActivity {

    private RecyclerAdapter_Docs f182RA;
    private ArrayList<String> docs;
    private String nm_or;
    private RecyclerView recyclerView;
    private Materia materia;

    public SwipeRefreshLayout swipeRefreshLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_apunte_01_2);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        materia = (Materia) getIntent().getExtras().get("materia");
        nm_or = materia.getName();

        setTitle("Mis Documentos de " + nm_or);
        actualizador();

        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        actualizador();
    }

    public void actualizador() {
        materia = Utils.getMateria(send_apunte_01_2.this, materia);
        docs = Utils.getDocumentsList(send_apunte_01_2.this, materia);

        this.recyclerView = findViewById(R.id.recyclerView);
        RecyclerAdapter_Docs recyclerAdapter_Docs = new RecyclerAdapter_Docs(materia, this, docs);
        this.f182RA = recyclerAdapter_Docs;
        this.recyclerView.setAdapter(recyclerAdapter_Docs);
        //this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    }
}
