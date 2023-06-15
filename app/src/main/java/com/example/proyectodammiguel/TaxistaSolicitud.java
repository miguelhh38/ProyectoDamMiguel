package com.example.proyectodammiguel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectodammiguel.clases.Solicitud;
import com.example.proyectodammiguel.clases.Taxi;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaxistaSolicitud extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    Button buttonLlamar;
    Button buttonSolicitar;
    ImageButton buttonBack;
    DatabaseReference mref;

    private static Taxi taxiAcual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxista_solicitud);

        Intent intent = getIntent();
        taxiAcual = (Taxi) intent.getSerializableExtra("taxi");
        Double latitud = (Double) intent.getDoubleExtra("latitud", 0.0);
        Double longitud = (Double) intent.getDoubleExtra("longitud", 0.0);

        mref = FirebaseDatabase.getInstance().getReference("solicitudes");

        textView1 = findViewById(R.id.textView52);
        textView2 = findViewById(R.id.textView53);
        textView3 = findViewById(R.id.textView54);
        textView4 = findViewById(R.id.textView55);
        textView5 = findViewById(R.id.textView56);
        textView6 = findViewById(R.id.textView57);
        textView7 = findViewById(R.id.textView58);
        buttonLlamar = findViewById(R.id.button8);
        buttonBack = findViewById(R.id.buttonBackSolitud);
        buttonSolicitar = findViewById(R.id.button7);

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

        buttonSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Solicitud solicitud = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    solicitud = new Solicitud(taxiAcual.getTaxista(), LocalDateTime.now().toString(),
                            taxiAcual.getTaxista().getUser(), latitud, longitud);
                }
                mref.push().setValue(solicitud);
                Toast.makeText(TaxistaSolicitud.this, "Solcitud correcta!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}