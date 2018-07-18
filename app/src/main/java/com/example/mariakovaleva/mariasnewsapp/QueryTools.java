package com.example.mariakovaleva.mariasnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryTools {

    /**
     * Private constructor to prevent from creating a {@link QueryTools} object
     */
    private QueryTools() {
    }

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryTools.class.getSimpleName();

    /**
     * Builds our query URL for dynamic change in query date
     * Fetch all technology news stories starting from yesterday
     */
    private static String urlBuilder() {
        StringBuilder builder = new StringBuilder();

        String baseUrl = "https://content.guardianapis.com/search?section=technology&from-date=";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        String formattedYesterday = dateFormat.format(yesterday);

        builder.append(baseUrl);
        builder.append(formattedYesterday);
        builder.append("&show-tags=contributor&show-fields=trailText");
        builder.append(BuildConfig.MyAPIToken);

        return builder.toString();
    }

    /**
     * Query the Guardian dataset and return a list of {@link News} objects
     */
    public static List<News> fetchNews() {
        // Create URL object
        URL url = createUrl(QueryTools.urlBuilder());

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        return extractNews(jsonResponse);

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, end program
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
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
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
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<News> extractNews(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            Log.v(LOG_TAG, "No information received from server (JSON is null)");
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<News> newsList = new ArrayList<>();

        // Try to parse the query.

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponse);
            JSONObject responseObject = jsonRootObject.optJSONObject("response");
            JSONArray resultsArray  = responseObject.optJSONArray("results");

            for (int i = 0; i < resultsArray .length(); i++){
                JSONObject newsObject = resultsArray .getJSONObject(i);

                JSONArray tagsArray = newsObject.optJSONArray("tags");
                String authorName = "";
                if (tagsArray != null && tagsArray.length() > 0) {
                    JSONObject tagsObject = tagsArray.getJSONObject(0);
                    authorName = tagsObject.optString("webTitle");
                }

                String dateAndTime = newsObject.optString("webPublicationDate");

                String title = newsObject.optString("webTitle");

                JSONObject fields = newsObject.getJSONObject("fields");
                String trailText = fields.optString("trailText");

                String category = newsObject.optString("sectionId");

                String url = newsObject.optString("webUrl");

                News news = new News(authorName, dateAndTime, title, trailText, category, url);
                newsList.add(news);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return newsList;
    }


}
