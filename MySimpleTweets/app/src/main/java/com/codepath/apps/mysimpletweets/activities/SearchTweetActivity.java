package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.FollowersFollowingArrayAdapter;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.utils.Network;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchTweetActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> results;
    private long maxId=1;
    private String query;
    @BindView(R.id.lvTweets) ListView lvTweets;
    @BindView(R.id.scTweets) SwipeRefreshLayout scTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tweet);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        query = getIntent().getStringExtra("q");
        client= TwitterApp.getRestClient();

        results=new ArrayList<Tweet>();
        aTweets=new TweetsArrayAdapter(this,results);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateSearch(maxId);
                return true;
            }
        });

        scTweets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });
        // Configure the refreshing colors
        scTweets.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        populateSearch(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                query=q;
                update();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void update(){
        aTweets.clear();
        populateSearch(1);
    }

    private void populateSearch(long max){

        if(!Network.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(),General.INTERNET_CONNECTION_ALERT
                    ,Toast.LENGTH_LONG);
        }

        client.searchTweets(query,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());
                try {
                    aTweets.addAll(Tweet.fromJsonArray(response.getJSONArray("statuses")));
                    if (Tweet.fromJsonArray(response.getJSONArray("statuses")).size() > 0) {
                        Tweet mostRecentTweet = Tweet.fromJsonArray(response.getJSONArray("statuses")).get(Tweet.fromJsonArray(response.getJSONArray("statuses")).size() - 1);
                        maxId = mostRecentTweet.getId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d(General.DEBUG,aTweets.toString());
                scTweets.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(General.DEBUG,errorResponse.toString());
            }
        }, max);
    }

}
