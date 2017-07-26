package com.jmgarzo.dublinbus.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class TimeTableBusInformationService extends IntentService {

    public TimeTableBusInformationService(){
        super("TimeTableBusInformationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTasks.getTimetableBusInformationByDate(this);
    }
}
