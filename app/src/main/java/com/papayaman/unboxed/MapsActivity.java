package com.papayaman.unboxed;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationClient;

    private static Client client;

    private ArrayList<Double[]> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
                final Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addSale = new Intent(MapsActivity.this, AddingActivity.class);
                startActivity(addSale);
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mGoogleApiClient.connect();
        client = new Client("papayaman.com", 8765);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        markers = new ArrayList<>();
        markers.addAll(client.getMarkers());
    }

    public static Client getClient() {
        return client;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final double[] lati = new double[1];
        final double[] longi = new double[1];

        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lati[0] = location.getLatitude();
                        Log.i("onMapReady", "" + lati[0]);
                        longi[0] = location.getLongitude();
                        System.out.println(longi[0]);
                    }
                }
            });

        } catch (SecurityException e) {
            e.printStackTrace();
            lati[0] = -34;
            longi[0] = 151;
        }
        Log.i("onMapReady", "YOOOO THERE: " + lati[0]);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lati[0], longi[0]);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        for (Double[] marker : markers) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(marker[0], marker[1])).title(Integer.parseInt(marker[2] + "") + "\\" + Integer.parseInt(marker[3] + "") + "\\" + Integer.parseInt(marker[4] + "")));
        }
    }
}
