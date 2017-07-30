package com.jmgarzo.dublinbus.objects;

import android.content.ContentValues;
import android.database.Cursor;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 30/07/17.
 */

public class RouteInformation {

    public RouteInformation(){}

    private long id;
    private String operator;
    private String route;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RouteInformationEntry.OPERATOR,getOperator());
        contentValues.put(DublinBusContract.RouteInformationEntry.ROUTE,getRoute());

        return contentValues;
    }

    private void cursorToRouteInformation(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_ROUTE_INFORMATION_ID);
        operator = cursor.getString(DBUtils.COL_ROUTE_INFORMATION_OPERATOR);
        route = cursor.getString(DBUtils.COL_ROUTE_INFORMATION_ROUTE);

    }
}
