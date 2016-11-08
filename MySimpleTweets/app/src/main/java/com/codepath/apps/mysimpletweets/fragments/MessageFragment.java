package com.codepath.apps.mysimpletweets.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.MessagesArrayAdapter;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.FragmentMessagesBinding;
import com.codepath.apps.mysimpletweets.databinding.FragmentTimelineBinding;
import com.codepath.apps.mysimpletweets.models.Message;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.utils.Network;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tugce on 11/7/2016.
 */
public class MessageFragment extends Fragment {
    private FragmentMessagesBinding binding;
    private TwitterClient client;
    private MessagesArrayAdapter aMessages;
    private ArrayList<Message> messages;
    private long maxId=1;
    private int mPage;


    public static MessageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(General.ARG_PAGE, page);
        MessageFragment fragment = new MessageFragment();
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
        messages=new ArrayList<Message>();
        aMessages=new MessagesArrayAdapter(getContext(),messages);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_messages, container, false);
        View view = binding.getRoot();

        binding.scMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(1);
            }
        });

        // Configure the refreshing colors
        binding.scMessage.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);

        populateTimeline(1);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.lvMessages.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(maxId);
                return true;
            }
        });

        binding.lvMessages.setAdapter(aMessages);
    }


    private void populateTimeline(long max){
        if(max==1 && messages.size()>0)
            aMessages.clear();

        if(!Network.isNetworkAvailable(getContext())){
            Toast.makeText(getContext(),"Please check your internet connection",Toast.LENGTH_LONG);
        }

        client.getMessages(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());

              /*  ArrayList<Message> messageArrayList = Message.fromJsonArray(response);
                ArrayList<Message> toAdd = new ArrayList<Message>();
                toAdd.add(messageArrayList.get(0));

                for (Message message : messageArrayList) {
                    boolean existed=false;

                    for (Message addedMessage:toAdd){
                        if (message.getSender().getScreenName().equals(addedMessage.getSender().getScreenName()))
                            existed=true;
                    }
                    if(existed==true)
                        toAdd.add(message);
                }*/

                aMessages.addAll(Message.fromJsonArray(response));
               // aMessages.addAll(toAdd);

                if (Message.fromJsonArray(response).size() > 0) {
                    Message mostRecentMessage = Message.fromJsonArray(response).get(Message.fromJsonArray(response).size() - 1);
                    maxId = mostRecentMessage.getMessageId();
                }
                Log.d(General.DEBUG,aMessages.toString());
                binding.scMessage.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(General.DEBUG,errorResponse.toString());
            }
        }, max);
    }
}
