package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.HomePagerAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.ActivityTimelineBinding;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class TimelineActivity extends AppCompatActivity implements ComposeTweetFragment.ComposeTweetDialogListener {

    private ActivityTimelineBinding binding;
    private HomePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        setSupportActionBar(binding.toolbar);

        adapter=new HomePagerAdapter(getSupportFragmentManager());
        binding.vpMain.setAdapter(adapter);
        binding.tabs.setViewPager(binding.vpMain);
        binding.ivProfileImage.setImageResource(0);

        getAutorizedUser();

        binding.btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == R.id.action_message) {
            Intent intent=new Intent(this,NewMessageActivity.class);
            startActivity(intent);
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Intent i=new Intent(TimelineActivity.this,SearchTweetActivity.class).putExtra("q",query);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void showEditDialog() {
        ComposeTweetFragment compose = ComposeTweetFragment.newInstance(General.COMPOSE_TWEET_SCREEN_NAME);
        compose.show(getSupportFragmentManager(),General.COMPOSE_TWEET_SCREEN_NAME);

    }

    public  void  getAutorizedUser() {
        TwitterClient client = TwitterApp.getRestClient();
        client.getAutorizedUser( new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                final User currentUser = User.fromJson(response);

                Picasso.with(TimelineActivity.this).load(currentUser.getProfileImageUrl())
                        .into(binding.ivProfileImage);

                binding.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(TimelineActivity.this, ProfileActivity.class).putExtra("User", Parcels.wrap(currentUser));
                        startActivity(i);
                    }
                });
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.v(General.TWITTER, errorResponse.toString());
            }
        });
    }

    @Override
    public void onFinishEditDialog(Tweet tweet) {
      /* Fragment fr= adapter.getItem(0);
        if(fr instanceof TimelineFragment)
        {
            ((TimelineFragment) fr).addTweet(tweet);
        }*/
        binding.vpMain.getAdapter().notifyDataSetChanged();
    }

}
