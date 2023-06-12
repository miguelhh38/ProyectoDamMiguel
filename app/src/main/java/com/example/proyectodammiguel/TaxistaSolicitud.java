package com.example.proyectodammiguel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectodammiguel.clases.Taxi;

public class TaxistaSolicitud extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    Button buttonLlamar;
    ImageButton buttonBack;
    private static Taxi taxiAcual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxista_solicitud);

        Intent intent = getIntent();
        taxiAcual = (Taxi) intent.getSerializableExtra("taxi");

        textView1 = findViewById(R.id.textView52);
        textView2 = findViewById(R.id.textView53);
        textView3 = findViewById(R.id.textView54);
        textView4 = findViewById(R.id.textView55);
        textView5 = findViewById(R.id.textView56);
        textView6 = findViewById(R.id.textView57);
        textView7 = findViewById(R.id.textView58);
        buttonLlamar = findViewById(R.id.button8);
        buttonBack = findViewById(R.id.buttonBackSolitud);

        textView1.setText(taxiAcual.getTaxista().getAgrupacion().getName());
        textView2.setText(taxiAcual.getTaxista().getUser().getName());
        textView3.setText(taxiAcual.getTaxista().getUser().getTelf());
        textView4.setText(String.valueOf(taxiAcual.getTaxista().getCalificacion()));
        textView5.setText(taxiAcual.getMarca());
        textView6.setText(taxiAcual.getModelo());
        textView7.setText(String.valueOf(taxiAcual.getNumPlazas()));

        buttonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + taxiAcual.getTaxista().getUser().getTelf()));
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaxistaSolicitud.this, UserActivity.class);
                startActivity(intent);
            }
        });


    }
}