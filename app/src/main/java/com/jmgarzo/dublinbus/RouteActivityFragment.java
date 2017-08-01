package com.jmgarzo.dublinbus;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.databinding.FragmentRouteBinding;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, RouteAdapter.RouteAdapterOnClickHandler{


    FragmentRouteBinding binding;
    private RouteAdapter mRouteAdapter;

    private static final int ID_ROUTES_LOADER = 14;



    public RouteActivityFragment() {
    }

    public interface Callback {
        public void OnItemSelected(Route route);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route, container, false);

        binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_route);

        LinearLayoutManager routeLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        binding.recyclerviewRoute.setLayoutManager(routeLayoutManager);
        binding.recyclerviewRoute.setHasFixedSize(true);

        mRouteAdapter = new RouteAdapter(getContext(), this);
        binding.recyclerviewRoute.setAdapter(mRouteAdapter);

        binding.recyclerviewRoute.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        getActivity().getSupportLoaderManager().initLoader(ID_ROUTES_LOADER, null, this);


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case ID_ROUTES_LOADER: {
                return new CursorLoader(getContext(),
                        DublinBusContract.RouteEntry.CONTENT_URI,
                        DBUtils.ROUTE_COLUMNS,
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
            case ID_ROUTES_LOADER: {
                mRouteAdapter.swapCursor(data);
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case ID_ROUTES_LOADER:
                mRouteAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onClick(Route trailer) {

        ((Callback) getActivity()).OnItemSelected(trailer);


    }
}
