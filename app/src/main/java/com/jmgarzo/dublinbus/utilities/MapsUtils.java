package com.jmgarzo.dublinbus.utilities;

import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.jmgarzo.dublinbus.model.BusStop;
import com.jmgarzo.dublinbus.model.Route;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 10/4/2017.
 */

public class MapsUtils {

    private static final String LOG_TAG = MapsUtils.class.getSimpleName();

    public static ArrayList<LatLng> getLatLngBusStop(ArrayList<BusStop> busStopsList) {
        ArrayList<LatLng> latLngBusStop = null;
        if (null != busStopsList && !busStopsList.isEmpty()) {
            latLngBusStop = new ArrayList<>();
            for (BusStop bs : busStopsList) {
                latLngBusStop.add(bs.getLatLng());
            }
        }
        return (latLngBusStop);
    }


    public static ArrayList<BusStop> getBusStopListFromBusStopsAndRoute(Cursor data) {
        ArrayList<BusStop> busStopArrayList = new ArrayList<>();
        int currentBusStopId = -1;

        try {
            if (data.moveToFirst()) {
                BusStop bs = null;
                do {
                    if (currentBusStopId == data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID)) {
                        currentBusStopId = data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID);
                        if (null != bs.getRoutesList()) {
                            bs.getRoutesList().add(getNewRoute(data));
                        }
                    } else {
                        if (bs != null) {
                            busStopArrayList.add(bs);
                        }
                        currentBusStopId = data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID);
                        bs = getNewBusStop(data);
                        bs.setRoutesList(new ArrayList<Route>());
                        bs.getRoutesList().add(getNewRoute(data));
                    }
                } while (data.moveToNext());
            }
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return busStopArrayList;
    }

    private static Route getNewRoute(Cursor data) {
        Route route = new Route();

        route.setId(data.getInt(DBUtils.COL_BUS_AND_ROUTE_ROUTE_ID));
        route.setTimestamp(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_TIMESTAMP));
        route.setName(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_NAME));
        route.setOperator(data.getLong(DBUtils.COL_BUS_AND_ROUTE_ROUTE_OPERATOR));
        route.setOrigin(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_ORIGIN));
        route.setOriginLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_ORIGIN_LOCALIZED));
        route.setDestination(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_DESTINATION));
        route.setDestinationLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_DESTINATION_LOCALIZED));
        route.setLastUpdated(data.getString(DBUtils.COL_BUS_AND_ROUTE_ROUTE_LAST_UPDATE));
        route.setNew(data.getInt(DBUtils.COL_BUS_AND_ROUTE_ROUTE_IS_NEW) != 0);

        return route;
    }

    private static BusStop getNewBusStop(Cursor data) {
        BusStop bs = new BusStop();
        bs.setId(data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_ID));
        bs.setNumber(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_NUMBER));
        bs.setDisplayStopId(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_DISPLAY_STOP_ID));
        bs.setShortName(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_SHORTNAME));
        bs.setShortNameLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_SHORT_NAME_LOCALIZED));
        bs.setFullName(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_FULL_NAME));
        bs.setFullNameLocalized(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_FULL_NAME_LOCALIZED));
        bs.setLatitude(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_LATITUDE));
        bs.setLongitude(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_LONGITUDE));
        bs.setLastUpdated(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_LAST_UPDATED));
        bs.setFavourite(data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_IS_FAVORITE) != 0);
        bs.setAlias(data.getString(DBUtils.COL_BUS_AND_ROUTE_STOP_IS_ALIAS));
        bs.setNew(data.getInt(DBUtils.COL_BUS_AND_ROUTE_STOP_IS_NEW) != 0);

        bs.setRoutesList(new ArrayList<Route>());

        return bs;
    }
}
