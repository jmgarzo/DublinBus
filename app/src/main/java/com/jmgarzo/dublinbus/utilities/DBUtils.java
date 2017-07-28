package com.jmgarzo.dublinbus.utilities;

import android.content.Context;
import android.database.Cursor;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.data.DublinBusProvider;
import com.jmgarzo.dublinbus.objects.BusStop;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DBUtils {


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
            DublinBusContract.BusStopEntry.LAST_UPDATED
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


    public static long getOperator(Context contect, String operatorReference){
        Cursor cursor = contect.getContentResolver().query(
                DublinBusContract.OperatorEntry.CONTENT_URI,
                DBUtils.OPERATOR_COLUMNS,
                DublinBusContract.OperatorEntry.REFERENCE + " = ? ",
                new String[]{operatorReference},
                null
        );
        Long result = null;
        if(cursor.moveToFirst()) {
            result = cursor.getLong(DBUtils.COL_OPERATOR_ID);
        }

        return result;
    }

    public static long getRouteId(Context context,String routeName,String routeDestination){
        Long result = null;
        String selection = DublinBusContract.RouteEntry.NAME + " = ? AND "  +
                DublinBusContract.RouteEntry.DESTINATION + " = ? " ;

        Cursor cursor = context.getContentResolver().query(
                DublinBusContract.RouteEntry.CONTENT_URI,
                DBUtils.ROUTE_COLUMNS,
                selection,
                new String[]{routeName,routeDestination},
                null);
        if(cursor.moveToFirst()){
            result = cursor.getLong(DBUtils.COL_ROUTE_ID);
        }
        return result;
    }

    public static long getBusStopId(Context context, String stopNumber){
        Long result = null;

        Cursor cursor = context.getContentResolver().query(DublinBusContract.BusStopEntry.CONTENT_URI,
                DBUtils.BUS_STOP_COLUMNS,
                DublinBusContract.BusStopEntry.NUMBER + " =? ",
                new String[]{stopNumber},
                null);
        if(cursor.moveToFirst()){
            result = cursor.getLong(DBUtils.COL_BUS_STOP_ID);
        }

        return result;
    }


}
