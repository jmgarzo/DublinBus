package com.jmgarzo.dublinbus.sync;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.jmgarzo.dublinbus.sync.services.RealTimeStopService;

/**
 * Created by jmgar on 16/08/2017.
 */

public class RealTimeSyncUtils {

    private static boolean sInitialized;

    private static final int SYNC_INTERVAL_SECONDS = 5;
    private static final int SYNC_FLEXTIME_SECONDS = 8;

    private static FirebaseJobDispatcher dispatcher;


    public static final String STOP_ID_TAG = "stop_id";
    private static final String REAL_TIME_SYNC_TAG="real_time_sync";

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context,String stopId){
        Driver driver = new GooglePlayDriver(context);
        dispatcher = new FirebaseJobDispatcher(driver);

        Bundle b = new Bundle();
        b.putString(STOP_ID_TAG,stopId);
        Job syncRealTimeJob = dispatcher.newJobBuilder()
                .setService(RealTimeJobService.class)
                .setExtras(b)
                .setTag(REAL_TIME_SYNC_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncRealTimeJob);
    }

    synchronized public static void cancelDispach(){
        sInitialized= false;
        dispatcher.cancel(REAL_TIME_SYNC_TAG);
    }


    synchronized public static void initialize(@NonNull final Context context,String stopId){
        if(sInitialized) return;
        sInitialized= true;
        scheduleFirebaseJobDispatcherSync(context,stopId);
        startImmediateSync(context,stopId);
    }

    public static void startImmediateSync(@NonNull final Context context, String stopId ) {

        Intent intentRealTimeStopService = new Intent(context, RealTimeStopService.class);
        intentRealTimeStopService.putExtra(Intent.EXTRA_TEXT, stopId);
        context.startService(intentRealTimeStopService);
    }
}
