package com.jmgarzo.dublinbus.model;

import android.content.ContentValues;

import com.jmgarzo.dublinbus.data.DublinBusContract;

/**
 * Created by jmgarzo on 14/08/2017.
 */

public class RealTimeStop {


    private long id;
    private String stopNumber;
    private String arrivalDateTime;
    private String dueTime;
    private String departureDateTime;
    private String departureDueTime;
    private String scheduleArrivalDateTime;
    private String scheduledDepartureDateTime;
    private String destination;
    private String destinationLocalized;
    private String origin;
    private String originLocalized;
    private String direction;
    private String operator;
    private String additionalInformation;
    private String lowFloorStatus;
    private String route;
    private String sourceTimestamp;
    private String monitored;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(String stopNumber) {
        this.stopNumber = stopNumber;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getDepartureDueTime() {
        return departureDueTime;
    }

    public void setDepartureDueTime(String departureDueTime) {
        this.departureDueTime = departureDueTime;
    }

    public String getScheduleArrivalDateTime() {
        return scheduleArrivalDateTime;
    }

    public void setScheduleArrivalDateTime(String scheduleArrivalDateTime) {
        this.scheduleArrivalDateTime = scheduleArrivalDateTime;
    }

    public String getScheduledDepartureDateTime() {
        return scheduledDepartureDateTime;
    }

    public void setScheduledDepartureDateTime(String scheduledDepartureDateTime) {
        this.scheduledDepartureDateTime = scheduledDepartureDateTime;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getLowFloorStatus() {
        return lowFloorStatus;
    }

    public void setLowFloorStatus(String lowFloorStatus) {
        this.lowFloorStatus = lowFloorStatus;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getSourceTimestamp() {
        return sourceTimestamp;
    }

    public void setSourceTimestamp(String sourceTimestamp) {
        this.sourceTimestamp = sourceTimestamp;
    }

    public String getMonitored() {
        return monitored;
    }

    public void setMonitored(String monitored) {
        this.monitored = monitored;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DublinBusContract.RealTimeStopEntry.STOP_NUMBER,getStopNumber());
        contentValues.put(DublinBusContract.RealTimeStopEntry.ARRIVAL_DATE_TIME,getArrivalDateTime());
        contentValues.put(DublinBusContract.RealTimeStopEntry.DUE_TIME,getDueTime());
        contentValues.put(DublinBusContract.RealTimeStopEntry.DEPARTURE_DATE_TIME,getDepartureDateTime());
        contentValues.put(DublinBusContract.RealTimeStopEntry.DEPARTURE_DUE_TIME,getDepartureDueTime());
        contentValues.put(DublinBusContract.RealTimeStopEntry.SCHEDULED_ARRIVAL_DATE_TIME,getScheduleArrivalDateTime());
        contentValues.put(DublinBusContract.RealTimeStopEntry.SCHEDULED_DEPARTURE_DATE_TIME,getScheduledDepartureDateTime());
        contentValues.put(DublinBusContract.RealTimeStopEntry.DESTINATION,getDestination());
        contentValues.put(DublinBusContract.RealTimeStopEntry.DESTINATION_LOCALIZED,getDestinationLocalized());
        contentValues.put(DublinBusContract.RealTimeStopEntry.ORIGIN,getOrigin());
        contentValues.put(DublinBusContract.RealTimeStopEntry.ORIGIN_LOCALIZED,getOriginLocalized());
        contentValues.put(DublinBusContract.RealTimeStopEntry.DIRECTION,getDirection());
        contentValues.put(DublinBusContract.RealTimeStopEntry.OPERATOR,getOperator());
        contentValues.put(DublinBusContract.RealTimeStopEntry.ADDITIONAL_INFORMATION,getAdditionalInformation());
        contentValues.put(DublinBusContract.RealTimeStopEntry.LOW_FLOOR_STATUS,getLowFloorStatus());
        contentValues.put(DublinBusContract.RealTimeStopEntry.ROUTE,getRoute());
        contentValues.put(DublinBusContract.RealTimeStopEntry.SOURCE_TIMESTAMP,getSourceTimestamp());
        contentValues.put(DublinBusContract.RealTimeStopEntry.MONITORED,getMonitored());

        return contentValues;
    }


}
