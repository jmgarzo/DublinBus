package com.jmgarzo.dublinbus.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jmgarzo.dublinbus.R;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.Route;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DBUtils {
    private static final String LOG_TAG = DBUtils.class.getSimpleName();


    public static final String[] OPERATOR_COLUMNS = {
            DublinBusContract.OperatorEntry._ID,
            DublinBusContract.OperatorEntry.REFERENCE,
            DublinBusContract.OperatorEntry.NAME,
            DublinBusContract.OperatorEntry.DESCRIPTION

    };

    public static final int COL_OPERATOR_ID = 0;
    public static final int COL_OPERATOR_REFERENCE = 1;
    public static final int COL_OPERATOR_NAME = 2;
    public static final int COL_OPERATOR_DESCRIPTION = 3;


    public static final String[] BUS_STOP_COLUMNS = {
            DublinBusContract.BusStopEntry._ID,
            DublinBusContract.BusStopEntry.NUMBER,
            DublinBusContract.BusStopEntry.DISPLAY_STOP_ID,
            DublinBusContract.BusStopEntry.SHORT_NAME,
            DublinBusContract.BusStopEntry.SHORT_NAME_LOCALIZED,
            DublinBusContract.BusStopEntry.FULL_NAME,
            DublinBusContract.BusStopEntry.FULL_NAME_LOCALIZED,
            DublinBusContract.BusStopEntry.LATITUDE,
            DublinBusContract.BusStopEntry.LONGITUDE,
            DublinBusContract.BusStopEntry.LAST_UPDATED,
            DublinBusContract.BusStopEntry.IS_FAVOURITE
    };

    public static final int COL_BUS_STOP_ID = 0;
    public static final int COL_BUS_STOP_NUMBER = 1;
    public static final int COL_BUS_STOP_DISPLAY_STOP_ID = 2;
    public static final int COL_BUS_STOP_SHORTNAME = 3;
    public static final int COL_BUS_STOP_SHORT_NAME_LOCALIZED = 4;
    public static final int COL_BUS_STOP_FULL_NAME = 5;
    public static final int COL_BUS_STOP_FULL_NAME_LOCALIZED = 6;
    public static final int COL_BUS_STOP_LATITUDE = 6;
    public static final int COL_BUS_STOP_LONGITUDE = 8;
    public static final int COL_BUS_STOP_LAST_UPDATED = 9;
    public static final int COL_BUS_STOP_IS_FAVORITE = 9;



    public static final String[] ROUTE_COLUMNS = {
            DublinBusContract.RouteEntry._ID,
            DublinBusContract.RouteEntry.TIMESTAMP,
            DublinBusContract.RouteEntry.NAME,
            DublinBusContract.RouteEntry.OPERATOR,
            DublinBusContract.RouteEntry.ORIGIN,
            DublinBusContract.RouteEntry.ORIGIN_LOCALIZED,
            DublinBusContract.RouteEntry.DESTINATION,
            DublinBusContract.RouteEntry.DESTINATION_LOCALIZED,
            DublinBusContract.RouteEntry.LAST_UPDATED
    };


    public static final int COL_ROUTE_ID = 0;
    public static final int COL_ROUTE_TIMESTAMP = 1;
    public static final int COL_ROUTE_NAME = 2;
    public static final int COL_ROUTE_OPERATOR = 3;
    public static final int COL_ROUTE_ORIGIN = 4;
    public static final int COL_ROUTE_ORIGIN_LOCALIZED = 5;
    public static final int COL_ROUTE_DESTINATION = 6;
    public static final int COL_ROUTE_DESTINATION_LOCALIZED = 7;
    public static final int COL_ROUTE_LAST_UPDATE = 8;


    public static final String[] ROUTE_INFORMATION_COLUMNS = {
            DublinBusContract.RouteInformationEntry._ID,
            DublinBusContract.RouteInformationEntry.OPERATOR,
            DublinBusContract.RouteInformationEntry.ROUTE

    };

    public static final int COL_ROUTE_INFORMATION_ID = 0;
    public static final int COL_ROUTE_INFORMATION_OPERATOR = 1;
    public static final int COL_ROUTE_INFORMATION_ROUTE = 2;


    public static final String[] REAL_TIME_STOP_COLUMNS = {
            DublinBusContract.RealTimeStopEntry._ID,
            DublinBusContract.RealTimeStopEntry.STOP_NUMBER,
            DublinBusContract.RealTimeStopEntry.ARRIVAL_DATE_TIME,
            DublinBusContract.RealTimeStopEntry.DUE_TIME,
            DublinBusContract.RealTimeStopEntry.DEPARTURE_DATE_TIME,
            DublinBusContract.RealTimeStopEntry.DEPARTURE_DUE_TIME,
            DublinBusContract.RealTimeStopEntry.SCHEDULED_ARRIVAL_DATE_TIME,
            DublinBusContract.RealTimeStopEntry.SCHEDULED_DEPARTURE_DATE_TIME,
            DublinBusContract.RealTimeStopEntry.DESTINATION,
            DublinBusContract.RealTimeStopEntry.DESTINATION_LOCALIZED,
            DublinBusContract.RealTimeStopEntry.ORIGIN,
            DublinBusContract.RealTimeStopEntry.ORIGIN_LOCALIZED,
            DublinBusContract.RealTimeStopEntry.DIRECTION,
            DublinBusContract.RealTimeStopEntry.OPERATOR,
            DublinBusContract.RealTimeStopEntry.ADDITIONAL_INFORMATION,
            DublinBusContract.RealTimeStopEntry.LOW_FLOOR_STATUS,
            DublinBusContract.RealTimeStopEntry.ROUTE,
            DublinBusContract.RealTimeStopEntry.SOURCE_TIMESTAMP,
            DublinBusContract.RealTimeStopEntry.MONITORED


    };

    public static final int COL_REAL_TIME_STOP_ID = 0;
    public static final int COL_REAL_TIME_STOP_STOP_NUMBER = 1;
    public static final int COL_REAL_TIME_STOP_ARRIVAL_DATE_TIME = 2;
    public static final int COL_REAL_TIME_STOP_DUE_TIME = 3;
    public static final int COL_REAL_TIME_STOP_DEPARTURE_DATE_TIME = 4;
    public static final int COL_REAL_TIME_STOP_DEPARTURE_DUE_TIME = 5;
    public static final int COL_REAL_TIME_STOP_SCHEDULED_ARRIVAL_DATE_TIME = 6;
    public static final int COL_REAL_TIME_STOP_SCHEDULED_DEPARTURE_DATE_TIME = 7;
    public static final int COL_REAL_TIME_STOP_DESTINATION = 8;
    public static final int COL_REAL_TIME_STOP_DESTINATION_LOCALIZED = 9;
    public static final int COL_REAL_TIME_STOP_ORIGIN = 10;
    public static final int COL_REAL_TIME_STOP_ORIGIN_LOCALIZED = 11;
    public static final int COL_REAL_TIME_STOP_DIRECTION = 12;
    public static final int COL_REAL_TIME_STOP_OPERATOR = 13;
    public static final int COL_REAL_TIME_STOP_ADDITIONAL_INFORMATION = 14;
    public static final int COL_REAL_TIME_STOP_LOW_FLOOR_STATUS = 15;
    public static final int COL_REAL_TIME_STOP_ROUTE = 16;
    public static final int COL_REAL_TIME_STOP_SOURCE_TIMESTAMP = 17;
    public static final int COL_REAL_TIME_STOP_MONITORED = 18;


    public static final int REAL_TIME_STATUS_SUCCCESS = 0;
    public static final int REAL_TIME_STATUS_NO_RESULTS = 1;
    public static final int REAL_TIME_STATUS_MISSING_PARAMETER = 2;
    public static final int REAL_TIME_STATUS_INVALID_PARAMETER = 3;
    public static final int REAL_TIME_STATUS_SCHEDULED_DOWN_TIME = 4;
    public static final int REAL_TIME_STATUS_UNEXPECTED_SERVER_ERROR = 5;
    public static final int REAL_TIME_STATUS_SERVER_DOWN = 6;
    public static final int REAL_TIME_STATUS_SERVER_INVALID = 7;
    public static final int REAL_TIME_STATUS_UNKNOWN = 8;
    public static final int REAL_TIME_STATUS_NETWORK_NOT_AVAILABLE=9;


    public static long getOperator(Context contect, String operatorReference) {
        Cursor cursor = contect.getContentResolver().query(
                DublinBusContract.OperatorEntry.CONTENT_URI,
                DBUtils.OPERATOR_COLUMNS,
                DublinBusContract.OperatorEntry.REFERENCE + " = ? ",
                new String[]{operatorReference},
                null
        );
        Long result = null;
        if (cursor.moveToFirst()) {
            result = cursor.getLong(DBUtils.COL_OPERATOR_ID);
        }

        return result;
    }

    public static long getRouteId(Context context, String routeName, String routeDestination) {
        Long result = null;
        String selection = DublinBusContract.RouteEntry.NAME + " = ? AND " +
                DublinBusContract.RouteEntry.DESTINATION + " = ? ";

        Cursor cursor = context.getContentResolver().query(
                DublinBusContract.RouteEntry.CONTENT_URI,
                DBUtils.ROUTE_COLUMNS,
                selection,
                new String[]{routeName, routeDestination},
                null);
        if (cursor.moveToFirst()) {
            result = cursor.getLong(DBUtils.COL_ROUTE_ID);
        }
        return result;
    }

    public static long getBusStopId(Context context, String stopNumber) {
        Long result = null;

        Cursor cursor = context.getContentResolver().query(DublinBusContract.BusStopEntry.CONTENT_URI,
                DBUtils.BUS_STOP_COLUMNS,
                DublinBusContract.BusStopEntry.NUMBER + " =? ",
                new String[]{stopNumber},
                null);
        if (cursor.moveToFirst()) {
            result = cursor.getLong(DBUtils.COL_BUS_STOP_ID);
        } else {
            Log.e("DB_Utils", "No existe bus stop");
            result = -1l;
        }

        return result;
    }


    public static void insertRouteBusStop(Context context, Route route) {

        String selection = DublinBusContract.RouteEntry.NAME + " = ? " +
                " AND " + DublinBusContract.RouteEntry.DESTINATION + " = ? ";
        String[] selectionArgs = new String[]{route.getName(), route.getDestination()};
        Cursor cursor = context.getContentResolver().query(DublinBusContract.RouteEntry.CONTENT_URI,
                new String[]{DublinBusContract.RouteEntry._ID},
                selection,
                selectionArgs,
                null);

        Long idRoute;
        if (null != cursor && cursor.moveToFirst()) {
            idRoute = cursor.getLong(0);
        } else {
            return;
        }
        ContentValues[] cvArray = new ContentValues[route.getStops().size()];
        for (int i = 0; i < route.getStops().size(); i++) {

            ContentValues cv = new ContentValues();
            cv.put(DublinBusContract.RouteBusStopEntry.ROUTE_ID, idRoute);
            cv.put(DublinBusContract.RouteBusStopEntry.BUS_STOP_ID, getBusStopIdFromNumber(context, route.getStops().get(i)));
            cv.put(DublinBusContract.RouteBusStopEntry.RECORD_ORDER, i);

            cvArray[i] = cv;
        }

        int inserted = context.getContentResolver().bulkInsert(DublinBusContract.RouteBusStopEntry.CONTENT_URI, cvArray);
        if (inserted <= 0) {
            Log.d(LOG_TAG, "insertRouteBusStop() no record Insert");
        }
    }

    private static String getBusStopIdFromNumber(Context context, String busStopNumber) {
        String busStopId = "";
        Cursor cursorBusStop = context.getContentResolver().query(DublinBusContract.BusStopEntry.CONTENT_URI,
                new String[]{DublinBusContract.BusStopEntry._ID},
                DublinBusContract.BusStopEntry.NUMBER + " = ? ",
                new String[]{busStopNumber},
                null);
        if (cursorBusStop != null && cursorBusStop.moveToFirst()) {
            busStopId = cursorBusStop.getString(0);
        }
        return busStopId;
    }


    public static boolean isDBCreated(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean("db_created", false);
    }

    public static void setCreatedDB(Context context, boolean b) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean("db_created", b).apply();
    }

    public static void setIsFilledRouteInformation(Context context, boolean b) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.pref_route_information_db), b).apply();

    }

    public static boolean isFilledRouteInformation(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.pref_route_information_db), false);
    }

    public static void setIsFilledOperatorInformation(Context context, boolean b) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.pref_operator_information), b).apply();
    }

    public static boolean isFilledOperatorInformation(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.pref_operator_information), false);
    }

    public static void setIsFilledBusStop(Context context, boolean b) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.pref_bus_stop_db), b).apply();
    }

    public static boolean isFilledBusStop(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.pref_bus_stop_db), false);
    }

    public static void setIsFilledRoute(Context context, boolean b) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putBoolean(context.getString(R.string.pref_route_db), b).apply();
    }

    public static boolean isFilledRoute(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(context.getString(R.string.pref_route_db), false);
    }

    public static void setRealTimeConnectionStatus(Context context, int i) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putInt(context.getString(R.string.pref_real_time_connection_status), i).apply();
    }

    static public  int getRealTimeConnectionStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.pref_real_time_connection_status),
                DBUtils.REAL_TIME_STATUS_UNKNOWN);
    }


}
