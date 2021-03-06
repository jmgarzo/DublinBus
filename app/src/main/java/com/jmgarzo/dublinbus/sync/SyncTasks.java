package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.jmgarzo.dublinbus.R;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.data.DublinBusDBHelper;
import com.jmgarzo.dublinbus.model.BusStop;
import com.jmgarzo.dublinbus.model.Operator;
import com.jmgarzo.dublinbus.model.RealTimeStop;
import com.jmgarzo.dublinbus.model.Route;
import com.jmgarzo.dublinbus.model.RouteInformation;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

import java.io.IOException;
import java.util.ArrayList;

import static android.util.Log.e;

/**
 * Created by jmgarzo on 25/07/17.
 */

public class SyncTasks {
    private static int numUpdates = 0;


    public static void syncDB(Context context) {

        deleteNewValues(context);


        ContentResolver contentResolver = context.getContentResolver();
        DBUtils.setIsUpdateDBActive(context, true);
        DBUtils.setIsErrorOnUpdateDB(context, false);

        try {
            ArrayList<ContentValues> operatorContentValues = getSyncOperators(context);
            if (null != operatorContentValues) {
                int operatorsInserted = contentResolver.bulkInsert(DublinBusContract.OperatorEntry.CONTENT_URI,
                        operatorContentValues.toArray(new ContentValues[operatorContentValues.size()]));
                Log.d(LOG_TAG, "Operator inserted: " + operatorsInserted);
            } else {
                DBUtils.setIsErrorOnUpdateDB(context, true);
            }
        } catch (Exception e) {
            DBUtils.setIsErrorOnUpdateDB(context, true);
            Log.e(LOG_TAG, e.toString());
        }

        if (!DBUtils.isErrorOnUpdateDB(context)) {
            try {
                ArrayList<ContentValues> busStopContentValues = getSyncBusStops(context);
                if (null != busStopContentValues) {
                    int busStopInserted = contentResolver.bulkInsert(DublinBusContract.BusStopEntry.CONTENT_URI,
                            busStopContentValues.toArray(new ContentValues[busStopContentValues.size()]));
                    Log.d(LOG_TAG, "Bus Stop inserted: " + busStopInserted);
                } else {
                    DBUtils.setIsErrorOnUpdateDB(context, true);
                }
            } catch (Exception e) {
                DBUtils.setIsErrorOnUpdateDB(context, true);
                Log.e(LOG_TAG, e.toString());
            }
        }

        if (!DBUtils.isErrorOnUpdateDB(context)) {
            try {
                ArrayList<ContentValues> routeInformationContentValues = getSyncRouteInformation(context);
                if (null != routeInformationContentValues) {
                    int routeInformationInserted = contentResolver.bulkInsert(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                            routeInformationContentValues.toArray(new ContentValues[routeInformationContentValues.size()]));
                    Log.d(LOG_TAG, "Route Information Inserted: " + routeInformationInserted);
                } else {
                    DBUtils.setIsErrorOnUpdateDB(context, true);
                }
            } catch (Exception e) {
                DBUtils.setIsErrorOnUpdateDB(context, true);
                Log.e(LOG_TAG, e.toString());
            }
        }


        ArrayList<Route> newTotalRoutes = getNewSyncRoute(context);
        if (!DBUtils.isErrorOnUpdateDB(context)) {

            ArrayList<ContentValues> newRouteContentValues = new ArrayList<>();
            for (Route route : newTotalRoutes) {
                newRouteContentValues.add(route.getContentValues());
            }

            try {

                if (!newRouteContentValues.isEmpty()) {
                    int routeInserted = contentResolver.bulkInsert(DublinBusContract.RouteEntry.CONTENT_URI,
                            newRouteContentValues.toArray(new ContentValues[newRouteContentValues.size()]));
                    Log.d(LOG_TAG, "Route Inserted: " + routeInserted);
                } else {
                    DBUtils.setIsErrorOnUpdateDB(context, true);
                }
            } catch (SQLiteException e) {
                DBUtils.setIsErrorOnUpdateDB(context, true);
                Log.e(LOG_TAG, e.toString());
            }
        }


        //ROUTE BUS STOP

        if (!DBUtils.isErrorOnUpdateDB(context)) {
            ArrayList<ContentValues> routeBusStopContentValues = new ArrayList<>();
            for (int j = 0; j < newTotalRoutes.size(); j++) {
                ArrayList<ContentValues> routeBusStop = DBUtils.getRouteBusStop(context, newTotalRoutes.get(j));
                if (null != routeBusStop) {
                    routeBusStopContentValues.addAll(routeBusStop);
                }
            }

            try {
                if (!routeBusStopContentValues.isEmpty()) {
                    int routeBusStopInserted = contentResolver.bulkInsert(DublinBusContract.RouteBusStopEntry.CONTENT_URI,
                            routeBusStopContentValues.toArray(new ContentValues[routeBusStopContentValues.size()]));
                    Log.d(LOG_TAG, "Route Bus Stop Inserted: " + routeBusStopInserted);
                } else {
                    DBUtils.setIsErrorOnUpdateDB(context, true);
                }
            } catch (SQLiteException e) {
                Log.e(LOG_TAG, "Error inserting Route Bus Stop: ", e);
                //TODO: check if we need stop the update
                //DBUtils.setIsErrorOnUpdateDB(context, true);
            }
        }
        if (DBUtils.isErrorOnUpdateDB(context)) {
            deleteNewValues(context);

        } else {
            DBUtils.setIsFirstStartDB(context,false);
            setFavouritesBusStop(context);
            deleteOldValues(context);
            updateNewValues(context);
            numUpdates++;
            Log.d(LOG_TAG, "NumUpdates: " + numUpdates);
        }
        DBUtils.setIsUpdateDBActive(context, false);
        DBUtils.setIsErrorOnUpdateDB(context, false);

    }

    private static void setFavouritesBusStop(Context context) {

        ArrayList<BusStop> oldFavouritesList = getFavouriteBusStop(context);
        int favoritesUpdated = 0;

        if (null != oldFavouritesList && !oldFavouritesList.isEmpty()) {
            String selection = DublinBusContract.BusStopEntry._ID + " = ? AND " +
                    DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? AND " +
                    DublinBusContract.BusStopEntry.IS_NEW + " = ? ";
            for (BusStop oldFavourite : oldFavouritesList) {

                BusStop newFavourite = getNewFavouriteFromOld(context, oldFavourite);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DublinBusContract.BusStopEntry.IS_FAVOURITE, 1);
                favoritesUpdated += context.getContentResolver().update(
                        DublinBusContract.BusStopEntry.CONTENT_URI,
                        contentValues,
                        selection,
                        new String[]{Integer.toString(newFavourite.getId()), "0", "1"});
            }
        }
        Log.d(LOG_TAG, "Favorites Updates " + favoritesUpdated);
    }


    private static BusStop getNewFavouriteFromOld(Context context, BusStop oldFavourite) {
        BusStop newFavoriteBusStop = null;
        String selection = DublinBusContract.BusStopEntry.NUMBER + " = ? AND " +
                DublinBusContract.BusStopEntry.IS_NEW + " = ? ";
        Cursor cursor = context.getContentResolver().query(
                DublinBusContract.BusStopEntry.CONTENT_URI,
                DBUtils.BUS_STOP_COLUMNS,
                selection,
                new String[]{oldFavourite.getNumber(), "1"},
                null);
        if (null != cursor && cursor.moveToFirst()) {
            newFavoriteBusStop = new BusStop();
            newFavoriteBusStop.cursorToBusStop(cursor);
        }
        return newFavoriteBusStop;
    }

    private static void updateNewValues(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues routeBusStopCv = new ContentValues();
        routeBusStopCv.put(DublinBusContract.RouteBusStopEntry.IS_NEW, 0);
        int routeBusStopUpdated = contentResolver.update(DublinBusContract.RouteBusStopEntry.CONTENT_URI, routeBusStopCv, null, null);
        Log.d(LOG_TAG, "Route Updated: " + routeBusStopUpdated);

        ContentValues operatorCv = new ContentValues();
        operatorCv.put(DublinBusContract.OperatorEntry.IS_NEW, 0);
        int operatorsUpdated = contentResolver.update(DublinBusContract.OperatorEntry.CONTENT_URI, operatorCv, null, null);
        Log.d(LOG_TAG, "Operators Updated : " + operatorsUpdated);


        ContentValues busStopCv = new ContentValues();
        busStopCv.put(DublinBusContract.BusStopEntry.IS_NEW, 0);
        int busStopsUpdated = contentResolver.update(DublinBusContract.BusStopEntry.CONTENT_URI, busStopCv, null, null);
        Log.d(LOG_TAG, "Bus Stop Updated: " + busStopsUpdated);

        ContentValues routeInformationCv = new ContentValues();
        routeInformationCv.put(DublinBusContract.RouteInformationEntry.IS_NEW, 0);
        int routeInformationUpdated = contentResolver.update(DublinBusContract.RouteInformationEntry.CONTENT_URI, routeInformationCv, null, null);
        Log.d(LOG_TAG, "Route Information Updated: " + routeInformationUpdated);

        ContentValues routeCv = new ContentValues();
        routeCv.put(DublinBusContract.RouteEntry.IS_NEW, 0);
        int routeUpdated = contentResolver.update(DublinBusContract.RouteEntry.CONTENT_URI, routeCv, null, null);
        Log.d(LOG_TAG, "Route Updated: " + routeUpdated);


    }

    private static void deleteOldValues(Context context) {
        deleteValuesDB(context, 0);
    }

    private static void deleteNewValues(Context context) {
        deleteValuesDB(context, 1);
    }

    private static void deleteValuesDB(Context context, int isNew) {
        ContentResolver contentResolver = context.getContentResolver();
        String sIsNew = Integer.toString(isNew);

        int routeBusStopDeleted = contentResolver.delete(DublinBusContract.RouteBusStopEntry.CONTENT_URI,
                DublinBusContract.RouteBusStopEntry.IS_NEW + " = ? ",
                new String[]{sIsNew});
        Log.d(LOG_TAG, "Route Bus Stop Deleted: " + routeBusStopDeleted);

        int operatorsDeleted = contentResolver.delete(DublinBusContract.OperatorEntry.CONTENT_URI,
                DublinBusContract.OperatorEntry.IS_NEW + " = ? ",
                new String[]{sIsNew});
        Log.d(LOG_TAG, "Operator Deleted: " + operatorsDeleted);

        int busStopDeleted = contentResolver.delete(DublinBusContract.BusStopEntry.CONTENT_URI,
                DublinBusContract.BusStopEntry.IS_NEW + " = ? ",
                new String[]{sIsNew});
        Log.d(LOG_TAG, "Bus Stop Deleted: " + busStopDeleted);


        int routeInformationDeleted = contentResolver.delete(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                DublinBusContract.RouteInformationEntry.IS_NEW + " = ? ",
                new String[]{sIsNew});
        Log.d(LOG_TAG, "Route Information Deleted: " + routeInformationDeleted);


        int routeDeleted = contentResolver.delete(DublinBusContract.RouteEntry.CONTENT_URI,
                DublinBusContract.RouteEntry.IS_NEW + " = ? ",
                new String[]{sIsNew});
        Log.d(LOG_TAG, "Route Deleted: " + routeDeleted);


    }


    private static String LOG_TAG = SyncTasks.class.getSimpleName();

    public static ArrayList<ContentValues> getSyncOperators(Context context) {
        ArrayList<ContentValues> contentValuesList = null;

        try {
            ArrayList<Operator> operatorList = NetworkUtilities.getOperatorInformation();

            if (operatorList != null && operatorList.size() > 0) {
                contentValuesList = new ArrayList<>();
                for (int i = 0; i < operatorList.size(); i++) {
                    Operator operator = operatorList.get(i);
                    contentValuesList.add(operator.getContentValues());
                }
            }
        } catch (Exception e) {
            DBUtils.setIsErrorOnUpdateDB(context, true);
            e(LOG_TAG, e.toString());
        }
        return contentValuesList;

    }


    public static ArrayList<ContentValues> getSyncBusStops(Context context) {
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
            DBUtils.setIsErrorOnUpdateDB(context, true);
            Log.e(LOG_TAG, e.toString());

        }
        return busStopCVList;
    }


    public static ArrayList<Route> getNewSyncRoute(Context context) {
        ArrayList<Route> totalRouteList = new ArrayList<>();
        try {
            String selection = DublinBusContract.RouteInformationEntry.OPERATOR + " = ?  AND " +
                    DublinBusContract.RouteInformationEntry.IS_NEW + " = ? ";
            String[] selectionArgs = new String[]{context.getString(R.string.constant_dublin_bus_operator), "1"};
            Cursor cursor = context.getContentResolver().query(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                    DBUtils.ROUTE_INFORMATION_COLUMNS,
                    selection,
                    selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ArrayList<Route> routeList = NetworkUtilities.getRouteInformation(context, cursor.getString(DBUtils.COL_ROUTE_INFORMATION_ROUTE));
                    //TODO: DELETE! JUST FOR DEBUG
                    if (routeList.size() > 2) {
                        Log.d(LOG_TAG, "Route: " + routeList.get(1).getName() + " Have " + routeList.size() + " results");
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
            Log.e(LOG_TAG, e.toString());
            DBUtils.setIsErrorOnUpdateDB(context, true);
        }
        return totalRouteList;

    }

    public static ArrayList<ContentValues> getSyncRouteInformation(Context context) {
        ArrayList<ContentValues> contentValuesList = null;
        try {
            ArrayList<RouteInformation> routeInformationList = NetworkUtilities.getRouteListInformation(context);

            if (routeInformationList != null && routeInformationList.size() > 0) {
                contentValuesList = new ArrayList<>();
                for (int i = 0; i < routeInformationList.size(); i++) {
                    RouteInformation routeInformation = routeInformationList.get(i);
                    contentValuesList.add(routeInformation.getContentValues());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValuesList;
    }


    public static void syncRealTimeStop(Context context, String stopId) {
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

    public static void deleteRealTimeBusStop(Context context) {
        int deleted = context.getContentResolver().delete(DublinBusContract.RealTimeStopEntry.CONTENT_URI, null, null);
        Log.d(LOG_TAG, "Time real stop bus deleted: " + deleted);
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
            busStop.setNew(false);


            int favoriteUpdates = 0;
            favoriteUpdates = context.getContentResolver().update(
                    DublinBusContract.BusStopEntry.CONTENT_URI,
                    busStop.getContentValues(),
                    DublinBusContract.BusStopEntry._ID + " = ? ",
                    new String[]{Integer.toString(busStop.getId())});
            if (favoriteUpdates != 0) {
                Log.d(LOG_TAG, favoriteUpdates + " Favourite Update");
            }
        }

        cursor.close();
    }


    public static void deleteFavoriteBusStop(Context context, String busStopNumber) {

        String selection = DublinBusContract.BusStopEntry.NUMBER + " = ?  AND "
                + DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ?";
        String[] selectionArgs = {busStopNumber, "1"};

        ContentValues cv = new ContentValues();
        cv.put(DublinBusContract.BusStopEntry.IS_FAVOURITE,0);

        context.getContentResolver().update(
                DublinBusContract.BusStopEntry.CONTENT_URI,
                cv,
                selection,
                selectionArgs);
    }

    public static ArrayList<BusStop> getFavouriteBusStop(Context context) {
        ArrayList<BusStop> favouriteList = null;

        String selection = DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? AND " +
                DublinBusContract.BusStopEntry.IS_NEW + " = ? ";

        Cursor cursor = context.getContentResolver().query(
                DublinBusContract.BusStopEntry.CONTENT_URI,
                DBUtils.BUS_STOP_COLUMNS,
                selection,
                new String[]{"1", "0"},
                null
        );

        if (null != cursor && cursor.moveToFirst()) {
            favouriteList = new ArrayList<>();
            do {
                BusStop favourite = new BusStop();
                favourite.cursorToBusStop(cursor);

                favouriteList.add(favourite);
            } while (cursor.moveToNext());
        }
        return favouriteList;
    }

}
