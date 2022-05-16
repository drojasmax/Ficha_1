package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class crearCuenta1 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta1);

    }


    public void capturarNumero(View v){
        EditText campoNumero = this.findViewById(R.id.textPhone);
        String numero = campoNumero.getText().toString();
        Intent i = new Intent(getApplicationContext(),crearCuenta2.class);
        i.putExtra("numero", numero);
        startActivity(i);
    }
}