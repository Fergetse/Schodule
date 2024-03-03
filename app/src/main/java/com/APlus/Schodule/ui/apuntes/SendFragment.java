package com.APlus.Schodule.ui.apuntes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SendFragment extends Fragment implements Serializable {
    private MyAdapter myadapter;
    private RecyclerView recyclerView;

    private ArrayList<Materia> materias = new ArrayList<>();

    private View root;
    private SwipeRefreshLayout swipeRefreshLayout;
    private transient SendFragment obj; //todo -> Learn more about transient

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_send, container, false);

        refrescar();
        getActivity().setTitle("Materias");

        //go to add new subject
        root.findViewById(R.id.bt_newmat).setOnClickListener((v) -> {
            Intent in = new Intent(getActivity(), send_nom_materias.class);
            try {
                startActivityForResult(in, 0);
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        swipeRefreshLayout = root.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));

        return root;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refrescar();
    }

    public void refrescar() {
        materias = Utils.getMaterias(root.getContext());
        recyclerView = root.findViewById(R.id.rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));

        MyAdapter myAdapter = new MyAdapter(this, materias);
        //myadapter = myAdapter;
        recyclerView.setAdapter(myAdapter);
        Toast.makeText(root.getContext(), "here", Toast.LENGTH_SHORT);
        //recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), 1));
    }
}
