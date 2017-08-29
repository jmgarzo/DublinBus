package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

import java.io.IOException;
import java.util.ArrayList;

import static android.util.Log.e;

/**
 * Created by jmgarzo on 25/07/17.
 */

public class SyncTasks {


    public static void syncDB(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        ArrayList<ContentValues> operatorContentValues = getSyncOperators(context);
        int operatorsInserted = contentResolver.bulkInsert(DublinBusContract.OperatorEntry.CONTENT_URI,
                operatorContentValues.toArray(new ContentValues[operatorContentValues.size()]));
        Log.d(LOG_TAG, "Operator inserted: " + operatorsInserted);

        ArrayList<ContentValues> busStopContententValues = getSyncBusStops(context);
        int busStopInserted = contentResolver.bulkInsert(DublinBusContract.BusStopEntry.CONTENT_URI,
                busStopContententValues.toArray(new ContentValues[busStopContententValues.size()]));
        Log.d(LOG_TAG, "Bus Stop inserted: " + busStopInserted);


        ArrayList<ContentValues> routeInformationContentValues = getSyncRouteInformation(context);
        int routeInformationInserted = contentResolver.bulkInsert(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                routeInformationContentValues.toArray(new ContentValues[routeInformationContentValues.size()]));
        Log.d(LOG_TAG, "Route Information Inserted: " + routeInformationInserted);


        ArrayList<Route> newTotalRoutes = getNewSyncRoute(context);
        ArrayList<ContentValues> newRouteContentValues = new ArrayList<>();
        for (Route route : newTotalRoutes) {
            newRouteContentValues.add(route.getContentValues());
        }

        int routeInserted = contentResolver.bulkInsert(DublinBusContract.RouteEntry.CONTENT_URI,
                newRouteContentValues.toArray(new ContentValues[newRouteContentValues.size()]));
        Log.d(LOG_TAG, "Route Inserted: " + routeInserted);


        //ROUTE BUS STOP

        ArrayList<ContentValues> routeBusStopContentValues = new ArrayList<>();
        for (int j = 0; j < newTotalRoutes.size(); j++) {
            ArrayList<ContentValues> routeBusStop = DBUtils.getRouteBusStop(context, newTotalRoutes.get(j));
            if (null != routeBusStop) {
                routeBusStopContentValues.addAll(routeBusStop);
            }
        }

        int routeBusStopInserted = contentResolver.bulkInsert(DublinBusContract.RouteBusStopEntry.CONTENT_URI,
                routeBusStopContentValues.toArray(new ContentValues[routeBusStopContentValues.size()]));
        Log.d(LOG_TAG, "Route Bus Stop Inserted: " + routeBusStopInserted);


        deleteOldValues(context);
        updateNewValues(context);



    }

    private static void updateNewValues(Context context){
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues routeBusStopCv = new ContentValues();
        routeBusStopCv.put(DublinBusContract.RouteBusStopEntry.IS_NEW,0);
        int routeBusStopUpdated = contentResolver.update(DublinBusContract.RouteBusStopEntry.CONTENT_URI,routeBusStopCv,null,null);
        Log.d(LOG_TAG,"Route Updated: " + routeBusStopUpdated);

        ContentValues operatorCv = new ContentValues();
        operatorCv.put(DublinBusContract.OperatorEntry.IS_NEW,0);
        int operatorsUpdated = contentResolver.update(DublinBusContract.OperatorEntry.CONTENT_URI,operatorCv,null,null);
        Log.d(LOG_TAG,"Operators Updated : " + operatorsUpdated);


        ContentValues busStopCv = new ContentValues();
        busStopCv.put(DublinBusContract.BusStopEntry.IS_NEW,0);
        int busStopsUpdated = contentResolver.update(DublinBusContract.BusStopEntry.CONTENT_URI,busStopCv,null,null);
        Log.d(LOG_TAG,"Bus Stop Updated: " + busStopsUpdated);

        ContentValues routeInformationCv = new ContentValues();
        routeInformationCv.put(DublinBusContract.RouteInformationEntry.IS_NEW,0);
        int routeInformationUpdated = contentResolver.update(DublinBusContract.RouteInformationEntry.CONTENT_URI,routeInformationCv,null,null);
        Log.d(LOG_TAG,"Route Information Updated: " + routeInformationUpdated);

        ContentValues routeCv = new ContentValues();
        routeCv.put(DublinBusContract.RouteEntry.IS_NEW,0);
        int routeUpdated = contentResolver.update(DublinBusContract.RouteEntry.CONTENT_URI,routeCv,null,null);
        Log.d(LOG_TAG,"Route Updated: " + routeUpdated);



    }

    private static void deleteOldValues(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        int routeBusStopDeleted = contentResolver.delete(DublinBusContract.RouteBusStopEntry.CONTENT_URI,
                DublinBusContract.RouteBusStopEntry.IS_NEW + " = ? ",
                new String[]{"0"});
        Log.d(LOG_TAG,"Route Bus Stop Deleted: " + routeBusStopDeleted);

        int operatorsDeleted = contentResolver.delete(DublinBusContract.OperatorEntry.CONTENT_URI,
                DublinBusContract.OperatorEntry.IS_NEW + " = ? ",
                new String[]{"0"});
        Log.d(LOG_TAG,"Operator Deleted: " + operatorsDeleted);

        int busStopDeleted = contentResolver.delete(DublinBusContract.BusStopEntry.CONTENT_URI,
                DublinBusContract.BusStopEntry.IS_NEW + " = ? AND " + DublinBusContract.BusStopEntry.IS_FAVOURITE + " = ? ",
                new String[]{"0", "0"});
        Log.d(LOG_TAG,"Bus Stop Deleted: " + busStopDeleted);



        int routeInformationDeleted = contentResolver.delete(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                DublinBusContract.RouteInformationEntry.IS_NEW + " = ? ",
                new String[]{"0"});
        Log.d(LOG_TAG,"Route Information Deleted: " + routeInformationDeleted);


        int routeDeleted = contentResolver.delete(DublinBusContract.RouteEntry.CONTENT_URI,
                DublinBusContract.RouteEntry.IS_NEW + " = ? ",
                new String[]{"0"});
        Log.d(LOG_TAG,"Route Deleted: " + routeDeleted);






    }


    private static String LOG_TAG = SyncTasks.class.getSimpleName();

    synchronized public static ArrayList<ContentValues> getSyncOperators(Context context) {
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
            e(LOG_TAG, e.toString());
        }
        return contentValuesList;

    }


    synchronized public static ArrayList<ContentValues> getSyncBusStops(Context context) {
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


    synchronized public static ArrayList<Route> getNewSyncRoute(Context context) {
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

    synchronized public static ArrayList<ContentValues> getSyncRouteInformation(Context context) {
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
