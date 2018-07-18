package com.example.mariakovaleva.mariasnewsapp;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        // Perform the network request, parse the response, and extract a list of news.
        return QueryTools.fetchNews();
    }

}
