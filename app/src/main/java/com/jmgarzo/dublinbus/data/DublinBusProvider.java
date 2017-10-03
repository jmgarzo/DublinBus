package com.jmgarzo.dublinbus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DublinBusProvider extends ContentProvider {

    private final String LOG_TAG = DublinBusProvider.class.getSimpleName();

    static final int OPERATOR = 100;

    static final int BUS_STOP = 200;
    static final int BUS_STOP_WITH_ROUTE_ID = 201;

    static final int ROUTE = 300;
    static final int ROUTE_WITH_STOP_BUS = 301;

    static final int ROUTE_BUS_STOP = 400;

    static final int ROUTE_INFORMATION = 500;

    static final int REAL_TIME_STOP = 600;

    static final int BUS_STOP_AND_ROUTES_WITH_ROUTE_ID = 700;


    private DublinBusDBHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final SQLiteQueryBuilder sStopsByRouteQueryBuilder;
    static {
        sStopsByRouteQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //bus_stop INNER JOIN route_bus_stop ON weather.location_id = location._id
        sStopsByRouteQueryBuilder.setTables(
                DublinBusContract.BusStopEntry.TABLE_NAME + " INNER JOIN " +
                        DublinBusContract.RouteBusStopEntry.TABLE_NAME +
                        " ON " + DublinBusContract.BusStopEntry.TABLE_NAME +
                        "." + DublinBusContract.BusStopEntry._ID +
                        " = " + DublinBusContract.RouteBusStopEntry.TABLE_NAME +
                        "." + DublinBusContract.RouteBusStopEntry.BUS_STOP_ID);
    }
    private static final SQLiteQueryBuilder sRoutesByBusStopQueryBuilder;
    static {
        sRoutesByBusStopQueryBuilder = new SQLiteQueryBuilder();
        sRoutesByBusStopQueryBuilder.setTables(DublinBusContract.BusStopEntry.TABLE_NAME +
                " INNER JOIN " + DublinBusContract.RouteBusStopEntry.TABLE_NAME +
                " INNER JOIN " + DublinBusContract.RouteEntry.TABLE_NAME +
                " ON " +
                DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID +
                " = " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + "." + DublinBusContract.RouteBusStopEntry.BUS_STOP_ID +
                " AND " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + "." + DublinBusContract.RouteBusStopEntry.ROUTE_ID +
                " = " + DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry._ID);
    }

    private static final SQLiteQueryBuilder sStopsAndRoutesByRouteQueryBuilder;
    static {
        sStopsAndRoutesByRouteQueryBuilder = new SQLiteQueryBuilder();
        sStopsAndRoutesByRouteQueryBuilder.setTables(
                DublinBusContract.RouteEntry.TABLE_NAME + " INNER JOIN " +
                        DublinBusContract.RouteBusStopEntry.TABLE_NAME + " INNER JOIN " +
                        DublinBusContract.BusStopEntry.TABLE_NAME +
                        " ON " + DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry._ID +
                        " = " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + "." + DublinBusContract.RouteBusStopEntry.ROUTE_ID +
                        " AND " + DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID +
                        " = " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + "."+ DublinBusContract.RouteBusStopEntry.BUS_STOP_ID);
    }





    public Cursor getRoutesByBusStopNumber(Uri uri, String[] projection, String sortOrder) {
        String selection = DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry.NUMBER + " = ? ";
        String[] selectionArgs = new String[]{DublinBusContract.RouteEntry.getBusNumberFromUri(uri)};
        return sRoutesByBusStopQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getBusStopsByRoute(
            Uri uri, String[] projection, String sortOrder) {
        String selection = DublinBusContract.RouteBusStopEntry.ROUTE_ID + " = ? ";
        String[] selectionArgs = new String[]{DublinBusContract.BusStopEntry.getRouteIdFromUri(uri)};
        return sStopsByRouteQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public  Cursor getStopsAndRoutesByRoute(Uri uri,String[] projection, String sortOrder) {

        String selection =
                DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID + " IN ( " +
                        " SELECT " + DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry._ID +
                        " FROM " + DublinBusContract.RouteEntry.TABLE_NAME +
                        " INNER JOIN " + DublinBusContract.RouteBusStopEntry.TABLE_NAME +
                        " ON " + DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry._ID +
                        " = " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + "." + DublinBusContract.RouteBusStopEntry.ROUTE_ID +
                        " AND " + DublinBusContract.BusStopEntry.TABLE_NAME + "."+ DublinBusContract.BusStopEntry._ID +
                        " = " + DublinBusContract.RouteBusStopEntry.TABLE_NAME + "." + DublinBusContract.RouteBusStopEntry.BUS_STOP_ID +
                        " WHERE " + DublinBusContract.RouteEntry.TABLE_NAME +"."+ DublinBusContract.RouteEntry._ID  + " =  ? ) ";

//        String selection = DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry.NUMBER + " = ? ";
//        String[] projection2 = new String[]{
//                DublinBusContract.BusStopEntry.TABLE_NAME + "." + DublinBusContract.BusStopEntry.NUMBER,
//                DublinBusContract.RouteEntry.TABLE_NAME + "." + DublinBusContract.RouteEntry.NAME};



//        String[] selectionArgs = new String[]{DublinBusContract.RouteEntry.getBusNumberFromUri(uri)};
        return sStopsAndRoutesByRouteQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                new String[]{uri.getLastPathSegment()},
                null,
                null,
                sortOrder
        );
    }



    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_OPERATOR, OPERATOR);

        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_BUS_STOP, BUS_STOP);
        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_BUS_STOP + "/*", BUS_STOP_WITH_ROUTE_ID);



        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_ROUTE, ROUTE);
        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_ROUTE + "/*", ROUTE_WITH_STOP_BUS);


        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_ROUTE_BUS_STOP, ROUTE_BUS_STOP);

        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_ROUTE_INFORMATION, ROUTE_INFORMATION);

        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_REAL_TIME_STOP, REAL_TIME_STOP);

        matcher.addURI(DublinBusContract.CONTENT_AUTHORITY, DublinBusContract.PATH_BUS_STOP_AND_ROUTES + "/*", BUS_STOP_AND_ROUTES_WITH_ROUTE_ID);


        return matcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DublinBusDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor returnCursor;

        switch (sUriMatcher.match(uri)) {
            case OPERATOR: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        DublinBusContract.OperatorEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case BUS_STOP: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        DublinBusContract.BusStopEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case BUS_STOP_WITH_ROUTE_ID: {
//                returnCursor = mOpenHelper.getReadableDatabase().query(
//                        DublinBusContract.BusStopEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
                returnCursor = getBusStopsByRoute(uri, projection, sortOrder);
                break;

            }

            case BUS_STOP_AND_ROUTES_WITH_ROUTE_ID: {
//                returnCursor = mOpenHelper.getReadableDatabase().query(
//                        DublinBusContract.BusStopEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
                returnCursor = getStopsAndRoutesByRoute(uri,projection,sortOrder);
                break;

            }

            case ROUTE: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        DublinBusContract.RouteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case ROUTE_WITH_STOP_BUS: {
//                returnCursor = mOpenHelper.getReadableDatabase().query(
//                        DublinBusContract.BusStopEntry.TABLE_NAME,
//                        projection,
//                        selection,
//                        selectionArgs,
//                        null,
//                        null,
//                        sortOrder
//                );
                returnCursor = getRoutesByBusStopNumber(uri, projection, sortOrder);
                break;
            }

            case ROUTE_BUS_STOP: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        DublinBusContract.RouteBusStopEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case ROUTE_INFORMATION: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        DublinBusContract.RouteInformationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case REAL_TIME_STOP: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        DublinBusContract.RealTimeStopEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case OPERATOR:
                return DublinBusContract.OperatorEntry.CONTENT_DIR_TYPE;
            case BUS_STOP:
                return DublinBusContract.BusStopEntry.CONTENT_DIR_TYPE;
            case BUS_STOP_WITH_ROUTE_ID:
                return DublinBusContract.BusStopEntry.CONTENT_DIR_TYPE;
            case ROUTE:
                return DublinBusContract.RouteEntry.CONTENT_DIR_TYPE;
            case ROUTE_BUS_STOP:
                return DublinBusContract.RouteBusStopEntry.CONTENT_DIR_TYPE;
            case ROUTE_INFORMATION:
                return DublinBusContract.RouteBusStopEntry.CONTENT_DIR_TYPE;
            case REAL_TIME_STOP:
                return DublinBusContract.RealTimeStopEntry.CONTENT_DIR_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri = null;

        switch (sUriMatcher.match(uri)) {

            case OPERATOR: {

                long id = db.insert(DublinBusContract.OperatorEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DublinBusContract.OperatorEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case BUS_STOP: {
                long id = db.insert(DublinBusContract.BusStopEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DublinBusContract.BusStopEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case ROUTE: {
                long id = db.insert(DublinBusContract.RouteEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DublinBusContract.RouteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case ROUTE_BUS_STOP: {
                try {
                    long id = db.insertOrThrow(DublinBusContract.RouteBusStopEntry.TABLE_NAME,
                            null,
                            contentValues);

                    if (id > 0) {
                        returnUri = ContentUris.withAppendedId(DublinBusContract.RouteBusStopEntry.CONTENT_URI, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into: " + uri);
                    }
                } catch (SQLiteException e) {
                    Log.e(LOG_TAG, e.toString());
                }
                break;
            }

            case ROUTE_INFORMATION: {
                long id = db.insert(DublinBusContract.RouteInformationEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DublinBusContract.RouteInformationEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            case REAL_TIME_STOP: {
                long id = db.insert(DublinBusContract.RealTimeStopEntry.TABLE_NAME,
                        null,
                        contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DublinBusContract.RealTimeStopEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numDeleted = 0;

        switch (sUriMatcher.match(uri)) {
            case OPERATOR: {
                numDeleted = db.delete(
                        DublinBusContract.OperatorEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }

            case BUS_STOP: {
                numDeleted = db.delete(
                        DublinBusContract.BusStopEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }

            case ROUTE: {
                numDeleted = db.delete(
                        DublinBusContract.RouteEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }

            case ROUTE_BUS_STOP: {
                numDeleted = db.delete(
                        DublinBusContract.RouteBusStopEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case ROUTE_INFORMATION: {
                numDeleted = db.delete(
                        DublinBusContract.RouteInformationEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case REAL_TIME_STOP: {
                numDeleted = db.delete(
                        DublinBusContract.RealTimeStopEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        int numUpdates = 0;

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case OPERATOR: {
                numUpdates = db.update(DublinBusContract.OperatorEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case BUS_STOP: {
                numUpdates = db.update(DublinBusContract.BusStopEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }

            case ROUTE: {
                numUpdates = db.update(DublinBusContract.RouteEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }

            case ROUTE_BUS_STOP: {
                numUpdates = db.update(DublinBusContract.RouteBusStopEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }

            case ROUTE_INFORMATION: {
                numUpdates = db.update(DublinBusContract.RouteInformationEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }

            case REAL_TIME_STOP: {
                numUpdates = db.update(DublinBusContract.RealTimeStopEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numUpdates;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numInserted = 0;

        switch (sUriMatcher.match(uri)) {
            case OPERATOR: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long id = db.insert(DublinBusContract.OperatorEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }


                break;
            }
            case BUS_STOP: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long id = db.insert(DublinBusContract.BusStopEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }

                break;
            }

            case ROUTE: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long id = db.insert(DublinBusContract.RouteEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }

                break;
            }

            case ROUTE_BUS_STOP: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long id = -1;
                        try {
                            id = db.insertOrThrow(DublinBusContract.RouteBusStopEntry.TABLE_NAME, null, value);
                        } catch (SQLiteException e) {
                            Log.d(LOG_TAG, e.toString());
                        }
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }

                break;
            }

            case ROUTE_INFORMATION: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long id = db.insert(DublinBusContract.RouteInformationEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }

            case REAL_TIME_STOP: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long id = db.insert(DublinBusContract.RealTimeStopEntry.TABLE_NAME, null, value);
                        if (id != -1) {
                            numInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            default:
                return super.bulkInsert(uri, values);
        }

        getContext().

                getContentResolver().

                notifyChange(uri, null);

        return numInserted;
    }
}
