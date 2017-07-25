package com.jmgarzo.dublinbus;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.sync.NetworkUtils;

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

        String sAnswer = NetworkUtils.getRealTimeBusInformation(getContext());
        tvAnswer.setText(sAnswer);

        return rootView;
    }
}
