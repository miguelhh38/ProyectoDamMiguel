package com.example.proyectodammiguel;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.proyectodammiguel.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class LoginActivity extends AppCompatActivity {

    ImageButton buttonBack;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    Button buttonLogin;
    EditText userText;
    EditText passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        userText = findViewById(R.id.userMailText);
        passText = findViewById(R.id.passTextLogin);

        buttonBack = findViewById(R.id.buttonBack3);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la accion de retroceso al menu principal cuando se hace clic en el botón.
             *
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            /**
             * Realiza la acción de inicio de sesión cuando se hace clic en el botón.
             * Obtiene el nombre de usuario o dirección de correo electrónico ingresado y
             * la contraseña ingresada. Luego, se llama a los métodos de inicio de sesión
             * correspondientes según el formato del nombre proporcionado.
             * @param v La vista del botón que se ha hecho clic.
             */
            @Override
            public void onClick(View v) {
                String name = userText.getText().toString();
                if (Validator.validarMail(name)) {
                    loginWithMail(name, passText.getText().toString());
                } else {
                    loginWithUserName(name, passText.getText().toString());
                }
            }
        });
    }

    /**
     * Inicia sesión con el correo electrónico y la contraseña proporcionados.
     *
     * @param name El nombre de usuario o dirección de correo electrónico.
     * @param pass La contraseña.
     */
    public void loginWithMail(String name, String pass) {
        mAuth.signInWithEmailAndPassword(name, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    //comprobar rol
                    Query query = usersRef.orderByChild("mail").equalTo(user.getEmail());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                                User user = dataSnapshot.getValue(User.class);
                                if (user.getTipo().equals(Tipo.USERGENERAL)) {
                                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                    startActivity(intent);
                                }
                                if (user.getTipo().equals(Tipo.TAXISTA)) {
                                    //case taxista
                                    Intent intent = new Intent(LoginActivity.this, TaxistaActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Inicia sesión con el nombre de usuario y la contraseña proporcionados.
     * Para ello realiza una consulta a la base de datos, para asi a traves del usuario obtener el correo
     * Posteriormente llama al metodo loginWithEmail()
     *
     * @param userName El nombre de usuario.
     * @param pass     La contraseña.
     */
    public void loginWithUserName(String userName, String pass) {
        if (userName.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe de completar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Query query = usersRef.orderByChild("user").equalTo(userName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        DataSnapshot dataSnapshot = snapshot.getChildren().iterator().next();
                        User user = dataSnapshot.getValue(User.class);
                        loginWithMail(user.getMail(), pass);
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}