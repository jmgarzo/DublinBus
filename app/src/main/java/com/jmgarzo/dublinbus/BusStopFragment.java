package com.jmgarzo.dublinbus;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private RecyclerView mRecyclerView;

    public BusStopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus_stop, container, false);

        getActivity().setTitle(getString(R.string.bus_stop_title));
        setHasOptionsMenu(true);

        LinearLayoutManager stopBusLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_bus_stop);
        mRecyclerView.setLayoutManager(stopBusLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mBusStopAdapter = new BusStopAdapter(getContext(), this);
        mRecyclerView.setAdapter(mBusStopAdapter);

        getActivity().getSupportLoaderManager().initLoader(ID_BUS_STOP_LOADER, null, this);

        return rootView;
    }

    public boolean onQueryTextChanged(String newText) {

        Bundle args = new Bundle();
        args.putString(FILTER_TAG, newText);
        getLoaderManager().restartLoader(ID_BUS_STOP_LOADER, args, this);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_BUS_STOP_LOADER: {
                String[] selectionArgs= new String[]{"0"};

                if (args != null) {
                    String filterArg = args.getString(FILTER_TAG);
//                    String selection = DublinBusContract.BusStopEntry.NUMBER + " LIKE '"+ filterArg + "%' ";
                    String selection = DublinBusContract.BusStopEntry.IS_FAVOURITE +" = ?  AND " +DublinBusContract.BusStopEntry.NUMBER + " LIKE '" + filterArg + "%' " +
                    " OR " + DublinBusContract.BusStopEntry.SHORT_NAME + " LIKE '" + filterArg + "%' ";

                    return new CursorLoader(getContext(),
                            DublinBusContract.BusStopEntry.CONTENT_URI,
                            DBUtils.BUS_STOP_COLUMNS,
                            selection,
                            selectionArgs,
                            null);
                } else {
                    return new CursorLoader(getContext(),
                            DublinBusContract.BusStopEntry.CONTENT_URI,
                            DBUtils.BUS_STOP_COLUMNS,
                            DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? ",
                            selectionArgs,
                            null);
                }
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_BUS_STOP_LOADER: {
                mBusStopAdapter.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ID_BUS_STOP_LOADER:
                mBusStopAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(BusStop busStop) {
        Intent intent = new Intent(getActivity(), RealTimeStopActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, busStop.getNumber());
        this.startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bus_stop_fragment, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
//        mSearchView = rootView.findViewById(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setActivated(true);
        searchView.setQueryHint(getString(R.string.bus_stop_search_hint));
        searchView.onActionViewExpanded();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextChanged(newText);
                return false;
            }
        });


    }
}
