package com.APlus.Schodule.ui.apuntes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Materia;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class GridViewCustomAdapter extends BaseAdapter implements Serializable {
    int[] Images = MainActivity.Images;
    Context context;
    int[] ext = MainActivity.ext;
    LayoutInflater layoutInflater;
    private int met;
    ArrayList<Materia> materias;
    Materia materia;

    public GridViewCustomAdapter(Context applicationContext, int met2) {
        this.context = applicationContext;
        this.met = met2;
        this.layoutInflater = LayoutInflater.from(applicationContext);
    }

    public GridViewCustomAdapter(Context applicationContext, ArrayList<Materia> materias, int met2) {
        this.context = applicationContext;
        this.materias = materias;
        this.met = met2;
        this.layoutInflater = LayoutInflater.from(applicationContext);
    }

    public int getCount() {
        int i = met;
        if (i == 1) {
            return materias.size();
        }else if (i == 0) {
            return Images.length;
        }
        return ext.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView2 = this.layoutInflater.inflate(R.layout.gridview_layout, (ViewGroup) null);
        ImageView imageView = (ImageView) convertView2.findViewById(R.id.icon);
        int i = met;
        if (i == 1) {
            materia = materias.get(position);
            TextView textview = (TextView) convertView2.findViewById(R.id.icon_txt);
            if(materia.isAnimatedIco()){
                if (materia.getIcoAnimated() != -1) {
                    try {
                        imageView.setImageResource(Images[materia.getIcoAnimated()]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageView.setImageResource(R.mipmap.ic_default_foreground);
                }
            }else
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(materia.getIco().getImg(), 0, materia.getIco().getImg().length));

            textview.setText(materia.getName());
            return convertView2;
        } else if (i == 0) {
            imageView.setImageResource(Images[position]);
            return convertView2;
        } else {
            imageView.setImageResource(ext[position]);
            return convertView2;
        }
    }
}