package com.example.locmanager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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
    final private int REQUEST_COURSE_ACCESS = 123;
    boolean permissionGranted = false;
    LocationManager lm;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COURSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.removeUpdates(locationListener);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COURSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }

        if(permissionGranted) {
          lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
        }
    }

    private void getLocation(){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_COURSE_ACCESS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true;
                }else{
                    permissionGranted = false;
                } break;
        }
    }




    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc){
            if(loc != null){
                Toast.makeText(getBaseContext(), "Location Changed : Lat : \"" +
                        "+loc.getLatitude() + \n" + "\" Lng: \n"
                        +loc.getLongitude(), Toast.LENGTH_SHORT).show();
                LatLng p = new LatLng((int) (loc.getLatitude()), (int) (loc.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
                mMap.addMarker(new MarkerOptions().position(p).title("Current Location"));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }
//        public void onProviderDisabled(String provider) {
//            Toast.makeText(getBaseContext(), provider + " Disabled", Toast.LENGTH_SHORT).show();
//        }
//        public void onProviderEnabled(String provider) {
//            Toast.makeText(getBaseContext(), provider + " Enabled",  Toast.LENGTH_SHORT).show();
//        }
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            String statusString = "";
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    statusString = "Available";
//                case LocationProvider.OUT_OF_SERVICE:
//                    statusString = "Out of Service";
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    statusString = "Temporarily Unavailable";         }
//            Toast.makeText(getBaseContext(),provider + " " + statusString,
//                    Toast.LENGTH_SHORT).show();
//        }
    }
}