package com.codepath.apps.mysimpletweets.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.ActivityTweetDetailBinding;
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
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class TweetDetailActivity extends AppCompatActivity {
    private ActivityTweetDetailBinding binding;
    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_detail);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(General.TWEET));

        binding.tvUserName.setText(tweet.getUser().getScreenName());
        binding.tvBody.setText(tweet.getBody());

        binding.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(binding.ivProfileImage);

        binding.tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        binding.tvFavouritesCount.setText(Integer.toString(tweet.getFavouritesCount()));
        binding.tvTime.setText(tweet.getTimeStamp());
        binding.tvName.setText(General.INITIAL_NAME + tweet.getUser().getName());
        binding.etReply.addTextChangedListener(textWatcher);

        binding.tvCount.setText(Integer.toString(General.TWEET_CHARACTER_COUNT));
        binding.btnReply.setEnabled(false);
        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyTweet(binding.etReply.getText().toString());
                finish();
            }
        });
    }

    //Reply tweet
    public void replyTweet(String tweet) {
        TwitterClient client = TwitterApp.getRestClient();

        client.postTweet(tweet,String.valueOf(this.tweet.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJson(response);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.v(General.TWEET, errorResponse.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tweet_menu, menu);
        return true;
    }

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