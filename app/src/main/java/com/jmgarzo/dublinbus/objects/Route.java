package com.jmgarzo.dublinbus.objects;

import android.content.ContentValues;
import android.database.Cursor;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class Route {

    private long id;
    private String timestamp;
    private String name;
    private String direction;
    private long operator;
    private String origin;
    private String originLocalized;
    private String destination;
    private String destinationLocalized;
    private String lastUpdated;

    public Route(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginLocalized() {
        return originLocalized;
    }

    public void setOriginLocalized(String originLocalized) {
        this.originLocalized = originLocalized;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationLocalized() {
        return destinationLocalized;
    }

    public void setDestinationLocalized(String destinationLocalized) {
        this.destinationLocalized = destinationLocalized;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }





    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RouteEntry.TIMESTAMP,getTimestamp());
        contentValues.put(DublinBusContract.RouteEntry.NAME,getName());
        contentValues.put(DublinBusContract.RouteEntry.DIRECTION,getDirection());
        contentValues.put(DublinBusContract.RouteEntry.OPERATOR,getOperator());
        contentValues.put(DublinBusContract.RouteEntry.ORIGIN,getOrigin());
        contentValues.put(DublinBusContract.RouteEntry.ORIGIN_LOCALIZED,getOriginLocalized());
        contentValues.put(DublinBusContract.RouteEntry.DESTINATION,getDestination());
        contentValues.put(DublinBusContract.RouteEntry.DESTINATION_LOCALIZED,getDestinationLocalized());
        contentValues.put(DublinBusContract.RouteEntry.LAST_UPDATED,getLastUpdated());

        return contentValues;
    }

    private void cursorToOperator(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_ROUTE_ID);
        timestamp = cursor.getString(DBUtils.COL_ROUTE_TIMESTAMP);
        name = cursor.getString(DBUtils.COL_ROUTE_NAME);
        direction = cursor.getString(DBUtils.COL_ROUTE_DIRECTION);
        operator = cursor.getLong(DBUtils.COL_ROUTE_OPERATOR);
        origin = cursor.getString(DBUtils.COL_ROUTE_ORIGIN);
        originLocalized = cursor.getString(DBUtils.COL_ROUTE_ORIGIN_LOCALIZED);
        destination = cursor.getString(DBUtils.COL_ROUTE_DESTINATION);
        destinationLocalized = cursor.getString(DBUtils.COL_ROUTE_DESTINATION_LOCALIZED);
        lastUpdated = cursor.getString(DBUtils.COL_ROUTE_LAST_UPDATE);



    }



}
