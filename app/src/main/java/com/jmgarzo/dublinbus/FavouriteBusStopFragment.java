package com.jmgarzo.dublinbus;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteBusStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FavouriteBusStopAdapter.FavouriteBusStopAdapterOnClickHandler {


    private static final int ID_BUS_FAVOURITE_STOP_LOADER = 99;
    private TextView tvMessageError;
    private AdView mAdView;



    FavouriteBusStopAdapter mFavouriteBusStopAdapter;
    private RecyclerView mRecyclerView;

    public FavouriteBusStopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootView =  inflater.inflate(R.layout.fragment_favourite_bus_stop, container, false);

        mAdView = rootView.findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);

        tvMessageError = rootView.findViewById(R.id.tv_favorite_error_message);

        LinearLayoutManager stopBusLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_bus_stop);
        mRecyclerView.setLayoutManager(stopBusLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mFavouriteBusStopAdapter = new FavouriteBusStopAdapter(getContext(), this);
        mRecyclerView.setAdapter(mFavouriteBusStopAdapter);

        getActivity().getSupportLoaderManager().initLoader(ID_BUS_FAVOURITE_STOP_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onClick(BusStop busStop) {
        Intent intent = new Intent(getActivity(), RealTimeStopActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, busStop.getNumber());
        this.startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_BUS_FAVOURITE_STOP_LOADER: {
                return new CursorLoader(getContext(),
                        DublinBusContract.BusStopEntry.CONTENT_URI,
                        DBUtils.BUS_STOP_COLUMNS,
                        DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? ",
                        new  String[]{"1"},
                        null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_BUS_FAVOURITE_STOP_LOADER: {
                if(!data.moveToFirst()){
                    showErrorMessage(getString(R.string.favorite_empty_error_message));
                    break;
                }
                showRecyclerView();
                mFavouriteBusStopAdapter.swapCursor(data);
                break;
            }
        }
    }

    private void showErrorMessage(String message){
        tvMessageError.setText(message);
        mRecyclerView.setVisibility(View.GONE);
        tvMessageError.setVisibility(View.VISIBLE);
    }
    private void showRecyclerView(){
        tvMessageError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case ID_BUS_FAVOURITE_STOP_LOADER:
                mFavouriteBusStopAdapter.swapCursor(null);
                break;
        }
    }
}
