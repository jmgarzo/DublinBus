package com.jmgarzo.dublinbus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DublinBusDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DublinBus.db";

    public DublinBusDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_OPERATOR_TABLE =
                "CREATE TABLE " + DublinBusContract.OperatorEntry.TABLE_NAME + " ( " +
                        DublinBusContract.OperatorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        DublinBusContract.OperatorEntry.REFERENCE + " TEXT NOT NULL, " +
                        DublinBusContract.OperatorEntry.NAME + " TEXT NOT NULL, " +
                        DublinBusContract.OperatorEntry.DESCRIPTION + " TEXT NOT NULL " +
                        " );";

        db.execSQL(SQL_CREATE_OPERATOR_TABLE);


        final String SQL_CREATE_BUS_STOP_TABLE =
                "CREATE TABLE " + DublinBusContract.BusStopEntry.TABLE_NAME + " ( " +
                        DublinBusContract.BusStopEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        DublinBusContract.BusStopEntry.NUMBER + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.DISPLAY_STOP_ID + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.SHORT_NAME + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.SHORT_NAME_LOCALIZED + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.FULL_NAME + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.FULL_NAME_LOCALIZED + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.LATITUDE + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.LONGITUDE + " TEXT NOT NULL, " +
                        DublinBusContract.BusStopEntry.LAST_UPDATED + " TEXT NOT NULL " +
                        " );";

        db.execSQL(SQL_CREATE_BUS_STOP_TABLE);

        final String SQL_CREATE_ROUTE_TABLE =
                "CREATE TABLE " + DublinBusContract.RouteEntry.TABLE_NAME + " ( " +
                        DublinBusContract.RouteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        DublinBusContract.RouteEntry.TIMESTAMP + " TEXT NOT NULL, " +
                        DublinBusContract.RouteEntry.NAME + " TEXT NOT NULL, " +
                        DublinBusContract.RouteEntry.OPERATOR + " INTEGER NOT NULL, " +
                        DublinBusContract.RouteEntry.ORIGIN + " TEXT NOT NULL, " +
                        DublinBusContract.RouteEntry.ORIGIN_LOCALIZED + " TEXT NOT NULL, " +
                        DublinBusContract.RouteEntry.DESTINATION + " TEXT NOT NULL, " +
                        DublinBusContract.RouteEntry.DESTINATION_LOCALIZED + " TEXT NOT NULL, " +
                        DublinBusContract.RouteEntry.LAST_UPDATED + " TEXT NOT NULL, " +
                        " FOREIGN KEY (" + DublinBusContract.RouteEntry.OPERATOR + ") REFERENCES " +
                        DublinBusContract.OperatorEntry.TABLE_NAME + " (" + DublinBusContract.OperatorEntry._ID + ") ON DELETE CASCADE " +
                        ");";

        db.execSQL(SQL_CREATE_ROUTE_TABLE);

        final String SQL_CREATE_ROUTE_BUS_STOP_TABLE =
                "CREATE TABLE " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + " ( " +
                        DublinBusContract.RouteBusStopEntry.ROUTE_ID + " INTEGER , " +
                        DublinBusContract.RouteBusStopEntry.BUS_STOP_ID + " INTEGER, " +
                        " PRIMARY KEY( " +
                        DublinBusContract.RouteBusStopEntry.ROUTE_ID + " , "+
                        DublinBusContract.RouteBusStopEntry.BUS_STOP_ID  +
                        " ), " +
                        " FOREIGN KEY( " +  DublinBusContract.RouteBusStopEntry.ROUTE_ID + " ) REFERENCES " +
                        DublinBusContract.RouteEntry.TABLE_NAME + " ( " + DublinBusContract.RouteEntry._ID +") ON DELETE CASCADE, " +
                        "FOREIGN KEY ( " + DublinBusContract.RouteBusStopEntry.BUS_STOP_ID + " ) REFERENCES " +
                        DublinBusContract.BusStopEntry.TABLE_NAME + " ( " + DublinBusContract.BusStopEntry._ID + ") ON DELETE CASCADE " +
                        ");";
        db.execSQL(SQL_CREATE_ROUTE_BUS_STOP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
