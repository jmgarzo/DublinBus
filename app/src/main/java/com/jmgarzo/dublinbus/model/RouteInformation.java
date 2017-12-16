package com.jmgarzo.dublinbus.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 30/07/17.
 */

public class RouteInformation implements Parcelable {

    public RouteInformation(){}

    private long id;
    private String operator;
    private String route;
    private boolean isNew;

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

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RouteInformationEntry.OPERATOR,getOperator());
        contentValues.put(DublinBusContract.RouteInformationEntry.ROUTE,getRoute());
        int iIsNew = (isNew) ? 1 : 0;
        contentValues.put(DublinBusContract.RouteInformationEntry.IS_NEW,iIsNew);


        return contentValues;
    }

    private void cursorToRouteInformation(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_ROUTE_INFORMATION_ID);
        operator = cursor.getString(DBUtils.COL_ROUTE_INFORMATION_OPERATOR);
        route = cursor.getString(DBUtils.COL_ROUTE_INFORMATION_ROUTE);
        isNew = cursor.getInt(DBUtils.COL_ROUTE_INFORMATION_IS_NEW) != 0;


    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.operator);
        dest.writeString(this.route);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
    }

    protected RouteInformation(Parcel in) {
        this.id = in.readLong();
        this.operator = in.readString();
        this.route = in.readString();
        this.isNew = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RouteInformation> CREATOR = new Parcelable.Creator<RouteInformation>() {
        @Override
        public RouteInformation createFromParcel(Parcel source) {
            return new RouteInformation(source);
        }

        @Override
        public RouteInformation[] newArray(int size) {
            return new RouteInformation[size];
        }
    };
}
