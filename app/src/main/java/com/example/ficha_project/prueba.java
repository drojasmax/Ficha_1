package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.math.MathUtils;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

public class prueba extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
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
                int color = Color.argb(alpha,0,0,0);
                scrim.setBackgroundColor(color);
            }
        });
    }
}