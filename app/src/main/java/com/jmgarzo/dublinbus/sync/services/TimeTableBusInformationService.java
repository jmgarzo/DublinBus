package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class TimeTableBusInformationService extends IntentService {

    public TimeTableBusInformationService(){
        super("TimeTableBusInformationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        NetworkUtilities.getTimetableBusInformationByDate(this);
    }
}
