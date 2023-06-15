package com.example.proyectodammiguel.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectodammiguel.R;
import com.example.proyectodammiguel.clases.Solicitud;
import com.example.proyectodammiguel.clases.Taxi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LiatAdapterSolicitudes extends BaseAdapter {
    public Activity activity;
    public ArrayList<Solicitud> solicitudes;
    Solicitud solicitud;

    public LiatAdapterSolicitudes(Activity activity, ArrayList<Solicitud> solicitudes) {
        this.activity = activity;
        this.solicitudes = solicitudes;
    }

    @Override
    public int getCount() {
        return solicitudes.size();
    }

    @Override
    public Object getItem(int position) {
        return solicitudes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.simple_row_solicitud, null);
        }
        Solicitud solicitud = solicitudes.get(position);
        TextView id = (TextView) v.findViewById(R.id.textView40);
        TextView cliente = (TextView) v.findViewById(R.id.textView41);
        TextView hora = (TextView) v.findViewById(R.id.textView42);
        id.setText("Solicitud " + solicitud.getId());
        cliente.setText("Cliente: " + solicitud.getCliente().getName());

        LocalDateTime localDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.parse(solicitud.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        }
        String formattedTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        hora.setText("Hora: " + formattedTime);
        ImageView imageView = v.findViewById(R.id.imageView40);
        imageView.setImageResource(R.drawable.usuario);
        return v;
    }
}
