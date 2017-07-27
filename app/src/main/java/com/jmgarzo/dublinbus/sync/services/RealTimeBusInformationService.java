package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 25/07/17.
 */

public class RealTimeBusInformationService extends IntentService {

    public RealTimeBusInformationService(){
        super("RealTimeBusInformationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        SyncTasks.getRealTimeBusInformation(this);

    }
}
