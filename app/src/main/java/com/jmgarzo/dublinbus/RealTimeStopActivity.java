package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RealTimeStopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_stop);


        // Create the detail fragment and add it to the activity
        // using a fragment transaction.

        if(savedInstanceState == null ) {
            Bundle arguments = new Bundle();
            arguments.putString(Intent.EXTRA_TEXT, getIntent().getStringExtra(Intent.EXTRA_TEXT));

            RealTimeStopFragment fragment = new RealTimeStopFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.real_time_stop_activity, fragment)
                    .commit();
        }


    }
}
