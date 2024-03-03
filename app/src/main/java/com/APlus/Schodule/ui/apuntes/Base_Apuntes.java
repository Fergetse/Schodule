package com.APlus.Schodule.ui.apuntes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Base_Apuntes extends AppCompatActivity {
    private String AMN = "";
    private String AMini = "";
    private String ENOMFOT = "";
    private String INIC = "";
    private String MNT = "";
    private String NOMFOT = "";
    private EditText cap_t_adm;

    public TextView cap_tv_fecha;
    private Context ctx;
    private String default_txt = "";

    public String fecha_apunte = null;
    private String nombre_foto = "";

    private Base_Apuntes(String[] args, EditText edittext, TextView tv, Context ct, int met) {
        EditText editText = edittext;
        int i = met;
        this.nombre_foto = args[0];
        this.fecha_apunte = args[1];
        this.default_txt = args[2];
        this.cap_t_adm = editText;
        this.cap_tv_fecha = tv;
        String str = args[3];
        this.AMN = str;
        this.NOMFOT = args[4];
        this.ENOMFOT = args[5];
        this.MNT = args[6];
        this.INIC = args[7];
        this.AMini = args[8];
        Context ctx2 = ct;
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    editText.setText("");
                    Calendar calendarioT = Calendar.getInstance();
                    Context context = ctx2;
                    new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Base_Apuntes base_Apuntes = Base_Apuntes.this;
                            String unused = base_Apuntes.fecha_apunte = String.valueOf(dayOfMonth) + String.valueOf(month) + String.valueOf(year);
                            TextView access$100 = cap_tv_fecha;
                            access$100.setText(String.valueOf(dayOfMonth) + " / " + String.valueOf(month + 1) + " / " + String.valueOf(year));
                        }
                    }, calendarioT.get(1), calendarioT.get(2), calendarioT.get(5)).show();
                } else if (i == 4) {
                    this.AMini = getSharedPreferences(str, 0).getString(this.INIC, "");
                    EditText editText2 = this.cap_t_adm;
                    editText2.setText("Historial de Apuntes:\n\n" + this.AMini);
                }
            } else if (!String.valueOf(tv.getText()).equalsIgnoreCase(this.default_txt)) {
                Toast.makeText(ctx2, "Mostrando Apunte", Toast.LENGTH_LONG).show();
                this.cap_t_adm.setText(getSharedPreferences(this.AMN, 0).getString(this.fecha_apunte, ""));
            } else {
                Toast.makeText(ctx2, "Primero selecciona una fecha", Toast.LENGTH_LONG).show();
            }
        } else if (!String.valueOf(tv.getText()).equalsIgnoreCase(this.default_txt)) {
            Toast.makeText(ctx2, "Guardando Apunte", Toast.LENGTH_LONG).show();
            String apunteMateria = this.cap_t_adm.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences(this.AMN, 0).edit();
            editor.putString(this.fecha_apunte, apunteMateria);
            SharedPreferences.Editor edit = getSharedPreferences(this.AMN, 0).edit();
            if (!this.AMini.equalsIgnoreCase("")) {
                String str2 = this.INIC;
                editor.putString(str2, this.AMini + this.cap_tv_fecha.getText().toString() + "\n");
            } else {
                String str3 = this.INIC;
                editor.putString(str3, this.cap_tv_fecha.getText().toString() + "\n");
            }
            editor.commit();
            this.cap_tv_fecha.setText(this.default_txt);
            this.cap_t_adm.setText("");
            shared_act();
        } else {
            Toast.makeText(ctx2, "Primero selecciona una fecha", Toast.LENGTH_LONG).show();
        }
    }

    public void guardarApunte(View v) {
    }

    public void Verapunte(View v) {
    }

    public void fecha(View v) {
    }

    private void shared_act() {
    }

    public static void main(String[] args, EditText edittext, TextView tv, Context ct, int met) {
        new Base_Apuntes(args, edittext, tv, ct, met);
    }
}
