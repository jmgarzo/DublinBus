package com.jmgarzo.dublinbus;

import android.content.Intent;
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
import com.jmgarzo.dublinbus.sync.services.RealTimeStopService;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.RealTimeStopAdapter;


public class RealTimeStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_REAL_TIME_STOP_LOADER = 165;

    private RecyclerView mRecyclerView;
    private RealTimeStopAdapter mRealTimeStopAdapter;

    public RealTimeStopFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time_stop, container, false);


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_real_time_stop);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRealTimeStopAdapter = new RealTimeStopAdapter(getContext());
        mRecyclerView.setAdapter(mRealTimeStopAdapter);

        Intent intentRealTimeStopService = new Intent(getContext(), RealTimeStopService.class);
        intentRealTimeStopService.putExtra(Intent.EXTRA_TEXT,"4747");
        getContext().startService(intentRealTimeStopService);

        getActivity().getSupportLoaderManager().initLoader(ID_REAL_TIME_STOP_LOADER, null, this);


        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_REAL_TIME_STOP_LOADER: {
                return new CursorLoader(getContext(),
                        DublinBusContract.RealTimeStopEntry.CONTENT_URI,
                        DBUtils.REAL_TIME_STOP_COLUMNS,
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
            case ID_REAL_TIME_STOP_LOADER: {
                mRealTimeStopAdapter.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        switch (loader.getId()) {
            case ID_REAL_TIME_STOP_LOADER:
                mRealTimeStopAdapter.swapCursor(null);
                break;
        }
    }

}
