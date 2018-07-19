package com.example.mariakovaleva.mariasnewsapp;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<News> results = new ArrayList<>();

        try {
            // Perform the network request, parse the response, and extract a list of news.
            results = QueryTools.fetchNews();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Failed to load in background");
        }
        return results;
    }

}
