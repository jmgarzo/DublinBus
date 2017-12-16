package com.jmgarzo.dublinbus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.model.BusStop;
import com.jmgarzo.dublinbus.model.Route;
import com.jmgarzo.dublinbus.utilities.AdUtils;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.MapsUtils;
import com.jmgarzo.dublinbus.utilities.PermissionUtils;

import java.util.ArrayList;

import static com.jmgarzo.dublinbus.R.id.map;

public class RouteMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RouteMapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    boolean mapReady = false;
    private ArrayList<BusStop> mBusStopList;
    private ArrayList<LatLng> mLatLngBusStop;
    private Route mRoute;
    private BusStop markerBusStop;

    private AdView mAdView;


    private final int BUS_STOPS_LOADER_ID = 223;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_maps);

        if(DBUtils.isAdmodActive(this)) {
            mAdView = findViewById(R.id.ad_view);
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(AdUtils.getAdRequest());
        }

        Intent intent = getIntent();
        if (null != intent) {
            if (null != getIntent().getParcelableExtra(RouteDetailActivityFragment.ROUTE_EXTRA_TAG)) {
                mRoute = getIntent().getParcelableExtra(RouteDetailActivityFragment.ROUTE_EXTRA_TAG);
                String title = getString(R.string.title_activity_route_maps) + " " +
                        getString(R.string.title_activity_route_maps_label_route) + " " + mRoute.getName();
                setTitle(title);
            }


        }
        this.getSupportLoaderManager().initLoader(BUS_STOPS_LOADER_ID, null, this);
//        this.getSupportLoaderManager().initLoader(ROUTES_PER_BUS_STOP,null,this);
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady = true;
        mMap = map;

        for (BusStop busStop : mBusStopList) {
            String snippet = busStop.getFullName();

            MarkerOptions newMarker = new MarkerOptions()
                    .position(busStop.getLatLng())
                    .title(busStop.getNumber())
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp));
            mMap.addMarker(newMarker);


        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        setupCamera();

    }



    private void setupCamera() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng ll : mLatLngBusStop) {
            builder.include(ll);
        }
        LatLngBounds bounds = builder.build();

        int padding = 75; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case BUS_STOPS_LOADER_ID: {
                Uri mUri = DublinBusContract.BusStopsAndRouteEntry.buildBusStopsAndRoutes(Long.toString(mRoute.getId()));
                String orderBy = DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID +
                        ", " + DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry.NAME + " *1, " +
                        DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry.NAME + " COLLATE NOCASE ASC ";
                if (null != mUri) {
                    return new CursorLoader(
                            this,
                            mUri,
                            DBUtils.BUS_STOP_AND_ROUTES_COLUMNS,
                            null,
                            null,
                            orderBy//DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID
                    );
                }
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
                    mBusStopList = MapsUtils.getBusStopListFromBusStopsAndRoute(data);
                }
                mLatLngBusStop = MapsUtils.getLatLngBusStop(mBusStopList);

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

    @Override
    public void onInfoWindowClick(Marker marker) {
        String busStopNumber = marker.getTitle();
        Intent intent = new Intent(this, RealTimeStopActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, busStopNumber);
        this.startActivity(intent);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        markerBusStop = null;
        String busStopNumber = marker.getTitle();
        for (BusStop bs : mBusStopList) {
            if (bs.getNumber().equalsIgnoreCase(busStopNumber)) {
                markerBusStop = bs;
                break;
            }
        }
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {

        return false;
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_map_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }


        private void render(Marker marker, View view) {


            ((ImageView) view.findViewById(R.id.badge)).setImageResource(R.drawable.yellow_a700_circle_480x480);

            String title = marker.getTitle();
//            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            TextView textImageBadge = view.findViewById(R.id.text_title_badge);
            if (title != null) {
                textImageBadge.setText(title);
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
//                titleUi.setText(titleText);
            } else {
//                titleUi.setText("");
                textImageBadge.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = view.findViewById(R.id.snippet);

            snippetUi.setText(snippet);

            TextView tvRoutesNumber = view.findViewById(R.id.tv_routes_number);
            TextView tvRoutesInfo = view.findViewById(R.id.tv_routes_info);

            if (markerBusStop != null) {
                String routesNumber = "";
                String routesInfo = "";
                for (Route rt : markerBusStop.getRoutesList()) {
                    if (routesNumber.equalsIgnoreCase("")) {
                        routesNumber = routesNumber + rt.getName();
                        routesInfo = routesInfo + rt.getOrigin() + " - " + rt.getDestination();
                    } else {
                        routesNumber = routesNumber + "\n" + rt.getName();
                        routesInfo = routesInfo + "\n" + rt.getOrigin() + " - " + rt.getDestination();
                    }
                }
                tvRoutesNumber.setText(routesNumber);
                tvRoutesInfo.setText(routesInfo);
            }

        }
    }
}
