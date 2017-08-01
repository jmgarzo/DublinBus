package com.jmgarzo.dublinbus.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class Route implements Parcelable {

    private long id;
    private String timestamp;
    private String name;
    private long operator;
    private String origin;
    private String originLocalized;
    private String destination;
    private String destinationLocalized;
    private String lastUpdated;
    private ArrayList<String> stops;

    public Route() {
    }

    public Route(Cursor cursor, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            cursorToRoute(cursor);
        }
    }

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

    public ArrayList<String> getStops() {
        return stops;
    }

    public void setStops(ArrayList<String> stops) {
        this.stops = stops;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RouteEntry.TIMESTAMP, getTimestamp());
        contentValues.put(DublinBusContract.RouteEntry.NAME, getName());
        contentValues.put(DublinBusContract.RouteEntry.OPERATOR, getOperator());
        contentValues.put(DublinBusContract.RouteEntry.ORIGIN, getOrigin());
        contentValues.put(DublinBusContract.RouteEntry.ORIGIN_LOCALIZED, getOriginLocalized());
        contentValues.put(DublinBusContract.RouteEntry.DESTINATION, getDestination());
        contentValues.put(DublinBusContract.RouteEntry.DESTINATION_LOCALIZED, getDestinationLocalized());
        contentValues.put(DublinBusContract.RouteEntry.LAST_UPDATED, getLastUpdated());

        return contentValues;
    }

    private void cursorToRoute(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_ROUTE_ID);
        timestamp = cursor.getString(DBUtils.COL_ROUTE_TIMESTAMP);
        name = cursor.getString(DBUtils.COL_ROUTE_NAME);
        operator = cursor.getLong(DBUtils.COL_ROUTE_OPERATOR);
        origin = cursor.getString(DBUtils.COL_ROUTE_ORIGIN);
        originLocalized = cursor.getString(DBUtils.COL_ROUTE_ORIGIN_LOCALIZED);
        destination = cursor.getString(DBUtils.COL_ROUTE_DESTINATION);
        destinationLocalized = cursor.getString(DBUtils.COL_ROUTE_DESTINATION_LOCALIZED);
        lastUpdated = cursor.getString(DBUtils.COL_ROUTE_LAST_UPDATE);


    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(timestamp);
        dest.writeString(name);
        dest.writeLong(operator);
        dest.writeString(origin);
        dest.writeString(originLocalized);
        dest.writeString(destination);
        dest.writeString(destinationLocalized);
        dest.writeString(lastUpdated);
        dest.writeStringList(stops);
    }

    public Route(Parcel parcel) {
        id = parcel.readInt();
        timestamp = parcel.readString();
        name = parcel.readString();
        operator = parcel.readLong();
        origin = parcel.readString();
        originLocalized = parcel.readString();
        destination = parcel.readString();
        destinationLocalized = parcel.readString();
        lastUpdated = parcel.readString();
        ArrayList<String> newList = null;
        newList= parcel.createStringArrayList();
        parcel.readStringList(newList);
        stops = new ArrayList<String>();
        parcel.readList(stops, Route.class.getClassLoader());
    }

    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {

        @Override
        public Route createFromParcel(Parcel parcel) {
            return new Route(parcel);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[0];
        }
    };

    public int describeContents() {
        return hashCode();
    }


}
