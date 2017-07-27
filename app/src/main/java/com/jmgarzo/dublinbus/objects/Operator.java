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

public class Operator implements Parcelable {
    private long id;
    private String reference;
    private String name;
    private String description;

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



    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.OperatorEntry.REFERENCE,getReference());
        contentValues.put(DublinBusContract.OperatorEntry.NAME,getName());
        contentValues.put(DublinBusContract.OperatorEntry.DESCRIPTION,getDescription());

        return contentValues;
    }

    private void cursorToMovie(Cursor cursor) {

        id = cursor.getInt(DBUtils.COL_OPERATOR_ID);
        reference = cursor.getString(DBUtils.COL_OPERATOR_REFERENCE);
        name = cursor.getString(DBUtils.COL_OPERATOR_NAME);
        description = cursor.getString(DBUtils.COL_OPERATOR_DESCRIPTION);


    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(reference);
        dest.writeString(name);
        dest.writeString(description);
    }

    public Operator(Parcel parcel) {
        id = parcel.readInt();
        reference = parcel.readString();
        name = parcel.readString();
        description = parcel.readString();
    }

    public static final Parcelable.Creator<Operator> CREATOR = new Parcelable.Creator<Operator>() {

        @Override
        public Operator createFromParcel(Parcel parcel) {
            return new Operator(parcel);
        }

        @Override
        public Operator[] newArray(int size) {
            return new Operator[0];
        }
    };

    public int describeContents() {
        return hashCode();
    }

}
