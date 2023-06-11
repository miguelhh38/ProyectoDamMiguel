package com.example.proyectodammiguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyectodammiguel.clases.Tipo;
import com.example.proyectodammiguel.clases.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView name;
    TextView mailT;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    AppCompatButton appCompatButton;
    private static User userStatic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = findViewById(R.id.textViewName);
        appCompatButton = findViewById(R.id.appCompactButtom);
        mailT = findViewById(R.id.textViewEmail);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        String mail = mAuth.getCurrentUser().getEmail();

        databaseReference.orderByChild("mail").equalTo(mail).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // El usuario con el correo electrónico especificado existe en la base de datos
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                // Obtén el usuario correspondiente
                                User user = dataSnapshot.getValue(User.class);
                                userStatic = user;
                                if (user != null) {
                                    name.setText(user.getName());
                                    mailT.setText(user.getMail());
                                }
                            }
                        }
                    }
                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }
                });

        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatic.getTipo().equals(Tipo.USERGENERAL)) {
                    Intent intent = new Intent(ProfileActivity.this, UserActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProfileActivity.this, TaxistaActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

}