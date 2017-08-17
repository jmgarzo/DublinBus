package com.jmgarzo.dublinbus.sync;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
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

public class DublinBusSyncUtils {

    private static boolean sInitialized;

    private static final int SYNC_INTERVAL_HOURS =3;
    private static final int SYNC_INTERVAL_SECONDS = (int) java.util.concurrent.TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

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
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        5,
                        7))
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


//        Thread checkForEmpty = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Cursor cursor = context.getContentResolver().query(
//                        PopularMovieContract.MovieEntry.CONTENT_URI,
//                        new String[]{PopularMovieContract.MovieEntry._ID},
//                        PopularMovieContract.MovieEntry.REGISTRY_TYPE + " <> ? ",
//                        new String[]{PopularMovieContract.FAVORITE_REGISTRY_TYPE},
//                        null
//                );
//
//                if(cursor == null || cursor.getCount() == 0){
//                    startImmediateSync(context);
//                }
//                cursor.close();
//            }
//        });
//        checkForEmpty.start();
        startImmediateSync(context,stopId);
    }

    public static void startImmediateSync(@NonNull final Context context, String stopId ) {

        Intent intentRealTimeStopService = new Intent(context, RealTimeStopService.class);
        intentRealTimeStopService.putExtra(Intent.EXTRA_TEXT, stopId);
        context.startService(intentRealTimeStopService);
    }
}
