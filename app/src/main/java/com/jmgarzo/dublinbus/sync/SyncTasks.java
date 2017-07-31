package com.jmgarzo.dublinbus.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.objects.RouteBusStop;
import com.jmgarzo.dublinbus.objects.RouteInformation;
import com.jmgarzo.dublinbus.sync.services.BusStopInformationService;
import com.jmgarzo.dublinbus.sync.services.OperatorInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteInformationService;
import com.jmgarzo.dublinbus.sync.services.RouteListInformationService;
import com.jmgarzo.dublinbus.utilities.DBUtils;
import com.jmgarzo.dublinbus.utilities.NetworkUtilities;

import java.util.ArrayList;

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

                //TODO: Mirar a ver si quiero borrar todo lo anterior antes de insertar
                int inserted = contentResolver.bulkInsert(DublinBusContract.OperatorEntry.CONTENT_URI,
                        contentValues);
                if(inserted>0){
                    DBUtils.setIsFilledOperatorInformation(context,true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

                //TODO: Mirar a ver si quiero borrar todo lo anterior antes de insertar
                int inserted = contentResolver.bulkInsert(DublinBusContract.BusStopEntry.CONTENT_URI,
                        contentValues);
                if(inserted>0){
                    DBUtils.setIsFilledBusStop(context,true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void syncRoute(Context context) {
        try {
            ArrayList<Route> routeList = NetworkUtilities.getRouteInformation(context);

            if (routeList != null && routeList.size() > 0) {
                ContentValues[] contentValues = new ContentValues[routeList.size()];
                for (int i = 0; i < routeList.size(); i++) {
                    Route route = routeList.get(i);
                    contentValues[i] = route.getContentValues();
                }

                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.bulkInsert(DublinBusContract.RouteEntry.CONTENT_URI,
                        contentValues);


                for (int j = 0; j < routeList.size(); j++) {
                    Route route = routeList.get(j);
                    long routeId = DBUtils.getRouteId(context, route.getName(), route.getDestination());

                    ArrayList<String> stopsList = route.getStops();
                    if (stopsList != null && stopsList.size() > 0) {
                        ContentValues[] busStopContentValues = new ContentValues[stopsList.size()];
                        for (int k = 0; k < stopsList.size(); k++) {
                            Long stopId = DBUtils.getBusStopId(context, stopsList.get(k));

                            if(stopId != -1l) {
                                ContentValues cv = new ContentValues();
                                cv.put(DublinBusContract.RouteBusStopEntry.ROUTE_ID.toString(), routeId);
                                cv.put(DublinBusContract.RouteBusStopEntry.BUS_STOP_ID, stopId);

//                            contentResolver.insert(DublinBusContract.RouteBusStopEntry.CONTENT_URI, cv);
                                busStopContentValues[k] = cv;
                            }else{
                                Log.e(LOG_TAG,"StopId not found");
                            }
                        }
                        int inserted = contentResolver.bulkInsert(DublinBusContract.RouteBusStopEntry.CONTENT_URI,busStopContentValues);
                        if(inserted>0){
                            DBUtils.setIsFilledRoute(context,true);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                if (numInsert>0){
                    DBUtils.setIsFilledRouteInformation(context,true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncDB(Context context){

        if(!DBUtils.isFilledOperatorInformation(context)) {
            Intent intentOperatorInformationService = new Intent(context, OperatorInformationService.class);
            context.startService(intentOperatorInformationService);
        }
        if(!DBUtils.isFilledRouteInformation(context)) {
            Intent intentRouteListInformationService = new Intent(context, RouteListInformationService.class);
            context.startService(intentRouteListInformationService);
        }

//        Intent intentRealTimeBusInformation = new Intent(getActivity(), RealTimeBusInformationService.class);
//        getContext().startService(intentRealTimeBusInformation);
        //tvAnswer.setText(sAnswer);

//        Intent intentTimeTableBusInformation = new Intent(getActivity(), TimeTableBusInformationService.class);
//        getContext().startService(intentTimeTableBusInformation);


//        Intent intentFullTimetableBusInformation = new Intent(getActivity(), FullTimetableBusInformationService.class);
//        getContext().startService(intentFullTimetableBusInformation);


        if(!DBUtils.isFilledBusStop(context)) {
            Intent intentBusStopInformationService = new Intent(context, BusStopInformationService.class);
            context.startService(intentBusStopInformationService);
        }

        if(!DBUtils.isFilledRoute(context)) {
            Intent intentRouteInformationService = new Intent(context, RouteInformationService.class);
            context.startService(intentRouteInformationService);
        }

//        Intent intentOperatorInformationService = new Intent(getActivity(), OperatorInformationService.class);
//        getContext().startService(intentOperatorInformationService);

    }

}
