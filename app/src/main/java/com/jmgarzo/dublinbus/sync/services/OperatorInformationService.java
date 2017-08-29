package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 26/07/17.
 */

public class OperatorInformationService extends IntentService {

    public OperatorInformationService(){
        super("OperatorInformationService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTasks.getSyncOperators(this);
    }
}
