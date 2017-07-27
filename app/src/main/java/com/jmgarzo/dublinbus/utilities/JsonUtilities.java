package com.jmgarzo.dublinbus.utilities;

import android.util.Log;

import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;

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





    public static ArrayList<Operator> getOperatorsFromJson(String jsonStr) {

        final String OPERATOR_RESULTS = "results";

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

        final String OPERATOR_RESULTS = "results";

        ArrayList<BusStop> busStopList = null;

        JSONObject busStopJson = null;
        try {
            busStopJson = new JSONObject(jsonStr);
            JSONArray busStopArray = busStopJson.getJSONArray(OPERATOR_RESULTS);
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
}
