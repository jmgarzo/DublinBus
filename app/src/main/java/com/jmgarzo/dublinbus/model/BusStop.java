package com.jmgarzo.dublinbus.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class BusStop implements Parcelable {
    private int id;
    private String number;
    private String displayStopId;
    private String shortName;
    private String shortNameLocalized;
    private String fullName;
    private String fullNameLocalized;
    private String latitude;
    private String longitude;
    private String lastUpdated;
    private boolean isFavourite;
    private String alias;
    private boolean isNew;
    private ArrayList<Route> routesList;

    public BusStop(){}

    public BusStop(Cursor cursor, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            cursorToBusStop(cursor);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisplayStopId() {
        return displayStopId;
    }

    public void setDisplayStopId(String displayStopId) {
        this.displayStopId = displayStopId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortNameLocalized() {
        return shortNameLocalized;
    }

    public void setShortNameLocalized(String shortNameLocalized) {
        this.shortNameLocalized = shortNameLocalized;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullNameLocalized() {
        return fullNameLocalized;
    }

    public void setFullNameLocalized(String fullNameLocalized) {
        this.fullNameLocalized = fullNameLocalized;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<Route> getRoutesList() {
        return routesList;
    }

    public void setRoutesList(ArrayList<Route> routesList) {
        this.routesList = routesList;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public LatLng getLatLng(){
        return new LatLng(Double.valueOf(getLatitude()),Double.valueOf(getLongitude()));
    }



    public void cursorToBusStop(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_BUS_STOP_ID);
        number = cursor.getString(DBUtils.COL_BUS_STOP_NUMBER);
        displayStopId = cursor.getString(DBUtils.COL_BUS_STOP_DISPLAY_STOP_ID);
        shortName = cursor.getString(DBUtils.COL_BUS_STOP_SHORTNAME);
        shortNameLocalized = cursor.getString(DBUtils.COL_BUS_STOP_SHORT_NAME_LOCALIZED);
        fullName = cursor.getString(DBUtils.COL_BUS_STOP_FULL_NAME);
        fullNameLocalized = cursor.getString(DBUtils.COL_BUS_STOP_FULL_NAME_LOCALIZED);
        latitude = cursor.getString(DBUtils.COL_BUS_STOP_LATITUDE);
        longitude = cursor.getString(DBUtils.COL_BUS_STOP_LONGITUDE);
        lastUpdated = cursor.getString(DBUtils.COL_BUS_STOP_LAST_UPDATED);
        isFavourite = cursor.getInt(DBUtils.COL_BUS_STOP_IS_FAVORITE) != 0;
        alias = cursor.getString(DBUtils.COL_BUS_STOP_IS_ALIAS);
        isNew = cursor.getInt(DBUtils.COL_BUS_STOP_IS_NEW) != 0;

    }


    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.BusStopEntry.NUMBER,getNumber());
        contentValues.put(DublinBusContract.BusStopEntry.DISPLAY_STOP_ID,getDisplayStopId());
        contentValues.put(DublinBusContract.BusStopEntry.SHORT_NAME,getShortName());
        contentValues.put(DublinBusContract.BusStopEntry.SHORT_NAME_LOCALIZED,getShortNameLocalized());
        contentValues.put(DublinBusContract.BusStopEntry.FULL_NAME,getFullName());
        contentValues.put(DublinBusContract.BusStopEntry.FULL_NAME_LOCALIZED,getFullNameLocalized());
        contentValues.put(DublinBusContract.BusStopEntry.LATITUDE,getLatitude());
        contentValues.put(DublinBusContract.BusStopEntry.LONGITUDE,getLongitude());
        contentValues.put(DublinBusContract.BusStopEntry.LAST_UPDATED,getLastUpdated());
        int iIsFavorite = (isFavourite) ? 1 : 0;
        contentValues.put(DublinBusContract.BusStopEntry.IS_FAVOURITE, iIsFavorite);
        contentValues.put(DublinBusContract.BusStopEntry.ALIAS, getAlias());
        int iIsNew = (isNew) ? 1 : 0;
        contentValues.put(DublinBusContract.BusStopEntry.IS_NEW,iIsNew);


        return contentValues;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.number);
        dest.writeString(this.displayStopId);
        dest.writeString(this.shortName);
        dest.writeString(this.shortNameLocalized);
        dest.writeString(this.fullName);
        dest.writeString(this.fullNameLocalized);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.lastUpdated);
        dest.writeByte(this.isFavourite ? (byte) 1 : (byte) 0);
        dest.writeString(this.alias);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.routesList);
    }

    protected BusStop(Parcel in) {
        this.id = in.readInt();
        this.number = in.readString();
        this.displayStopId = in.readString();
        this.shortName = in.readString();
        this.shortNameLocalized = in.readString();
        this.fullName = in.readString();
        this.fullNameLocalized = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.lastUpdated = in.readString();
        this.isFavourite = in.readByte() != 0;
        this.alias = in.readString();
        this.isNew = in.readByte() != 0;
        this.routesList = in.createTypedArrayList(Route.CREATOR);
    }

    public static final Creator<BusStop> CREATOR = new Creator<BusStop>() {
        @Override
        public BusStop createFromParcel(Parcel source) {
            return new BusStop(source);
        }

        @Override
        public BusStop[] newArray(int size) {
            return new BusStop[size];
        }
    };
}
