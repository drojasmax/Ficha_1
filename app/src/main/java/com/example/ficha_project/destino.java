package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

public class destino extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destino);
        //                              TOOLBAR
        //Referencia toolbar
        androidx.appcompat.widget.Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.inflateMenu(R.menu.menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(destino.this, "hola", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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
        //                              BOTTOM APP BAR (MENÚ INFERIOR)
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
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}