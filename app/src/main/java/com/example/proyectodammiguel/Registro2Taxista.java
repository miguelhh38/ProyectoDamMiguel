package com.example.proyectodammiguel;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectodammiguel.clases.Agrupacion;
import com.example.proyectodammiguel.clases.Taxi;
import com.example.proyectodammiguel.clases.Taxista;
import com.example.proyectodammiguel.clases.Tipo;
import com.example.proyectodammiguel.clases.User;
import com.example.proyectodammiguel.utils.Utils;
import com.example.proyectodammiguel.utils.Validator;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Registro2Taxista extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient googleApiClient;
    String siteKey = "6Lc4GHAmAAAAAEDCJbIegRpcbJ95doHFKsRMz41m";

    ImageButton buttonBack;
    Button buttonCompletar;
    CheckBox buttoncaptha;
    CheckBox checkBoxTerminos;
    EditText telfText;

    Spinner spinnerMarca;
    Spinner spinnerModelo;
    Spinner spinnerPlazas;
    Spinner spinnerCombustible;
    Spinner spinnerAgrupacion;

    DatabaseReference usersRef;
    DatabaseReference taxiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2_taxista);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        taxiRef = FirebaseDatabase.getInstance().getReference("taxis");
        telfText = findViewById(R.id.telfText2);
        checkBoxTerminos = findViewById(R.id.checkBox2);
        buttonCompletar = findViewById(R.id.buttonCompletar2);
        spinnerMarca = findViewById(R.id.spinner);
        spinnerModelo = findViewById(R.id.spinner2);
        spinnerPlazas = findViewById(R.id.spinner3);
        spinnerCombustible = findViewById(R.id.spinner4);
        spinnerAgrupacion = findViewById(R.id.spinner5);

        loadSpinner1();
        loadSpinner2();
        loadSpinner3();
        loadSpinner4();
        loadSpinner5();


        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Establece un listener de selección de elemento en el spinnerMarca.
             * Cuando se selecciona un elemento en el spinnerMarca, se cargan los modelos correspondientes en el spinnerModelo.
             *
             * @param parent   El AdapterView del spinnerMarca.
             * @param view     La vista del elemento seleccionado.
             * @param position La posición del elemento seleccionado en el spinnerMarca.
             * @param id       El ID del elemento seleccionado en el spinnerMarca.
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                String selectedModels[] = {"Seleccione una marca"};

                if (selected.equals("Toyota")) {
                    selectedModels = getResources().getStringArray(R.array.car_models_toyota);
                }
                if (selected.equals("Opel")) {
                    selectedModels = getResources().getStringArray(R.array.car_models_opel);
                }
                if (selected.equals("Honda")) {
                    selectedModels = getResources().getStringArray(R.array.car_models_honda);
                }
                if (selected.equals("Audi")) {
                    selectedModels = getResources().getStringArray(R.array.car_models_audi);
                }
                if (selected.equals("Citroen")) {
                    selectedModels = getResources().getStringArray(R.array.car_models_citroen);
                }
                if (selected.equals("Peugeot")) {
                    selectedModels = getResources().getStringArray(R.array.car_models_peugeot);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Registro2Taxista.this, android.R.layout.simple_spinner_item, selectedModels);
                spinnerModelo.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonBack = findViewById(R.id.buttonBack2);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la accion de retroceso al menu principal cuando se hace clic en el botón.
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro2Taxista.this, RegistroTaxista.class);
                startActivity(intent);

            }
        });


        buttoncaptha = findViewById(R.id.checkBox);
        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API)
                .addConnectionCallbacks(Registro2Taxista.this)
                .build();
        googleApiClient.connect();


        buttoncaptha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttoncaptha.isChecked()) {
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, siteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && status.isSuccess()) {
                                        Toast.makeText(Registro2Taxista.this, "Verificación correcta", Toast.LENGTH_SHORT).show();
                                        buttoncaptha.setTextColor(Color.BLUE);
                                    } else {
                                        //Error
                                        Toast.makeText(Registro2Taxista.this, "Verificación correcta", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    buttoncaptha.setTextColor(Color.RED);
                }

            }

        });

        buttonCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskCompletionSource<Boolean> telfExistsSource = new TaskCompletionSource<>();
                final String[] result = {Validator.validarTelfString(telfText)};

                usersRef.orderByChild("telf").equalTo(telfText.getText().toString()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean telfExists = snapshot.exists();
                                telfExistsSource.setResult(telfExists);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                telfExistsSource.setException(error.toException());
                            }
                        }
                );

                Task<Boolean> telfExistsTask = telfExistsSource.getTask();
                Tasks.whenAll(telfExistsTask).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (telfExistsTask.getResult()) {
                                    result[0] += " - El número introducido ya esta registrado";
                                }
                                if (result[0].isEmpty()) {
                                    if (spinnerMarca.isSelected()) {
                                        result[0] += " - Seleccione marca";
                                    }
                                    if (spinnerModelo.isSelected()) {
                                        result[0] += " - Seleccione marca";
                                    }
                                    if (spinnerPlazas.isSelected()) {
                                        result[0] += " - Seleccione plazas";
                                    }
                                    if (spinnerCombustible.isSelected()) {
                                        result[0] += " - Seleccione combustible";
                                    }
                                    if (spinnerAgrupacion.isSelected()) {
                                        result[0] += " - Seleccione agrupacion";
                                    }
                                    if (!checkBoxTerminos.isChecked()) {
                                        result[0] += " - Acepte términos";
                                    }
                                    if (!buttoncaptha.isChecked()) {
                                        result[0] += " - Realice verificación";
                                    }

                                    if (result[0].isEmpty()) {
                                        Intent intent = getIntent();
                                        User user = (User) intent.getSerializableExtra("user");
                                        String pass = intent.getStringExtra("pass");
                                        user.setTelf(telfText.getText().toString());
                                        user.setTipo(Tipo.TAXISTA);

                                        Taxista taxista = new Taxista(user, (Agrupacion)
                                                spinnerAgrupacion.getSelectedItem());

                                        Taxi taxi = new Taxi(spinnerMarca.getSelectedItem().toString(),
                                                spinnerModelo.getSelectedItem().toString(),
                                                Integer.parseInt(spinnerPlazas.getSelectedItem().toString()),
                                                taxista, spinnerCombustible.getSelectedItem().toString()
                                                );
                                        registro(user, pass, taxi);

                                        Toast.makeText(Registro2Taxista.this, "Registro correcto", Toast.LENGTH_SHORT).show();
                                        Intent intent1 = new Intent(Registro2Taxista.this, TaxistaActivity.class);
                                        startActivity(intent1);
                                    } else {
                                        Utils.mostrarAlertDialog(Registro2Taxista.this, result[0]);
                                    }
                                }

                                else {
                                    Utils.mostrarAlertDialog(Registro2Taxista.this, result[0]);
                                }
                            }
                        }
                );
            }
        });
    }

    @Override
    public void onConnected(@androidx.annotation.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Carga el spinner "spinnerMarca" con los elemetos proporcionados
     */
    private void loadSpinner1() {
        String[] marcas = getResources().getStringArray(R.array.car_brands);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marcas);
        spinnerMarca.setAdapter(adapter);
    }

    /**
     * Carga el spinner "spinnerModelo" con los elemetos proporcionados
     */
    private void loadSpinner2() {
        String[] init = {"Seleccione marca"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, init);
        spinnerModelo.setAdapter(adapter);

    }

    /**
     * Carga el spinner "spinnerPlazas" con los elementos proporcionados.
     */
    private void loadSpinner3() {
        List<Integer> elementos = Arrays.asList(4, 5, 7);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elementos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlazas.setAdapter(adapter);
    }

    /**
     * Carga el spinner "spinnerCombustible" con los elementos proporcionados.
     */
    private void loadSpinner4() {
        List<String> elementos = Arrays.asList("Diesel", "Gasolina", "Eléctrico", "Híbrido", "Híbrido enchufable",
                "Gas natural", "Gas licuado", "Otros");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elementos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCombustible.setAdapter(adapter);
    }

    private void loadSpinner5() {
        DatabaseReference agrupacionRef = FirebaseDatabase.getInstance().getReference("agrupaciones");
        List<Agrupacion> allAgrup = new ArrayList<>();
        agrupacionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Agrupacion agrupacion = snapshot1.getValue(Agrupacion.class);
                    if (agrupacion != null) {
                        allAgrup.add(agrupacion);
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter<>(Registro2Taxista.this, android.R.layout.simple_spinner_item, allAgrup);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerAgrupacion.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Registra un nuevo usuario con la información proporcionada (Uso Firebase)
     *
     * @param user El objeto User que contiene los detalles del usuario a registrar.
     * @param pass La contraseña para el nuevo usuario.
     */
    public void registro(User user, String pass, Taxi taxi) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getMail(), pass).addOnCompleteListener(Registro2Taxista.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Registro correcto!");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "Error de registro", task.getException());
                        }
                    }
                });
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.push().setValue(user);

        taxiRef.push().setValue(taxi);

    }


}