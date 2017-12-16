package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.RealTimeStopFragment;
import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 19/08/17.
 */

public class DeleteFromFavoriteBusStopService extends IntentService{
    public DeleteFromFavoriteBusStopService() {
        super("DeleteFromFavoriteBusStopService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
         String busStopNumber= intent.getStringExtra(RealTimeStopFragment.FAVORITE_BUS_STOP_TAG);
        SyncTasks.deleteFavoriteBusStop(this,busStopNumber);
    }
}
