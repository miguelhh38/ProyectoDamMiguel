package com.example.proyectodammiguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.proyectodammiguel.clases.User;
import com.example.proyectodammiguel.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistroTaxista extends AppCompatActivity {

    ImageButton retrocesoButton;
    Button registroUserButton;
    Button continuarButton;

    EditText nameText;
    EditText userText;
    EditText mailText;
    EditText passText;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_taxista);

        nameText = findViewById(R.id.nameText);
        userText = findViewById(R.id.userText);
        mailText = findViewById(R.id.mailText);
        passText = findViewById(R.id.passText);


        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        retrocesoButton = findViewById(R.id.imageButtonBack);
        retrocesoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroTaxista.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        registroUserButton = findViewById(R.id.buttonCambioRegistro);
        registroUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroTaxista.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        continuarButton = findViewById(R.id.buttonContinue);
        continuarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] result = {RegistroActivity.validarCamposString(nameText, userText, mailText, passText)};
                TaskCompletionSource<Boolean> userExistsSource = new TaskCompletionSource<>();
                TaskCompletionSource<Boolean> emailExistsSource = new TaskCompletionSource<>();

                usersRef.orderByChild("user").equalTo(userText.getText().toString()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean userExists = snapshot.exists();
                                userExistsSource.setResult(userExists);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                userExistsSource.setException(error.toException());
                            }
                        }
                );

                usersRef.orderByChild("mail").equalTo(mailText.getText().toString()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean emailExists = snapshot.exists();
                                emailExistsSource.setResult(emailExists);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                emailExistsSource.setException(error.toException());
                            }
                        }
                );

                Task<Boolean> userExistsTask = userExistsSource.getTask();
                Task<Boolean> emailExistsTask = emailExistsSource.getTask();

                Tasks.whenAll(userExistsTask, emailExistsTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean userExists = userExistsTask.getResult();
                        boolean emailExists = emailExistsTask.getResult();

                        if (userExists) {
                            result[0] += " - El usuario introducido ya está registrado\n";
                        }
                        if (emailExists) {
                            result[0] += " - El email introducido ya está registrado\n";
                        }

                        if (result[0].isEmpty()) {
                            User user = new User(nameText.getText().toString(), userText.getText().toString(), mailText.getText().toString());
                            Intent intent = new Intent(RegistroTaxista.this, Registro2Taxista.class);
                            intent.putExtra("user", user);
                            intent.putExtra("pass", passText.getText().toString());
                            startActivity(intent);
                        } else {
                            Utils.mostrarAlertDialog(RegistroTaxista.this, result[0]);
                        }
                    }
                });
            }
        });
    }
}