package com.jmgarzo.dublinbus.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DublinBusDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "DublinBus.db";
   private static String DB_PATH;

    private SQLiteDatabase myDataBase;

    private Context mContext;



    private final String SQL_CREATE_OPERATOR_TABLE =
            "CREATE TABLE " + DublinBusContract.OperatorEntry.TABLE_NAME + " ( " +
                    DublinBusContract.OperatorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    DublinBusContract.OperatorEntry.REFERENCE + " TEXT NOT NULL, " +
                    DublinBusContract.OperatorEntry.NAME + " TEXT NOT NULL, " +
                    DublinBusContract.OperatorEntry.DESCRIPTION + " TEXT NOT NULL, " +
                    DublinBusContract.OperatorEntry.IS_NEW + " INTEGER NOT NULL " +
                    " );";

    private final String SQL_CREATE_BUS_STOP_TABLE =
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
                    DublinBusContract.BusStopEntry.LAST_UPDATED + " TEXT NOT NULL, " +
                    DublinBusContract.BusStopEntry.IS_FAVOURITE + " INTEGER NOT NULL, " +
                    DublinBusContract.BusStopEntry.ALIAS + " INTEGER NOT NULL, " +
                    DublinBusContract.BusStopEntry.IS_NEW + " INTEGER NOT NULL " +
                    " );";

    private final String SQL_CREATE_ROUTE_TABLE =
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
                    DublinBusContract.RouteEntry.IS_NEW + " INTEGER NOT NULL, " +

                    " FOREIGN KEY (" + DublinBusContract.RouteEntry.OPERATOR + ") REFERENCES " +
                    DublinBusContract.OperatorEntry.TABLE_NAME + " (" + DublinBusContract.OperatorEntry._ID + ") ON DELETE CASCADE " +
                    ");";

    private final String SQL_CREATE_ROUTE_BUS_STOP_TABLE =
            "CREATE TABLE " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + " ( " +
                    DublinBusContract.RouteBusStopEntry.ROUTE_ID + " INTEGER , " +
                    DublinBusContract.RouteBusStopEntry.BUS_STOP_ID + " INTEGER, " +
                    DublinBusContract.RouteBusStopEntry.RECORD_ORDER  + " INTEGER NOT NULL,  " +
                    DublinBusContract.RouteBusStopEntry.IS_NEW + " INTEGER NOT NULL, " +
                    " PRIMARY KEY( " +
                    DublinBusContract.RouteBusStopEntry.ROUTE_ID + " , "+
                    DublinBusContract.RouteBusStopEntry.BUS_STOP_ID  +

                    " ), " +
                    " FOREIGN KEY( " +  DublinBusContract.RouteBusStopEntry.ROUTE_ID + " ) REFERENCES " +
                    DublinBusContract.RouteEntry.TABLE_NAME + " ( " + DublinBusContract.RouteEntry._ID +") ON DELETE CASCADE, " +
                    "FOREIGN KEY ( " + DublinBusContract.RouteBusStopEntry.BUS_STOP_ID + " ) REFERENCES " +
                    DublinBusContract.BusStopEntry.TABLE_NAME + " ( " + DublinBusContract.BusStopEntry._ID + ") ON DELETE CASCADE " +
                    ");";



    private final String SQL_CREATE_ROUTE_INFORMATION_TABLE =
            "CREATE TABLE " + DublinBusContract.RouteInformationEntry.TABLE_NAME + " ( " +
                    DublinBusContract.RouteInformationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    DublinBusContract.RouteInformationEntry.OPERATOR + " TEXT NOT NULL, " +
                    DublinBusContract.RouteInformationEntry.ROUTE + " TEXT NOT NULL, " +
                    DublinBusContract.RouteInformationEntry.IS_NEW + " INTEGER NOT NULL " +

                    " );";

    private final String SQL_CREATE_REAL_TIME_STOP =
            "CREATE TABLE " + DublinBusContract.RealTimeStopEntry.TABLE_NAME + " ( " +
                    DublinBusContract.RealTimeStopEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    DublinBusContract.RealTimeStopEntry.STOP_NUMBER + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.ARRIVAL_DATE_TIME + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.DUE_TIME + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.DEPARTURE_DATE_TIME + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.DEPARTURE_DUE_TIME + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.SCHEDULED_ARRIVAL_DATE_TIME + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.SCHEDULED_DEPARTURE_DATE_TIME + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.DESTINATION + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.DESTINATION_LOCALIZED + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.ORIGIN + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.ORIGIN_LOCALIZED + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.DIRECTION + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.OPERATOR + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.ADDITIONAL_INFORMATION + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.LOW_FLOOR_STATUS + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.ROUTE + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.SOURCE_TIMESTAMP + " TEXT NOT NULL, " +
                    DublinBusContract.RealTimeStopEntry.MONITORED + " TEXT NOT NULL " +

                    " );";

    public DublinBusDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        DB_PATH = context.getDatabasePath(DATABASE_NAME).toString();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        boolean dbExist = checkDataBase();

//        if(!dbExist){
//            getReadableDatabase();
//            try {
//                copyDataBase();
//            } catch (IOException e) {
//
//                throw new Error("Error copying database");
//
//            }
//        }

//        db.execSQL(SQL_CREATE_OPERATOR_TABLE);
//        db.execSQL(SQL_CREATE_BUS_STOP_TABLE);
//        db.execSQL(SQL_CREATE_ROUTE_TABLE);
//        db.execSQL(SQL_CREATE_ROUTE_BUS_STOP_TABLE);
//        db.execSQL(SQL_CREATE_ROUTE_INFORMATION_TABLE);
//        db.execSQL(SQL_CREATE_REAL_TIME_STOP);

    }
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (!dbExist) {
            getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }
    }


        @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

//        db.execSQL("DROP TABLE IF EXISTS " + DublinBusContract.OperatorEntry.TABLE_NAME );
//        db.execSQL("DROP TABLE IF EXISTS " + DublinBusContract.BusStopEntry.TABLE_NAME );
//        db.execSQL("DROP TABLE IF EXISTS " + DublinBusContract.RouteEntry.TABLE_NAME );
//        db.execSQL("DROP TABLE IF EXISTS " + DublinBusContract.RouteBusStopEntry.TABLE_NAME );
//        db.execSQL("DROP TABLE IF EXISTS " + DublinBusContract.RouteInformationEntry.TABLE_NAME );
//        db.execSQL("DROP TABLE IF EXISTS " + DublinBusContract.RealTimeStopEntry.TABLE_NAME );
//
//
//
//
//
//        db.execSQL(SQL_CREATE_OPERATOR_TABLE);
//        db.execSQL(SQL_CREATE_BUS_STOP_TABLE);
//        db.execSQL(SQL_CREATE_ROUTE_TABLE);
//        db.execSQL(SQL_CREATE_ROUTE_BUS_STOP_TABLE);
//        db.execSQL(SQL_CREATE_ROUTE_INFORMATION_TABLE);
//        db.execSQL(SQL_CREATE_REAL_TIME_STOP);

    }

    private void  upgradeVersion2(SQLiteDatabase db){

        db.execSQL("ALTER TABLE TABLE " + DublinBusContract.OperatorEntry.TABLE_NAME +
        " ADD COLUMN " + DublinBusContract.OperatorEntry.IS_NEW + " INTEGER NOT NULL DEFAULT 0");

        db.execSQL("ALTER TABLE TABLE " + DublinBusContract.BusStopEntry.TABLE_NAME +
                " ADD COLUMN " + DublinBusContract.BusStopEntry.IS_NEW + " INTEGER NOT NULL DEFAULT 0");

        db.execSQL("ALTER TABLE TABLE " + DublinBusContract.RouteEntry.TABLE_NAME +
                " ADD COLUMN " + DublinBusContract.RouteEntry.IS_NEW + " INTEGER NOT NULL DEFAULT 0");

        db.execSQL("ALTER TABLE TABLE " + DublinBusContract.RouteBusStopEntry.TABLE_NAME +
                " ADD COLUMN " + DublinBusContract.RouteBusStopEntry.IS_NEW + " INTEGER NOT NULL DEFAULT 0");

        db.execSQL("ALTER TABLE TABLE " + DublinBusContract.RouteInformationEntry.TABLE_NAME +
                " ADD COLUMN " + DublinBusContract.RouteInformationEntry.IS_NEW + " INTEGER NOT NULL DEFAULT 0");


    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }


    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }
}
