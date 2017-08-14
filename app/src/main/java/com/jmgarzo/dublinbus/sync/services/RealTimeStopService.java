package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 14/08/2017.
 */

public class RealTimeStopService extends IntentService {
    String mStopID;
    public RealTimeStopService(String stopId){
        super("RealTimeStopService");
        mStopID = stopId;
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTasks.syncRealTimeStop(this,mStopID);

    }
}
