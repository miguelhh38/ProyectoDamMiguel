package com.example.proyectodammiguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyectodammiguel.clases.Solicitud;
import com.example.proyectodammiguel.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DatosSolicitudActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    GoogleMap mMap;
    TextView textView;
    TextView textView1;
    TextView textView2;
    private static Solicitud solicitudActiva;
    Button buttonLlamar;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_solicitud);
        textView = findViewById(R.id.textView34);
        textView1 = findViewById(R.id.textView36);
        textView2 = findViewById(R.id.textView38);
        buttonBack = findViewById(R.id.buttonBack50);
        buttonLlamar = findViewById(R.id.button6);

        Intent intent = getIntent();
        solicitudActiva = (Solicitud) intent.getSerializableExtra("solicitud");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa2);
        mapFragment.getMapAsync(this);

        textView.setText(solicitudActiva.getCliente().getName());
        textView1.setText(solicitudActiva.getCliente().getTelf());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView2.setText(DateTimeFormatter.ofPattern("HH:mm").format(solicitudActiva.getFecha()));
        }
        buttonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + solicitudActiva.getCliente().getTelf()));
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatosSolicitudActivity.this, TaxistaActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(solicitudActiva.getLatitud(), solicitudActiva.getLongitud());
        mMap.addMarker(new MarkerOptions().position(location).title("Cliente"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
    }
}