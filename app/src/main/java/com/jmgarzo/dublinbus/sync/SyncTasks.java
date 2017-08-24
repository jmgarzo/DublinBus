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

                int inserted = contentResolver.bulkInsert(DublinBusContract.BusStopEntry.CONTENT_URI,
                        contentValues);
                if (inserted > 0) {
                    DBUtils.setIsFilledBusStop(context, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncRoute(Context context) {
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
                    if(routeList.size()>2){
                       Log.e(LOG_TAG, "Route:" + routeList.get(1).getName() +" Have " + routeList.size() + " results");
                    }
                    if (routeList != null && routeList.size() >1) {
                        ContentValues[] contentValues = new ContentValues[2];
                        for (int i = 0; i < routeList.size() && i<2 ; i++) {
                            Route route = routeList.get(i);
                            contentValues[i] = route.getContentValues();
                        }

                        ContentResolver contentResolver = context.getContentResolver();

                        int inserted = contentResolver.bulkInsert(DublinBusContract.RouteEntry.CONTENT_URI,
                                contentValues);
                        if (inserted > 0) {
                            DBUtils.setIsFilledRoute(context, true);
                        }

                        for (int j = 0; j < routeList.size(); j++) {
                            DBUtils.insertRouteBusStop(context, routeList.get(j));
                        }
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e(LOG_TAG, e.toString());
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

                int numInsert = contentResolver.bulkInsert(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                        contentValues);
                if (numInsert > 0) {
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

    public static void syncDB(Context context) {

        DublinBusDBHelper myDbHelper = new DublinBusDBHelper(context);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

//        if (!DBUtils.isFilledOperatorInformation(context)) {
//            Intent intentOperatorInformationService = new Intent(context, OperatorInformationService.class);
//            context.startService(intentOperatorInformationService);
//        }
//
//
//        if (!DBUtils.isFilledBusStop(context)) {
//            Intent intentBusStopInformationService = new Intent(context, BusStopInformationService.class);
//            context.startService(intentBusStopInformationService);
//        }
//
//        if (!DBUtils.isFilledRouteInformation(context)) {
//            Intent intentRouteListInformationService = new Intent(context, RouteListInformationService.class);
//            context.startService(intentRouteListInformationService);
//        }
//
//        if (!DBUtils.isFilledRoute(context)) {
//            Intent intentRouteInformationService = new Intent(context, RouteInformationService.class);
//            context.startService(intentRouteInformationService);
//        }

//        Intent intentOperatorInformationService = new Intent(getActivity(), OperatorInformationService.class);
//        getContext().startService(intentOperatorInformationService);

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
