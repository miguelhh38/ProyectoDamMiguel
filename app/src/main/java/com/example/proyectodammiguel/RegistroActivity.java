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
import com.example.proyectodammiguel.utils.Validator;
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

public class RegistroActivity extends AppCompatActivity {
    ImageButton buttonBack;
    Button buttonContinue;
    Button buttonCambioRegistro;

    EditText nameText;
    EditText userText;
    EditText passText;
    EditText mailText;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nameText = findViewById(R.id.nameText);
        userText = findViewById(R.id.userText);
        mailText = findViewById(R.id.mailText);
        passText = findViewById(R.id.passText);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        buttonCambioRegistro = findViewById(R.id.buttonCambioRegistro);
        buttonCambioRegistro.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la acción cuando se hace clic en el botón "Cambio Registro".
             * Inicia la actividad RegistroTaxista.
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, RegistroTaxista.class);
                startActivity(intent);
            }
        });

        buttonBack = findViewById(R.id.imageButtonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la accion de retroceso al menu principal cuando se hace clic en el botón.
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la acción cuando se hace clic en el botón "Continuar".
             * Valida los campos de texto introducidos y verifica la existencia de usuarios y correos electrónicos en la base de datos.
             * Si los campos son válidos y no existen usuarios o correos electrónicos duplicados, inicia la actividad Registro2.
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                final String[] result = {validarCamposString(nameText, userText, mailText, passText)};
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
                            Intent intent = new Intent(RegistroActivity.this, Registro2.class);
                            intent.putExtra("user", user);
                            intent.putExtra("pass", passText.getText().toString());
                            startActivity(intent);
                        } else {
                            Utils.mostrarAlertDialog(RegistroActivity.this, result[0]);
                        }
                    }
                });
            }
        });
    }

    /**
     * Valida los campos de texto introducidos y devuelve un mensaje de error si alguno de los campos no cumple con los requisitos.
     *
     * @param nameText El campo de texto del nombre.
     * @param userText El campo de texto del usuario.
     * @param mailText El campo de texto del correo electrónico.
     * @param passText El campo de texto de la contraseña.
     * @return Un mensaje de error si algún campo no cumple con los requisitos, o una cadena vacía si todos los campos son válidos.
     */
    public static String validarCamposString(EditText nameText, EditText userText, EditText
            mailText, EditText passText) {
        String result = "";
        if (!Validator.validarName(nameText.getText().toString())) {
            result += " - Nombre introducido no cumple con los requisitos\n";
        }
        if (!Validator.validarMail(mailText.getText().toString())) {
            result += " - El email no cumple con los requistos\n";
        }
        if (!Validator.validarUser(userText.getText().toString())) {
            result += " - El usuario no cumple con los requistos\n";
        }
        if (!Validator.validarPass(passText.getText().toString())) {
            result += " - La contraseña debe de contener mayúsculas, minúsculas y dígitos\n";
        }
        return result;
    }

}
