package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.jmgarzo.dublinbus.R;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.data.DublinBusDBHelper;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.RealTimeStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.objects.RouteInformation;
import com.jmgarzo.dublinbus.sync.services.BusStopInformationService;
import com.jmgarzo.dublinbus.sync.services.OperatorInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteListInformationService;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

import java.io.IOException;
import java.util.ArrayList;

import static android.util.Log.e;

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
                DBUtils.setIsFilledOperatorInformation(context, false);
                int deleted = contentResolver.delete(DublinBusContract.OperatorEntry.CONTENT_URI, null, null);
                Log.d(LOG_TAG, deleted + " Operators Deleted");

                int inserted = contentResolver.bulkInsert(DublinBusContract.OperatorEntry.CONTENT_URI,
                        contentValues);
                if (inserted > 0) {
                    DBUtils.setIsFilledOperatorInformation(context, true);
                }
            }
        } catch (Exception e) {
            e(LOG_TAG, e.toString());
        }
    }



    synchronized public static ArrayList<ContentValues> syncBusStops(Context context) {
        ArrayList<ContentValues> busStopCVList = null;

        try {
            ArrayList<BusStop> busStopList = NetworkUtilities.getBusStopInformation();

            if (busStopList != null && busStopList.size() > 0) {
                busStopCVList = new ArrayList<>();
                for (int i = 0; i < busStopList.size(); i++) {
                    BusStop busStop = busStopList.get(i);
                    busStopCVList.add(busStop.getContentValues());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busStopCVList;
    }




    synchronized public static ArrayList<Route> syncRoute(Context context) {
        ArrayList<Route> totalRouteList = new ArrayList<>();

        try {

            Cursor cursor = context.getContentResolver().query(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                    DBUtils.ROUTE_INFORMATION_COLUMNS,
                    DublinBusContract.RouteInformationEntry.OPERATOR + " = ? ",
                    new String[]{context.getString(R.string.constant_dublin_bus_operator)},
                    null);


            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ArrayList<Route> routeList = NetworkUtilities.getRouteInformation(context, cursor.getString(DBUtils.COL_ROUTE_INFORMATION_ROUTE));

                    //TODO: DELETE! JUST FOR DEBUG
                    if (routeList.size() > 2) {
                        Log.e(LOG_TAG, "Route:" + routeList.get(1).getName() + " Have " + routeList.size() + " results");
                    }
                    if (routeList != null && routeList.size() > 1) {
                        for (int i = 0; i < routeList.size() && i < 2; i++) {
                            Route route = routeList.get(i);
                            totalRouteList.add(route);
                        }
                    }

                } while (cursor.moveToNext());
                cursor.close();

            }
        } catch (Exception e) {
            e(LOG_TAG, e.toString());
        }
        return totalRouteList;

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
                int deleted = contentResolver.delete(DublinBusContract.RouteInformationEntry.CONTENT_URI, null, null);
                Log.d(LOG_TAG, deleted + " records deleted from route_information_table");
                int numInsert = contentResolver.bulkInsert(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                        contentValues);
                if (numInsert > 0) {
                    Log.d(LOG_TAG, numInsert + " records inserted from route_information_table");

                    DBUtils.setIsFilledRouteInformation(context, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    synchronized public static void syncRealTimeStop(Context context, String stopId) {
        try {
            if (!NetworkUtilities.isNetworkAvailable(context)) {
                DBUtils.setRealTimeConnectionStatus(context, DBUtils.REAL_TIME_STATUS_NETWORK_NOT_AVAILABLE);

                return;
            }
            ArrayList<RealTimeStop> realTimeStopList = NetworkUtilities.getRealTimeStop(context, stopId);

            if (realTimeStopList != null && realTimeStopList.size() > 0) {
                ContentValues[] contentValues = new ContentValues[realTimeStopList.size()];
                for (int i = 0; i < realTimeStopList.size(); i++) {
                    RealTimeStop realTimeStop = realTimeStopList.get(i);
                    contentValues[i] = realTimeStop.getContentValues();
                }
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(DublinBusContract.RealTimeStopEntry.CONTENT_URI, null, null);
                int inserted = contentResolver.bulkInsert(DublinBusContract.RealTimeStopEntry.CONTENT_URI,
                        contentValues);
                if (inserted > 0) {
                    Log.v(LOG_TAG, "Inserted: " + Integer.valueOf(inserted).toString());
//                    DBUtils.setIsFilledOperatorInformation(context, true);
                }
            }
        } catch (Exception e) {
            e(LOG_TAG, e.toString());
        }
    }

    public static void copyDbFromAssets(Context context) {
        DublinBusDBHelper myDbHelper = new DublinBusDBHelper(context);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            //myDbHelper.openDataBase();
            DBUtils.setIsExistDb(context, true);
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    public static void syncDB(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        syncOperators(context);

        ArrayList<ContentValues> busStopContententValues = syncBusStops(context);

        syncRouteInformation(context);

        ArrayList<Route> totalRoutes = syncRoute(context);
        ArrayList<ContentValues> routeContentValues = new ArrayList<>();
        for (Route route : totalRoutes) {
            routeContentValues.add(route.getContentValues());
        }


        int busStopDeleted = contentResolver.delete(DublinBusContract.BusStopEntry.CONTENT_URI,
                DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? ",
                new String[]{"0"});
        int busStopInserted = contentResolver.bulkInsert(DublinBusContract.BusStopEntry.CONTENT_URI,
                busStopContententValues.toArray(new ContentValues[busStopContententValues.size()]));

        int routeDelete = contentResolver.delete(DublinBusContract.RouteEntry.CONTENT_URI, null, null);
        int routeInserted = contentResolver.bulkInsert(DublinBusContract.RouteEntry.CONTENT_URI,
                routeContentValues.toArray(new ContentValues[routeContentValues.size()]));

        //ROUTE BUS STOP

        ArrayList<ContentValues> routeBusStopContentValues = new ArrayList<>();
        for (int j = 0; j < totalRoutes.size(); j++) {
            ArrayList<ContentValues> routeBusStop = DBUtils.insertRouteBusStop2(context, totalRoutes.get(j));
            if (null != routeBusStop) {
                routeBusStopContentValues.addAll(routeBusStop);
            }
        }

        int routeBusStopDeleted = contentResolver.delete(DublinBusContract.RouteBusStopEntry.CONTENT_URI, null, null);
        int routeBusStopInserted = contentResolver.bulkInsert(DublinBusContract.RouteBusStopEntry.CONTENT_URI,
                routeBusStopContentValues.toArray(new ContentValues[routeBusStopContentValues.size()]));

        Log.d(LOG_TAG, busStopDeleted + " Bus Stop Deleted");
        Log.d(LOG_TAG, busStopInserted + " Bus Stop Inserted");

        Log.d(LOG_TAG, routeDelete + " Route Deleted");
        Log.d(LOG_TAG, routeInserted + " Route Inserted");

        Log.d(LOG_TAG, routeBusStopDeleted + " Route Bus Stop Deleted");
        Log.d(LOG_TAG, routeBusStopInserted + " Route Bus Stop Inserted");
    }


    public static void addFavoriteBusStop(Context context, String busStopNumber) {

        Cursor cursor = context.getContentResolver().query(
                DublinBusContract.BusStopEntry.CONTENT_URI,
                DBUtils.BUS_STOP_COLUMNS,
                DublinBusContract.BusStopEntry.NUMBER + " = ? AND " + DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? ",
                new String[]{busStopNumber, "0"},
                null
        );
        if (cursor.moveToFirst()) {

            BusStop busStop = new BusStop();
            busStop.cursorToBusStop(cursor);
            busStop.setFavourite(true);


            Uri insertResultUri = context.getContentResolver().insert(
                    DublinBusContract.BusStopEntry.CONTENT_URI,
                    busStop.getContentValues());

            String newIdBusStop = null;
            if (insertResultUri != null) {
                newIdBusStop = insertResultUri.getLastPathSegment();
                Log.d(LOG_TAG, newIdBusStop + " New Bus Stop inserted");
            }
            cursor.close();
        }

    }

    public static void deleteFavoriteBusStop(Context context, String busStopNumber) {

        String selection = DublinBusContract.BusStopEntry.NUMBER + " = ?  AND "
                + DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ?";
        String[] selectionArgs = {busStopNumber, "1"};

        context.getContentResolver().delete(
                DublinBusContract.BusStopEntry.CONTENT_URI,
                selection,
                selectionArgs);
    }

}
