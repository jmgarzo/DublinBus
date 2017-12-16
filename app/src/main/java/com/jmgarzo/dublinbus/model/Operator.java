package com.jmgarzo.dublinbus.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.utilities.DBUtils;


/**
 * Created by jmgarzo on 27/07/17.
 */

public class Operator implements Parcelable {
    private long id;
    private String reference;
    private String name;
    private String description;
    private boolean isNew;

    public Operator(){}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.OperatorEntry.REFERENCE,getReference());
        contentValues.put(DublinBusContract.OperatorEntry.NAME,getName());
        contentValues.put(DublinBusContract.OperatorEntry.DESCRIPTION,getDescription());
        int iIsNew = (isNew) ? 1 : 0;
        contentValues.put(DublinBusContract.OperatorEntry.IS_NEW,iIsNew);

        return contentValues;
    }

    private void cursorToOperator(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_OPERATOR_ID);
        reference = cursor.getString(DBUtils.COL_OPERATOR_REFERENCE);
        name = cursor.getString(DBUtils.COL_OPERATOR_NAME);
        description = cursor.getString(DBUtils.COL_OPERATOR_DESCRIPTION);
        isNew = cursor.getInt(DBUtils.COL_OPERATOR_IS_NEW) != 0;



    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.reference);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
    }

    protected Operator(Parcel in) {
        this.id = in.readLong();
        this.reference = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.isNew = in.readByte() != 0;
    }

    public static final Creator<Operator> CREATOR = new Creator<Operator>() {
        @Override
        public Operator createFromParcel(Parcel source) {
            return new Operator(source);
        }

        @Override
        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };
}
