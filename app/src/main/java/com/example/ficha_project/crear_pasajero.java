package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class crear_pasajero extends AppCompatActivity {

    EditText etNombre, etApellido, etCorreo, etContra, etFoto;
    RequestQueue requestQueue;
    private static final String URL = "http://192.168.1.95/bd_ficha/saveP.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);

    }
    public void registrarse(View v){
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contrasena = etContra.getText().toString().trim();
        String foto = etFoto.getText().toString().trim();
        Bundle paquete = getIntent().getExtras();
        if (paquete!=null){
        String telefono = (String) paquete.getSerializable("telefono");
        crearPasajero(nombre, apellido, correo, contrasena, telefono, foto);
        }
    }

    private void crearPasajero(final String nombre, final String apellido, final String correo, final String contrasena, final String telefono, final String foto) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                response -> Toast.makeText(crear_pasajero.this, "Registro exitoso", Toast.LENGTH_SHORT).show(),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(crear_pasajero.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            ){
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("apellido", apellido);
                parametros.put("correo", correo);
                parametros.put("contrasena", contrasena);
                parametros.put("telefono", telefono);
                parametros.put("foto", foto);
                return parametros;
            }
        };
    }
}