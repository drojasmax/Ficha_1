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

import Modelo.Cuenta;

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
        databaseReference.child("Cuenta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Imprimiendo ...");
                for (DataSnapshot objeto: snapshot.getChildren()) {
                    Cuenta cuenta = objeto.getValue(Cuenta.class);
                    System.out.println(cuenta);
                    if(correo.equals(cuenta.getCorreo()) && contrasenia.equals(cuenta.getContrasenia())){
                        Intent i = new Intent(getApplicationContext(),destino.class);
                        Toast.makeText(Login.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
                        i.putExtra("idusuario",cuenta.getIdusuario());
                        i.putExtra("correo",cuenta.getCorreo());
                        startActivity(i);}
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