package com.jmgarzo.dublinbus.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.objects.Operator;
import com.jmgarzo.dublinbus.objects.Route;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jmgarzo on 25/07/17.
 */

public class NetworkUtilities {

    private static final String LOG_TAG = NetworkUtilities.class.getSimpleName();

    private static final String BASE_URL = "https://data.dublinked.ie/cgi-bin/rtpi/";

    private static final String REAL_TIME_BUS_INFORMATION_PATH = "realtimebusinformation";
    private static final String TIME_TABLE_INFORMATION_PATH ="timetableinformation";
    private static final String BUS_STOP_INFORMATION_PATH = "busstopinformation";
    private static final String ROUTE_INFORMATION_PATH = "routeinformation";
    private static final String OPERATOR_INFORMATION_PATH ="operatorinformation";
    private static final String ROUTE_LIST_INFORMATION_PATH="routelistinformation";

    private static final String STOP_ID_PARAM = "stopid";
    private static final String ROUTER_ID_PARAM="routeid";
    private static final String FORMAT_PARAM = "format";
    private static final String OPERATOR_PARAM="operator";



    private static final String TYPE_PARAM="type";
    private static final String JSON_VALUE_PARAM = "json";
    private static final String TYPE_DAY_PARAM = "day";
    private static final String TYPE_WEEK_PARAM ="week";
    private static final String DUBLIN_BUS_OPERATOR_PARAM="bac";



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


    public static String getRealTimeBusInformation(Context context) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(REAL_TIME_BUS_INFORMATION_PATH)
                .appendQueryParameter(STOP_ID_PARAM, "7602")
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String response="";
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



    //3.4.2 Retrieve Timetable Bus Information by Date
    /*
    http://[rtpiserver]/timetableinformation?
    /timetableinformation?type=day&stopid=[stopid]&routeid=[routeid]&datetime=[Dat
    stopid=[stopid]&routeid=[routeid]&datetime=[Date time]&maxresults=[maxresults]&operator=[operator]&format=[format]
     */


    public static String getTimetableBusInformationByDate(Context context){

        String response="";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(TIME_TABLE_INFORMATION_PATH)
                .appendQueryParameter(TYPE_PARAM,TYPE_DAY_PARAM)
                .appendQueryParameter(STOP_ID_PARAM, "7025")
                .appendQueryParameter(ROUTER_ID_PARAM,"39")
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

    public static String getFullTimeTableBusInformation(Context context){
        String response="";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(TIME_TABLE_INFORMATION_PATH)
                .appendQueryParameter(TYPE_PARAM,TYPE_WEEK_PARAM)
                .appendQueryParameter(STOP_ID_PARAM, "7025")
                .appendQueryParameter(ROUTER_ID_PARAM,"39")
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

    public static ArrayList<BusStop> getBusStopInformation(){

        String response="";
        ArrayList<BusStop> busStopList = null;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(BUS_STOP_INFORMATION_PATH)
//                .appendQueryParameter(STOP_ID_PARAM, "7025")
                .appendQueryParameter(OPERATOR_PARAM,DUBLIN_BUS_OPERATOR_PARAM)
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
            busStopList = JsonUtilities.getBusStopsFromJson(response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }


        return busStopList;
    }



    //3.4.5 Retrieve Route Information
    //http://[rtpiserver]/routeinformation?routeid=[route]&operator=[operator]&operator=[operator]&format=[format]

    public static ArrayList<Route>  getRouteInformation(Context context){
        String response="";
        ArrayList<Route> routeList = null;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(ROUTE_INFORMATION_PATH)
                .appendQueryParameter(ROUTER_ID_PARAM,"39")
                .appendQueryParameter(OPERATOR_PARAM, DUBLIN_BUS_OPERATOR_PARAM)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            response = getResponseFromHttpUrl(url);
            routeList = JsonUtilities.getRouteFromJson(context,response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }
        return routeList;
    }



    //3.4.6 Operator Information
    //http://[rtpiserver]/operatorinformation?format=[format]

    public static ArrayList<Operator> getOperatorInformation(){
        String response="";
        ArrayList<Operator> operatorList = null;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(OPERATOR_INFORMATION_PATH)
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
            operatorList = JsonUtilities.getOperatorsFromJson(response);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return operatorList;
    }

    //3.4.7 Route List Information
    //http://[rtpiserver]/routelistinformation? operator=[operator]&format=[format]
    public static String getRouteListInformation(){

        String response="";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(ROUTE_LIST_INFORMATION_PATH)
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
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return response;
    }

}
