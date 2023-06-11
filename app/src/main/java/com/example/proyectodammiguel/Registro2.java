package com.example.proyectodammiguel;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.proyectodammiguel.clases.Tipo;
import com.example.proyectodammiguel.clases.User;
import com.example.proyectodammiguel.utils.Utils;
import com.example.proyectodammiguel.utils.Validator;
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

import java.io.InputStreamReader;
import java.util.List;

public class Registro2 extends AppCompatActivity {
    ImageButton buttonBack;
    Button buttonCompletar;
    EditText telfText;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        telfText = findViewById(R.id.telfText);

        buttonBack = findViewById(R.id.buttonBack2);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        buttonBack.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la accion de retroceso al menu principal cuando se hace clic en el botón.
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro2.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        buttonCompletar = findViewById(R.id.buttonCompletar);
        buttonCompletar.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la acción cuando se hace clic en el botón "Completar".
             * Valida el número de teléfono introducido en el campo de texto y realiza acciones adicionales
             * en función de si el número de teléfono ya está registrado o no.
             * @param v La vista del botón que se ha hecho clic.
             */
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
                Tasks.whenAll(telfExistsTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (telfExistsTask.getResult()) {
                            result[0] += " - El número introducido ya esta registrado\n";
                        }

                        if (result[0].isEmpty()) {
                            Intent intent = getIntent();
                            User user = (User) intent.getSerializableExtra("user");
                            String pass = intent.getStringExtra("pass");
                            user.setTelf(telfText.getText().toString());
                            user.setTipo(Tipo.USERGENERAL);
                            registro(user, pass);

                            Intent intent2 = new Intent(Registro2.this, UserActivity.class);
                            startActivity(intent2);
                        } else {
                            Utils.mostrarAlertDialog(Registro2.this, result[0]);
                        }

                    }
                });

            }
        });
    }

    /**
     * Registra un nuevo usuario con la información proporcionada (Uso Firebase)
     *
     * @param user El objeto User que contiene los detalles del usuario a registrar.
     * @param pass La contraseña para el nuevo usuario.
     */
    public void registro(User user, String pass) {
        mAuth.createUserWithEmailAndPassword(user.getMail(), pass).addOnCompleteListener(Registro2.this,
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
    }

}