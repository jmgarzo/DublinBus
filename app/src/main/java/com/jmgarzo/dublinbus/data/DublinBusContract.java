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
}
