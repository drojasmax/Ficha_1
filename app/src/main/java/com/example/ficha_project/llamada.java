package com.example.ficha_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class llamada extends AppCompatActivity {

    public Button cbtn;
    public EditText nmbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);
        cbtn = (Button)findViewById(R.id.call_btn);
        nmbr = (EditText)findViewById(R.id.phone_number);
        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = nmbr.getText().toString(); // Guardamos el numero de telefono en un string
                if(num!="") { // Verificamos si el numero de telefono no esta vacio
                    Uri number = Uri.parse("tel:" + num); // Creamos una uri con el numero de telefono
                    Intent dial = new Intent(Intent.ACTION_DIAL, number); // Creamos una llamada al Intent de llamadas
                    startActivity(dial); // Ejecutamos el Intent
                }else{ // Si el numero esta vacio
                    // Mostramos una alerta de que debemos escribir un numero
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(llamada.this);
                    alert1.setTitle("No hay numero"); // Titulo de la alerta
                    alert1.setMessage("Debes escribir un Numero!"); // Contenido de la alerta
                    alert1.show(); // mostrar alerta
                }
            }
        });
    }
}