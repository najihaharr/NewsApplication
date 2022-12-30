package com.example.android.newsapplication;

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
import java.util.List;

/**
 * Helper methods related to requesting and receiving Guardian news data.
 */
public class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /** Keys used by Guardian API to get the news data */
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_WEBTITLE = "webTitle";
    private static final String KEY_SECTIONNAME = "sectionName";
    private static final String KEY_WEBPUBLICATIONDATE = "webPublicationDate";
    private static final String KEY_WEBURL = "webUrl";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create a list
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
            e.printStackTrace();
        }
        return url;
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
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();
        //SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy");

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject(KEY_RESPONSE);
            JSONArray resultsArray = response.getJSONArray(KEY_RESULTS);

            // For each article in the resultsArray, create an {@link News} object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single article at position i within the list of news
                JSONObject currentNews = resultsArray.getJSONObject(i);

                String newsTitle = currentNews.getString(KEY_WEBTITLE);
                String category = currentNews.getString(KEY_SECTIONNAME);
                String date = currentNews.getString(KEY_WEBPUBLICATIONDATE);
                String url = currentNews.getString(KEY_WEBURL);

                JSONArray tagsAuthor = currentNews.getJSONArray(KEY_TAGS);
                String firstName = "";
                String lastName = "";
                if (tagsAuthor.length() != 0) {
                    JSONObject currentTagsAuthor = tagsAuthor.getJSONObject(0);
                    firstName = currentTagsAuthor.getString(KEY_FIRSTNAME);
                    lastName = currentTagsAuthor.getString(KEY_LASTNAME);
                }

                date = date.replaceAll("[a-zA-Z]", " ").substring(0, 9);
                //date = DateFor.format(date);

                // Add the new article to the list of news.
                newsList.add(new News(newsTitle, category, date, url, firstName, lastName));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return newsList;
    }
}
