package com.example.ficha_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MapsSetDirection extends destino {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino);
        EditText etUbiActual = findViewById(R.id.etUbiActual);
        EditText etUbiDestino = findViewById(R.id.etUbiDestino);
        Button btnBuscarVehiculo = findViewById(R.id.btnBuscarVehiculo);
        btnBuscarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String origin = etUbiActual.getText().toString();
                String destination = etUbiDestino.getText().toString();
                if (origin.isEmpty() || destination.isEmpty()){
                    return;
                }
                try {
                    String originEncoded = URLEncoder.encode(origin, "utf-8");
                    String destinationEncoded = URLEncoder.encode(destination, "utf-8");
                    Intent intent = new Intent(MapsSetDirection.this, destino.class);
                    intent.putExtra("location_from", originEncoded);
                    intent.putExtra("location_to", destinationEncoded);
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
