package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BusStopAdapter.BusStopAdapterOnClickHandler{


    private String idRoute;

    private BusStopAdapter mBusStopAdapter;
    private RecyclerView mBusStopRecyclerView;

    private final int BUS_STOPS_LOADER_ID =223;



    public RouteDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route_detail, container, false);

        mBusStopRecyclerView =rootView.findViewById(R.id.recyclerview_bus_stop);

        LinearLayoutManager busStopLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mBusStopRecyclerView.setLayoutManager(busStopLayoutManager);
        mBusStopRecyclerView.setHasFixedSize(true);

        mBusStopAdapter = new BusStopAdapter(getContext(), this);
        mBusStopRecyclerView.setAdapter(mBusStopAdapter);

        mBusStopRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        Intent intent = getActivity().getIntent();
        if (null != intent) {
             idRoute= intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        getActivity().getSupportLoaderManager().initLoader(BUS_STOPS_LOADER_ID, null, this);

        return rootView;
    }
//        sWeatherByLocationSettingQueryBuilder.setTables(
//    WeatherContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
//    WeatherContract.LocationEntry.TABLE_NAME +
//            " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
//            "." + WeatherContract.WeatherEntry.COLUMN_LOC_KEY +
//            " = " + WeatherContract.LocationEntry.TABLE_NAME +
//            "." + WeatherContract.LocationEntry._ID);
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
                break;
                }
            }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(Route route) {

    }
}
