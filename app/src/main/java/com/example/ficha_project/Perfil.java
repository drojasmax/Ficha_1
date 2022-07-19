package com.example.ficha_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        recuperarUsuario();
    }

    public void recuperarUsuario(){

    }
}