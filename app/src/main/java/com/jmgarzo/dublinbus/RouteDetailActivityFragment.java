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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BusStopAdapter.BusStopAdapterOnClickHandler {


//    private String idRoute;
      private Route mRoute;


    private BusStopAdapter mBusStopAdapter;
    private RecyclerView mBusStopRecyclerView;
    private AdView mAdView;



    private final int BUS_STOPS_LOADER_ID = 223;
    private final int ROUTE_BUS_STOP_LOADER_ID = 432;
    public static final String BUS_STOP_LIST_EXTRA_TAG = "bus_top_list_tag";
    public static final String ROUTE_EXTRA_TAG = "route_extra_tag";
    private ArrayList<BusStop> mBusStopsList;
    private ArrayList<Route> mRoutesList;



    public RouteDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route_detail, container, false);

        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.route_detail_activity_fragment_title));

        mAdView = rootView.findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);



        FloatingActionButton fab =  rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RouteMapsActivity.class);
                intent.putExtra(BUS_STOP_LIST_EXTRA_TAG, mBusStopsList);
                intent.putExtra(ROUTE_EXTRA_TAG,mRoute);
                startActivity(intent);

            }
        });
        Intent intent = getActivity().getIntent();
        if (null != intent) {

             mRoute = intent.getParcelableExtra(RouteActivityFragment.ROUTE_EXTRA_TAG);
            getActivity().setTitle(getString(R.string.route_detail_activity_fragment_title)
                    + " " + mRoute.getName());
        }

        mBusStopRecyclerView = rootView.findViewById(R.id.recyclerview_bus_stop);

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
                        Long.toString(mRoute.getId()));
                return new CursorLoader(getActivity(),
                        stopBusWithRouteIdUri,
                        DBUtils.BUS_STOP_COLUMNS,
                        null,
                        null,
                        DublinBusContract.RouteBusStopEntry.RECORD_ORDER);
            }
            case ROUTE_BUS_STOP_LOADER_ID:{
                return new CursorLoader(
                        getActivity(),
                        DublinBusContract.RoutesPerBusStopEntry.CONTENT_URI,
                        DBUtils.ROUTE_PER_BUS_STOP_COLUMNS,
                        DublinBusContract.BusStopEntry.TABLE_NAME+ "." +DublinBusContract.BusStopEntry._ID + " = ? ",
                        new String[]{Long.toString(mRoute.getId())},
                        null);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case BUS_STOPS_LOADER_ID: {
                mBusStopAdapter.swapCursor(data);
                if (data.moveToFirst()) {
                    mBusStopsList = cursorToBusStopList(data);
                }
//                getActivity().getSupportLoaderManager().initLoader(ROUTE_BUS_STOP_LOADER_ID, null, this);
                break;
            }
//            case ROUTE_BUS_STOP_LOADER_ID:{
//
//                if(null!=data && data.moveToFirst()){
//                    mRoutesList = new ArrayList<>();
//                    do {
//                        Route route = new Route();
//                        route.setName(data.getString(DBUtils.COL_ROUTE_PER_BUS_STOP_NAME));
//                        route.setOrigin(data.getString(DBUtils.COL_ROUTE_PER_BUS_STOP_ORIGIN));
//                        route.setOriginLocalized(data.getString(DBUtils.COL_ROUTE_PER_BUS_STOP_ORIGIN_LOCALIZED));
//                        route.setDestination(data.getString(DBUtils.COL_ROUTE_PER_BUS_STOP_DESTINATION));
//                        route.setDestinationLocalized(data.getString(DBUtils.COL_ROUTE_PER_BUS_STOP_DESTINATION_LOCALIZED));
//                        mRoutesList.add(route);
//                    }while(data.moveToNext());
//
//                }
//                break;
//            }
        }
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(BusStop busStop) {

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


}
