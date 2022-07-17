package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText etCorreo, etContra;
    Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
    public void login(View v){
        String correo = etCorreo.getText().toString().trim();
        String contra = etContra.getText().toString().trim();


    }

    public void crearCuenta(View v){
        Intent i = new Intent(this, redireccionar_pasajero_conductor.class);
        startActivity(i);}
}