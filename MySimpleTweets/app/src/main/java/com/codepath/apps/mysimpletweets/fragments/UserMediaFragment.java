package com.codepath.apps.mysimpletweets.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.MediaArrayAdapter;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.FragmentMediaBinding;
import com.codepath.apps.mysimpletweets.databinding.FragmentTimelineBinding;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tugce on 11/4/2016.
 */
public class UserMediaFragment extends Fragment {

    private FragmentMediaBinding binding;
    private TwitterClient client;
    private MediaArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private long maxId=1;
    private SwipeRefreshLayout swipeContainer;
    private static User pUser;

    private int mPage;

    public static UserMediaFragment newInstance(int page, User user) {
        Bundle args = new Bundle();
        args.putInt(General.ARG_PAGE, page);
        UserMediaFragment fragment = new UserMediaFragment();
        fragment.setArguments(args);
        pUser=user;
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
        aTweets=new MediaArrayAdapter(getContext(),tweets);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_media, container, false);
        View view = binding.getRoot();

        swipeContainer=(SwipeRefreshLayout)view.findViewById(R.id.scMedia);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateUserTweets(1);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        populateUserTweets(1);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.lvMedia.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateUserTweets(maxId);
                return true;
            }
        });
        binding.lvMedia.setAdapter(aTweets);
    }

    private void populateUserTweets(long max){
        if(max==1 && tweets.size()>0)
            aTweets.clear();

        client.getUserTimeLine(pUser.getUid(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<Tweet> tweetArrayList = Tweet.fromJsonArray(response);
                Log.d(General.DEBUG,response.toString());
                ArrayList<Tweet> toRemove = new ArrayList<>();
                for (Tweet tweet : tweetArrayList) {
                    if (tweet.getMediaURL() == null)
                        toRemove.add(tweet);
                }
                tweetArrayList.removeAll(toRemove);

                aTweets.addAll(tweetArrayList);

                if (Tweet.fromJsonArray(response).size() > 0) {
                    Tweet mostRecentTweet = Tweet.fromJsonArray(response).get(Tweet.fromJsonArray(response).size() - 1);
                    maxId = mostRecentTweet.getId();
                }
                Log.d(General.DEBUG,aTweets.toString());
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
