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


    public static final class OperatorEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_OPERATOR).build();

        public static final String TABLE_NAME = "operator";
        public static final String _ID = "_id";
        public static final String REFERENCE="reference";
        public static final String NAME="name";
        public static final String DESCRIPTION="description";

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

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
    public static final class RouteEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTE).build();
        public static final String TABLE_NAME = "route";
        public static final String TIMESTAMP = "timestamp";
        public static final String NAME = "name";
        public static final String DIRECTION = "direction";
        public static final String OPERATOR = "operator";
        public static final String ORIGIN = "origin";
        public static final String ORIGIN_LOCALIZED = "origin_localized";
        public static final String DESTINATION = "destination";
        public static final String DESTINATION_LOCALIZED = "destination_localized";
        public static final String LAST_UPDATED = "last_updated";

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }


}
