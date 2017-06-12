package com.example.wisnuekas.laporin;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import static com.example.wisnuekas.laporin.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker marker;
    private Button btnSetLocation;
    private String koordinat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnSetLocation = (Button) findViewById(R.id.btn_set_location);

        btnSetLocation.setOnClickListener(new SetLocationListener());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
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
        

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-6.714780, 111.338441);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Do Something

        if (marker != null) {
            marker.remove();
        }

        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Lat:" + latLng.latitude + " Lng:"
                        + latLng.longitude));

        koordinat = latLng.latitude + ", " + latLng.longitude;
    }

    protected class SetLocationListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent prev = getIntent();
            String imgEncode = prev.getStringExtra("IMAGE_ENCODE");
            String annotation = prev.getStringExtra("ANNOTATION");

            Intent i = new Intent(MapsActivity.this, LaporActivity.class);
            i.putExtra("KOORDINAT", koordinat);
            i.putExtra("IMAGE_ENCODE", imgEncode);
            i.putExtra("ANNOTATION", annotation);
            startActivity(i);

        }
    }
}
