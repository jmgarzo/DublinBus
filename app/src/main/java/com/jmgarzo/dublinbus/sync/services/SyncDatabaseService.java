package com.jmgarzo.dublinbus.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jmgarzo.dublinbus.sync.SyncTasks;

/**
 * Created by jmgarzo on 29/08/17.
 */

public class SyncDatabaseService extends IntentService {
    public SyncDatabaseService() {
        super("SyncDatabaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTasks.syncDB(this);

    }
}
