package com.example.ficha_project;

import com.example.ficha_project.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText etCorreo, etContra;
    Button btnIniciarSesion;
    String correo,contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etCorreo = findViewById(R.id.etCorreo);
        etContra = findViewById(R.id.etContra);
        btnIniciarSesion = findViewById(R.id.btnLogin);

        recuperarPreferencias();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correo=etCorreo.getText().toString();
                contra=etContra.getText().toString();
                if (!correo.isEmpty() && !contra.isEmpty()){
                    validarUsuario("http://200.29.219.25:3307/duvan/validarPasajero.php");
                }else{
                  Toast.makeText(Login.this, "No colocar campos vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void validarUsuario (String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    guardarPreferencias();
                    Intent intent = new Intent(getApplicationContext(), pasajero_principal.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Usuario o contrasenia incorrecta", Toast.LENGTH_SHORT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Correo", correo);
                parametros.put("Contrasenia", contra);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest); //todas las peticiones de nuestra app
    }
private void guardarPreferencias(){
    SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor=preferences.edit();
    editor.putString("usuario", correo);
    editor.putString("contra", contra);
    editor.putBoolean("sesion", true);
    editor.commit();
}
private void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        etCorreo.setText(preferences.getString("usuario" , "diego@gmail.com"));
        etContra.setText(preferences.getString("contra" , "123"));
}
}