package com.APlus.Schodule.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.ui.apuntes.send_nom_materias;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    AppBarLayout appBar;
    String correo = "schodule@hotmail.com";
    private HomeViewModel homeViewModel;
    TabLayout tabLayout;
    ViewPager vpPaginas;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //this.homeViewModel = (HomeViewModel) ViewModelProviders.of((Fragment) this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Horario");

        if (getFirstTimeRun(getContext()) == 0) {
            startActivity(new Intent(getContext(), send_nom_materias.class));
        }

        this.appBar = (AppBarLayout) container.findViewById(R.id.appBar);
        TabLayout tabLayout2 = new TabLayout(getActivity());
        this.tabLayout = tabLayout2;

        tabLayout2.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        tabLayout2.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        tabLayout2.setSelectedTabIndicatorColor(Color.WHITE);

        this.appBar.addView(this.tabLayout);

        this.vpPaginas = (ViewPager) root.findViewById(R.id.vpPaginas);
        this.vpPaginas.setAdapter(new ViewPagerAdapter(getFragmentManager()));
        this.tabLayout.setupWithViewPager(this.vpPaginas);

        String dias[] = getPages();
        String names[] = {"D", "L", "M", "Mi", "J", "V", "S"};

        int dia = Calendar.getInstance().get(7);
        String nombre = names[dia-1];
        for(int i = 0; i < dias.length; i++){
            if(dias[i].equalsIgnoreCase(nombre)) {
                vpPaginas.setCurrentItem(i);
                break;
            }else if(dias.length-1 == i && !dias[i].equalsIgnoreCase(nombre)){
                vpPaginas.setCurrentItem(0);
            }
        }

        ((FloatingActionButton) root.findViewById(R.id.fab)).setOnClickListener((View.OnClickListener) view -> {
            Intent email = new Intent("android.intent.action.SEND");
            email.putExtra("android.intent.extra.EMAIL", new String[]{HomeFragment.this.correo});
            email.putExtra("android.intent.extra.SUBJECT", "Mis sugerencias de la app");
            email.setType("message/rfc822");
            HomeFragment.this.startActivity(Intent.createChooser(email, "Escoje tu servidor de Correos"));
        });
        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.appBar.removeView(this.tabLayout);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        String[] paginasTitulos = getPages();//{"L", "M", "Mi", "J", "V"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int i) {
            return new Home_FragmentGeneral(paginasTitulos[i]);
        }

        public int getCount() {
            return this.paginasTitulos.length;
        }

        public CharSequence getPageTitle(int position) {
            return this.paginasTitulos[position];
        }
    }

    public static int getFirstTimeRun(Context contexto) {
        int result;
        SharedPreferences sp = contexto.getSharedPreferences("MYAPP", 0);
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) {
            result = 0;
        } else {
            result = lastVersionCode == 1 ? 1 : 2;
        }
        sp.edit().putInt("FIRSTTIMERUN", 1).apply();
        return result;
    }

    public String[] getPages(){
        String names[] = {"L", "M", "Mi", "J", "V", "S", "D"};
        ArrayList<String> lista = new ArrayList<>();
        for(int i = 0; i < names.length; i++) {
            if (MainActivity.dias_horario[i])
                lista.add(names[i]);
        }

        String list[] = new String[lista.size()];
        for(int i = 0; i < list.length; i++)
            list[i] = lista.get(i);
        return list;
    }
}