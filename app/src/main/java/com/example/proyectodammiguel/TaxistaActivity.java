package com.example.proyectodammiguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.example.proyectodammiguel.clases.Solicitud;
import com.example.proyectodammiguel.clases.Taxi;
import com.example.proyectodammiguel.utils.LiatAdapterSolicitudes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaxistaActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    ImageButton imageButton;
    LiatAdapterSolicitudes listAdapter;

    ListView listaSolicitudes;
    ArrayList<Solicitud> solicitudes = new ArrayList<>();

    FirebaseAuth mUser;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxista);
        imageButton = findViewById(R.id.buttonProfile22);
        listaSolicitudes = findViewById(R.id.listaSolitudes);
        mUser = FirebaseAuth.getInstance();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("solicitudes");
        databaseReference2.orderByChild("taxista/user/mail").equalTo(mUser.getCurrentUser().getEmail()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Solicitud solicitud = snapshot1.getValue(Solicitud.class);
                                solicitud.setId(snapshot1.getKey());
                                solicitudes.add(solicitud);
                            }
                            TaxistaActivity.this.listAdapter = new LiatAdapterSolicitudes(TaxistaActivity.this, TaxistaActivity.this.solicitudes);
                            listaSolicitudes.setAdapter(listAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


        listaSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Solicitud solicitud = (Solicitud) parent.getItemAtPosition(position);
                Intent intent = new Intent(TaxistaActivity.this, DatosSolicitudActivity.class);
                intent.putExtra("solicitud", solicitud);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaxistaActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        textView1 = findViewById(R.id.textView31);
        textView2 = findViewById(R.id.textView32);

        databaseReference = FirebaseDatabase.getInstance().getReference("taxis");

        databaseReference.orderByChild("taxista/user/mail").equalTo(mUser.getCurrentUser().getEmail()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Taxi taxi = dataSnapshot.getValue(Taxi.class);

                                taxi.getTaxista().setCalificacion(2);
                                databaseReference.child(dataSnapshot.getKey()).setValue(taxi);

                                if (taxi != null) {
                                    textView1.setText("Tu valoracion es: " + taxi.getTaxista().getCalificacion() + "/5" );
                                    if (taxi.getTaxista().getCalificacion()<3) {
                                        textView2.setText("¡Debes de mejorar!");
                                    } else if (taxi.getTaxista().getCalificacion() == 3) {
                                        textView2.setText("¡Buen trabajo!");
                                    } else if (taxi.getTaxista().getCalificacion()>3) {
                                        textView2.setText("¡Excelente!");
                                    } else {
                                        textView1.setText("");
                                        textView2.setText("Debes de realizar servicios");
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }
}