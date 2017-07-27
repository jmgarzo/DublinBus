package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.sync.services.OperatorInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteListInformationService;

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

//        Intent intentRealTimeBusInformation = new Intent(getActivity(), RealTimeBusInformationService.class);
//        getContext().startService(intentRealTimeBusInformation);
        //tvAnswer.setText(sAnswer);

//        Intent intentTimeTableBusInformation = new Intent(getActivity(), TimeTableBusInformationService.class);
//        getContext().startService(intentTimeTableBusInformation);


//        Intent intentFullTimetableBusInformation = new Intent(getActivity(), FullTimetableBusInformationService.class);
//        getContext().startService(intentFullTimetableBusInformation);

//        Intent intentBusStopInformationService = new Intent(getActivity(), BusStopInformationService.class);
//        getContext().startService(intentBusStopInformationService);

//        Intent intentRouteInformationService = new Intent(getActivity(), RouteInformationService.class);
//        getContext().startService(intentRouteInformationService);

        Intent intentOperatorInformationService = new Intent(getActivity(), OperatorInformationService.class);
        getContext().startService(intentOperatorInformationService);

//        Intent intentRouteListInformationService = new Intent(getActivity(), RouteListInformationService.class);
//        getContext().startService(intentRouteListInformationService);


        return rootView;
    }
}
