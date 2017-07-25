package com.jmgarzo.dublinbus.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=7602&format=json
    private static final String BASE_URL = "https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation";
    private static final String STOP_ID_PARAM = "stopid";
    private static final String FORMAT_PARAM = "format";
    private static final String JSON_VALUE_PARAM = "json";


    //Retrieve Real Time Bus Information
    /*http://[rtpiserver]/realtimebusinformation
    realtimebusinformation?stopid=[stopid]&routeid=[routeid]
            ?stopid=[stopid]&routeid=[routeid]&maxresults&operator=
            [operator]&format=[format] */
    public static String getRealTimeBusInformation(Context context) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(STOP_ID_PARAM, "7602")
                .appendQueryParameter(FORMAT_PARAM, JSON_VALUE_PARAM)
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String jsonMoviesResponse="";
        try {

            jsonMoviesResponse = getResponseFromHttpUrl(url);
//            moviesList = getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.MOST_POPULAR_REGISTRY_TYPE);
//
//            jsonMoviesResponse=NetworksUtils.getResponseFromHttpUrl(moviesURLTopRate);
//            moviesList.addAll(getMoviesFromJson(jsonMoviesResponse,PopularMovieContract.TOP_RATE_REGISTRY_TYPE));
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        return jsonMoviesResponse;
    }


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
}
