package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Modelo.Conductor;
import Modelo.Cuenta;
import Modelo.Pasajero;

public class Perfil extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        iniciarFirebase();
        recuperarUsuario();
    }

    public void iniciarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void recuperarUsuario(){
        TextView etNombreperfil,etApellidoperfil,etCorreoperfil,etNumeroperfil;

        etNombreperfil = (TextView) findViewById(R.id.etNombreperfil);
        etApellidoperfil = (TextView) findViewById(R.id.etApellidoperfil);
        etCorreoperfil = (TextView) findViewById(R.id.etCorreoperfil);
        etNumeroperfil = (TextView) findViewById(R.id.etNumeroperfil);

        Bundle paquete = getIntent().getExtras();
        String idusuario = null;
        String correo = null;
        if(paquete != null){
            idusuario = paquete.getString("idusuario");
            correo = paquete.getString("correo");
        }

        String finalIdusuario = idusuario;
        String finalCorreo = correo;
        databaseReference.child("Pasajero").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objeto: snapshot.getChildren()) {
                    Pasajero p = objeto.getValue(Pasajero.class);
                    if(p.getId().equals(finalIdusuario)){
                        etNombreperfil.setText(p.getNombre());
                        etApellidoperfil.setText(p.getApellido());
                        etCorreoperfil.setText(finalCorreo);
                        etNumeroperfil.setText(p.getNumero());
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
                for (DataSnapshot objeto: snapshot.getChildren()) {
                    Conductor c = objeto.getValue(Conductor.class);
                    if(c.getId().equals(finalIdusuario)){
                        etNombreperfil.setText(c.getNombre());
                        etApellidoperfil.setText(c.getApellido());
                        etCorreoperfil.setText(finalCorreo);
                        etNumeroperfil.setText(c.getNumero());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}