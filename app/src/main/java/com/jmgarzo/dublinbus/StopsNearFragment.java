package com.jmgarzo.dublinbus;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.MyItem;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.MapsUtils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopsNearFragment extends Fragment implements OnMapReadyCallback,
        ClusterManager.OnClusterItemClickListener<MyItem>,
        //
        // GoogleMap.OnInfoWindowClickListener,
//        GoogleMap.OnMarkerClickListener,
//        GoogleMap.OnMyLocationButtonClickListener,

        LoaderManager.LoaderCallbacks<Cursor> {

    private String LOG_TAG = StopsNearFragment.class.getSimpleName();

    private static final int ID_BUS_STOP_LOADER = 47;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private View mView;
    private GoogleMap mMap;
    boolean mapReady = false;
    private ArrayList<LatLng> mLatLngBusStop;

    private ArrayList<BusStop> mBusStopList;
    private BusStop markerBusStop;
    private boolean mPermissionDenied = false;
    private MapView mMapView;
    private ClusterManager<MyItem> mClusterManager;


    public StopsNearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_stops_near, container, false);


        mMapView = mView.findViewById(R.id.map_stops_near);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActivity().getSupportLoaderManager().initLoader(ID_BUS_STOP_LOADER, null, this);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mapReady = true;
        mMap = map;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.3464, -6.2618), 17));
        mMap.setOnMarkerClickListener(mClusterManager);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mClusterManager.onCameraIdle();
                readItems();
                mClusterManager.cluster();
            }
        });

        if(mClusterManager == null) {
            mClusterManager = new ClusterManager<MyItem>(getContext(), mMap);
        }
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(
                new CustomInfoWindowAdapter());

        mClusterManager.setOnClusterItemClickListener(this);

        mClusterManager.setRenderer(new BusStopRenderer(getContext(),mMap,mClusterManager));
        readItems();


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
                mLatLngBusStop = MapsUtils.getLatLngBusStop(mBusStopList);

//                SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                        .findFragmentById(R.id.map_stops_near);
                setUpMap();
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void setUpMap() {
        if (null != mBusStopList && !mBusStopList.isEmpty()) {
//            mMapView.getMapAsync(this);

        }
    }


    private void readItems() {

        LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;


        if (null != mBusStopList && !mBusStopList.isEmpty()) {
            ArrayList<MyItem> items = new ArrayList<>();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_24dp);
            mClusterManager.clearItems();

            for (BusStop busStop : mBusStopList) {
                if (bounds.contains(busStop.getLatLng())) {
                    MyItem myItem = new MyItem(Double.valueOf(busStop.getLatitude()), Double.valueOf(busStop.getLongitude()), busStop.getNumber(), busStop.getFullName(), icon);
                    items.add(myItem);
                    mClusterManager.addItem(myItem);

                }
            }
        }
    }


    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        markerBusStop = null;
        String busStopNumber = myItem.getTitle();
        for (BusStop bs : mBusStopList) {
            if (bs.getNumber().equalsIgnoreCase(busStopNumber)) {
                markerBusStop = bs;
                break;
            }
        }
        return false;
    }


    private class BusStopRenderer extends DefaultClusterRenderer<MyItem> {

        public BusStopRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }


        @Override
        protected void onBeforeClusterItemRendered(MyItem myItem, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
//            markerOptions.icon(myItem.getmIcon()).title(myItem.getTitle());

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


//        @Override
//        protected boolean shouldRenderAsCluster(Cluster cluster) {
//            // Always render clusters.
//            return cluster.getSize() > 1;
//        }
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
//        setUpMap();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
