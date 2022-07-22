package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;
import java.util.List;

public class pasajero_principal extends AppCompatActivity {
Button btnCerrar;
    private static final int REQUEST_CODE_AUTOCOMPLETE_FROM = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_TO = 2;
    private static final String TAG = "pasajero_principal";


    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private MarkerOptions options;
    EditText etUbiActual, etUbiDestino;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero_principal);
        setupGoogleApis();
        btnCerrar=findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Toolbar
        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        // BottomSheet (Menú inferior desplegable
        FloatingActionButton fab = findViewById(R.id.fab);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setHideable(true);
        fab.setOnClickListener(view -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet1, int newState) {
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
                public void onSlide(@NonNull View bottomSheet1, float slideOffset) {

                }
            });
        });
    }

    private void setupGoogleApis() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
    }

    private void startAutocomplete(int requestCode) {
        Places.initialize(getApplicationContext(), getString(R.string.googleAPIKEY));
        // Fields of place data to return after the user has made a selection
        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        assert data != null;
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        etUbiActual = findViewById(R.id.etUbiActual);
                        etUbiActual.setText(place.getAddress());
                        LatLng ubiActual = place.getLatLng();
                        assert ubiActual != null;
                        options = new MarkerOptions()
                                .position(ubiActual)
                                .title("Desde");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubiActual,15));
                        googleMap.addMarker(options);
                    }
                });
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            return;
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO) {
            if (resultCode == RESULT_OK) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        assert data != null;
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        etUbiDestino = findViewById(R.id.etUbiDestino);
                        etUbiDestino.setText(place.getAddress());
                        LatLng ubiDestino = place.getLatLng();
                        assert ubiDestino != null;
                        options = new MarkerOptions()
                                .position(ubiDestino)
                                .title("Hacia");
                        googleMap.addMarker(options);
                    }
                });
            }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // MÉTODOS TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        //Incoporar menú dentro de Activity
        getMenuInflater().inflate(R.menu.menu_pasajero, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.opPerfil) {
            Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), Perfil.class);
            Bundle paquete = getIntent().getExtras();
            String idusuario = null;
            String correo = null;
            if (paquete != null) {
                idusuario = paquete.getString("idusuario");
                correo = paquete.getString("correo");
            }
            i.putExtra("idusuario", idusuario);
            i.putExtra("correo", correo);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private Marker addMarker(LatLng latLng, String title) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        return mMap.addMarker(markerOptions);
    }
}