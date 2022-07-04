package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Modelo.Direction;
import Modelo.Polyline;
import Modelo.Step;
import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import retrofit2.Retrofit;


public class destino extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * Código asociado a la solicitud "Desde"
     */
    private static final int REQUEST_CODE_AUTOCOMPLETE_FROM = 1;
    /**
     * Código asociado a la solicitud "Hacia"
     */
    private static final int REQUEST_CODE_AUTOCOMPLETE_TO = 2;
    /**
     * Etiqueta relacionada al activity en el que interactuan los datos
     */
    private static final String TAG = "destino";
    //Google Maps Api
    /**
     * Referencia al objeto Maps de Google Maps
     */
    private GoogleMap mMap;
    //Google Places Api
    /**
     * Referencia al objeto Place de SDK Places
     */
    Place place;
    /**
     * Elementos globales vinculados a las solicitudes From y To
     */
    EditText etUbiActual, etUbiDestino;
    //Origen y destino
    /**
     * Marcadores relacionados a las solicitudes Desde y Hacia
     */
    private Marker mMarkerFrom = null;
    private Marker mMarkerTo = null;
    /**
     * Textos relacionados a las solicitudes codificadas de Desde y Hacia
     */
    String originEncoded, destinationEncoded;
    //Bottom Sheet Behavior
    /**
     * Interacción de comportamiento asociada al menú inferior desplegable (Persistent Bottom Sheet)
     */
    BottomSheetBehavior bottomSheetBehavior;

    /**
     * Este metodo ejecuta la lógica de arranque básica de la aplicación
     *
     * Inicializa los elementos de las api implementadas (Places SDK, Direction API, Autocomplete Api)
     * Y se le asignan las solicitudes de Desde y Hacia a los botones correspondientes relacionado a las vistas
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino);
        // Setup Google Maps Api
        setupMap();
        // Setup Places Api
        Places.initialize(getApplicationContext(), getString(R.string.googleAPIKEY));
        // Toolbar
        androidx.appcompat.widget.Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        // BottomSheet (Menú inferior desplegable
        FloatingActionButton fab = findViewById(R.id.fab);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            etUbiActual = findViewById(R.id.etUbiActual);
                            etUbiActual.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_FROM);
                                }
                            });
                            etUbiDestino = findViewById(R.id.etUbiDestino);
                            etUbiDestino.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_TO);
                                }
                            });
                        }
                    }
                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
        });
    }
    /**
     * Este metodo obtiene el soporte de fragmento del mapa y notifica cuando el mapa está listo para ser usado
     *
     */
    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    // METODOS PLACES SDK
    /**
     * Este metodo permite devolver campos después de que el usuario haya hecho una selección
     * E inicia la intención de Autocomplete Api
     * @param requestCode
     */
    private void startAutocomplete(int requestCode){
        // Fields of place data to return after the user has made a selection
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, requestCode);
    }
    /**
     * Este metodo procesa si el codigo de solicitud es igual al codigo de solicitud de Desde y Hacia y controla errores / excepciones
     * @param data  está relacionada la intención del Autocomplete Api
     * @param requestCode si es el codigo de solicitud desde o solicitud hacia, hará lo que esté dentro del if
     * @param resultCode si es RESULT_OK hará lo que está dentro del if
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK){
                this.place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place" + place);
                etUbiActual = findViewById(R.id.etUbiActual);
                etUbiActual.setText(place.getAddress());
                LatLng mFromLatLng = this.place.getLatLng();
                String sFromLocation = etUbiActual.getText().toString();
                if (sFromLocation.isEmpty()){
                    Toast.makeText(this, "Ubicación no identificada", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    originEncoded = URLEncoder.encode(sFromLocation, "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                setMarkerFrom(mFromLatLng);
            }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
            }
            return;
        }else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO){
            if (resultCode == RESULT_OK){
                this.place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place" + place);
                etUbiDestino = findViewById(R.id.etUbiDestino);
                etUbiDestino.setText(place.getAddress());
                LatLng mToLatLng = this.place.getLatLng();
                String sToLocation = etUbiDestino.getText().toString();
                setMarkerTo(mToLatLng);
                if (sToLocation.isEmpty()){
                    Toast.makeText(this, "Destino no identificado", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    destinationEncoded = URLEncoder.encode(sToLocation, "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // MÉTODOS TOOLBAR
    /**
     * Este metodo permite crear el menú de ocpiones cuando el usuario abre el menú por primera vez
     *
     * @param menu Este código permite integrar el menú anexado a la toolbar dentro del activity
     * @return Retornando true si de verdad se incorporó
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Incoporar menú dentro de Activity
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /**
     * Este metodo permite interactuar con las opciones dentro del menú relacionadas al método anterior
     *
     * @param item Hace referencia al item seleccionado dentro del menú
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.opPerfil){
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),Perfil.class);
            Bundle paquete = getIntent().getExtras();
            String idusuario = null;
            String correo = null;
            if(paquete != null){
                idusuario = paquete.getString("idusuario");
                correo = paquete.getString("correo");
            }
            i.putExtra("idusuario",idusuario);
            i.putExtra("correo",correo);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    // METODOS GOOGLE MAPS & DIRECTIONS API
    /**
     *Es el método que permite interactuar con el mapa cuando se inicia una vista
     *
     * @param googleMap Hace referencia al mapa que queremos exponer en la vista
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        callDirectionsApi();
    }
    /**
     * Este metodo hace referencia al servidor del api service de Direction Api y permite trazar una linea entre un punto origen y un punto destino
     *
     */
    private void callDirectionsApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GMapsDirectionsAPI api = retrofit.create(GMapsDirectionsAPI.class);
        Call<Direction> call = api.getDirection(originEncoded, destinationEncoded, "AIzaSyCgUYiQ5ke0B_-BDPKMdLS22vQdZg0Dzh8");
        call.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(@NonNull Call<Direction> call, @NonNull Response<Direction> response) {
                boolean setMarkerFirstTime = false;
                for (Step step : response.body().getRoutes().get(0).getLegs().get(0).getSteps()){
                    Polyline polyline = step.getPolyline();
                    List<LatLng> points = PolyUtil.decode(polyline.getPoints());
                    mMap.addPolyline(new PolylineOptions().addAll(points).width(5).color(Color.GRAY));
                    if (!setMarkerFirstTime){
                        mMap.addMarker(new MarkerOptions().position(points.get(0)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
                        setMarkerFirstTime = true;
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Direction> call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }
    /**
     * Este metodo permite agregar marcadores en base a una latitud y longitud
     * @param latLng Hace referencia a la ubicación en donde irá el marcador
     * @param title Hace referencia al nombre que tendrá el marcador
     *
     */
    private Marker addMarker(LatLng latLng, String title){
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        return mMap.addMarker(markerOptions);
    }
    /**
     * Este metodo permite crear un marcador relacionado a la solicitud Desde de Direction Api
     *
     * @param mFromLatLng Hace referencia a la latitud y longitud de la solicitud de Desde
     */
    private void setMarkerFrom(LatLng mFromLatLng) {
        // If already set, remove it
        if (mMarkerFrom != null){
            mMarkerFrom.remove();
        }
        mMarkerFrom = addMarker(mFromLatLng, getString(R.string.marker_from));
    }
    /**
     * Este metodo permite crear un marcador relacionado a la solicitud Hacia de Direction Api
     *
     * @param mToLatLng Hace referencia a la latitud y longitud de la solicitud de Desde
     */
    private void setMarkerTo(LatLng mToLatLng){
        // If already set, remove it
        if (mMarkerTo != null){
            mMarkerTo.remove();
        }
        mMarkerTo = addMarker(mToLatLng, getString(R.string.marker_to));
    }
}