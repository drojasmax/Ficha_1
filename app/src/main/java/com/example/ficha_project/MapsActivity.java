package com.example.ficha_project;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ficha_project.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker mMarkerFrom = null;
    private Marker mMarkerTo = null;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    // METODOS GOOGLE MAPS & DIRECTIONS API
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    private Marker addMarker(LatLng latLng, String title) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        return mMap.addMarker(markerOptions);
    }

    private void setMarkerFrom(LatLng mFromLatLng) {
        // If already set, remove it
        if (mMarkerFrom != null) {
            mMarkerFrom.remove();
        }
        mMarkerFrom = addMarker(mFromLatLng, getString(R.string.marker_from));
    }

    private void setMarkerTo(LatLng mToLatLng) {
        // If already set, remove it
        if (mMarkerTo != null) {
            mMarkerTo.remove();
        }else{
            mMarkerTo = addMarker(mToLatLng, getString(R.string.marker_to));
        }
    }
}