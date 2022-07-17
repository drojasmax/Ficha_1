package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioGroup;
import android.widget.Toast;

public class redireccionar_pasajero_conductor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redireccionar_pasajero_conductor);

    }

    public void redireccionarVista(View v){
        Button btnRedireccionar = findViewById(R.id.btnSiguienteRedirec);
        EditText campoNumero = findViewById(R.id.etPhone);
        String numero = campoNumero.getText().toString();
        if (!numero.equals("")){
            RadioGroup rgClases = findViewById(R.id.rgClases);
            int id = rgClases.getCheckedRadioButtonId();
            switch (id){
                case R.id.rbPasajero:
                    btnRedireccionar.setOnClickListener(view -> {
                        Intent i = new Intent(getApplicationContext(),crear_pasajero.class);
                        startActivity(i);
                        i.putExtra("telefono",numero);
                    });
                    break;
                case R.id.rbConductor:
                    btnRedireccionar.setOnClickListener(view -> {
                        Intent i = new Intent(getApplicationContext(),crear_conductor.class);
                        startActivity(i);
                        i.putExtra("telefono",numero);
                    });
                    break;
                default:
                    Toast.makeText(redireccionar_pasajero_conductor.this, "Escoga una opción", Toast.LENGTH_SHORT).show();
            }
        }
    }
}