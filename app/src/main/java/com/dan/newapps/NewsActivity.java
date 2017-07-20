package com.dan.newapps;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

//the app is aimed to give a story of news about Film Review using API from Guardian
public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    //the api request the news which contain tags, film/film and tone/review, order by newest
    public static final String DEFAULT_NEWS_REQUEST_URL =
            "https://content.guardianapis.com/search?q&tag=film/film,tone/reviews&show-fields=thumbnail&order-by=newest&api-key=test&page-size=20&page=1";
    public int NEWS_LOADER_ID = 0;
    private ListView listView;
    private TextView emptyTextView;
    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;
    private List<News> listOfNews = new ArrayList<News>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //get emptyTextView, ListView, and Button from in news_list_item by their id
        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        listView = (ListView) findViewById(R.id.list);
        Button button = (Button) findViewById(R.id.button);
        //set adapter to listView
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);

        //display the view properly when there is internet connection and there is not.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check internet connection
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    //connection is okay, fetch data
                    emptyTextView.setVisibility(GONE);
                    listView.setVisibility(View.VISIBLE);
                    //restart loader, fetch new data
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsActivity.this);
                } else {
                    // No network connection, inform user
                    listView.setVisibility(GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(getString(R.string.no_internet));
                    Log.e(LOG_TAG, getString(R.string.no_internet));
                }
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // no internet, inform the user
            listView.setVisibility(GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_internet);
        }
    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, DEFAULT_NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> result) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        listOfNews = result;

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (listOfNews != null && !listOfNews.isEmpty()) {
            //show the results
            mAdapter.addAll(listOfNews);
            listView.setAdapter(mAdapter);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
