package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 05/09/17.
 */

public class DeleteRealTimeStopBusService extends IntentService {
    public DeleteRealTimeStopBusService() {
        super("DeleteRealTimeStopBusService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTasks.deleteRealTimeBusStop(this);
    }
}
