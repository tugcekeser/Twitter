package com.codepath.apps.mysimpletweets.activities;

import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.FollowersFollowingArrayAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
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

public class FollowersActivity extends AppCompatActivity {
    private TwitterClient client;
    private FollowersFollowingArrayAdapter aFollowers;
    private ArrayList<User> followers;
    private long maxId=(-1);
    private SwipeRefreshLayout swipeContainer;
    private ListView lvFollowingFollowers;
    private User user;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_following);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Followers");


        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(General.USER));

        client= TwitterApp.getRestClient();
        followers=new ArrayList<User>();
        aFollowers=new FollowersFollowingArrayAdapter(this,followers);
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.scFollowing);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        lvFollowingFollowers=(ListView)findViewById(R.id.lvFollowingFollowers);
        lvFollowingFollowers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateFollowers(maxId);
                return true;
            }
        });
        lvFollowingFollowers.setAdapter(aFollowers);

        populateFollowers((-1));
    }

    private void update(){
        aFollowers.clear();
        populateFollowers((-1));
    }

    private void populateFollowers(long max){

        if(!Network.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(),General.INTERNET_CONNECTION_ALERT,Toast.LENGTH_LONG);
        }

        client.getFollowers(user.getUid(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());
                try {
                    ArrayList<User> usersArrayList = User.fromJsonArray(response.getJSONArray("users"));
                    maxId = response.getLong("next_cursor");
                    aFollowers.addAll(usersArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(General.DEBUG,aFollowers.toString());
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(General.DEBUG,errorResponse.toString());
            }
        }, max);
    }

}
