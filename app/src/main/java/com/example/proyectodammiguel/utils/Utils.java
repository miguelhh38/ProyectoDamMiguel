package com.example.proyectodammiguel.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Address;

import androidx.appcompat.app.AlertDialog;

import com.example.proyectodammiguel.clases.Agrupacion;
import com.google.android.gms.safetynet.SafetyNetClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    /**
     * Muestra un diálogo de alerta con un título y un mensaje personalizados.
     *
     * @param activity La actividad actual desde la cual se muestra el diálogo.
     * @param result   El mensaje a mostrar en el cuerpo del diálogo.
     */
    public static void mostrarAlertDialog(Activity activity, String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("ERROR DE REGISTRO");
        builder.setMessage(result);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción a realizar al hacer clic en el botón "Aceptar"
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * Genera coordenadas aleatorias dentro de la zona del Oriente de Asturias.
     * La zona está delimitada por los siguientes límites:
     * Latitud máxima: 43.395677
     * Latitud mínima: 43.219732
     * Longitud máxima: -4.591688
     * Longitud mínima: -5.413075
     *
     * @return Un arreglo de double que contiene las coordenadas generadas [latitud, longitud].
     */
    public static double[] generateRandomLocationUserOrienteAsturias() {
        //coordenadas oriente asturias
        double latitudMax = 43.395677;
        double latitudMin = 43.219732;

        double longitudMax = -4.591688;
        double longitudMin = -5.413075;

        Random random = new Random();

        double latitud = latitudMin + (latitudMax - latitudMin) * random.nextDouble();
        double longitud = longitudMin + (longitudMax - longitudMin) * random.nextDouble();

        return new double[]{latitud, longitud};
    }

    /**
     * Método para guardar las agrupaciones en Firebase.
     */
    public static void guardarAgrupaciones() {
        List<Agrupacion> agrupacions = new ArrayList<>();
        Agrupacion agrupacion1 = new Agrupacion("Cangas de Onís", 43.350935, -5.128437);
        Agrupacion agrupacion2 = new Agrupacion("Arenas de Cabrales", 43.302651, -4.816313);
        Agrupacion agrupacion3 = new Agrupacion("Llanes", 43.420843, -4.755059);
        Agrupacion agrupacion4 = new Agrupacion("Ribadesella", 43.462413, -5.059635);
        Agrupacion agrupacion5 = new Agrupacion("Arriondas", 43.390194, -5.186007);
        Agrupacion agrupacion6 = new Agrupacion("San Juan de Beleño", 43.190972, -5.166563);
        Agrupacion agrupacion7 = new Agrupacion("Infiesto", 43.347704, -5.363567);
        Agrupacion agrupacion8 = new Agrupacion("Caravia", 43.462806, -5.186934);
        Agrupacion agrupacion9 = new Agrupacion("Panes", 43.325051, -4.583636);

        agrupacions.add(agrupacion1);
        agrupacions.add(agrupacion2);
        agrupacions.add(agrupacion3);
        agrupacions.add(agrupacion4);
        agrupacions.add(agrupacion5);
        agrupacions.add(agrupacion6);
        agrupacions.add(agrupacion7);
        agrupacions.add(agrupacion8);
        agrupacions.add(agrupacion9);
        DatabaseReference agrupacionesRef = FirebaseDatabase.getInstance().getReference("agrupaciones");
        for (Agrupacion agrupacion : agrupacions)
            agrupacionesRef.push().setValue(agrupacion);
    }
}
