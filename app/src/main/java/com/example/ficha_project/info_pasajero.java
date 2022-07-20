package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class info_pasajero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pasajero);
    }
        public void Siguiente(View view){
            Intent siguiente = new Intent(this, info_conductor_llamada.class);
            startActivity(siguiente);


    }
}