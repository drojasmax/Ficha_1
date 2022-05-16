package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

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

    public void registrarPasajero(View v){
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

        String id = UUID.randomUUID().toString();


        if(usuario.equals("Pasajero")){
          Pasajero p = new Pasajero();
          p.setId(id);
          p.setNombre(nombre);
          p.setApellido(apellido);
          p.setCorreo(correo);
          p.setContrasenia(contrasenia);
          p.setNumero(numero);

          databaseReference.child("Pasajero").child(id).setValue(p);
        }
        else if(usuario.equals("Conductor")){
            Conductor c = new Conductor();
            c.setId(id);
            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setCorreo(correo);
            c.setContrasenia(contrasenia);
            c.setNumero(numero);

            databaseReference.child("Conductor").child(id).setValue(c);
        }
    }
}