package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import Modelo.Conductor;
import Modelo.Cuenta;
import Modelo.Pasajero;

public class crearCuenta2 extends AppCompatActivity {

    EditText textNombre,textApellido,textCorreo,textContrasenia;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta2);
        iniciarFirebase();
    }

    public void iniciarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void registrarCuenta(View v){
        textNombre = (EditText) findViewById(R.id.textNombre);
        textApellido = (EditText) findViewById(R.id.textApellido);
        textCorreo = (EditText) findViewById(R.id.textCorreo);
        textContrasenia = (EditText) findViewById(R.id.textContrasenia);

        String numero = null;
        Bundle paquete = getIntent().getExtras();
        if(paquete != null){
            numero = paquete.getString("numero");
        }

        String nombre = textNombre.getText().toString();
        String apellido = textApellido.getText().toString();
        String correo = textCorreo.getText().toString();
        String contrasenia = textContrasenia.getText().toString();

        RadioGroup rgUsuario = (RadioGroup) findViewById(R.id.rgUsuario);

        int idradio = rgUsuario.getCheckedRadioButtonId();
        String usuario = "";
        switch (idradio){
            case R.id.rbPasajero:
                usuario = "Pasajero";
                break;
            case R.id.rbConductor:
                usuario = "Conductor";
                break;
            default:
                Toast.makeText(this, "Error en la seccion Usuario", Toast.LENGTH_SHORT).show();
        }

        String idusuario = UUID.randomUUID().toString();
        String idcuenta = UUID.randomUUID().toString();

        Cuenta cuenta = new Cuenta();
        //Creacion cuenta
        cuenta.setId(idcuenta);
        cuenta.setCorreo(correo);
        cuenta.setContrasenia(contrasenia);

        if(usuario.equals("Pasajero")){

            //Creacion pasajero
            Pasajero p = new Pasajero();
            p.setId(idusuario);
            p.setNombre(nombre);
            p.setApellido(apellido);
            p.setNumero(numero);

            cuenta.setIdusuario(idusuario);

            databaseReference.child("Pasajero").child(idusuario).setValue(p);
            databaseReference.child("Cuenta").child(idcuenta).setValue(cuenta);

        }
        else if(usuario.equals("Conductor")){
            Conductor c = new Conductor();
            c.setId(idusuario);
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setNumero(numero);

            cuenta.setIdusuario(idusuario);

            databaseReference.child("Conductor").child(idusuario).setValue(c);
            databaseReference.child("Cuenta").child(idcuenta).setValue(cuenta);
        }
    }
}