package com.jmgarzo.dublinbus.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.jmgarzo.dublinbus.R;
import com.jmgarzo.dublinbus.data.DublinBusContract;
import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.RealTimeStop;
import com.jmgarzo.dublinbus.objects.Route;
import com.jmgarzo.dublinbus.objects.RouteInformation;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.jmgarzo.dublinbus.R.string.empty_real_time_stop_server_down;
import static com.jmgarzo.dublinbus.R.string.empty_real_time_stop_server_error;

/**
 * Created by jmgarzo on 25/07/17.
 */

public class NetworkUtilities {

    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();

    private static final String BASE_URL = "https://data.dublinked.ie/cgi-bin/rtpi/";

    private static final String REAL_TIME_BUS_INFORMATION_PATH = "realtimebusinformation";
    private static final String TIME_TABLE_INFORMATION_PATH = "timetableinformation";
    private static final String BUS_STOP_INFORMATION_PATH = "busstopinformation";
    private static final String ROUTE_INFORMATION_PATH = "routeinformation";
    private static final String OPERATOR_INFORMATION_PATH = "operatorinformation";
    private static final String ROUTE_LIST_INFORMATION_PATH = "routelistinformation";

    private static final String STOP_ID_PARAM = "stopid";
    private static final String ROUTER_ID_PARAM = "routeid";
    private static final String FORMAT_PARAM = "format";
    private static final String OPERATOR_PARAM = "operator";


    private static final String TYPE_PARAM = "type";
    private static final String JSON_VALUE_PARAM = "json";
    private static final String TYPE_DAY_PARAM = "day";
    private static final String TYPE_WEEK_PARAM = "week";
    private static final String DUBLIN_BUS_OPERATOR_PARAM = "bac";


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    //3.4.1 Retrieve Real Time Bus Information
    /*http://[rtpiserver]/realtimebusinformation
    realtimebusinformation?stopid=[stopid]&routeid=[routeid]
            ?stopid=[stopid]&routeid=[routeid]&maxresults&operator=
            [operator]&format=[format] */

    public static ArrayList<RealTimeStop> getRealTimeStop(Context context, String stopid) {
        String response = "";
        ArrayList<RealTimeStop> realTimeStopsList = null;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(REAL_TIME_BUS_INFORMATION_PATH)
                .appendQueryParameter(STOP_ID_PARAM, stopid)
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            response = getResponseFromHttpUrl(url);

            realTimeStopsList = JsonUtilities.getRealTimeStopFromJson(context, response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            if (response.equalsIgnoreCase("")) {
                DBUtils.setRealTimeConnectionStatus(context, DBUtils.REAL_TIME_STATUS_SERVER_DOWN);
            }
        }

        return realTimeStopsList;
    }
//    public static String getRealTimeBusInformation(String stopId) {
//
//        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
//                .appendEncodedPath(REAL_TIME_BUS_INFORMATION_PATH)
//                .appendQueryParameter(STOP_ID_PARAM, "7602")
//                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
//                .build();
//
//        URL url = null;
//        try {
//            url = new URL(builtUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        String response = "";
//        try {
//
//            response = getResponseFromHttpUrl(url);
////            moviesList = getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.MOST_POPULAR_REGISTRY_TYPE);
////
////            jsonMoviesResponse=NetworksUtils.getResponseFromHttpUrl(moviesURLTopRate);
////            moviesList.addAll(getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.TOP_RATE_REGISTRY_TYPE));
//        } catch (IOException e) {
//            Log.e(LOG_TAG, e.toString());
//        }
//
//
//        return response;
//    }


    //3.4.2 Retrieve Timetable Bus Information by Date
    /*
    http://[rtpiserver]/timetableinformation?
    /timetableinformation?type=day&stopid=[stopid]&routeid=[routeid]&datetime=[Dat
    stopid=[stopid]&routeid=[routeid]&datetime=[Date time]&maxresults=[maxresults]&operator=[operator]&format=[format]
     */


    public static String getTimetableBusInformationByDate(Context context) {

        String response = "";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(TIME_TABLE_INFORMATION_PATH)
                .appendQueryParameter(TYPE_PARAM, TYPE_DAY_PARAM)
                .appendQueryParameter(STOP_ID_PARAM, "7025")
                .appendQueryParameter(ROUTER_ID_PARAM, "39")
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {

            response = getResponseFromHttpUrl(url);
//            moviesList = getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.MOST_POPULAR_REGISTRY_TYPE);
//
//            jsonMoviesResponse=NetworksUtils.getResponseFromHttpUrl(moviesURLTopRate);
//            moviesList.addAll(getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.TOP_RATE_REGISTRY_TYPE));
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return response;
    }

    //3.4.3Retrieve Full Timetable Bus Information
    //http://[rtpiserver]/timetableinformation?timetableinformation?type=week&stopid=[stopid]&routeid=[routeid]&
    //stopid=[stopid]&routeid=[routeid]&format=[format]

    public static String getFullTimeTableBusInformation(Context context) {
        String response = "";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(TIME_TABLE_INFORMATION_PATH)
                .appendQueryParameter(TYPE_PARAM, TYPE_WEEK_PARAM)
                .appendQueryParameter(STOP_ID_PARAM, "7025")
                .appendQueryParameter(ROUTER_ID_PARAM, "39")
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {

            response = getResponseFromHttpUrl(url);
//            moviesList = getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.MOST_POPULAR_REGISTRY_TYPE);
//
//            jsonMoviesResponse=NetworksUtils.getResponseFromHttpUrl(moviesURLTopRate);
//            moviesList.addAll(getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.TOP_RATE_REGISTRY_TYPE));
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }


        return response;

    }


    //3.4.4 Retrieve Bus Stop Information
    //http://[rtpiserver]/busstopinformation?stopid=[stopid]&stopname=[stopnamestopname]&format=[format]

    public static ArrayList<BusStop> getBusStopInformation() throws IOException, JSONException {

        String response = "";
        ArrayList<BusStop> busStopList = null;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(BUS_STOP_INFORMATION_PATH)
                .appendQueryParameter(OPERATOR_PARAM, DUBLIN_BUS_OPERATOR_PARAM)
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }
        try {
            response = getResponseFromHttpUrl(url);
            busStopList = JsonUtilities.getBusStopsFromJson(response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }

        return busStopList;
    }


    //3.4.5 Retrieve Route Information
    //http://[rtpiserver]/routeinformation?routeid=[route]&operator=[operator]&operator=[operator]&format=[format]

    public static ArrayList<Route> getRouteInformation(Context context, String routeName) throws IOException, JSONException {
        String response = "";
        ArrayList<Route> routeList = null;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(ROUTE_INFORMATION_PATH)
                .appendQueryParameter(ROUTER_ID_PARAM, routeName)
                .appendQueryParameter(OPERATOR_PARAM, DUBLIN_BUS_OPERATOR_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }
        try {
            response = getResponseFromHttpUrl(url);
            routeList = JsonUtilities.getRouteFromJson(context, response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }
        return routeList;
    }

    public static ArrayList<Route> getAllRouteInformation(Context context) throws IOException, JSONException {
        String response = "";
        ArrayList<Route> routeListPerQuery = null;
        ArrayList<Route> routeListTotal = null;

        Cursor cursor = context.getContentResolver().query(DublinBusContract.RouteInformationEntry.CONTENT_URI,
                DBUtils.ROUTE_INFORMATION_COLUMNS,
                null,
                null,
                null);


        if (cursor != null && cursor.moveToFirst()) {
            routeListTotal = new ArrayList<>();
            do {
                routeListPerQuery = getRouteInformation(context, cursor.getString(DBUtils.COL_ROUTE_INFORMATION_ROUTE));

                if (null != routeListPerQuery && routeListPerQuery.size() > 0) {
                    routeListTotal.addAll(routeListPerQuery);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return routeListTotal;
    }


    //3.4.6 Operator Information
    //http://[rtpiserver]/operatorinformation?format=[format]

    public static ArrayList<Operator> getOperatorInformation() throws IOException, JSONException {
        String response = "";
        ArrayList<Operator> operatorList = null;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(OPERATOR_INFORMATION_PATH)
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }

        try {
            response = getResponseFromHttpUrl(url);
            operatorList = JsonUtilities.getOperatorsFromJson(response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        } catch (JSONException e) {
            throw e;
        }

        return operatorList;
    }

    //3.4.7 Route List Information
    //http://[rtpiserver]/routelistinformation? operator=[operator]&format=[format]
    public static ArrayList<RouteInformation> getRouteListInformation(Context context) throws IOException, JSONException {

        ArrayList<RouteInformation> routeInformationList = null;
        String response = "";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(ROUTE_LIST_INFORMATION_PATH)
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }

        try {
            response = getResponseFromHttpUrl(url);
            routeInformationList = JsonUtilities.getRouteInformationFromJson(response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
            throw e;
        }
        return routeInformationList;
    }

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }


    static public int getErrorMessage(Context context) {
        // if cursor is empty, why? do we have an invalid location
        int message = R.string.empty_real_time_stop_list;
        int status = DBUtils.getRealTimeConnectionStatus(context);
        switch (status) {
            case DBUtils.REAL_TIME_STATUS_NO_RESULTS:
                message = R.string.empty_real_time_stop_server_error;
                break;
            case DBUtils.REAL_TIME_STATUS_MISSING_PARAMETER:
                message = empty_real_time_stop_server_down;
                break;
            case DBUtils.REAL_TIME_STATUS_INVALID_PARAMETER:
                message = empty_real_time_stop_server_down;
                break;
            case DBUtils.REAL_TIME_STATUS_SCHEDULED_DOWN_TIME:
                message = empty_real_time_stop_server_down;
                break;
            case DBUtils.REAL_TIME_STATUS_UNEXPECTED_SERVER_ERROR:
                message = empty_real_time_stop_server_down;
                break;
            case DBUtils.REAL_TIME_STATUS_SERVER_DOWN:
                message = empty_real_time_stop_server_down;
                break;
            case DBUtils.REAL_TIME_STATUS_SERVER_INVALID:
                message = empty_real_time_stop_server_error;
                break;
            case DBUtils.REAL_TIME_STATUS_UNKNOWN:
                message = empty_real_time_stop_server_down;
                break;
            case DBUtils.REAL_TIME_STATUS_NETWORK_NOT_AVAILABLE:
                message = R.string.empty_real_time_stop_no_network;
                break;
            default:
                message = R.string.empty_real_time_stop_list;
        }
        return message;
    }


}


