package com.APlus.Schodule;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.APlus.Schodule.ui.ajustes.fragment_ajustes;
import com.APlus.Schodule.ui.apuntes.CopiarArchivos;
import com.APlus.Schodule.ui.apuntes.SendFragment;
import com.APlus.Schodule.ui.form.FormFragment;
import com.APlus.Schodule.ui.tareas.GalleryFragment;
import com.APlus.Schodule.ui.home.HomeFragment;
import com.APlus.Schodule.ui.eventos.SlideshowFragment;
import com.APlus.Schodule.ui.tic_tac_toe;
import com.APlus.Schodule.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int[] Images = {
            R.mipmap.ic_1_foreground, R.mipmap.ic_2_foreground, R.mipmap.ic_3_foreground,
            R.mipmap.ic_20_foreground, R.mipmap.ic_4_foreground, R.mipmap.ic_5_foreground, R.mipmap.ic_6_foreground,
            R.mipmap.ic_7_foreground, R.mipmap.ic_8_foreground, R.mipmap.ic_9_foreground, R.mipmap.ic_10_foreground,
            R.mipmap.ic_11_foreground, R.mipmap.ic_12_foreground, R.mipmap.ic_13_foreground, R.mipmap.ic_14_foreground,
            R.mipmap.ic_15_foreground, R.mipmap.ic_16_foreground, R.mipmap.ic_17_foreground, R.mipmap.ic_18_foreground,
            R.mipmap.ic_19_foreground, R.mipmap.ic_default_foreground, R.mipmap.mdt24,
            R.mipmap.mdt25, R.mipmap.mdt26, R.mipmap.mdt27, R.mipmap.mdt28, R.mipmap.mdt29, R.mipmap.mdt30,
            R.mipmap.mdt31, R.mipmap.mdt32, R.mipmap.mdt33, R.mipmap.mdt34, R.mipmap.mdt35, R.mipmap.mdt36, R.mipmap.mdt37,
            R.mipmap.mdt38, R.mipmap.mdt39, R.mipmap.mdt40, R.mipmap.mdt41, R.mipmap.mdt42, R.mipmap.mdt43,
            R.mipmap.ic_21_foreground
    };
    public static int[] ext = {
            R.mipmap.ic_ext1_foreground, R.mipmap.ic_ext2_foreground, R.mipmap.ic_ext3_foreground,
            R.mipmap.ic_ext4_foreground, R.mipmap.ic_ext5_foreground, R.mipmap.ic_ext6_foreground,
            R.mipmap.ic_ext7_foreground, R.mipmap.ic_ext8_foreground, R.mipmap.ic_ext9_foreground,
            R.mipmap.ic_ext10_foreground, R.mipmap.ic_ext11_foreground, R.mipmap.ic_ext12_foreground,
            R.mipmap.ic_ext13_foreground
    };
    public static String[] term = {"PNG", "GIF", "JAV/JAVA", "PYT/PY", "PPT/PPTX", "PDF", "TXT", "DOCX/DOC", "XLS/XLSX", "OBJ", "MP4", "MP3", "JPG/JPEG"};
    public static String[] tipoDoc = {"image/*", "video/*", "audio/*", "text/plain", "application/pdf", "*/*"};
    public static boolean[] dias_horario = {true, true, true, true, true, false, false};

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferences settings;
    public static File carp_copias;
    public static String name = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dias_horario = Utils.getSchedule(this.getApplicationContext());
        if(dias_horario == null || dias_horario.length < 7)
            dias_horario = new boolean[]{true, true, true, true, true, false, false};

        //tic tac toe
        findViewById(R.id.bt_color).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, tic_tac_toe.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);

        this.settings = sharedPreferences;
        String hour = sharedPreferences.getString("hour", "");
        String minute = this.settings.getString("minute", "");

        /*if (!(hour == "" || minute == "")) {
            Calendar today = Calendar.getInstance();
            today.set(11, Integer.parseInt(hour));
            today.set(12, Integer.parseInt(minute));
            today.set(13, 0);
            Utils.setAlarm(1, Long.valueOf(today.getTimeInMillis()), this);
        }*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        MenuItem tools = navigationView.getMenu().findItem(R.id.head_notas);
        SpannableString spanString = new SpannableString(tools.getTitle());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blanco100)), 0, spanString.length(), 0);
        tools.setTitle(spanString);

        MenuItem tools2 = navigationView.getMenu().findItem(R.id.head_extras);
        SpannableString spanString2 = new SpannableString(tools2.getTitle());
        spanString2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blanco100)), 0, spanString2.length(), 0);
        tools2.setTitle(spanString2);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new HomeFragment()).commit();

        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getpermission = new Intent();
                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getpermission);
            }
        }
        //subp(this);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new HomeFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new GalleryFragment()).commit();
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new SlideshowFragment()).commit();
        } /*else if (id == R.id.nav_tools) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ToolsFragment()).commit();
        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ShareFragment()).commit();
        }*/ else if (id == R.id.nav_send) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new SendFragment()).commit();
        } else if (id == R.id.nav_form) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new FormFragment()).commit();
        } else if (id == R.id.nav_ajustes) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new fragment_ajustes()).commit();
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.buscar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertD));
            builder.setTitle("¡Próximamente más!");
            builder.setMessage("Hola, primero que nada, gracias por instalar esta app y apoyarme en este proceso que es aprender y poder ofrecer algo cómodo hacia los usuarios como tú.\n\nYo continúo creciendo con cada idea que me aportan todos ustedes y para mí, aunque apenas comenzando con mi carrera profesional, cada nueva actualización es un gran salto que doy, que se siente con profesionalismo.\n\nEspero que disfrutes de las herramientas que te brinda la app, y espero que cualquier comentario me lo permitas saber.\n\nCon gusto: F/G/S/C; @SchoduleT.\n\nPor cierto, la app esconde un pequeño \"EasterEgg\", te reto a encontrarlo.");
            builder.setPositiveButton("Aceptar", null);
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode == 1001){
            createBackup(ct);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}