package com.jmgarzo.dublinbus.utilities;

import android.content.Context;
import android.util.Log;

import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.RealTimeStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.objects.RouteInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 27/07/17.
 */

public class JsonUtilities {

    private static final String LOG_TAG = JsonUtilities.class.getSimpleName();

    //OPERATOR JSON FIELDS
    private static final String OPERATOR_REFERENCE = "operatorreference";
    private static final String OPERATOR_NAME = "operatorname";
    private static final String OPERATOR_DESCRIPTION="operatordescription";
    private static final String OPERATOR_RESULTS = "results";

    //BUS STOP FIELDS
    private static final String BUS_STOP_NUMBER="stopid";
    private static final String BUS_STOP_DISPLAY_STOP_ID="displaystopid";
    private static final String BUS_STOP_SHORT_NAME="shortname";
    private static final String BUS_STOP_SHORT_NAME_LOCALIZED="shortnamelocalized";
    private static final String BUS_STOP_FULL_NAME="fullname";
    private static final String BUS_STOP_FULL_NAME_LOCALIZED="fullnamelocalized";
    private static final String BUS_STOP_LATITUDE="latitude";
    private static final String BUS_STOP_LONGITUDE="longitude";
    private static final String BUS_STOP_LAST_UPDATED="lastupdated";
    private static final String BUS_STOP_RESULTS = "results";


    //ROUTE FIELDS
    private static final String ROUTE_TIMESTAMP="timestamp";
    private static final String ROUTE_NAME = "route";
    private static final String ROUTE_OPERATOR = "operator";
    private static final String ROUTE_ORIGIN = "origin";
    private static final String ROUTE_ORIGIN_LOCALIZED="originlocalized";
    private static final String ROUTE_DESTINATION="destination";
    private static final String ROUTE_DESTINATION_LOCALIZED="destinationlocalized";
    private static final String ROUTE_LAST_UPDATED ="lastupdated";
    private static final String ROUTE_RESULTS = "results";
    private static final String ROUTE_STOPS = "stops";


    //OPERATOR JSON FIELDS
    private static final String ROUTE_INFORMATION_OPERATOR = "operator";
    private static final String ROUTE_INFORMATION_ROUTE = "route";
    private static final String ROUTE_INFORMATION_RESULTS = "results";

    //REAL TIME STOP FIELDS

    private static final String REAL_TIME_STOP_RESULTS="results";

    private static final String REAL_TIME_STOP_NUMBER="stopid";
    private static final String REAL_TIME_ARRIVAL_DATE_TIME = "arrivaldatetime";
    private static final String REAL_TIME_DUE_TIME = "duetime";
    private static final String REAL_TIME_DEPARTURE_DATE_TIME = "departuredatetime";
    private static final String REAL_TIME_DEPARTURE_DUE_TIME = "departureduetime";
    private static final String REAL_TIME_SCHEDULED_ARRIVAL_DATE_TIME = "scheduledarrivaldatetime";
    private static final String REAL_TIME_SCHEDULED_DEPARTURE_DATE_TIME = "scheduleddeparturedatetime";
    private static final String REAL_TIME_DESTINATION = "destination";
    private static final String REAL_TIME_DESTINATION_LOCALIZED = "destinationlocalized";
    private static final String REAL_TIME_ORIGIN = "origin";
    private static final String REAL_TIME_ORIGIN_LOCALIZED = "originlocalized";
    private static final String REAL_TIME_DIRECTION = "direction";
    private static final String REAL_TIME_OPERATOR = "operator";
    private static final String REAL_TIME_ADDITIONAL_INFORMATION = "additionalinformation";
    private static final String REAL_TIME_LOW_FLOOR_STATUS = "lowfloorstatus";
    private static final String REAL_TIME_ROUTE = "route";
    private static final String REAL_TIME_SOURCE_TIMESTAMP = "sourcetimestamp";
    private static final String REAL_TIME_MONITORED = "monitored";

    //COMMON FIELDS
    private static final String ERROR_CODE="errorcode";
    private static final String ERROR_MESSAGE="errormessage";












    public static ArrayList<RealTimeStop> getRealTimeStopFromJson(Context context ,String jsonStr) {


        ArrayList<RealTimeStop> realTimeStopList = null;

        JSONObject realTimeStopJson = null;
        String stopId;
        String errorCode;
        String errorMessage;
        try {
            realTimeStopJson = new JSONObject(jsonStr);
            stopId = realTimeStopJson.getString(REAL_TIME_STOP_NUMBER);
            errorCode= realTimeStopJson.getString(ERROR_CODE);
            if(null!=errorCode){
                DBUtils.setRealTimeConnectionStatus(context,Integer.valueOf(errorCode));
            }
            //TODO:Show de error message in logs and search for other error case
            errorMessage = realTimeStopJson.getString(ERROR_MESSAGE);
            JSONArray realTimeStopArray = realTimeStopJson.getJSONArray(REAL_TIME_STOP_RESULTS);
            realTimeStopList = new ArrayList<>();
            for (int i = 0; i < realTimeStopArray.length(); i++) {
                JSONObject jsonRealTimeStop = realTimeStopArray.getJSONObject(i);

                RealTimeStop realTimeStop = new RealTimeStop();

                realTimeStop.setStopNumber(stopId);
                realTimeStop.setArrivalDateTime(jsonRealTimeStop.getString(REAL_TIME_ARRIVAL_DATE_TIME));
                realTimeStop.setDueTime(jsonRealTimeStop.getString(REAL_TIME_DUE_TIME));
                realTimeStop.setDepartureDateTime(jsonRealTimeStop.getString(REAL_TIME_DEPARTURE_DATE_TIME));
                realTimeStop.setDepartureDueTime(jsonRealTimeStop.getString(REAL_TIME_DEPARTURE_DUE_TIME));
                realTimeStop.setScheduleArrivalDateTime(jsonRealTimeStop.getString(REAL_TIME_SCHEDULED_ARRIVAL_DATE_TIME));
                realTimeStop.setScheduledDepartureDateTime(jsonRealTimeStop.getString(REAL_TIME_SCHEDULED_DEPARTURE_DATE_TIME));
                realTimeStop.setDestination(jsonRealTimeStop.getString(REAL_TIME_DESTINATION));
                realTimeStop.setDestinationLocalized(jsonRealTimeStop.getString(REAL_TIME_DESTINATION_LOCALIZED));
                realTimeStop.setOrigin(jsonRealTimeStop.getString(REAL_TIME_ORIGIN));
                realTimeStop.setOriginLocalized(jsonRealTimeStop.getString(REAL_TIME_ORIGIN_LOCALIZED));
                realTimeStop.setDirection(jsonRealTimeStop.getString(REAL_TIME_DIRECTION));
                realTimeStop.setOperator(jsonRealTimeStop.getString(REAL_TIME_OPERATOR));
                realTimeStop.setAdditionalInformation(jsonRealTimeStop.getString(REAL_TIME_ADDITIONAL_INFORMATION));
                realTimeStop.setLowFloorStatus(jsonRealTimeStop.getString(REAL_TIME_LOW_FLOOR_STATUS));
                realTimeStop.setRoute(jsonRealTimeStop.getString(REAL_TIME_ROUTE));
                realTimeStop.setSourceTimestamp(jsonRealTimeStop.getString(REAL_TIME_SOURCE_TIMESTAMP));
                realTimeStop.setMonitored(jsonRealTimeStop.getString(REAL_TIME_MONITORED));

                realTimeStopList.add(realTimeStop);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return realTimeStopList;
    }


    public static ArrayList<Operator> getOperatorsFromJson(String jsonStr) {


        ArrayList<Operator> operatorList = null;

        JSONObject operatorsJson = null;
        try {
            operatorsJson = new JSONObject(jsonStr);
            JSONArray operatorArray = operatorsJson.getJSONArray(OPERATOR_RESULTS);
            operatorList = new ArrayList<>();
            for (int i = 0; i < operatorArray.length(); i++) {
                JSONObject jsonOperator = operatorArray.getJSONObject(i);

                Operator operator = new Operator();

                operator.setReference(jsonOperator.getString(OPERATOR_REFERENCE));
                operator.setName(jsonOperator.getString(OPERATOR_NAME));
                operator.setDescription(jsonOperator.getString(OPERATOR_DESCRIPTION));

                operatorList.add(operator);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return operatorList;
    }



    public static ArrayList<BusStop> getBusStopsFromJson(String jsonStr) {



        ArrayList<BusStop> busStopList = null;

        JSONObject busStopJson = null;
        try {
            busStopJson = new JSONObject(jsonStr);
            JSONArray busStopArray = busStopJson.getJSONArray(BUS_STOP_RESULTS);
            busStopList = new ArrayList<>();
            for (int i = 0; i < busStopArray.length(); i++) {
                JSONObject jsonBusStop = busStopArray.getJSONObject(i);

                BusStop busStop = new BusStop();

                busStop.setNumber(jsonBusStop.getString(BUS_STOP_NUMBER));
                busStop.setDisplayStopId(jsonBusStop.getString(BUS_STOP_DISPLAY_STOP_ID));
                busStop.setShortName(jsonBusStop.getString(BUS_STOP_SHORT_NAME));
                busStop.setShortNameLocalized(jsonBusStop.getString(BUS_STOP_SHORT_NAME_LOCALIZED));
                busStop.setFullName(jsonBusStop.getString(BUS_STOP_FULL_NAME));
                busStop.setFullNameLocalized(jsonBusStop.getString(BUS_STOP_FULL_NAME_LOCALIZED));
                busStop.setLatitude(jsonBusStop.getString(BUS_STOP_LATITUDE));
                busStop.setLongitude(jsonBusStop.getString(BUS_STOP_LONGITUDE));
                busStop.setLastUpdated(jsonBusStop.getString(BUS_STOP_LAST_UPDATED));

                busStopList.add(busStop);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return busStopList;
    }


    public static ArrayList<Route> getRouteFromJson(Context context, String jsonStr) {

        final String OPERATOR_RESULTS = "results";

        ArrayList<Route> routeList = null;
        ArrayList<String> stopsList = null;

        JSONObject routeJson = null;
        try {
            routeJson = new JSONObject(jsonStr);
            String name = routeJson.getString(ROUTE_NAME);
            String timestamp = routeJson.getString(ROUTE_TIMESTAMP);

            JSONArray routeArray = routeJson.getJSONArray(ROUTE_RESULTS);
            routeList = new ArrayList<>();
            for (int i = 0; i < routeArray.length(); i++) {


                JSONObject jsonRoute = routeArray.getJSONObject(i);

                Route route = new Route();

                route.setTimestamp(timestamp);
                route.setName(name);

                Long operator = DBUtils.getOperator(context,jsonRoute.getString(ROUTE_OPERATOR));

                //TODO: query to save operator_id
                route.setOperator(operator);
                route.setOrigin(jsonRoute.getString(ROUTE_ORIGIN));
                route.setOriginLocalized(jsonRoute.getString(ROUTE_ORIGIN_LOCALIZED));
                route.setDestination(jsonRoute.getString(ROUTE_DESTINATION));
                route.setDestinationLocalized(jsonRoute.getString(ROUTE_DESTINATION_LOCALIZED));
                route.setLastUpdated(jsonRoute.getString(ROUTE_LAST_UPDATED));

                JSONArray stopsArray = jsonRoute.getJSONArray(ROUTE_STOPS);
                stopsList = new ArrayList<>();

                for (int j = 0; j < stopsArray.length(); j++) {

                    JSONObject jsonStop = stopsArray.getJSONObject(j);
                    stopsList.add(jsonStop.getString(BUS_STOP_NUMBER));
                }
                route.setStops(stopsList);


                routeList.add(route);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return routeList;
    }


    public static ArrayList<RouteInformation> getRouteInformationFromJson(String jsonStr) {


        ArrayList<RouteInformation> routeInformationList = null;

        JSONObject routeInformationJson = null;
        try {
            routeInformationJson = new JSONObject(jsonStr);
            JSONArray routeInformationArray = routeInformationJson.getJSONArray(ROUTE_INFORMATION_RESULTS);
            routeInformationList = new ArrayList<>();
            for (int i = 0; i < routeInformationArray.length(); i++) {
                JSONObject jsonRouteInformation = routeInformationArray.getJSONObject(i);

                RouteInformation routeInformation = new RouteInformation();

                routeInformation.setOperator(jsonRouteInformation.getString(ROUTE_INFORMATION_OPERATOR));
                routeInformation.setRoute(jsonRouteInformation.getString(ROUTE_INFORMATION_ROUTE));

                routeInformationList.add(routeInformation);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return routeInformationList;
    }

}
