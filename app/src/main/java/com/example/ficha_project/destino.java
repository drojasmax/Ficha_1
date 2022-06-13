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
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class destino extends AppCompatActivity {

    private static int REQUEST_CODE_AUTOCOMPLETE_FROM = 1;
    private static int REQUEST_CODE_AUTOCOMPLETE_TO = 2;
    private static String TAG="destino.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino);
        //                              PLACES API
        Places.initialize(getApplicationContext(), getString(R.string.googleAPIKEY));
        //                              TOOLBAR
        //Referencia toolbar
        androidx.appcompat.widget.Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        /*DrawerLayout dl = (DrawerLayout) findViewById(R.id.principal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dl,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        dl.addDrawerListener(toggle);
        toggle.syncState();
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dl.isDrawerOpen(GravityCompat.START)){
                    dl.closeDrawer(GravityCompat.START);
                }
                else{
                    dl.openDrawer((int) Gravity.START);
                }
            }
        });*/
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
        //                            BOTONES PLACES SDK
        Button btnFrom = (Button) navigationView.getHeaderView(0).findViewById(R.id.btnFrom);
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_FROM);
            }
        });
        Button btnTo = (Button) navigationView.getHeaderView(0).findViewById(R.id.btnTo);
        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAutocomplete(REQUEST_CODE_AUTOCOMPLETE_TO);
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
    //                                  METODOS PLACES SDK
    private void startAutocomplete(int requestCode){
        // Fields of place data to return after the user has made a selection
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);

        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place Name & Id: " + place.getName() + ", " + place.getId());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}