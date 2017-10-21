package com.jmgarzo.dublinbus;


import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.algo.PreCachingAlgorithmDecorator;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.MyItem;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.MapsUtils;
import com.jmgarzo.dublinbus.utilities.PermissionUtils;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearBusStopFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        ClusterManager.OnClusterItemClickListener<MyItem> {


    private String LOG_TAG = StopsNearFragment.class.getSimpleName();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private static final int ID_BUS_STOP_LOADER = 47;
    private ArrayList<BusStop> mBusStopList;
    private BusStop markerBusStop;


    private GoogleMap mMap;

    private ClusterManager<MyItem> mClusterManager;


    public NearBusStopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_near_bus_stop, container, false);

        getActivity().getSupportLoaderManager().initLoader(ID_BUS_STOP_LOADER, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_BUS_STOP_LOADER: {
                String orderBy = DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID +
                        ", " + DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry.NAME + " *1, " +
                        DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry.NAME + " COLLATE NOCASE ASC ";
                return new CursorLoader(
                        getContext(),
                        DublinBusContract.BusStopsAndRouteEntry.CONTENT_URI,
                        DBUtils.BUS_STOP_AND_ROUTES_COLUMNS,
                        null,
                        null,
                        orderBy//DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID
                );
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case (ID_BUS_STOP_LOADER): {
                if (null != data && data.moveToFirst()) {
                    mBusStopList = MapsUtils.getBusStopListFromBusStopsAndRoute(data);
                }
                setUpMap();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (mMap != null) {
            return;
        }
        mMap = googleMap;

//        if (null != getLocation()) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocation(),17));
//        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.3464, -6.2618), 17));

//        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        //Location


        mClusterManager = new ClusterManager<MyItem>(getContext(), mMap);

//        googleMap.setOnMarkerClickListener(this);
        mClusterManager.setRenderer(new BusStopRenderer(getContext(), mMap, mClusterManager));

        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new CustomInfoWindowAdapter());

        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(mClusterManager);

        readItems();
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

//       mClusterManager.setOnClusterItemClickListener(this);
//        GridBasedAlgorithm<MyItem> gridAlgorithm = new GridBasedAlgorithm<MyItem>();
//        NonHierarchicalDistanceBasedAlgorithm nonHierarchicalDistanceBasedAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();
//        mClusterManager.setAlgorithm(new PreCachingAlgorithmDecorator<MyItem>(nonHierarchicalDistanceBasedAlgorithm));
    }


    private void setUpMap() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.near_bus_stops_map)).getMapAsync(this);
    }

    private void readItems() {


        if (null != mBusStopList && !mBusStopList.isEmpty()) {
            ArrayList<MyItem> items = new ArrayList<>();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp);
            mClusterManager.clearItems();
            for (BusStop busStop : mBusStopList) {
                MyItem myItem = new MyItem(Double.valueOf(busStop.getLatitude()), Double.valueOf(busStop.getLongitude()), busStop.getNumber(), busStop.getFullName(), icon);
                items.add(myItem);
                mClusterManager.addItem(myItem);
            }
        }
    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        return false;
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
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getContext(), LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);


        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

        }
    }

    private LatLng getLocation() {

        LocationManager locationManager = (LocationManager)
                getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = null;
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            return new LatLng(location.getLatitude(), location.getLongitude());

        } else {
            return null;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
            onResume();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getActivity().getSupportFragmentManager(), "dialog");
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private class BusStopRenderer extends DefaultClusterRenderer<MyItem> {


        public BusStopRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }


        @Override
        protected void onBeforeClusterItemRendered(MyItem myItem, MarkerOptions markerOptions) {
            markerOptions.position(myItem.getPosition())
                    .title(myItem.getTitle())
                    .snippet(myItem.getSnippet())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp));
            super.onBeforeClusterItemRendered(myItem, markerOptions);

        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_map_window, null);
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
