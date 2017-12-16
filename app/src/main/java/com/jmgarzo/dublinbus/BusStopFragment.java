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

import com.google.android.gms.ads.AdView;
import com.jmgarzo.dublinbus.Adapter.BusStopAdapter;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.model.BusStop;
import com.jmgarzo.dublinbus.utilities.AdUtils;
import com.jmgarzo.dublinbus.utilities.DBUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BusStopAdapter.BusStopAdapterOnClickHandler {

    private static final int ID_BUS_STOP_LOADER = 66;
    public static final String FILTER_TAG = "arg_filter_tag";
    private static final String SEARCH_VIEW_TEXT_TAG = "search_view_text_tag";

    BusStopAdapter mBusStopAdapter;
    private RecyclerView mRecyclerView;
    private String searchViewText = "";
    private AdView mAdView;

    public BusStopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            searchViewText = savedInstanceState.getString(SEARCH_VIEW_TEXT_TAG);

        }
        View rootView = inflater.inflate(R.layout.fragment_bus_stop, container, false);

        if (DBUtils.isAdmodActive(getContext())) {
            mAdView = rootView.findViewById(R.id.ad_view);
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(AdUtils.getAdRequest());
        }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_VIEW_TEXT_TAG, searchViewText);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public boolean onQueryTextChanged(String newText) {

        searchViewText = newText;
        Bundle args = new Bundle();
        args.putString(FILTER_TAG, newText);
        getLoaderManager().restartLoader(ID_BUS_STOP_LOADER, args, this);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_BUS_STOP_LOADER: {
                String orderBy = " length(" + DublinBusContract.BusStopEntry.NUMBER + "), " +
                        DublinBusContract.BusStopEntry.NUMBER + " COLLATE NOCASE ASC ";

                if (args != null) {
                    String filterArg = args.getString(FILTER_TAG);

//                    String selection = DublinBusContract.BusStopEntry.NUMBER + " LIKE '"+ filterArg + "%' ";
                    String selection = DublinBusContract.BusStopEntry.IS_NEW + " = ? AND ( " +
                            DublinBusContract.BusStopEntry.NUMBER + "  LIKE '" + filterArg + "%' " +
                            " OR " + DublinBusContract.BusStopEntry.SHORT_NAME + " LIKE '" + filterArg + "%')";


                    return new CursorLoader(getContext(),
                            DublinBusContract.BusStopEntry.CONTENT_URI,
                            DBUtils.BUS_STOP_COLUMNS,
                            selection,
                            new String[]{"0"},
                            orderBy);
                } else {
                    String selection = DublinBusContract.BusStopEntry.IS_NEW + " = ? "
                            ;
                    return new CursorLoader(getContext(),
                            DublinBusContract.BusStopEntry.CONTENT_URI,
                            DBUtils.BUS_STOP_COLUMNS,
                            selection,
                            new String[]{"0"},
                            orderBy);
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
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setActivated(true);
            searchView.setQueryHint(getString(R.string.bus_stop_search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.contains("'")){
                       newText = newText.replace("'", "");
                    }
                    if(newText.contains("%")){
                        newText = newText.replace("%", "");
                    }
                    onQueryTextChanged(newText);
                    return false;
                }
            });

            if (!searchViewText.isEmpty()) {

                searchView.setIconified(false);
                searchView.setQuery(searchViewText, false);
                searchView.onActionViewExpanded();

            }
        }
    }
}
