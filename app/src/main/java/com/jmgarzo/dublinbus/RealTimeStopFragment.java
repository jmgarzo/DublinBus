package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.RealTimeStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.sync.RealTimeSyncUtils;
import com.jmgarzo.dublinbus.sync.services.AddFavouriteBusStopService;
import com.jmgarzo.dublinbus.sync.services.DeleteFromFavoriteBusStopService;
import com.jmgarzo.dublinbus.sync.services.DeleteRealTimeStopBusService;
import com.jmgarzo.dublinbus.sync.services.RealTimeStopService;
import com.jmgarzo.dublinbus.utilities.AdUtils;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;


public class RealTimeStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        RealTimeStopAdapter.RealTimeStopAdapterOnClickHandler{

    private static final int ID_REAL_TIME_STOP_LOADER = 16;
    private static final int ID_FAB_FAVOURITE_FAB_LOADER = 34;
    public static final String FAVORITE_BUS_STOP_TAG = "favourite_bus_stop tag";



    private RecyclerView mRecyclerView;
    private RealTimeStopAdapter mRealTimeStopAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mBusStopNumber;
    private TextView mError;
    private FloatingActionButton fab;
    private boolean isFavorite;
    private ProgressBar mProgressBar;
    private AdView mAdView;

    public RealTimeStopFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time_stop, container, false);
        setHasOptionsMenu(true);

        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_real_time);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);

        showProgressBar();
        if(DBUtils.isAdmodActive(getContext())) {
            mAdView = rootView.findViewById(R.id.ad_view);
            mAdView.setVisibility(View.VISIBLE);
            mAdView.loadAd(AdUtils.getAdRequest());
        }

        isFavorite = false;
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    Intent deleteFavouriteBusStop = new Intent(getContext(), DeleteFromFavoriteBusStopService.class);
                    deleteFavouriteBusStop.putExtra(FAVORITE_BUS_STOP_TAG, mBusStopNumber);
                    getActivity().startService(deleteFavouriteBusStop);
                } else {
                    Intent addToFavoriteIntent = new Intent(getContext(), AddFavouriteBusStopService.class);
                    addToFavoriteIntent.putExtra(FAVORITE_BUS_STOP_TAG, mBusStopNumber);
                    getActivity().startService(addToFavoriteIntent);

                }
            }
        });
        mError = rootView.findViewById(R.id.tv_real_time_stop_error);

        if (getActivity().getIntent() != null) {
            mBusStopNumber = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        }

        getActivity().setTitle(getString(R.string.real_time_stop_title) + " " + (mBusStopNumber));


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRealTimeStopAdapter = new RealTimeStopAdapter(getContext(),this);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_real_time_stop);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mRecyclerView.setAdapter(mRealTimeStopAdapter);
        refreshData();

        getActivity().getSupportLoaderManager().initLoader(ID_FAB_FAVOURITE_FAB_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }
        getActivity().getSupportLoaderManager().initLoader(ID_REAL_TIME_STOP_LOADER, null, this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        RealTimeSyncUtils.initialize(getContext(), mBusStopNumber);
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        RealTimeSyncUtils.cancelDispach();
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent intentDeleteRealTimeStopService = new Intent(getContext(), DeleteRealTimeStopBusService.class);
        getContext().startService(intentDeleteRealTimeStopService);

    }

    private void refreshData() {
        //TODO: code for mBusStopNumber == null -> show a message
        DBUtils.setRealTimeConnectionStatus(getContext(), DBUtils.REAL_TIME_STATUS_SUCCCESS);
        Intent intentRealTimeStopService = new Intent(getContext(), RealTimeStopService.class);
        intentRealTimeStopService.putExtra(Intent.EXTRA_TEXT, mBusStopNumber);
        getContext().startService(intentRealTimeStopService);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_REAL_TIME_STOP_LOADER: {
                showProgressBar();
                return new CursorLoader(getContext(),
                        DublinBusContract.RealTimeStopEntry.CONTENT_URI,
                        DBUtils.REAL_TIME_STOP_COLUMNS,
                        null,
                        null,
                        null);
            }

            case ID_FAB_FAVOURITE_FAB_LOADER: {

                String selection = DublinBusContract.BusStopEntry.NUMBER + "= ? AND "
                        + DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? ";
                return new CursorLoader(getActivity(),
                        DublinBusContract.BusStopEntry.CONTENT_URI,
                        DBUtils.BUS_STOP_COLUMNS,
                        selection,
                        new String[]{mBusStopNumber, "1"},
                        null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_REAL_TIME_STOP_LOADER: {
                mSwipeRefreshLayout.setRefreshing(false);
                mRealTimeStopAdapter.swapCursor(data);
                if (data != null && data.moveToFirst()) {
                    hideProgressBar();
                    showRecyclerView();
                }
                break;
            }
            case ID_FAB_FAVOURITE_FAB_LOADER: {
                if (data.moveToFirst()) {
                    isFavorite = true;
                    fab.setImageResource(R.drawable.ic_favorite_white_24px);
                } else {
                    isFavorite = false;
                    fab.setImageResource(R.drawable.ic_favorite_border_white_24px);
                }
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

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void showError() {
        mRecyclerView.setVisibility(View.GONE);
        hideProgressBar();
        mError.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mError.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void updateEmptyView() {

        if (DBUtils.REAL_TIME_STATUS_SUCCCESS != DBUtils.getRealTimeConnectionStatus(getContext())) {
            showError();
            mError.setText(NetworkUtilities.getErrorMessage(getContext()));
        } else {
            showRecyclerView();
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateEmptyView();
    }

    @Override
    public void onClick(RealTimeStop realTimeStop) {
//        Intent intent = new Intent(getContext(), RouteMapsActivity.class);
//        intent.putExtra(FAVOURITE_ROUTE_EXTRA_TAG, route);
//        startActivity(intent);
    }


}
