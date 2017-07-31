package com.jmgarzo.dublinbus.objects;

import android.content.ContentValues;

import com.jmgarzo.dublinbus.data.DublinBusContract;

/**
 * Created by jmgarzo on 30/07/17.
 */

public class RouteBusStop {
    private long routeBusId;
    private long busStopId;

    public RouteBusStop(){

    }

    public long getRouteBusId() {
        return routeBusId;
    }

    public void setRouteBusId(long routeBusId) {
        this.routeBusId = routeBusId;
    }

    public long getBusStopId() {
        return busStopId;
    }

    public void setBusStopId(long busStopId) {
        this.busStopId = busStopId;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RouteBusStopEntry.ROUTE_ID,getRouteBusId());
        contentValues.put(DublinBusContract.RouteBusStopEntry.BUS_STOP_ID,getBusStopId());

        return contentValues;
    }
}
