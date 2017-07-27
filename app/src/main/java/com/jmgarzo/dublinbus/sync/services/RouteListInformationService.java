package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class RouteListInformationService extends IntentService {
    public RouteListInformationService(){
        super("RouteInformationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        NetworkUtilities.getRouteListInformation();
    }
}
