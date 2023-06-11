package com.example.proyectodammiguel.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.proyectodammiguel.R;
import com.example.proyectodammiguel.clases.Taxi;


import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    public Activity activity;
    public ArrayList<Taxi> taxis;
    Taxi taxi;

    public ListAdapter(Activity activity, ArrayList<Taxi> taxis) {
        this.activity = activity;
        this.taxis = taxis;
    }

    @Override
    public int getCount() {
        return taxis.size();
    }

    @Override
    public Object getItem(int position) {
        return taxis.get(position);
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
            v = inflater.inflate(R.layout.simple_row, null);
        }
            Taxi taxi = taxis.get(position);
            TextView nom = (TextView) v.findViewById(R.id.textView23);
            TextView conductor = (TextView) v.findViewById(R.id.textView29);
            TextView plazas = (TextView) v.findViewById(R.id.textView28);
            nom.setText("Taxi " + position);
            conductor.setText("Conductor: " + taxi.getTaxista().getUser().getName());
            plazas.setText("Plazas: " + taxi.getNumPlazas());

            ImageView imageView = v.findViewById(R.id.imageView4);
            imageView.setImageResource(R.drawable.ic_baseline_local_taxi_24);

            return v;
    }
}
