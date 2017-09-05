package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jmgarzo.dublinbus.R.id.map;

public class RouteMapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    boolean mapReady = false;
    private ArrayList<BusStop> mBusStopList;
    private ArrayList<LatLng> mLatLngBusStop;
    private Route mRoute;

    private final int BUS_STOPS_LOADER_ID = 223;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_maps);

        Intent intent = getIntent();
        if (null != intent) {
            mRoute = getIntent().getParcelableExtra(RouteDetailActivityFragment.ROUTE_EXTRA_TAG);
        }
        this.getSupportLoaderManager().initLoader(BUS_STOPS_LOADER_ID, null, this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady = true;
        mMap = map;

        for (BusStop busStop : mBusStopList) {
            MarkerOptions newMarker = new MarkerOptions()
                    .position(busStop.getLatLng())
                    .title(busStop.getNumber())
                    .snippet(busStop.getFullName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp));
            mMap.addMarker(newMarker);

        }
        setupCamera();
    }

    private void setupCamera() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng ll : mLatLngBusStop) {
            builder.include(ll);
        }
        LatLngBounds bounds = builder.build();

        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case BUS_STOPS_LOADER_ID: {
                Uri stopBusWithRouteIdUri = DublinBusContract.BusStopEntry.buildBusStopWithRouteId(
                        Long.toString(mRoute.getId()));
                return new CursorLoader(this,
                        stopBusWithRouteIdUri,
                        DBUtils.BUS_STOP_COLUMNS,
                        DublinBusContract.BusStopEntry.IS_NEW + " = ?",
                        new String[]{"0"},
                        DublinBusContract.RouteBusStopEntry.RECORD_ORDER);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case BUS_STOPS_LOADER_ID: {
                if (null != data && data.moveToFirst()) {
                    mBusStopList = cursorToBusStopList(data);
                }
                mLatLngBusStop = getLatLngBusStop(mBusStopList);

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(map);
                mapFragment.getMapAsync(this);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ArrayList<BusStop> cursorToBusStopList(Cursor data) {
        ArrayList<BusStop> busStopsList = new ArrayList<>();
        do {
            BusStop busStop = new BusStop();
            busStop.cursorToBusStop(data);
            busStopsList.add(busStop);
        } while (data.moveToNext());
        return busStopsList;
    }

    private LatLng getCentrer(List<LatLng> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        return new LatLng(latitude / n, longitude / n);
    }

    private ArrayList<LatLng> getLatLngBusStop(ArrayList<BusStop> busStopsList) {
        ArrayList<LatLng> latLngBusStop = null;
        if (null != busStopsList && !busStopsList.isEmpty()) {
            latLngBusStop = new ArrayList<>();
            for (BusStop bs : busStopsList) {
                latLngBusStop.add(bs.getLatLng());
            }
        }
        return (latLngBusStop);
    }
}
