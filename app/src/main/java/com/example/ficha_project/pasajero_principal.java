package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;
import java.util.List;

public class pasajero_principal extends AppCompatActivity  {

    private static final int REQUEST_CODE_AUTOCOMPLETE_FROM = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE_TO = 2;
    private static final String TAG = "pasajero_principal";

    //Google Maps API'S
    private GoogleMap mMap;
    Place place;

    EditText etUbiActual, etUbiDestino;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero_principal);
        // Setup
        Places.initialize(getApplicationContext(), getString(R.string.googleAPIKEY));

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
                        etUbiActual.setOnClickListener(view1 -> startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_FROM));
                        etUbiDestino = findViewById(R.id.etUbiDestino);
                        etUbiDestino.setOnClickListener(view12 -> startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_TO));
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet1, float slideOffset) {

                }
            });
        });
    }

    // METODOS PLACES SDK
    private void startAutocomplete(int requestCode) {
        // Fields of place data to return after the user has made a selection
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                this.place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place" + place);
                etUbiActual = findViewById(R.id.etUbiActual);
                etUbiActual.setText(place.getAddress());
                LatLng mFromLatLng = this.place.getLatLng();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                assert data != null;
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            return;
        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                this.place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place" + place);
                etUbiDestino = findViewById(R.id.etUbiDestino);
                etUbiDestino.setText(place.getAddress());
                LatLng mToLatLng = this.place.getLatLng();

            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // MÉTODOS TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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



}