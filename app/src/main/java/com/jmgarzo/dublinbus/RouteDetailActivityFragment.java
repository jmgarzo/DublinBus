package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmgarzo.dublinbus.objects.Route;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailActivityFragment extends Fragment {

    Route route;

    public RouteDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_route_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (null != intent) {
            route = intent.getParcelableExtra(RouteActivity.ROUTE_INTENT_TAG);
        }

        return rootView;
    }
}
