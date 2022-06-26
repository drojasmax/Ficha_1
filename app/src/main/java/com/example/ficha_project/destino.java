package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

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
        setupPlaces();
        //

        //                              TOOLBAR
        androidx.appcompat.widget.Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        //                       BOTTOM APP BAR (MENÚ INFERIOR)
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.navi));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bttmAppBar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.navi);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                return true;
            }
        });

        //                         BOTTOM APP BAR (MENÚ INFERIOR)
        FrameLayout scrim = (FrameLayout) findViewById(R.id.scrim);
        scrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //Color baseColor = new Color(); //= Color.BLACK;
                float baseAlpha = ResourcesCompat.getFloat(getResources(), com.google.android.material.R.dimen.material_emphasis_medium);
                float offset = (slideOffset-(-1f))/(1f-(-1f))*(1f-0f)+0f;
                float alpha = com.google.android.material.math.MathUtils.lerp(0f,255f,offset*baseAlpha);
                int a = (int) alpha;
                int color = Color.argb(a,0,0,0);
                scrim.setBackgroundColor(color);
            }
        });
    }
    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void setupPlaces(){
        NavigationView navi = (NavigationView) findViewById(R.id.navi);
        Places.initialize(getApplicationContext(), getString(R.string.googleAPIKEY));
        TextInputEditText txtUbiActual = (TextInputEditText) navi.getHeaderView(0).findViewById(R.id.txtUbiActual);
        txtUbiActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_FROM);
            }
        });
        TextInputEditText txtUbiDestino = (TextInputEditText) navi.getHeaderView(0).findViewById(R.id.txtUbiDestino);
        txtUbiDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_TO);
            }
        });
    }
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
            TextInputEditText txtUbiActual = (TextInputEditText) findViewById(R.id.txtUbiActual);
            processAutocompleteResult(resultCode, data, txtUbiActual);
            mFromLatLng = this.place.getLatLng();
            setMarkerFrom(mFromLatLng);
            return;
        }else if (requestCode == REQUEST_CODE_AUTOCOMPLETE_TO){
            TextInputEditText txtUbiDestino = (TextInputEditText) findViewById(R.id.txtUbiDestino);
            processAutocompleteResult(resultCode, data, txtUbiDestino);
            mToLatLng = this.place.getLatLng();
            setMarkerTo(mToLatLng);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void processAutocompleteResult(int resultCode, Intent data, TextInputEditText label){
        if (resultCode == RESULT_OK) {
            this.place = Autocomplete.getPlaceFromIntent(data);
            Log.i(TAG, "Place" + place);
            label.setText(place.getName());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
        }
    }
    //                                  MÉTODOS TOOLBAR
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
    //METODOS GOOGLE MAPS
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
        mMap.setMinZoomPreference(15f);
        mMap.setMaxZoomPreference(20f);
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