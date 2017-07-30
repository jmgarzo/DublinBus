package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.objects.RouteInformation;
import com.jmgarzo.dublinbus.utilities.DBUtils;
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


                for (int j = 0; j < routeList.size(); j++) {
                    Route route = routeList.get(j);
                    long routeId = DBUtils.getRouteId(context, route.getName(), route.getDestination());

                    ArrayList<String> stopsList = route.getStops();
                    if (stopsList != null && stopsList.size() > 0) {
                        for (int k = 0; k < stopsList.size(); k++) {
                            Long stopId = DBUtils.getBusStopId(context, stopsList.get(k));

                            ContentValues cv = new ContentValues();
                            cv.put(DublinBusContract.RouteBusStopEntry.ROUTE_ID.toString(), routeId);
                            cv.put(DublinBusContract.RouteBusStopEntry.BUS_STOP_ID, stopId);

                            contentResolver.insert(DublinBusContract.RouteBusStopEntry.CONTENT_URI, cv);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncRouteInformation(Context context) {
        try {
            ArrayList<RouteInformation> routeInformationList = NetworkUtilities.getRouteListInformation(context);

            if (routeInformationList != null && routeInformationList.size() > 0) {
                ContentValues[] contentValues = new ContentValues[routeInformationList.size()];
                for (int i = 0; i < routeInformationList.size(); i++) {
                    RouteInformation routeInformation = routeInformationList.get(i);
                    contentValues[i] = routeInformation.getContentValues();
                }

                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.bulkInsert(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                        contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
