package com.APlus.Schodule.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.APlus.Schodule.R;
import com.APlus.Schodule.ui.apuntes.Activity_Icons;
import com.APlus.Schodule.ui.apuntes.SendFragment;
import com.APlus.Schodule.utils.Horario;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.util.ArrayList;

/* renamed from: com.App.SchoduleT.ui.home.Home_FragmentGeneral */
public class Home_FragmentGeneral extends Fragment {
    private int day;
    Home_Basic[] hb = new Home_Basic[5];
    View root;

    public Home_FragmentGeneral(String dia) {
        int day2 = 0;
        String names[] = {"L", "M", "Mi", "J", "V", "S", "D"};
        for(int i = 0; i < names.length; i++){
            if(dia.equalsIgnoreCase(names[i])){
                day2 = i;
                break;
            }
        }
        this.day = day2;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_homegeneral, container, false);

        int i = this.day;
        Home_Basic[] hb1 = {
                new Home_Basic(getActivity(), "Lunes", root, 0, this),
                new Home_Basic(getActivity(), "Martes", root, 1, this),
                new Home_Basic(getActivity(), "Miércoles", root, 2, this),
                new Home_Basic(getActivity(), "Jueves", root, 3, this),
                new Home_Basic(getActivity(), "Viernes", root, 4, this),
                new Home_Basic(getActivity(), "Sábado", root, 5, this),
                new Home_Basic(getActivity(), "Domingo", root, 6, this)
        };
        hb = hb1;
        hb[i].generacion();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hb[requestCode].actualizador(root);
    }
}