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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
