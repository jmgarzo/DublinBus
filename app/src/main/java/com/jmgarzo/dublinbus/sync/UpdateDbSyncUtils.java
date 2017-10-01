package com.jmgarzo.dublinbus.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by jmgarzo on 24/08/17.
 */

public class UpdateDbSyncUtils {

    private static boolean sInitialized;

    private static final int SYNC_INTERVAL_SECONDS = 60 * 14  ;
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS + (60 );

    private static FirebaseJobDispatcher dispatcher;

    private static final String UPDATE_DB_SYNC_TAG="database_sync";

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context){
        Driver driver = new GooglePlayDriver(context);
        dispatcher = new FirebaseJobDispatcher(driver);


        Job syncUpdateDbJob = dispatcher.newJobBuilder()
                .setService(UpdateDbJobService.class)
                .setTag(UPDATE_DB_SYNC_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncUpdateDbJob);
    }

    synchronized public static void cancelDispach(){
        sInitialized= false;
        dispatcher.cancel(UPDATE_DB_SYNC_TAG);
    }


    synchronized public static void initialize(@NonNull final Context context){
        if(sInitialized) return;
        sInitialized= true;
        scheduleFirebaseJobDispatcherSync(context);
/*
        startImmediateSync(context);
*/
    }

    public static void startImmediateSync(@NonNull final Context context) {

//        Intent intentUpdateDbService = new Intent(context, UpdateDbJobService.class);
//        context.startService(intentUpdateDbService);
    }

}
