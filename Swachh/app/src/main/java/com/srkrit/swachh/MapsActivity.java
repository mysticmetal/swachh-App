package com.srkrit.swachh;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent=getIntent();


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

        if ((intent.getStringExtra("latitude")!="")&&(intent.getStringExtra("latitude")!=null)){
            LatLng issueLoc = new LatLng(Double.valueOf(intent.getStringExtra("latitude")), Double.valueOf(intent.getStringExtra("longitude")));
            mMap.addMarker(new MarkerOptions().position(issueLoc).title(intent.getStringExtra("title")).snippet(intent.getStringExtra("address")));


            mMap.moveCamera(CameraUpdateFactory.newLatLng(issueLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        }
        else{
            Toast.makeText(MapsActivity.this, "Location not available", Toast.LENGTH_LONG).show();
        }


    }
}
