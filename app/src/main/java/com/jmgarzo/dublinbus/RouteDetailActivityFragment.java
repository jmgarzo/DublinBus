package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteDetailActivityFragment extends Fragment {

    String idRoute;



    public RouteDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_route_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (null != intent) {
             idRoute= intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        return rootView;
    }
}
