package com.jmgarzo.dublinbus.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DublinBusContract {


    public static final String CONTENT_AUTHORITY ="com.jmgarzo.dublinbus";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_OPERATOR="operator";
    public static final String PATH_BUS_STOP = "bus_stop";
    public static final String PATH_ROUTE = "route";
    public static final String PATH_ROUTE_BUS_STOP="route_bus_stop";
    public static final String PATH_ROUTE_INFORMATION="route_information";
    public static final String PATH_REAL_TIME_STOP = "real_time_stop";
    public static final String PATH_ROUTE_PER_BUS_STOP = "route_per_bus_stop";


    public static final class OperatorEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_OPERATOR).build();

        public static final String TABLE_NAME = "operator";
        public static final String _ID = "_id";
        public static final String REFERENCE="reference";
        public static final String NAME="name";
        public static final String DESCRIPTION="description";
        public static final String IS_NEW = "is_new";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class BusStopEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BUS_STOP).build();
        public static final String TABLE_NAME = "bus_stop";
        public static final String _ID = "_id";
        public static final String NUMBER ="number";
        public static final String DISPLAY_STOP_ID="display_stop_id";
        public static final String SHORT_NAME ="short_name";
        public static final String SHORT_NAME_LOCALIZED ="short_name_localized";
        public static final String FULL_NAME="full_name";
        public static final String FULL_NAME_LOCALIZED = "full_name_localized";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LAST_UPDATED = "last_updated";
        public static final String IS_FAVOURITE =  "is_favorite";
        public static final String ALIAS = "alias";
        public static final String IS_NEW = "is_new";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static Uri buildBusStopWithRouteId(String routeId) {
            return CONTENT_URI.buildUpon().appendPath(routeId).build();
        }

        public static String getRouteIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
    public static final class RouteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTE).build();
        public static final String TABLE_NAME = "route";
        public static final String _ID = "_id";
        public static final String TIMESTAMP = "timestamp";
        public static final String NAME = "name";
        public static final String OPERATOR = "operator";
        public static final String ORIGIN = "origin";
        public static final String ORIGIN_LOCALIZED = "origin_localized";
        public static final String DESTINATION = "destination";
        public static final String DESTINATION_LOCALIZED = "destination_localized";
        public static final String LAST_UPDATED = "last_updated";
        public static final String IS_NEW = "is_new";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }

    public static final class RouteBusStopEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTE_BUS_STOP).build();

        public static final String TABLE_NAME = "route_bus_stop";
        public static final String ROUTE_ID = "route_id";
        public static final String BUS_STOP_ID = "bus_stop_id";
        public static final String RECORD_ORDER = "record_insert_order";
        public static final String IS_NEW = "route_bus_stop_is_new";


        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;


    }
    public static final class RouteInformationEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTE_INFORMATION).build();
        public static final String TABLE_NAME = "route_information";
        public static final String _ID = "_id";
        public static final String OPERATOR = "operator";
        public static final String ROUTE = "route";
        public static final String IS_NEW = "is_new";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }

    public static final class RealTimeStopEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REAL_TIME_STOP).build();
        public static final String TABLE_NAME = "real_time_stop";
        public static final String _ID = "_id";
        public static final String STOP_NUMBER = "stopid";
        public static final String ARRIVAL_DATE_TIME = "arrivaldatetime";
        public static final String DUE_TIME = "duetime";
        public static final String DEPARTURE_DATE_TIME = "departuredatetime";
        public static final String DEPARTURE_DUE_TIME = "departureduetime";
        public static final String SCHEDULED_ARRIVAL_DATE_TIME = "scheduledarrivaldatetime";
        public static final String SCHEDULED_DEPARTURE_DATE_TIME = "scheduleddeparturedatetime";
        public static final String DESTINATION = "destination";
        public static final String DESTINATION_LOCALIZED = "destinationlocalized";
        public static final String ORIGIN = "origin";
        public static final String ORIGIN_LOCALIZED = "originlocalized";
        public static final String DIRECTION = "direction";
        public static final String OPERATOR = "operator";
        public static final String ADDITIONAL_INFORMATION = "additionalinformation";
        public static final String LOW_FLOOR_STATUS = "lowfloorstatus";
        public static final String ROUTE = "route";
        public static final String SOURCE_TIMESTAMP = "sourcetimestamp";
        public static final String MONITORED = "monitored";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }


    public static final class RoutesPerBusStopEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTE_PER_BUS_STOP).build();


        public static final String TABLE_NAME = RouteBusStopEntry.TABLE_NAME +
                " INNER JOIN " + RouteEntry.TABLE_NAME +
                " ON " +
                RouteBusStopEntry.TABLE_NAME + "."+ RouteBusStopEntry.ROUTE_ID  +
                " = "+ RouteEntry.TABLE_NAME + "."+ RouteEntry._ID;


        public static final String NAME = "name";
        public static final String OPERATOR = "operator";
        public static final String ORIGIN = "origin";
        public static final String ORIGIN_LOCALIZED = "origin_localized";
        public static final String DESTINATION = "destination";
        public static final String DESTINATION_LOCALIZED = "destination_localized";

        public static final String COLUMN_ROUTE_ID = RouteEntry.TABLE_NAME + "." +
                RouteEntry._ID;
        public static final String COLUMN_ROUTE_NAME = RouteEntry.TABLE_NAME + "." +
                RouteEntry.NAME;
        public static final String COLUMN_ROUTE_ORIGIN = RouteEntry.TABLE_NAME + "." +
                RouteEntry.ORIGIN;
        public static final String COLUMN_ROUTE_ORIGIN_LOCALIZED = RouteEntry.TABLE_NAME + "." +
                RouteEntry.ORIGIN_LOCALIZED;
        public static final String COLUMN_ROUTE_DESTINATION = RouteEntry.TABLE_NAME + "." +
                RouteEntry.DESTINATION;
        public static final String COLUMN_ROUTE_DESTINATION_LOCALIZED = RouteEntry.TABLE_NAME + "." +
                RouteEntry.DESTINATION_LOCALIZED;


        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;



    }


}
