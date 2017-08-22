package com.jmgarzo.dublinbus;


import android.database.Cursor;
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
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RouteAdapter.RouteAdapterOnClickHandler {

    private RouteAdapter mRouteAdapter;
    private RecyclerView recyclerView;

    private static final int ID_ROUTES_LOADER = 14;


    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_route2, container, false);

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
        return null;    }

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
    public void onClick(Route route) {

    }




}
