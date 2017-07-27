package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class BusStopInformationService extends IntentService {

    public BusStopInformationService(){
        super("BusStopInformationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        SyncTasks.getBusStopInformation();
    }
}
