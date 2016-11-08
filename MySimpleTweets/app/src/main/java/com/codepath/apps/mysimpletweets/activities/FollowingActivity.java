package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.FollowersFollowingArrayAdapter;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.ActivityFollowingBinding;
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

import cz.msebera.android.httpclient.Header;

public class FollowingActivity extends AppCompatActivity {
    private ActivityFollowingBinding binding;
    private TwitterClient client;
    private FollowersFollowingArrayAdapter aFollowing;
    private ArrayList<User> following;
    private long maxId=(-1);
    private SwipeRefreshLayout swipeContainer;
    private ListView lvFollowing;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(General.USER));

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO:
            }
        });

        client= TwitterApp.getRestClient();
        following=new ArrayList<User>();
        aFollowing=new FollowersFollowingArrayAdapter(this,following);
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
        lvFollowing=(ListView)findViewById(R.id.lvFollowing);
        lvFollowing.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateFollowing(maxId);
                return true;
            }
        });
        lvFollowing.setAdapter(aFollowing);

        populateFollowing((-1));
    }

    private void update(){
        aFollowing.clear();
        populateFollowing((-1));
    }

    private void populateFollowing(long max){

        if(!Network.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(),General.INTERNET_CONNECTION_ALERT,Toast.LENGTH_LONG);
        }

        client.getFollowing(user.getUid(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());
                try {
                    ArrayList<User> usersArrayList = User.fromJsonArray(response.getJSONArray("users"));
                    maxId = response.getLong("next_cursor");
                    aFollowing.addAll(usersArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(General.DEBUG,aFollowing.toString());
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
