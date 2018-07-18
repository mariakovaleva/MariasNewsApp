package com.example.mariakovaleva.mariasnewsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    /**
     * Adapter for the list of news stories
     */
    private NewsAdapter mAdapter;

    /**
     * Implementing the "pull to update" gesture
     */
    private SwipeRefreshLayout mSwipeView;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Constant value for the news loader.
     */
    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeView = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeView.setOnRefreshListener(this);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        ListView newsListView = (ListView) findViewById(R.id.news_stories_list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty);
        newsListView.setEmptyView(mEmptyStateTextView);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        } else {

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {

        mEmptyStateTextView.setText(R.string.empty_text);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mEmptyStateTextView.setVisibility(View.GONE);
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
    }

    @Override
    public void onRefresh() {
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            mSwipeView.setRefreshing(false);
        }
    }, 5000);
    }
}
