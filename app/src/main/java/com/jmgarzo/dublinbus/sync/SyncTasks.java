package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 25/07/17.
 */

public class SyncTasks {

    private static String LOG_TAG = SyncTasks.class.getSimpleName();

    synchronized public static void syncOperators(Context context) {

        try {


            ArrayList<Operator> operatorList = NetworkUtilities.getOperatorInformation();

            if (operatorList != null && operatorList.size() > 0) {
                ContentValues[] contentValues = new ContentValues[operatorList.size()];
                for (int i = 0; i < operatorList.size(); i++) {
                    Operator operator = operatorList.get(i);
                    contentValues[i] = operator.getContentValues();
                }

                ContentResolver contentResolver = context.getContentResolver();

                //TODO: Mirar a ver si quiero borrar todo lo anterior antes de insertar
                contentResolver.bulkInsert(DublinBusContract.OperatorEntry.CONTENT_URI,
                        contentValues);

                //We can't update all trailer and review here because the API have a limitation.
                //syncTrailersAndReviews(context);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
