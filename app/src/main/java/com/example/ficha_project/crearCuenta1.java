package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class crearCuenta1 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta1);

    }

    /**
     * Este metodo captura el numero de telefono ingresado y lo envia al Activity crearCuenta2.
     */
    public void capturarNumero(View v){
        EditText campoNumero = this.findViewById(R.id.textPhone);
        String numero = campoNumero.getText().toString();
        if(!numero.equals("")){
            Intent i = new Intent(getApplicationContext(),crearCuenta2.class);
            i.putExtra("numero", numero);
            startActivity(i);
        }else{
            Toast.makeText(this, "Ingresa un numero Crack", Toast.LENGTH_SHORT).show();
        }

    }
}