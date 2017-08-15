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
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.utilities.DBUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BusStopAdapter.BusStopAdapterOnClickHandler {

    private static final int ID_BUS_STOP_LOADER = 66;
    public static final String FILTER_TAG = "arg_filter_tag";

    BusStopAdapter mBusStopAdapter;
    SearchView mSearchView;
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

        mSearchView = rootView.findViewById(R.id.search);

        mSearchView.setActivated(true);
        mSearchView.setQueryHint("Type your keyword here");
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);
        mSearchView.clearFocus();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                onQueryTextChanged(newText);
                   //mBusStopAdapter.getFilter().filter(newText);
                return false;
            }
        });








        return rootView;
    }

    public boolean onQueryTextChanged(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.

        Bundle args = new Bundle();
        args.putString(FILTER_TAG,newText);
        getLoaderManager().restartLoader(ID_BUS_STOP_LOADER,args,this);
//        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        //getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case ID_BUS_STOP_LOADER: {

                if(args != null){
                    String filterArg = args.getString(FILTER_TAG);

                    String selection = DublinBusContract.BusStopEntry.NUMBER + " LIKE '%"+filterArg+ "%' ";
                    return new CursorLoader(getContext(),
                            DublinBusContract.BusStopEntry.CONTENT_URI,
                            DBUtils.BUS_STOP_COLUMNS,
                            selection,
                            null,
                            null);
                }else {

                    return new CursorLoader(getContext(),
                            DublinBusContract.BusStopEntry.CONTENT_URI,
                            DBUtils.BUS_STOP_COLUMNS,
                            null,
                            null,
                            null);
                }
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
    public void onClick(BusStop busStop) {
        Intent intent = new Intent(getActivity(),RealTimeStopActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,busStop.getNumber());
        this.startActivity(intent);
    }
}
