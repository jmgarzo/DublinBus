package com.jmgarzo.dublinbus.utilities;

import android.util.Log;

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
}
