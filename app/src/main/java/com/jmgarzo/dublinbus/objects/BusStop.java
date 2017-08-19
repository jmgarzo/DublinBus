package com.jmgarzo.dublinbus.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class BusStop  implements Parcelable {
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

    public void cursorToBusStop(Cursor cursor) {

        if(cursor.moveToFirst()){
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
        }
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

        return contentValues;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(number);
        dest.writeString(displayStopId);
        dest.writeString(shortName);
        dest.writeString(shortNameLocalized);
        dest.writeString(fullName);
        dest.writeString(fullNameLocalized);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(lastUpdated);
        dest.writeByte((byte) (isFavourite ? 1 : 0));;

    }




    public BusStop(Parcel parcel) {
        id = parcel.readInt();
        number = parcel.readString();
        displayStopId = parcel.readString();
        shortName = parcel.readString();
        shortNameLocalized = parcel.readString();
        fullName = parcel.readString();
        fullNameLocalized = parcel.readString();
        latitude = parcel.readString();
        longitude = parcel.readString();
        lastUpdated = parcel.readString();
        isFavourite = parcel.readByte() != 0;

    }

    public static final Parcelable.Creator<BusStop> CREATOR = new Parcelable.Creator<BusStop>() {

        @Override
        public BusStop createFromParcel(Parcel parcel) {
            return new BusStop(parcel);
        }

        @Override
        public BusStop[] newArray(int size) {
            return new BusStop[0];
        }
    };

    public int describeContents() {
        return hashCode();
    }
}
