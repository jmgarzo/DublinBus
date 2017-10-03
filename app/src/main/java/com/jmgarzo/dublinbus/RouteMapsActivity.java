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
import com.jmgarzo.dublinbus.utilities.PermissionUtils;

import java.util.ArrayList;

import static com.jmgarzo.dublinbus.R.id.map;

public class RouteMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private String NUMBER_BUS_STOP_TAG = "number_bus_stop_tag";
    private String FAVOURITE_ROUTE_EXTRA_TAG = "favourite_route_tag";
    private GoogleMap mMap;
    boolean mapReady = false;
    private ArrayList<BusStop> mBusStopList;
    private ArrayList<LatLng> mLatLngBusStop;
    private Route mRoute;

    private final int BUS_STOPS_LOADER_ID = 223;
    private final int ROUTES_PER_BUS_STOP = 245;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_maps);

        Intent intent = getIntent();
        if (null != intent) {
            if(null != getIntent().getParcelableExtra(RouteDetailActivityFragment.ROUTE_EXTRA_TAG)){
                mRoute = getIntent().getParcelableExtra(RouteDetailActivityFragment.ROUTE_EXTRA_TAG);
                String title = getString(R.string.title_activity_route_maps) + " "+
                        getString(R.string.title_activity_route_maps_label_route)+" " + mRoute.getName();
                setTitle(title);
            }


        }
        this.getSupportLoaderManager().initLoader(BUS_STOPS_LOADER_ID, null, this);
//        this.getSupportLoaderManager().initLoader(ROUTES_PER_BUS_STOP,null,this);
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

                if ( null != mUri ) {
                    // Now create and return a CursorLoader that will take care of
                    // creating a Cursor for the data being displayed.
                    return new CursorLoader(
                            this,
                            mUri,
                            DBUtils.BUS_STOP_AND_ROUTES_COLUMNS,
                            null,
                            null,
                            DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID
                    );
                }

            }

//            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case BUS_STOPS_LOADER_ID: {
                ArrayList<BusStop> busStopList;
                if (null != data && data.moveToFirst()) {
                    mBusStopList = getBusStopList(data);
                }
                mLatLngBusStop = getLatLngBusStop(mBusStopList);

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(map);
                mapFragment.getMapAsync(this);
                break;
            }
//            case ROUTES_PER_BUS_STOP:{
//                if(null != data && data.moveToFirst()){
//                    ArrayList<Route>
//                    do{
//                        Route route = new Route();
//                    }while(data.moveToNext());
//                }
//            }
        }
    }





    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ArrayList<BusStop>getBusStopList(Cursor data){
        ArrayList<BusStop> busStopArrayList = new ArrayList<>();
        int currentBusStopId = -1;
        if(data.moveToFirst()){
            BusStop bs = null;
            do{
                if(currentBusStopId == data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID) && bs !=null){
                    bs.getRoutesList().add(getNewRoute(data));
                }else{
                    if(bs!=null) {
                        busStopArrayList.add(bs);
                    }
                    currentBusStopId = data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID);

                    bs = getNewBusStop(data);
                }


            }while(data.moveToNext());
        }
        return busStopArrayList;
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

    private BusStop getNewBusStop(Cursor data){
        BusStop bs = new BusStop();
        bs.setId(data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID));
        bs.setNumber(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_NUMBER));
        bs.setDisplayStopId(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_DISPLAY_STOP_ID));
        bs.setShortName(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_SHORTNAME));
        bs.setShortNameLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_SHORT_NAME_LOCALIZED));
        bs.setFullName(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_FULL_NAME));
        bs.setFullNameLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_FULL_NAME_LOCALIZED));
        bs.setLatitude(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_LATITUDE));
        bs.setLongitude(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_LONGITUDE));
        bs.setLastUpdated(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_LAST_UPDATED));
        bs.setFavourite(data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_IS_FAVORITE) != 0);
        bs.setAlias(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_IS_ALIAS));
        bs.setNew(data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_IS_NEW) != 0);

        bs.setRoutesList(new ArrayList<Route>());

        return bs;
    }

    private Route getNewRoute(Cursor data){
        Route route = new Route();

        route.setId(data.getInt(DBUtils.COL_BUS_AND_ROUTE_ROUTE_ID));
        route.setTimestamp(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_TIMESTAMP));
        route.setName(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_NAME));
        route.setOperator(data.getLong(DBUtils.COL_BUS_AND_ROUTE_ROUTE_OPERATOR));
        route.setOrigin(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_ORIGIN));
        route.setOriginLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_ORIGIN_LOCALIZED));
        route.setDestination(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_DESTINATION));
        route.setDestinationLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_DESTINATION_LOCALIZED));
        route.setLastUpdated(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_LAST_UPDATE));
        route.setNew(data.getInt(DBUtils.COL_BUS_AND_ROUTE_ROUTE_IS_NEW) != 0);

        return route;
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
        Intent intent = new Intent(this, RealTimeStopActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, busStopNumber);
        this.startActivity(intent);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String busStopNumber = marker.getTitle();

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

                snippetUi.setText(snippet);

        }
    }
}
