package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class RouteInformationService extends IntentService{

    public RouteInformationService(){
        super("RouteInformationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SyncTasks.syncRoute(this);
    }
}
