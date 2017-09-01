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
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.AdUtils;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RouteAdapter.RouteAdapterOnClickHandler{


    private RouteAdapter mRouteAdapter;
    private RecyclerView recyclerView;
    private AdView mAdView;

    private static final String ROUTE_FILTER_TAG = "route_filter_tag";
    public static final String ROUTE_EXTRA_TAG = "route_extra_tag";


    private static final String SEARCH_VIEW_TEXT_TAG="search_view_text_tag";
    private String searchViewText = "";

    private static final int ID_ROUTES_LOADER = 14;


    public RouteActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route, container, false);

        mAdView = rootView.findViewById(R.id.ad_view);

        mAdView.loadAd(AdUtils.getAdRequest());

        if(savedInstanceState!=null){
            searchViewText = savedInstanceState.getString(SEARCH_VIEW_TEXT_TAG);
        }

       // getActivity().setTitle(getString(R.string.route_title));
        setHasOptionsMenu(true);
        LinearLayoutManager routeLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView = rootView.findViewById(R.id.recyclerview_route);

        recyclerView.setLayoutManager(routeLayoutManager);
        recyclerView.setHasFixedSize(true);

        mRouteAdapter = new RouteAdapter(getContext(), this);
        recyclerView.setAdapter(mRouteAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));


        getActivity().getSupportLoaderManager().initLoader(ID_ROUTES_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_VIEW_TEXT_TAG, searchViewText);
        super.onSaveInstanceState(outState);
    }


    public boolean onQueryTextChanged(String newText) {
        searchViewText = newText;
        Bundle args = new Bundle();
        args.putString(ROUTE_FILTER_TAG, newText);
        getLoaderManager().restartLoader(ID_ROUTES_LOADER, args, this);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_route_fragment, menu);
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
        searchView.setQueryHint(getString(R.string.route_search_hint));

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

        if (!searchViewText.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(searchViewText, false);
            searchView.onActionViewExpanded();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ID_ROUTES_LOADER: {
                if(args!=null) {
                    String filterArg = args.getString(ROUTE_FILTER_TAG);
                    String selection = DublinBusContract.RouteEntry.IS_NEW  + " = ? AND " +
                            DublinBusContract.RouteEntry.NAME + " LIKE '" + filterArg + "%' " +
                            " OR " + DublinBusContract.RouteEntry.ORIGIN + " LIKE '" + filterArg + "%' " +
                            " OR " + DublinBusContract.RouteEntry.DESTINATION + " LIKE '" + filterArg + "%' ";
                    return new CursorLoader(getContext(),
                            DublinBusContract.RouteEntry.CONTENT_URI,
                            DBUtils.ROUTE_COLUMNS,
                            selection,
                            new String[]{"0"},
                            null);
                }else{
                    return new CursorLoader(getContext(),
                            DublinBusContract.RouteEntry.CONTENT_URI,
                            DBUtils.ROUTE_COLUMNS,
                            DublinBusContract.RouteEntry.IS_NEW + " = ? ",
                            new String[]{"0"},
                            null);
                }
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case ID_ROUTES_LOADER: {
                mRouteAdapter.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        switch (loader.getId()) {
            case ID_ROUTES_LOADER:
                mRouteAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(Route route) {
        Intent intent = new Intent(getContext(),RouteDetailActivity.class);
        intent.putExtra(ROUTE_EXTRA_TAG,route);
        startActivity(intent);
    }

}
