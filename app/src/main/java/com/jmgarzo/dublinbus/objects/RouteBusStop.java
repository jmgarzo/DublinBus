package com.jmgarzo.dublinbus.objects;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.jmgarzo.dublinbus.data.DublinBusContract;

/**
 * Created by jmgarzo on 30/07/17.
 */

public class RouteBusStop implements Parcelable {
    private long routeBusId;
    private long busStopId;
    private int recordOrder;
    private boolean isNew;

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


    public int getRecordOrder() {
        return recordOrder;
    }

    public void setRecordOrder(int recordOrder) {
        this.recordOrder = recordOrder;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RouteBusStopEntry.ROUTE_ID,getRouteBusId());
        contentValues.put(DublinBusContract.RouteBusStopEntry.BUS_STOP_ID,getBusStopId());
        contentValues.put(DublinBusContract.RouteBusStopEntry.RECORD_ORDER,getRecordOrder());
        int iIsNew = (isNew) ? 1 : 0;
        contentValues.put(DublinBusContract.RouteBusStopEntry.IS_NEW,iIsNew);


        return contentValues;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.routeBusId);
        dest.writeLong(this.busStopId);
        dest.writeInt(this.recordOrder);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
    }

    protected RouteBusStop(Parcel in) {
        this.routeBusId = in.readLong();
        this.busStopId = in.readLong();
        this.recordOrder = in.readInt();
        this.isNew = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RouteBusStop> CREATOR = new Parcelable.Creator<RouteBusStop>() {
        @Override
        public RouteBusStop createFromParcel(Parcel source) {
            return new RouteBusStop(source);
        }

        @Override
        public RouteBusStop[] newArray(int size) {
            return new RouteBusStop[size];
        }
    };
}
