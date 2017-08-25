package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BusStopAdapter.BusStopAdapterOnClickHandler{


    private String idRoute;

    private BusStopAdapter mBusStopAdapter;
    private RecyclerView mBusStopRecyclerView;

    private final int BUS_STOPS_LOADER_ID =223;
    public static final String BUS_STOP_LIST_TAG = "bus_top_list_tag";
    private ArrayList<LatLng> busStopList;



    public RouteDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route_detail, container, false);

        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.route_detail_activity_fragment_title));



        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),RouteMapsActivity.class);
                intent.putExtra(BUS_STOP_LIST_TAG,busStopList);
                startActivity(intent);

            }
        });
        Intent intent = getActivity().getIntent();
        if (null != intent) {

            Route route = intent.getParcelableExtra(RouteFragment.ROUTE_EXTRA_TAG);
            idRoute= Long.toString(route.getId());
            route.getName();
            getActivity().setTitle(getString(R.string.route_detail_activity_fragment_title)
            +" " + getString(R.string.route_label) + route.getName());


        }

        mBusStopRecyclerView =rootView.findViewById(R.id.recyclerview_bus_stop);

        LinearLayoutManager busStopLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mBusStopRecyclerView.setLayoutManager(busStopLayoutManager);
        mBusStopRecyclerView.setHasFixedSize(true);

        mBusStopAdapter = new BusStopAdapter(getContext(), this);
        mBusStopRecyclerView.setAdapter(mBusStopAdapter);

        mBusStopRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));



        getActivity().getSupportLoaderManager().initLoader(BUS_STOPS_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case BUS_STOPS_LOADER_ID: {

                Uri stopBusWithRouteIdUri = DublinBusContract.BusStopEntry.buildBusStopWithRouteId(
                        idRoute);

                return new CursorLoader(getActivity(),
                        stopBusWithRouteIdUri,
                        DBUtils.BUS_STOP_COLUMNS,
                        null,
                        null,
                        DublinBusContract.RouteBusStopEntry.RECORD_ORDER);
            }

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case BUS_STOPS_LOADER_ID:{
                mBusStopAdapter.swapCursor(data);
                if(data.moveToFirst()){
                    busStopList = cursorToLatLongList(data);
                }
                break;
                }
            }
    }

    private ArrayList<LatLng> cursorToLatLongList(Cursor data){
        ArrayList<LatLng> routeList = new ArrayList<>();
        do{
            Double lat = Double.valueOf(data.getString(DBUtils.COL_BUS_STOP_LATITUDE));
            Double lon = Double.valueOf(data.getString(DBUtils.COL_BUS_STOP_LONGITUDE));
            LatLng latLng = new LatLng(lat,lon);
            routeList.add(latLng);
        }while (data.moveToNext());
        return routeList;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(BusStop busStop) {

    }


}
