package com.jmgarzo.dublinbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RealTimeStopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_stop);
        getIntent();
    }
}
