package com.APlus.Schodule.ui.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.APlus.Schodule.R;

/* renamed from: com.App.SchoduleT.ui.form.GridViewMain */
public class GridViewMain extends BaseAdapter {
    int[] colores;
    Context context;
    LayoutInflater layoutInflater;
    String[] nombs;

    public GridViewMain(Context applicationContext, String[] nombs2, int[] colores2) {
        this.context = applicationContext;
        this.nombs = nombs2;
        this.colores = colores2;
        this.layoutInflater = LayoutInflater.from(applicationContext);
    }

    public int getCount() {
        return this.nombs.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView2 = this.layoutInflater.inflate(R.layout.custom_main_gridview, (ViewGroup) null);
        ((ImageView) convertView2.findViewById(R.id.icon)).setImageResource(this.colores[position]);
        return convertView2;
    }
}
