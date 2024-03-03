package com.APlus.Schodule.ui.ajustes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.APlus.Schodule.MainActivity;
import com.APlus.Schodule.R;
import com.APlus.Schodule.ui.apuntes.CopiarArchivos;
import com.APlus.Schodule.utils.Horario;
import com.APlus.Schodule.utils.Materia;
import com.APlus.Schodule.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class fragment_ajustes extends Fragment {
    public SharedPreferences respaldoshared;
    View root;
    boolean auto_var = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ajustes, container, false);
        this.root = inflate;
        Button auto = root.findViewById(R.id.auto);
        if (!(ContextCompat.checkSelfPermission(this.root.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ActivityCompat.checkSelfPermission(this.root.getContext(), "android.permission.CAMERA") == 0)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1000);
        }

        getActivity().setTitle("Ajustes");
        this.respaldoshared = getActivity().getSharedPreferences("respaldo", 0);
        if (respaldoshared.getString("respaldo?", "").equalsIgnoreCase("")) auto_var = false;
        else auto_var = Boolean.parseBoolean(respaldoshared.getString("respaldo?", ""));
        stateAuto(auto);

        auto.setOnClickListener((v) -> {
            auto_var = !auto_var;
            stateAuto(auto);
            setShared();


        });

        root.findViewById(R.id.errase).setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertD));
            builder.setTitle("Importante");
            builder.setMessage("¿Deseas conservar tus eventos?");
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                borrar(root.getContext().getExternalFilesDir(null).list(), "", true);
                Toast.makeText(root.getContext(), "Todos los documentos eliminados", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertD));
                builder1.setTitle("Eliminar todos los datos de la app");
                builder1.setMessage("¿Deseas eliminar todo?");
                builder1.setPositiveButton("Aceptar", (dialog1, which1) -> {
                    borrar(root.getContext().getExternalFilesDir(null).list(), "");
                    Toast.makeText(root.getContext(), "Todos los documentos eliminados", Toast.LENGTH_SHORT).show();
                });
                builder1.setNegativeButton("Cancelar", null);
                builder1.create().show();
            });
            builder.create().show();
        });

        root.findViewById(R.id.manual).setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertD));
            builder.setTitle("Crear copia de todo");
            builder.setMessage("¿Deseas crear una copia de todo?");
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (!Environment.isExternalStorageManager()) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 1001);
                            return;
                        }
                    } else {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                            return;
                        }
                    }

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(intent, 1002);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.create().show();
        });

        this.root.findViewById(R.id.recuperar).setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertD));
            builder.setTitle("Recuperar datos");
            builder.setMessage("¿Deseas recuperar todo?");
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivityForResult(intent, 1003);
                        return;
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1003);
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, 1004);
            });
            builder.setNegativeButton("Cancelar",null);
            builder.create().show();
        });

        ///Change days in schedule
        root.findViewById(R.id.dias_hor).setOnClickListener((v) -> {
            changeSchedule();
        });

        return this.root;
    }

    //change schedule days
    private void changeSchedule() {
        View promptsView = LayoutInflater.from(root.getContext()).inflate(R.layout.schedule, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(root.getContext(), R.style.AlertD));
        alertDialogBuilder.setView(promptsView);

        CheckBox cb[] = new CheckBox[7];
        int idcb[] = {R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5, R.id.cb6, R.id.cb7};
        for (int i = 0; i < cb.length; i++) {
            cb[i] = promptsView.findViewById(idcb[i]);
            if (MainActivity.dias_horario[i])
                cb[i].setChecked(true);
        }

        alertDialogBuilder.setCancelable(false).setPositiveButton("Incluir días", (dialog, id) -> {
            boolean dias[] = new boolean[7];
            for (int i = 0; i < dias.length; i++)
                dias[i] = cb[i].isChecked();
            Utils.createSchedule(root.getContext(), dias);
            MainActivity.dias_horario = dias;
            Toast.makeText(root.getContext(), "Configuración almacenada", Toast.LENGTH_SHORT).show();
        }).setNegativeButton("Cancel", null);
        alertDialogBuilder.create().show();
    }

    ///Change value of shared
    private void setShared() {
        SharedPreferences.Editor edit = respaldoshared.edit();
        edit.putString("respaldo?", String.valueOf(auto_var));
        edit.commit();
    }

    private void stateAuto(Button auto) {
        if (!auto_var)
            auto.setBackgroundTintList(getResources().getColorStateList(R.color.teal_200));
        else
            auto.setBackgroundTintList(getResources().getColorStateList(R.color.darker));
    }

    public void recuperar(DocumentFile[] names, String dirO) {
        String barra = "";
        if (dirO != null && !dirO.equalsIgnoreCase(barra)) {
            barra = "/";
        }
        for (DocumentFile file : names) {
            if (file.isDirectory() && file.listFiles().length > 0) {
                recuperar(file.listFiles(), dirO + "/" + file.getName());
            } else if (!file.isDirectory() && file.length() > 0) {
                try {
                    File doc = new File(dirO + "/" + file.getName());
                    if (doc.getParentFile() != null) {
                        doc.getParentFile().mkdirs();
                    }
                    doc.createNewFile();
                    if (doc.exists() && doc.isFile()) {
                        InputStream in = this.root.getContext().getContentResolver().openInputStream(file.getUri());
                        OutputStream out = new FileOutputStream(doc);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                        in.close();
                        out.close();
                    }
                } catch (IOException e) {
                    Toast.makeText(this.root.getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void borrar(String[] names, String dir, boolean eventos) {
        String barra = "";
        barra = dir != null && !dir.equalsIgnoreCase(barra) ? "/" : barra;

        for (int i = 0; i < names.length; i++) {
            File princ = this.root.getContext().getExternalFilesDir(dir + barra + names[i]);
            String Sdir = dir + barra + names[i];
            if (princ.isDirectory() && princ.list().length > 0) {
                borrar(this.root.getContext().getExternalFilesDir(Sdir).list(), Sdir, true);
                if (eventos && princ.getName().equalsIgnoreCase("ListaEventos"))
                    continue;
                this.root.getContext().getExternalFilesDir(Sdir).delete();
            } else if (princ.length() > 0) {
                if (eventos && princ.getName().equalsIgnoreCase("ListaEventos"))
                    continue;
                new File(this.root.getContext().getExternalFilesDir(null) + barra + Sdir).delete();
            }
        }
    }

    public void borrar(String[] names, String dir) {
        String barra = "";
        if (dir != null && !dir.equalsIgnoreCase(barra)) {
            barra = "/";
        }
        for (int i = 0; i < names.length; i++) {
            if (names[i].contains("COPIAS"))
                continue;
            File princ = this.root.getContext().getExternalFilesDir(dir + barra + names[i]);
            String Sdir = dir + barra + names[i];
            if (princ.isDirectory() && princ.list().length > 0) {
                borrar(this.root.getContext().getExternalFilesDir(Sdir).list(), Sdir);
                this.root.getContext().getExternalFilesDir(Sdir).delete();
            } else if (princ.length() > 0) {
                new File(this.root.getContext().getExternalFilesDir((String) null) + barra + Sdir).delete();
            }
        }
    }

    ///For copy

    public String name_copia() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    private void createBackup(String[] names, String dir, String name, DocumentFile destination) {
        Context ct = getContext();
        String barra = "";
        if (dir != null && !dir.equalsIgnoreCase(barra)) {
            barra = "/";
        }
        for (String filename : names) {
            File princ = new File(ct.getExternalFilesDir(dir + barra + filename).getPath());
            String Sdir =  dir + barra + filename;

            if (princ.isDirectory() && princ.list().length > 0) {
                DocumentFile destDir = destination.createDirectory(filename);
                createBackup(ct.getExternalFilesDir(dir + barra + filename).list(), Sdir, name, destDir);

            } else if (princ.isDirectory() && princ.list().length == 0) {
                princ.delete();
            } else if (!princ.isDirectory() && princ.length() > 0) {
                try {
                    DocumentFile destFile = destination.createFile("plain", filename);
                    if (destFile != null) {
                        OutputStream outputStream = ct.getContentResolver().openOutputStream(destFile.getUri());
                        if (outputStream != null) {
                            FileInputStream inputStream = new FileInputStream(princ);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            inputStream.close();
                            outputStream.close();
                            //CopiarArchivos.main(new String[]{princ.getPath(), destFile.getUri().getPath()});
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(ct, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode){
            case 1001:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, 1002);
                break;
            case 1002:
                Uri treeUri = data.getData();
                DocumentFile pickedDir = DocumentFile.fromTreeUri(getContext(), treeUri);

                createBackup(getContext().getExternalFilesDir(null).list(), "", name_copia(), pickedDir.createDirectory("COPIA_"+name_copia()));
                Toast.makeText(root.getContext(), "Respaldo creado", Toast.LENGTH_SHORT).show();
                break;
            case 1003:
                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent2, 1004);
                break;
            case 1004:
                Uri treeUri2 = data.getData();
                DocumentFile pickedDir2 = DocumentFile.fromTreeUri(getContext(), treeUri2);
                recover(pickedDir2);
                break;
        }
    }

    private void recover(DocumentFile files){
        borrar(root.getContext().getExternalFilesDir(null).list(), "");
        recuperar(files.listFiles(), getContext().getExternalFilesDir(null).getPath());
        Toast.makeText(root.getContext(), "Información recuperada", Toast.LENGTH_SHORT).show();
    }

    ////////Thread

    public void execWithThread() {
        new Thread(() -> root.post(() -> {
            while(true) {
                Toast.makeText(getActivity(), "¡Números Ordenados!", Toast.LENGTH_LONG).show();
            }
        }
        )).start();
    }
}