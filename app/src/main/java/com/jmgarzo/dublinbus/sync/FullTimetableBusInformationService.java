package com.jmgarzo.dublinbus.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class FullTimetableBusInformationService extends IntentService{

    public FullTimetableBusInformationService(){
        super("FullTimetableBusInformationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTasks.getFullTimeTableBusInformation(this);
    }
}
