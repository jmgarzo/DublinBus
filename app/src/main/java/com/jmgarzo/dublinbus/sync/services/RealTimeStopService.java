package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 14/08/2017.
 */

public class RealTimeStopService extends IntentService {

    public RealTimeStopService() {
        super("RealTimeStopService");
    }

    public RealTimeStopService(String stopId) {
        super("RealTimeStopService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String stopid = null;
        if (intent.getExtras() != null) {
            stopid = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        SyncTasks.syncRealTimeStop(this, stopid);

    }
}
