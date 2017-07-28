package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.Route;
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncBusStops(Context context) {
        try {
            ArrayList<BusStop> busStopList = NetworkUtilities.getBusStopInformation();

            if (busStopList != null && busStopList.size() > 0) {
                ContentValues[] contentValues = new ContentValues[busStopList.size()];
                for (int i = 0; i < busStopList.size(); i++) {
                    BusStop busStop = busStopList.get(i);
                    contentValues[i] = busStop.getContentValues();
                }

                ContentResolver contentResolver = context.getContentResolver();

                //TODO: Mirar a ver si quiero borrar todo lo anterior antes de insertar
                contentResolver.bulkInsert(DublinBusContract.BusStopEntry.CONTENT_URI,
                        contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncRoute(Context context) {
        try {
            ArrayList<Route> routeList = NetworkUtilities.getRouteInformation(context);

            if (routeList != null && routeList.size() > 0) {
                ContentValues[] contentValues = new ContentValues[routeList.size()];
                for (int i = 0; i < routeList.size(); i++) {
                    Route route = routeList.get(i);
                    contentValues[i] = route.getContentValues();
                }

                ContentResolver contentResolver = context.getContentResolver();

                //TODO: Mirar a ver si quiero borrar todo lo anterior antes de insertar
                contentResolver.bulkInsert(DublinBusContract.RouteEntry.CONTENT_URI,
                        contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
