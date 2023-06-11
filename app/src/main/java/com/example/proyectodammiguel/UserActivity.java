package com.example.proyectodammiguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        taxisRef = FirebaseDatabase.getInstance().getReference("taxis");
        ListView listView = (ListView) findViewById(R.id.listViewTaxistas);


        taxisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                    Taxi taxi = dataSnapshot1.getValue(Taxi.class);
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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        double[] ubi = Utils.generateRandomLocationUserOrienteAsturias();
        LatLng location = new LatLng(ubi[0], ubi[1]);
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


}