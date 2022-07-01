package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Arrays;
import java.util.List;

public class destino extends AppCompatActivity implements OnMapReadyCallback {

    private static int REQUEST_CODE_AUTOCOMPLETE_FROM = 1;
    private static int REQUEST_CODE_AUTOCOMPLETE_TO = 2;
    private static String TAG="destino";
    //Google Maps Api
    private GoogleMap mMap;
    //Google Places Api
    Place place;
    //Origen y destino
    private LatLng mFromLatLng;
    private LatLng mToLatLng;
    private Marker mMarkerFrom = null;
    private Marker mMarkerTo = null;

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

        Button btnDesplegarMenu = (Button) findViewById(R.id.btnBuscar);


        btnDesplegarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_sheet bottomSheetFragment = new bottom_sheet();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

    }
    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /*
    //                                  METODOS PLACES SDK
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

            processAutocompleteResult(resultCode, data, etUbiActual);
            mFromLatLng = this.place.getLatLng();
            setMarkerFrom(mFromLatLng);
            return;
        }else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO){

            processAutocompleteResult(resultCode, data, etUbiDestino);
            mToLatLng = this.place.getLatLng();
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
    }*/
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
    // METODOS GOOGLE MAPS
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     *
     * This is where we can add markers or lines, add listeners or move the camera. In this case we add 2 markers (origin and destination)
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Set zoom preferences
        //mMap.setMinZoomPreference(15f);
        //mMap.setMaxZoomPreference(20f);
        // . . .
    }
    private Marker addMarker(LatLng latLng, String titulo){
        final MarkerOptions markerOptions = (new MarkerOptions()
                .position(latLng)
                .title(titulo));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        return mMap.addMarker(markerOptions);
    }
    private void setMarkerFrom(LatLng latLng){
        if(mMarkerFrom != null){
            mMarkerFrom.remove();
        }
        mMarkerFrom = addMarker(latLng, getString(R.string.marker_title_from));
    }
    private  void setMarkerTo(LatLng latLng){
        if (mMarkerTo != null){
            mMarkerTo.remove();
        }
        mMarkerTo = addMarker(latLng, getString(R.string.marker_title_to));
    }
}