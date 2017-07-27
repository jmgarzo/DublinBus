package com.jmgarzo.dublinbus.utilities;

import com.jmgarzo.dublinbus.data.DublinBusContract;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class DBUtils {


    public static final String[] MOVIE_COLUMNS = {
            DublinBusContract.OperatorEntry._ID,
            DublinBusContract.OperatorEntry.REFERENCE,
            DublinBusContract.OperatorEntry.NAME,
            DublinBusContract.OperatorEntry.DESCRIPTION

    };

    public static final int COL_OPERATOR_ID = 0;
    public static final int COL_OPERATOR_REFERENCE = 1;
    public static final int COL_OPERATOR_NAME = 2;
    public static final int COL_OPERATOR_DESCRIPTION = 3;


}
