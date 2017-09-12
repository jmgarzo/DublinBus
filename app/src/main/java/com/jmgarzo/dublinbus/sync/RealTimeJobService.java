package com.jmgarzo.dublinbus.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by jmgar on 16/08/2017.
 */

public class RealTimeJobService extends JobService {

    private AsyncTask<String, Void, Void> mFetchRealTimeDataTask;

    @Override
    public boolean onStartJob(final JobParameters params) {

        mFetchRealTimeDataTask = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                String busStop = params[0];
                Context context = getApplicationContext();
                SyncTasks.syncRealTimeStop(context,busStop);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(params, false);
            }
        };
        Bundle b =params.getExtras();
        String stopId =b.getString(RealTimeSyncUtils.STOP_ID_TAG);
        mFetchRealTimeDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,stopId);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

