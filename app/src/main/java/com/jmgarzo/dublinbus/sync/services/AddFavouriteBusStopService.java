package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.RealTimeStopFragment;
import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 19/08/17.
 */

public class AddFavouriteBusStopService extends IntentService {

    public AddFavouriteBusStopService() {
        super("AddFavouriteBusStopService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
         String  busStopNumber= intent.getStringExtra(RealTimeStopFragment.FAVORITE_BUS_STOP_TAG);
        SyncTasks.addFavoriteBusStop(this,busStopNumber);
    }
}
