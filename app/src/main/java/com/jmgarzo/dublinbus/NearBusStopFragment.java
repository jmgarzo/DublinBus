package com.jmgarzo.dublinbus;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.MapsUtils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearBusStopFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener,
        ClusterManager.OnClusterItemClickListener<MyItem>{


    private String LOG_TAG = StopsNearFragment.class.getSimpleName();

    private static final int ID_BUS_STOP_LOADER = 47;
    private ArrayList<BusStop> mBusStopList;

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
        googleMap.clear();
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.3464, -6.2618), 17));
        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<MyItem>(getContext(), mMap);
        }
        mClusterManager.setOnClusterItemClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        mClusterManager.setRenderer(new BusStopRenderer(getContext(),mMap,mClusterManager));

        mMap.setOnCameraIdleListener(this);
        readItems();
    }

    private void setUpMap() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.near_bus_stops_map)).getMapAsync(this);
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
            mClusterManager.cluster();
        }
    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        Toast.makeText(getContext(),"Click Item ",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onCameraIdle() {
        readItems();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getContext(),"Click Item ",Toast.LENGTH_LONG).show();

        return false;
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


}
