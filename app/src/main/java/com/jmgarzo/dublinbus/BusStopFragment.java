package com.jmgarzo.dublinbus;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BusStopAdapter.BusStopAdapterOnClickHandler {

    private static final int ID_BUS_STOP_LOADER = 66;

    BusStopAdapter mBusStopAdapter;
    private RecyclerView mRecyclerView;
    public BusStopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus_stop, container, false);

        LinearLayoutManager stopBusLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_bus_stop);
        mRecyclerView.setLayoutManager(stopBusLayoutManager);
        mRecyclerView.setHasFixedSize(true);

         mBusStopAdapter = new BusStopAdapter(getContext(), this);
        mRecyclerView.setAdapter(mBusStopAdapter);

        getActivity().getSupportLoaderManager().initLoader(ID_BUS_STOP_LOADER, null, this);








        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case ID_BUS_STOP_LOADER: {
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
        switch(loader.getId()) {
            case ID_BUS_STOP_LOADER: {
                mBusStopAdapter.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case ID_BUS_STOP_LOADER:
                mBusStopAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(Route route) {

    }
}
