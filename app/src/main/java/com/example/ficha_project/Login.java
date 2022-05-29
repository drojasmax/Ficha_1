package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Modelo.Conductor;
import Modelo.Pasajero;

public class Login extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarFirebase();
    }

    public void iniciarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void login(View v){
        System.out.println("Entre al login");
        EditText campo1 = this.findViewById(R.id.loginCorreo);
        String correo = campo1.getText().toString();
        EditText campo2 = this.findViewById(R.id.loginContrasenia);
        String contrasenia = campo2.getText().toString();

        databaseReference.child("Pasajero").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Imprimiendo ...");
                for (DataSnapshot objeto: snapshot.getChildren()) {
                    Pasajero p = objeto.getValue(Pasajero.class);
                    System.out.println(p);
                    if(correo.equals(p.getCorreo()) && contrasenia.equals(p.getContrasenia())){

                        Intent i = new Intent(getApplicationContext(),prueba.class);
                        Toast.makeText(Login.this, "Logeado como: "+p, Toast.LENGTH_SHORT).show();
                        //i.putExtra("idcliente",c.getId());
                        startActivity(i);}
                    else{
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Conductor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Imprimiendo ...");
                for (DataSnapshot objeto: snapshot.getChildren()) {
                    Conductor c = objeto.getValue(Conductor.class);
                    System.out.println(c);
                    if(correo.equals(c.getCorreo()) && contrasenia.equals(c.getContrasenia())){

                        Intent i = new Intent(getApplicationContext(),prueba.class);
                        Toast.makeText(Login.this, "Logeado como: "+c, Toast.LENGTH_SHORT).show();
                        //i.putExtra("idcliente",c.getId());
                        startActivity(i);}
                    else{
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }
    public void crearCuenta(View v){

        Intent i = new Intent(this,crearCuenta1.class);
        startActivity(i);}

    }
