package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jmgarzo.dublinbus.R.id.map;

public class RouteMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

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

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        String busStopNumber = marker.getTitle();


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String busStopNumber = marker.getTitle();

        return false;
    }

    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

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

/*        @Override
        public View getInfoContents(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
            return mContents;
        }*/

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
            if (snippet != null && snippet.length() > 12) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }
        }
    }
}
