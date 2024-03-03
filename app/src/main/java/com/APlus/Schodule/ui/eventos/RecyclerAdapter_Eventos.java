package com.APlus.Schodule.ui.eventos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.APlus.Schodule.R;
import com.APlus.Schodule.utils.EventoN;
import com.APlus.Schodule.utils.Utils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;

public class RecyclerAdapter_Eventos extends RecyclerView.Adapter<RecyclerAdapter_Eventos.ViewHolder> {
    CompactCalendarView compactCalendar;

    Context f183ct;
    public ArrayList<EventoN> lista_eventos = new ArrayList<>();
    public int mDefaultColor;
    private View root;

    public RecyclerAdapter_Eventos(ArrayList<EventoN> lista_eventos, View root) {
        this.f183ct = root.getContext();
        this.root = root;
        this.lista_eventos = lista_eventos;
        this.mDefaultColor = ContextCompat.getColor(f183ct, R.color.design_default_color_primary);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleradapter_eventos, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        EventoN evento = lista_eventos.get(position);
        holder.textView.setText(evento.getContenido());
        holder.colB.setBackgroundTintList(ColorStateList.valueOf(evento.getColor()));

        int ran = new Random().nextInt(4) + 1;
        if (ran == 1) {
            holder.imageView.setImageResource(R.mipmap.ic_ev1_foreground);
        } else if (ran == 2) {
            holder.imageView.setImageResource(R.mipmap.ic_ev2_foreground);
        } else if (ran == 3) {
            holder.imageView.setImageResource(R.mipmap.ic_ev3_foreground);
        } else if (ran == 4) {
            holder.imageView.setImageResource(R.mipmap.ic_ev4_foreground);
        }
    }

    public int getItemCount() {
        return lista_eventos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FloatingActionButton colB;
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.colB = (FloatingActionButton) itemView.findViewById(R.id.colB);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    return true;
                }
            });
            this.colB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
                    builder.setTitle((CharSequence) "Editar Color");
                    builder.setMessage((CharSequence) "¿Deseas cambiar el color?");
                    builder.setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            openColorPicker_editar(getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                    builder.create().show();
                }
            });
        }

        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
            builder.setTitle((CharSequence) "Opciones de evento");
            builder.setMessage((CharSequence) "¿Qué deseas hacer?");
            builder.setPositiveButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
            builder.setNegativeButton((CharSequence) "Editar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
                    builder.setTitle((CharSequence) "Edición de evento");
                    builder.setMessage((CharSequence) "¿Qué deseas editar?");
                    builder.setPositiveButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                    builder.setNegativeButton((CharSequence) "Título", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
                            builder.setTitle((CharSequence) "Editar Título");
                            builder.setMessage((CharSequence) "¿Deseas cambiar título?");
                            builder.setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    editar_nombre(getAdapterPosition());
                                }
                            });
                            builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                            builder.create().show();
                        }
                    });
                    builder.setNeutralButton((CharSequence) "Color", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
                            builder.setTitle((CharSequence) "Editar Color");
                            builder.setMessage((CharSequence) "¿Deseas cambiar el color?");
                            builder.setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultColor = lista_eventos.get(getAdapterPosition()).getColor();
                                    openColorPicker_editar(getAdapterPosition());}
                            });
                            builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                            builder.create().show();
                        }
                    });
                    builder.create().show();
                }
            });
            builder.setNeutralButton((CharSequence) "Borrar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
                    builder.setTitle((CharSequence) "Eliminar evento");
                    builder.setMessage((CharSequence) "¿Deseas eliminar este evento?");
                    builder.setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EventoN evento = lista_eventos.get(getAdapterPosition());
                            Utils.removeEvento(f183ct, evento);
                            lista_eventos.remove(getAdapterPosition());
                            lista_eventos = Utils.getEventosDia(f183ct, evento.getDia());
                            notifyItemRemoved(getAdapterPosition());
                            actualizar_simple();
                            actualizador_individual(evento);
                        }
                    });
                    builder.setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null);
                    builder.create().show();
                }
            });
            builder.create().show();
        }
    }

    public void editar_nombre(int poc) {
        EventoN evt = lista_eventos.get(poc);
        final String[] nomb = {null};
        View promptsVieww = LayoutInflater.from(this.f183ct).inflate(R.layout.slide_insertartitulo_evento, (ViewGroup) null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(f183ct, R.style.AlertD));
        alertDialogBuilder.setView(promptsVieww);
        EditText userInputt = (EditText) promptsVieww.findViewById(R.id.etAdd);
        userInputt.setText(evt.getContenido());
        alertDialogBuilder.setCancelable(false).setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nomb[0] = userInputt.getText().toString();
                if(nomb[0].equals("")){
                    Toast.makeText(f183ct, "Cuidado, no ingresaste texto", Toast.LENGTH_SHORT).show();
                    return;
                }
                evt.setContenido(nomb[0]);
                Utils.replaceEvento(f183ct, evt);
                lista_eventos = Utils.getEventosDia(f183ct, evt.getDia());
                notifyItemChanged(poc);
                actualizar_simple();
                Toast.makeText(f183ct, "Actualizado", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton((CharSequence) "Cancelar", null);
        alertDialogBuilder.create().show();
    }

    public void openColorPicker_editar(final int poc) {
        new AmbilWarnaDialog(this.f183ct, this.mDefaultColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            public void onOk(AmbilWarnaDialog dialog, int color) {
                actualizar_simple();
                EventoN evt = lista_eventos.get(poc);
                actualizador_individual(evt);
                evt.setColor(color);
                Utils.replaceEvento(f183ct, evt);
                lista_eventos = Utils.getEventosDia(f183ct, evt.getDia());
                actualizador_añade(lista_eventos.get(poc));
                notifyItemChanged(poc);
                Toast.makeText(f183ct, "Actualizado", Toast.LENGTH_SHORT).show();
            }
        }).show();
        notifyDataSetChanged();
    }

    public void actualizador_individual(EventoN evento) {
        this.compactCalendar.removeEvent(new Event(evento.getColor(), evento.getFecha(), ""));
        actualizar_simple();
    }

    public void actualizador_añade(EventoN evt){
        this.compactCalendar.addEvent(new Event(evt.getColor(), evt.getFecha(), ""));
        actualizar_simple();
    }

    public void actualizar_simple() {
        CompactCalendarView compactCalendarView = (CompactCalendarView) this.root.findViewById(R.id.compactcalendar_view);
        this.compactCalendar = compactCalendarView;
        compactCalendarView.setUseThreeLetterAbbreviation(true);
    }
}
