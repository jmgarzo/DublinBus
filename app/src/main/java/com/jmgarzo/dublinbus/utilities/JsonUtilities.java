package com.jmgarzo.dublinbus.utilities;

import android.util.Log;

import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.Route;

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


    public static ArrayList<Route> getRouteFromJson(String jsonStr) {

        final String OPERATOR_RESULTS = "results";

        ArrayList<Route> routeList = null;

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

                //TODO: query to save operator_id
                route.setOperator(1);
                route.setOrigin(jsonRoute.getString(ROUTE_ORIGIN));
                route.setOriginLocalized(jsonRoute.getString(ROUTE_ORIGIN_LOCALIZED));
                route.setDestination(jsonRoute.getString(ROUTE_DESTINATION));
                route.setDestinationLocalized(jsonRoute.getString(ROUTE_DESTINATION_LOCALIZED));
                route.setLastUpdated(jsonRoute.getString(ROUTE_LAST_UPDATED));

                routeList.add(route);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return routeList;
    }
}
