package com.APlus.Schodule.utils;

import java.io.Serializable;
import java.util.Base64;

public class Ico implements Serializable {
    private byte[] img;
    private String codigo;
    private static final long serialVersionUID = 04L;

    public Ico(byte data[]){
        this.img = data;
        this.codigo = ((data[0]+data[(int)((data.length-1)/2)]+data[data.length-1])+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public static byte[] decoder(String encode){
        return Base64.getDecoder().decode(encode);
    }

    public static String encoder(byte[] array){
        return Base64.getEncoder().encodeToString(array);
    }

    public String getCodigo() {
        if(codigo == null)
            codigo = ((img[0]+img[(int)((img.length-1)/2)]+img[img.length-1])+Utils.getDate()+(Math.random()*Integer.MAX_VALUE+1)+this.hashCode()).trim().replace(" ", "");
        return codigo;
    }
}
