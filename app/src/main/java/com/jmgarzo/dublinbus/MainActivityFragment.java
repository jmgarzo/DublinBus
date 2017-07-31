package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.sync.SyncTasks;
import com.jmgarzo.dublinbus.sync.services.BusStopInformationService;
import com.jmgarzo.dublinbus.sync.services.OperatorInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteListInformationService;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TextView tvAnswer;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        tvAnswer = getActivity().findViewById(R.id.tv_prueba);
        SyncTasks.syncDB(getContext());


        return rootView;
    }

}
