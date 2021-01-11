package com.example.android.vfund.controller;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.android.vfund.model.FundraisingEvent;
import com.example.android.vfund.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();



            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the user JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Make an HTTP request to the given URL and update event with current money
     */
    public static boolean makeHttpRequestUpdateEvent(URL url, int currentMoney) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return false;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("PATCH");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("CurrentMoney", currentMoney);

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("MSG" , urlConnection.getResponseMessage());
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Successfully request");
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the user JSON results.", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return true;
    }

    public static User fetchUserData(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream" , e);
        }

        //Extract relevant fields from the JSON response and create an {@link User} object
        User currentClient = extractUser(jsonResponse);

        return currentClient;
    }

    /**
     * Return a {@link User} objects that has been built up from
     * parsing a JSON response.
     */
    public static User extractUser(String jsonResponse) {

        User thisUser = null;
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(jsonResponse);
            root = root.getJSONObject("result");
            JSONArray featuresArray = root.getJSONArray("recordset");
            JSONObject properties = featuresArray.getJSONObject(0);
            thisUser = new User(properties.getInt("ID"), properties.getString("Username"),
                    properties.getString("UserEmail"), properties.getString("UserDOB"),
                    properties.getString("UserPhoneNumber"));
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a User objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the user JSON results");
            thisUser = null;
        }

        // Return the user
        return thisUser;
    }

    public static ArrayList<FundraisingEvent> fetchEventData(String requestUrl, boolean isFollowed) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream" , e);
        }

        //Extract relevant fields from the JSON response and create an {@link User} object
        ArrayList<FundraisingEvent> eventList = extractEventList(jsonResponse, isFollowed);

        return eventList;
    }

    /**
     * Return a list of {@link FundraisingEvent} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<FundraisingEvent> extractEventList(String jsonResponse, boolean isFollowed) {

        ArrayList<FundraisingEvent> eventList = new ArrayList<FundraisingEvent>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(jsonResponse);
            root = root.getJSONObject("result");
            JSONArray events = root.getJSONArray("recordset");

            for(int i = 0; events.getJSONObject(i) != null; i++) {
                JSONObject event = events.getJSONObject(i);

                FundraisingEvent newEvent = new FundraisingEvent(event.getInt("ID"), event.getString("EventName"),
                        event.getString("EventDescription"),event.getString("EventDate"),isFollowed,
                        event.getInt("EventGoal"), event.getInt("CurrentMoney"));

                JSONObject hostJSON = event.getJSONObject("HostID");
                User host = new User(hostJSON.getInt("ID"), hostJSON.getString("Username"),
                        hostJSON.getString("UserEmail"), hostJSON.getString("UserDOB"),
                        hostJSON.getString("UserPhoneNumber"));
                
                newEvent.set_owner(host);
                eventList.add(newEvent);

            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a User objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the event JSON results");
        }

        // Return the user
        return eventList;
    }

    private static int parseIdFromPrefix(String idPrefix){
        int i = 0;
        for(i = 0; i <= idPrefix.length(); i++) {
            char c = idPrefix.charAt(i);
            if(c >= '0' && c <= '9') {
                break;
            }
        }
        String idStr = idPrefix.substring(i);
        if(idStr == null || idStr.isEmpty()) {
            return -1;
        }
        return  Integer.parseInt(idStr);
    }

    /**
     * Make an HTTP request to the given URL and update event with current money
     */
    public static boolean makeHttpRequestFollowEvent(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return false;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            Log.i("MSG" , urlConnection.getResponseMessage());
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Successfully request");
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the user JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return true;
    }

    /**
     * Make an HTTP request to the given URL and update event with current money
     */
    public static boolean makeHttpRequestUnFollowEvent(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return false;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("DELETE");
            urlConnection.connect();

            Log.i("MSG" , urlConnection.getResponseMessage());
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Successfully request");
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the user JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return true;
    }
}
