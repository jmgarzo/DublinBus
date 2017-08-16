package com.jmgarzo.dublinbus;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopsNearFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        StopNearAdapter.StopNearAdapterOnClickHandler {

    private String LOG_TAG = StopsNearFragment.class.getSimpleName();

    private static final int ID_BUS_STOP_LOADER = 47;


    private List<BusStop> busStopList;


    public StopsNearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_stops_near, container, false);


        getActivity().getSupportLoaderManager().initLoader(ID_BUS_STOP_LOADER, null, this);


        return viewRoot;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case (ID_BUS_STOP_LOADER): {
                return new CursorLoader(getContext(),
                        DublinBusContract.BusStopEntry.CONTENT_URI,
                        DBUtils.BUS_STOP_COLUMNS,
                        null,
                        null,
                        null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case (ID_BUS_STOP_LOADER): {
                if (null != data && data.moveToFirst()) {
                    busStopList = new ArrayList<>();
                    do {
                        BusStop busStop = new BusStop();
                        busStop.cursorToBusStop(data);
                        busStopList.add(busStop);
                    } while (data.moveToNext());
                    Log.i(LOG_TAG, busStopList.toString());
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(BusStop busStop) {

    }
}
