package com.APlus.Schodule.ui.apuntes;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.Documento;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Nota;
import com.APlus.Schodule.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;


public class send_apunte_01 extends AppCompatActivity {
    public static final String BARRA = "/";
    private static final int CAMERA_REQUEST = 500;
    private static final String IMAGE_SELECT_ALL_TYPE = "image/*";
    private static final int REQUEST_CODE_GALLERY = 1;

    private Animation f180FB;
    String nombre = "";
    private File photoFile = null;
    private ProgressBar progressBar;

    private Animation f181TB;
    private FloatingActionButton animation;
    private boolean click = false;

    public Button fecha_selected;
    private Send_Ap_Gridview gridViewCustomAdapter;

    private String[] materias;
    private GridView mgridView;

    public String nm_m1;
    private String nombre_foto = "";
    private Animation rotC;
    private Animation rotO;
    FloatingActionButton sap_b_sf;
    private FloatingActionButton sap_b_ssf;
    private FloatingActionButton sap_b_tf;

    public AutoCompleteTextView searcher;

    public String titulo;
    private Materia materia = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_apunte_01);
        sap_b_tf = findViewById(R.id.sap_b_tf);
        sap_b_ssf = findViewById(R.id.sap_b_ssf);
        sap_b_sf = findViewById(R.id.sap_b_sf);
        animation = findViewById(R.id.animation);
        fecha_selected = findViewById(R.id.fecha_selected);
        searcher = findViewById(R.id.searcher);
        progressBar = findViewById(R.id.progressBar);

        rotO = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotC = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        f181TB = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        f180FB = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);

        materia = (Materia) getIntent().getExtras().get("materia");
        nm_m1 = materia.getName();

        setTitle("Mis notas de " + nm_m1);

        if (!(ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0)) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"}, 1000);
        }

        findViewById(R.id.animation).setOnClickListener((v) -> {
            alclick();
        });

        refrescar();

        ///custom_listview_simple is a test
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.custom_listview_simple, materia.getNotas());
        searcher.setAdapter(adapter);
        searcher.setThreshold(1);
        searcher.setAdapter(adapter);

        searcher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Nota> notas = materia.getNotas();
                for(int j = 0; j < notas.size(); j++){
                    if(searcher.getText().toString().equalsIgnoreCase(notas.get(i).getTitulo())){
                        herramientas_nota(i);
                        break;
                    }
                }
            }
        });

        /////Create a new note
        findViewById(R.id.add_ap).setOnClickListener((v) -> {
            if (fecha_selected.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(send_apunte_01.this, "Primero selecciona una fecha", Toast.LENGTH_SHORT).show();
                return;
            }
            crear_apunte();
        });

        ///////Actions for the note
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                herramientas_nota(position);
            }
        });

        //////Select a date
        findViewById(R.id.bot_fecha).setOnClickListener((v) -> {
            fecha(v);
        });

        ///////Take picture from camera
        sap_b_tf.setOnClickListener((v) -> {
            final String[] nomb = {null};
            View promptsView = LayoutInflater.from(send_apunte_01.this).inflate(R.layout.prompts, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertD));
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);
            alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    nombre = nomb[0] = userInput.getText().toString();
                    if (nomb[0].equals("")){
                        Toast.makeText(send_apunte_01.this, "Error de nombre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tomarFoto(nomb[0]);
                }
            }).setNegativeButton((CharSequence) "Cancel", null);
            alertDialogBuilder.create().show();
        });


        ////////Select document from gallery
        sap_b_ssf.setOnClickListener((v) -> {
            final String[] nomb = {null};
            View promptsView = LayoutInflater.from(send_apunte_01.this).inflate(R.layout.prompts, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(send_apunte_01.this, R.style.AlertD));
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
            alertDialogBuilder.setCancelable(false).setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    nomb[0] = userInput.getText().toString();
                    if (nomb[0].equals("")) {
                        Toast.makeText(send_apunte_01.this, "Primero establece un nombre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    nombre = nomb[0];
                    selecFoto(nomb[0]);
                }
            }).setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alertDialogBuilder.create().show();
        });
    }

    public void herramientas_nota(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertD));
        builder.setTitle("Nota:");
        builder.setMessage(lector(position));
        builder.setPositiveButton("Aceptar", null);
        builder.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(send_apunte_01.this, R.style.AlertD));
                builder.setTitle("Editar Nota");
                builder.setMessage("¿Deseas editar esta nota?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editar_nota(position);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.create().show();
            }
        });

        builder.setNeutralButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(send_apunte_01.this, R.style.AlertD));
                builder.setTitle("Eliminar nota");
                builder.setMessage("¿Deseas eliminar esta nota?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(send_apunte_01.this, "Nota eliminada con éxito", Toast.LENGTH_SHORT).show();
                        materia.getNotas().remove(position);
                        Utils.replaceMateria(send_apunte_01.this, materia);
                        refrescar();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.create().show();
            }
        });
        builder.create().show();
    }

    public void crear_apunte() {
        final String[] nomb = {null, null};
        View promptsView = LayoutInflater.from(this).inflate(R.layout.send_insertartitulo,  null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertD));
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.etAdd);
        final EditText userInputC = promptsView.findViewById(R.id.etcont);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nomb[0] = userInput.getText().toString();
                nomb[1] = userInputC.getText().toString();

                if(nomb[0].equals("") || nomb[1].equals("")){
                    Toast.makeText(send_apunte_01.this, "Dejaste campos vacíos", Toast.LENGTH_LONG).show();
                    return;
                }

                materia.getNotas().add(new Nota(fecha_selected.getText().toString(), nomb[0], nomb[1]));
                Utils.replaceMateria(send_apunte_01.this, materia);
                refrescar();
            }
        }).setNegativeButton("Cancelar",null);
        alertDialogBuilder.create().show();
    }

    public void editar_nota(int position) {
        Nota nota = materia.getNotas().get(position);
        final String[] nomb = {null, null};
        View promptsVieww = LayoutInflater.from(this).inflate(R.layout.send_insertartitulo,  null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertD));
        alertDialogBuilder.setView(promptsVieww);
        EditText userInputt = (EditText) promptsVieww.findViewById(R.id.etAdd);
        EditText userInputC = promptsVieww.findViewById(R.id.etcont);

        userInputt.setText(nota.getTitulo());
        userInputC.setText(nota.getContenido());

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nomb[0] = userInputt.getText().toString();
                nomb[1] = userInputC.getText().toString();

                if(nomb[0].equals("") || nomb[1].equals("")){
                    Toast.makeText(send_apunte_01.this, "Hay campos vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }
                nota.setTitulo(nomb[0]);
                nota.setContenido(nomb[1]);
                Utils.replaceMateria(send_apunte_01.this, materia);
                refrescar();
            }
        }).setNegativeButton((CharSequence) "Cancelar", null);
        alertDialogBuilder.create().show();
    }

    private String lector(int position) {
        Nota nota = materia.getNotas().get(position);
        return nota.getTitulo() + "\n\n" + nota.getContenido();
    }

    /*public void establecer_nom_foto(String val) {
        SharedPreferences.Editor editor_nomF = getSharedPreferences(NOMFOT, 0).edit();
        editor_nomF.putString(ENOMFOT, val);
        editor_nomF.commit();
    }*/

    private File CrearArchivoFoto(String nombre) throws IOException {
        File storageDir = getExternalFilesDir(nm_m1);
        return new File(storageDir + BARRA + nombre);
    }
/*
    private File CrearArchivoFoto(String nombre) throws IOException {
        File storageDir = getExternalFilesDir("Materias" + BARRA + "docs" + BARRA + nm_m1);
        return new File(storageDir + BARRA + nombre + ".jpg");
    }*/

    public void tomarFoto(String nombre) {
        try {
            Toast.makeText(getApplicationContext(), "Toma la Foto", Toast.LENGTH_LONG).show();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    photoFile = CrearArchivoFoto(nombre);
                } catch (IOException e) {
                }
                if (photoFile != null) {
                    takePictureIntent.putExtra("output", FileProvider.getUriForFile((Objects.requireNonNull(getApplicationContext())), "com.APlus.Schodule.provider", photoFile));
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
        }catch (Exception e){Toast.makeText(send_apunte_01.this, e.getMessage(), Toast.LENGTH_LONG).show();}
    }

    public void selecFoto(String nombre) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.deleteDir(send_apunte_01.this, nombre);
        if (requestCode == 1 && resultCode == -1 && data != null) {
            String filesrc = getPath(getApplicationContext(), data.getData());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                byte[] buffer = new byte[is.available()];
                int i = 0;
                do {
                    i = is.read(buffer);
                    if (i != -1) {
                        baos.write(buffer, 0, i);
                    } else break;
                } while (true);

                byte[] doc = baos.toByteArray();
                String extS = getExt(filesrc);
                Documento doci = new Documento(doc, materia, nombre);
                doci.setExt(getIntExt(extS));
                doci.setName(doci.getName()+"."+extS);
                //HashMap<String, String> docs = Utils.getDocumentsList(send_apunte_01.this, materia);
                //doci.setExt(12);
                //ArrayList<Documento> docs = Utils.getDocuments(send_apunte_01.this, materia);
                //docs.put(doci.getCodigo(), doci.getName());
                //Utils.replaceDocumentsList(send_apunte_01.this, materia, docs);
                Utils.replaceDocumentDirect(send_apunte_01.this, materia, doci, null);
                //Utils.replaceDocuments(send_apunte_01.this, materia, docs);
                //refrescar();

                /*Intent in = new Intent(send_apunte_01.this, Activity_Extensiones.class);
                in.putExtra("materia", materia);
                in.putExtra("nombredoc", doci.getName());*/
                refrescar();
                Snackbar.make(findViewById(R.id.Rel_send_01), "Documento guardado correctamente", Snackbar.LENGTH_LONG).show();
                //startActivityForResult(in, 20);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == CAMERA_REQUEST){
            Snackbar.make(findViewById(R.id.Rel_send_01), "Documento guardado correctamente", Snackbar.LENGTH_LONG).show();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                InputStream is = getContentResolver().openInputStream(Uri.fromFile(photoFile));
                byte[] buffer = new byte[1024];
                int i = 0;
                do {
                    i = is.read(buffer);
                    if (i != -1) {
                        baos.write(buffer, 0, i);
                    } else break;
                } while (true);

                byte[] doc = baos.toByteArray();
                Documento doci = new Documento(doc, materia, nombre);
                doci.setExt(12);
                doci.setName(doci.getName()+".jpg");
                Utils.replaceDocumentDirect(send_apunte_01.this, materia, doci, null);

                /*ArrayList<Documento> docs = Utils.getDocuments(send_apunte_01.this, materia);
                docs.add(doci);
                Utils.replaceDocuments(send_apunte_01.this, materia, docs);*/

                refrescar();
                new File(photoFile.getPath()).delete();
                new File(getExternalFilesDir(nm_m1)+"").delete();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == 20){
            Snackbar.make(findViewById(R.id.Rel_send_01), "Documento guardado correctamente", Snackbar.LENGTH_LONG).show();
        }
    }

    public String getExt(String data){
        String res = "", resreal = "";
        for(int i = data.length()-1; i >= 0; i--){
            if(data.charAt(i) == '.')
                break;
            res += String.valueOf(data.charAt(i));
        }

        for(int i = res.length()-1; i >= 0; i--){
            resreal += String.valueOf(res.charAt(i));
        }
        return resreal;
    }

    public int getIntExt(String ext){
        String data[] = MainActivity.term;
        int value = -1;
        for(int i = 0; i < data.length; i++){
            if(!data[i].toLowerCase().contains(ext.toLowerCase()))
                continue;
            value = i;
            break;
        }
        if(value == -1)
            value = 9;
        return value;
    }

    public static String getPath( Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "";
        }
        return result;
    }

    public void ver(View v) {
        Intent intento1 = new Intent(this, send_apunte_01_2.class);
        intento1.putExtra("materia", materia);
        startActivityForResult(intento1, 0);
    }

    public void fecha(View v) {
        Calendar calendarioT = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Button access$100 = fecha_selected;
                access$100.setText(String.valueOf(dayOfMonth) + " - " + String.valueOf(month + 1) + " - " + String.valueOf(year));
            }
        }, calendarioT.get(1), calendarioT.get(2), calendarioT.get(5)).show();
    }

    public void refrescar() {
        materia = Utils.getMateria(send_apunte_01.this, materia);
        mgridView = (GridView) findViewById(R.id.gridview);
        Send_Ap_Gridview send_Ap_Gridview = new Send_Ap_Gridview(this, materia.getNotas());
        gridViewCustomAdapter = send_Ap_Gridview;
        mgridView.setAdapter(send_Ap_Gridview);
    }

    //////////////////////////////// Animation Methods
    public void alclick() {
        visibilidad(click);
        anim(click);
        click = !click;
    }

    private void visibilidad(boolean clicked) {
        if (!clicked) {
            sap_b_sf.show();
            sap_b_sf.setClickable(true);
            //sap_b_tf.setVisibility(0);
            sap_b_tf.show();
            sap_b_tf.setClickable(true);
            sap_b_ssf.show();
            //sap_b_ssf.setVisibility(0);
            sap_b_ssf.setClickable(true);
            return;
        }
        sap_b_sf.hide();//.setVisibility(8);
        sap_b_sf.setClickable(false);
        sap_b_tf.hide();
        //sap_b_tf.setVisibility(8);
        sap_b_tf.setClickable(false);
        sap_b_ssf.hide();
        //sap_b_ssf.setVisibility(8);
        sap_b_ssf.setClickable(false);
    }

    private void anim(boolean clicked) {
        if (!clicked) {
            sap_b_ssf.startAnimation(f180FB);
            sap_b_tf.startAnimation(f180FB);
            sap_b_sf.startAnimation(f180FB);
            animation.startAnimation(rotO);
            return;
        }
        sap_b_ssf.startAnimation(f181TB);
        sap_b_tf.startAnimation(f181TB);
        sap_b_sf.startAnimation(f181TB);
        animation.startAnimation(rotC);
    }
}
