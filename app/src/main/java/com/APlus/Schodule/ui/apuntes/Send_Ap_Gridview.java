package com.APlus.Schodule.ui.apuntes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Nota;

import java.util.ArrayList;
import java.util.Random;

public class Send_Ap_Gridview extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Nota> lista_apuntes;

    private Random f179r = new Random();
    private int ran;

    public Send_Ap_Gridview(Context applicationContext, ArrayList<Nota> lista_apuntes2) {
        this.context = applicationContext;
        this.lista_apuntes = lista_apuntes2;
        this.layoutInflater = LayoutInflater.from(applicationContext);
    }

    public int getCount() {
        return this.lista_apuntes.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this.ran = this.f179r.nextInt(4) + 1;
        View convertView2 = this.layoutInflater.inflate(R.layout.activity_send_ap_gridview, (ViewGroup) null);
        ImageView imageView = (ImageView) convertView2.findViewById(R.id.icon);
        TextView tv = (TextView) convertView2.findViewById(R.id.icon_txt);
        int i = this.ran;
        if (i == 1) {
            imageView.setImageResource(R.mipmap.ic_note1_foreground);
        } else if (i == 2) {
            imageView.setImageResource(R.mipmap.ic_note2_foreground);
        } else if (i == 3) {
            imageView.setImageResource(R.mipmap.ic_note3_foreground);
        } else if (i == 4) {
            imageView.setImageResource(R.mipmap.ic_note4_foreground);
        }
        tv.setText(lista_apuntes.get(position).getTitulo());
        return convertView2;
    }
}
