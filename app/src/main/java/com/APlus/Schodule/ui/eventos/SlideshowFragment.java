package com.APlus.Schodule.ui.eventos;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.lifecycle.ViewModelProvider;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.EventoN;
import com.APlus.Schodule.utils.Utils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SlideshowFragment extends Fragment {
    private String[] Archivos;
    private ArrayAdapter<String> adaptador;
    CompactCalendarView compactCalendar;
    private ArrayList<EventoN> Leventos = new ArrayList<>();

    public SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM  -  yyyy", Locale.getDefault());

    public String dateformat = "yyyy/MM/dd hh:mm:ss";
    Long fecha;

    public Button fecha_bot;

    public int mDefaultColor;
    RecyclerAdapter_Eventos recyclerAdapter;
    RecyclerView recyclerViewS;
    View root;
    SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        Calendar calT = Calendar.getInstance();
        actualizar_simple();
        this.recyclerViewS = (RecyclerView) this.root.findViewById(R.id.recyclerViewS);
        img = root.findViewById(R.id.img);

        int a = calT.get(1);
        int b = calT.get(2);

        calT.set(a, b, calT.get(5), 12, 0, 0);
        Long dat = Long.valueOf(calT.getTimeInMillis());

        fecha_bot = (Button) this.root.findViewById(R.id.fecha);
        actualizador_lista(getDate(dat, dateformat));

        Button but = (Button) this.root.findViewById(R.id.buttonDate);
        but.setText(mes(b) + " - " + a);
        but.setTextColor(-1);

        getActivity().setTitle("Eventos");

        this.mDefaultColor = ContextCompat.getColor(getActivity(), R.color.design_default_color_primary);
        actualizar_simple();
        actualizar();
        this.fecha_bot.setText(getDate_just(dat.longValue()));
        this.fecha_bot.setTextColor(-1);
        SwipeRefreshLayout swipeRefreshLayout2 = (SwipeRefreshLayout) this.root.findViewById(R.id.swipeRefreshLayout);
        this.swipeRefreshLayout = swipeRefreshLayout2;

        swipeRefreshLayout2.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
        });

        ////Add a new Event
        this.root.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertD));
                builder.setTitle((CharSequence) "Agregar EventoN");
                builder.setMessage((CharSequence) "¿Deseas agregar un nuevo evento?");
                builder.setPositiveButton((CharSequence) "Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fecha();
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancelar", null);
                builder.create().show();
            }
        });

        ///////Clicking one day on calendar
        this.compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            public void onDayClick(Date dateClicked) {
                Calendar cal = Calendar.getInstance();
                cal.set((dateClicked.getYear() - 100) + 2000, dateClicked.getMonth(), dateClicked.getDate(), 12, 0, 0);
                Long date = Long.valueOf(cal.getTimeInMillis());
                fecha_bot.setText(SlideshowFragment.getDate_just(date.longValue()));
                fecha_bot.setTextColor(-1);
                actualizador_lista(getDate(date, dateformat));
            }

            public void onMonthScroll(Date firstDayOfNewMonth) {
                Button b = (Button) SlideshowFragment.this.root.findViewById(R.id.buttonDate);
                b.setText(dateFormatMonth.format(firstDayOfNewMonth));
                b.setTextColor(-1);
            }
        });

        /////not sure what to do in here
        root.findViewById(R.id.buttonDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///todo
            }
        });

        return this.root;
    }

    ///Open the color picker
    public void openColorPicker() {
        new AmbilWarnaDialog(getActivity(), this.mDefaultColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                crear_evento(getDate(fecha.longValue(),dateformat));
            }
        }).show();
    }

    public void crear_evento(String dia) {
        final String[] nomb = {null};
        View promptsVieww = LayoutInflater.from(getActivity()).inflate(R.layout.slide_insertartitulo_evento, (ViewGroup) null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertD));
        alertDialogBuilder.setView(promptsVieww);
        final EditText userInputt = (EditText) promptsVieww.findViewById(R.id.etAdd);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nomb[0] = userInputt.getText().toString();
                if(nomb[0].equals("")){
                    Toast.makeText(SlideshowFragment.this.getActivity(), "No ingresaste un título", Toast.LENGTH_SHORT).show();
                    return;
                }
                EventoN evento = new EventoN(nomb[0], fecha, dia, mDefaultColor);
                Leventos.add(evento);
                Utils.createEventos(root.getContext(), Leventos);
                Toast.makeText(SlideshowFragment.this.getActivity(), "EventoN agregado", Toast.LENGTH_SHORT).show();
                //Utils.setAlarm(1, SlideshowFragment.this.fecha, SlideshowFragment.this.getActivity());
                compactCalendar.addEvent(new Event(mDefaultColor, fecha.longValue(), ""));
                actualizar_simple();
                actualizador_lista(dia);
            }

        }).setNegativeButton((CharSequence) "Cancelar", null);
        alertDialogBuilder.create().show();
    }

    ////Get selected date on datePicker
    public void fecha() {
        Calendar calendarioT = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendarioT = Calendar.getInstance();
                calendarioT.set(year, month, dayOfMonth, 12, 0, 0);
                calendarioT.getTimeInMillis();
                fecha = Long.valueOf(calendarioT.getTimeInMillis());
                openColorPicker();
            }
        }, calendarioT.get(1), calendarioT.get(2), calendarioT.get(5)).show();
    }

    private void actualizar() {
        for(int i = 0; i < Leventos.size(); i++){
            EventoN evt = Leventos.get(i);
            compactCalendar.addEvent(new Event(evt.getColor(), evt.getFecha(), ""));
        }

        CompactCalendarView compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        this.compactCalendar = compactCalendarView;
        compactCalendarView.setUseThreeLetterAbbreviation(true);
    }

    public void actualizador_lista(String dia) {
        Leventos = Utils.getEventos(root.getContext());

        if(!Utils.getEventosDia(root.getContext(), dia).isEmpty()){
            img.setVisibility(View.INVISIBLE);
        }else{
            img.setVisibility(View.VISIBLE);
        }

        this.recyclerViewS = (RecyclerView) this.root.findViewById(R.id.recyclerViewS);
        RecyclerAdapter_Eventos recyclerAdapter_Eventos = new RecyclerAdapter_Eventos(Utils.getEventosDia(root.getContext(), dia), root);
        this.recyclerAdapter = recyclerAdapter_Eventos;
        this.recyclerViewS.setAdapter(recyclerAdapter_Eventos);
        //this.recyclerViewS.addItemDecoration(new DividerItemDecoration(getActivity(), 1));

    }

    public void actualizar_simple() {
        CompactCalendarView compactCalendarView = (CompactCalendarView) this.root.findViewById(R.id.compactcalendar_view);
        this.compactCalendar = compactCalendarView;
        compactCalendarView.setUseThreeLetterAbbreviation(true);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String fech = formatter.format(calendar.getTime()).trim();
        return fech;
    }

    public static String getDate_just(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd / MM / yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private String mes(int b) {
        switch (b) {
            case 0:
                return "ENERO";
            case 1:
                return "FEBRERO";
            case 2:
                return "MARZO";
            case 3:
                return "ABRIL";
            case 4:
                return "MAYO";
            case 5:
                return "JUNIO";
            case 6:
                return "JULIO";
            case 7:
                return "AGOSTO";
            case 8:
                return "SEPTIEMBRE";
            case 9:
                return "OCTUBRE";
            case 10:
                return "NOVIEMBRE";
            case 11:
                return "DICIEMBRE";
            default:
                return "Error";
        }
    }
}