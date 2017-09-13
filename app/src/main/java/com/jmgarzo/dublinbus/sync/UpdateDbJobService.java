package com.jmgarzo.dublinbus.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.Voice;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.jmgarzo.dublinbus.sync.SyncTasks;

import java.util.concurrent.Executor;

/**
 * Created by jmgarzo on 24/08/17.
 */

public class UpdateDbJobService extends JobService {
    private AsyncTask<Void, Void, Void> mUpdateDataBaseTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        mUpdateDataBaseTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                SyncTasks.syncDB(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(params,false);
            }
        };
        //mUpdateDataBaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        mUpdateDataBaseTask.execute();

        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
