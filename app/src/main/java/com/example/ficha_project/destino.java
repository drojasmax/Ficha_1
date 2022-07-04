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

import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import retrofit2.Retrofit;


public class destino extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_AUTOCOMPLETE_FROM = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_TO = 2;
    private static final String TAG = "destino";
    //Google Maps Api
    private GoogleMap mMap;
    //Google Places Api
    Place place;
    EditText etUbiActual, etUbiDestino;
    //Origen y destino
    private Marker mMarkerFrom = null;
    private Marker mMarkerTo = null;
    private String sFromLocation, sToLocation;
    //Bottom Sheet Behavior
    BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino);
        setupMap();
        // Setup Places Api
        Places.initialize(getApplicationContext(), getString(R.string.googleAPIKEY));
        // TOOLBAR
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
        sFromLocation = getIntent().getStringExtra("from_location");
        sToLocation = getIntent().getStringExtra("to_location");
    }
    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    // METODOS PLACES SDK
    private void startAutocomplete(int requestCode){
        // Fields of place data to return after the user has made a selection
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            etUbiActual = findViewById(R.id.etUbiActual);
            processAutocompleteResult(resultCode, data, etUbiActual);
            LatLng mFromLatLng = this.place.getLatLng();
            setMarkerFrom(mFromLatLng);
            return;
        }else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO){
            etUbiDestino = findViewById(R.id.etUbiDestino);
            processAutocompleteResult(resultCode, data, etUbiDestino);
            LatLng mToLatLng = this.place.getLatLng();
            setMarkerTo(mToLatLng);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void processAutocompleteResult(int resultCode, Intent data, EditText label){
        if (resultCode == RESULT_OK) {
            this.place = Autocomplete.getPlaceFromIntent(data);
            Log.i(TAG, "Place" + place);
            label.setText(place.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
        }
    }
    // MÉTODOS TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Incoporar menú dentro de Activity
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
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
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void callDirectionsApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GMapsDirectionsAPI api = retrofit.create(GMapsDirectionsAPI.class);
        Call<Direction> call = api.getDirection("-27.3603996,-70.3129455", "-27.3602978,-70.3400296", "AIzaSyCgUYiQ5ke0B_-BDPKMdLS22vQdZg0Dzh8");
        call.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                for (Step step : response.body().getRoutes().get(0).getLegs().get(0).getSteps()){
                    Polyline polyline = step.getPolyline();
                    List<LatLng> points = PolyUtil.decode(polyline.getPoints());
                    mMap.addPolyline(new PolylineOptions().addAll(points).width(5).color(Color.GRAY));
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {

            }
        });
    }
    private Marker addMarker(LatLng latLng, String title){
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        return mMap.addMarker(markerOptions);
    }
    private void setMarkerFrom(LatLng mFromLatLng) {
        // If already set, remove it
        if (mMarkerFrom != null){
            mMarkerFrom.remove();
        }
        mMarkerFrom = addMarker(mFromLatLng, getString(R.string.marker_from));
    }
    private void setMarkerTo(LatLng mToLatLng){
        // If already set, remove it
        if (mMarkerTo != null){
            mMarkerTo.remove();
        }
        mMarkerTo = addMarker(mToLatLng, getString(R.string.marker_to));
    }
}