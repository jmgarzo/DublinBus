package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Route;

import java.util.ArrayList;

public class RouteMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    boolean mapReady=false;
    private ArrayList<BusStop> mBusStopList;
    private Route mRoute;
    private CameraPosition camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_maps);

        Intent intent = getIntent();
        if (null != intent) {
            mBusStopList = getIntent().getParcelableArrayListExtra(RouteDetailActivityFragment.BUS_STOP_LIST_EXTRA_TAG);
            mRoute = getIntent().getParcelableExtra(RouteDetailActivityFragment.ROUTE_EXTRA_TAG);
        }
        LatLng firstStop = mBusStopList.get(0).getLatLng();
        camera = CameraPosition.builder()
                .target(firstStop)
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map){
        mapReady=true;
        mMap = map;

        for (BusStop  busStop : mBusStopList){
            MarkerOptions newMarker = new MarkerOptions()
                    .position(busStop.getLatLng())
                    .title(busStop.getNumber())
                    .snippet(busStop.getFullName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp));
            mMap.addMarker(newMarker);
        }
        mapReady=true;
        flyTo(camera);
    }

    private void flyTo(CameraPosition target)
    {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }


//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
