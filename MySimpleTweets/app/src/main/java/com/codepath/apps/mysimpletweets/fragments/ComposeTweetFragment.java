package com.codepath.apps.mysimpletweets.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.constants.General;

import com.codepath.apps.mysimpletweets.databinding.FragmentComposeTweetBinding;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tugce on 10/30/2016.
 */
public class ComposeTweetFragment extends DialogFragment {
    private FragmentComposeTweetBinding binding;
    private ComposeTweetDialogListener composeTweetDialogListener;

    public ComposeTweetFragment() {

    }

    public interface ComposeTweetDialogListener {
        void onFinishEditDialog(Tweet tweet);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ComposeTweetDialogListener)
        {
            composeTweetDialogListener = (ComposeTweetDialogListener) context;
        }
        else {
            throw new IllegalArgumentException("Please impliment ComposeTweetDialogListener");
        }
    }

    public static ComposeTweetFragment newInstance(String title) {
        ComposeTweetFragment frag = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putString(General.TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_compose_tweet, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Show soft keyboard automatically and request focus to field
        binding.etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        binding.etTweet.addTextChangedListener(textWatcher);
        binding.btnReply.setEnabled(false);
        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet(binding.etTweet.getText().toString());
            }
        });
    }

    //Post Tweet
    public void postTweet(String tweet) {
            TwitterClient client = TwitterApp.getRestClient();

                client.postTweet(tweet, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet newTweet = Tweet.fromJson(response);
                        composeTweetDialogListener.onFinishEditDialog(newTweet);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.v(General.TWITTER, errorResponse.toString());
                    }
                });
        dismiss();
    }

    //Tweet word counter
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int charsRemaining = General.TWEET_CHARACTER_COUNT - s.length();
            binding.tvCount.setText(Integer.toString(charsRemaining));

            if (charsRemaining >= 0 && charsRemaining < General.TWEET_CHARACTER_COUNT) {
                binding.btnReply.setEnabled(true);
            } else {
                binding.btnReply.setEnabled(false);
            }
        }
    };

}
