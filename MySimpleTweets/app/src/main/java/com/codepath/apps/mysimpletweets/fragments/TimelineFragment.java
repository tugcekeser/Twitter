package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.FragmentTimelineBinding;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.utils.Network;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class TimelineFragment extends Fragment {
    private FragmentTimelineBinding binding;
    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private long maxId=1;
    // private ProgressBar progressBarFooter;

    private int mPage;

    public static TimelineFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(General.ARG_PAGE, page);
        TimelineFragment fragment = new TimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViews();
    }

    private void createViews(){
        mPage = getArguments().getInt(General.ARG_PAGE);
        client= TwitterApp.getRestClient();
        tweets=new ArrayList<Tweet>();
        aTweets=new TweetsArrayAdapter(getContext(),tweets);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_timeline, container, false);
        View view = binding.getRoot();

        binding.scTimeline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(1);
            }
        });

        // Configure the refreshing colors
        binding.scTimeline.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        populateTimeline(1);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(maxId);
                return true;
            }
        });

        binding.lvTweets.setAdapter(aTweets);
    }

   /* public void setupListWithFooter() {
        // Inflate the footer
        View footer=LayoutInflater.from(getContext()).inflate(R.layout.footer_progress,null);
        // Find the progressbar within footer
        progressBarFooter = (ProgressBar)
                footer.findViewById(R.id.pbFooterLoading);
        // Add footer to ListView before setting adapter
        binding.lvTweets.addFooterView(footer);
    }*/

    private void populateTimeline(long max){
        if(max==1 && tweets.size()>0)
            aTweets.clear();

        if(!Network.isNetworkAvailable(getContext())){
            Toast.makeText(getContext(),"Please check your internet connection",Toast.LENGTH_LONG);
        }

        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());
                aTweets.addAll(Tweet.fromJsonArray(response));

                if (Tweet.fromJsonArray(response).size() > 0) {
                    Tweet mostRecentTweet = Tweet.fromJsonArray(response).get(Tweet.fromJsonArray(response).size() - 1);
                    maxId = mostRecentTweet.getId();
                }
                Log.d(General.DEBUG,aTweets.toString());
                binding.scTimeline.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(General.DEBUG,errorResponse.toString());
            }
        }, max);
    }


}
