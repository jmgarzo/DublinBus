package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.sync.DublinBusSyncUtils;
import com.jmgarzo.dublinbus.sync.services.RealTimeStopService;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;


public class RealTimeStopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int ID_REAL_TIME_STOP_LOADER = 165;

    private RecyclerView mRecyclerView;
    private RealTimeStopAdapter mRealTimeStopAdapter;
    private String mBusStopNumber;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mError;

    public RealTimeStopFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time_stop, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isFavorite) {
//                    Intent deleteFavoriteIntent = new Intent(getContext(), DeleteFromFavoriteIntentService.class);
//                    deleteFavoriteIntent.putExtra(FAVORITE_MOVIE_TAG, mMovie);
//                    getActivity().startService(deleteFavoriteIntent);
//                } else {
//                    Intent addToFavoriteIntent = new Intent(getContext(), AddFavoriteIntentService.class);
//                    addToFavoriteIntent.putExtra(FAVORITE_MOVIE_TAG, mMovie);
//                    getActivity().startService(addToFavoriteIntent);
//
//                }
            }
        });

        mError = rootView.findViewById(R.id.tv_real_time_stop_error);

        if (getActivity().getIntent() != null) {
            mBusStopNumber = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        }

        getActivity().setTitle(mBusStopNumber + " " + getString(R.string.real_time_stop_title));


        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperl_real_time_stop);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView = rootView.findViewById(R.id.recyclerview_real_time_stop);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        mRealTimeStopAdapter = new RealTimeStopAdapter(getContext());

        mRecyclerView.setAdapter(mRealTimeStopAdapter);
        refreshData();


        getActivity().getSupportLoaderManager().initLoader(ID_REAL_TIME_STOP_LOADER, null, this);


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        DublinBusSyncUtils.initialize(getContext(), mBusStopNumber);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        DublinBusSyncUtils.cancelDispach();

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
                mSwipeRefreshLayout.setRefreshing(false);
//                updateEmptyView();
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

    private void showError() {
        mRecyclerView.setVisibility(View.GONE);
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

}
