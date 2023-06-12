package com.example.proyectodammiguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectodammiguel.clases.Agrupacion;
import com.example.proyectodammiguel.clases.Taxi;
import com.example.proyectodammiguel.utils.ListAdapter;
import com.example.proyectodammiguel.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    GoogleMap mMap;
    ImageButton buttonProfile;

    ListAdapter listAdapter;
    ArrayList<Taxi> taxisZona = new ArrayList<>();
    DatabaseReference taxisRef;
    Button buttonSolitar;

    private static LatLng ubiActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        taxisRef = FirebaseDatabase.getInstance().getReference("taxis");
        ListView listView = (ListView) findViewById(R.id.listViewTaxistas);


        taxisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Taxi> allTaxis = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Taxi taxi = dataSnapshot1.getValue(Taxi.class);
                    if (taxi != null) {
                        allTaxis.add(taxi);
                    }
                }

                Agrupacion agrupacionCercana = allTaxis.get(0).getTaxista().getAgrupacion();
                double distanciaMinima = calcularDistancia(ubiActual, new LatLng(allTaxis.get(0)
                        .getTaxista().getAgrupacion().getLatitud(), allTaxis.get(0).getTaxista()
                        .getAgrupacion().getLongitud()));
                for (Taxi taxi : allTaxis) {
                    LatLng latLng = new LatLng(taxi.getTaxista().getAgrupacion().getLatitud(),
                            taxi.getTaxista().getAgrupacion().getLongitud());
                    double distancia = calcularDistancia(ubiActual, latLng);

                    if (distancia < distanciaMinima) {
                        distanciaMinima = distancia;
                        agrupacionCercana = taxi.getTaxista().getAgrupacion();
                    }
                }
                taxisRef.orderByChild("taxista/agrupacion/name").equalTo(agrupacionCercana.getName()).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot2 : snapshot.getChildren()) {
                                    Taxi taxi = dataSnapshot2.getValue(Taxi.class);
                                    if (taxi != null) {
                                        taxisZona.add(taxi);
                                    }
                                }
                                UserActivity.this.listAdapter = new ListAdapter(UserActivity.this, UserActivity.this.taxisZona);
                                listView.setAdapter(listAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        buttonSolitar = findViewById(R.id.buttonSolictar);
        buttonSolitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserActivity.this, "Pulsa un taxista!", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Taxi taxi = (Taxi) parent.getItemAtPosition(position);
                Intent intent = new Intent(UserActivity.this, TaxistaSolicitud.class);
                intent.putExtra("taxi", taxi);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        double[] ubi = Utils.generateRandomLocationUserOrienteAsturias();
        LatLng location = new LatLng(ubi[0], ubi[1]);
        ubiActual = location;
        mMap.addMarker(new MarkerOptions().position(location).title("Yo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    private void moveCameraToLocation(LatLng location, float zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(zoomLevel)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static double calcularDistancia(LatLng ubicacion1, LatLng ubicacion2) {
        double radioTierra = 6371.0;  // Radio de la Tierra en kilómetros

        double lat1Rad = Math.toRadians(ubicacion1.latitude);
        double lng1Rad = Math.toRadians(ubicacion1.longitude);
        double lat2Rad = Math.toRadians(ubicacion2.latitude);
        double lng2Rad = Math.toRadians(ubicacion2.longitude);

        double dlat = lat2Rad - lat1Rad;
        double dlng = lng2Rad - lng1Rad;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dlng / 2) * Math.sin(dlng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distancia = radioTierra * c;
        return distancia;
    }


}