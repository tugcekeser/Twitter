package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.activities.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.codepath.apps.mysimpletweets.utils.Network;
import com.codepath.apps.mysimpletweets.utils.PatternEditableBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;
import java.util.List;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Tugce on 10/29/2016.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,0, tweets);
    }

    //TODO:ButterKnife
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        ImageView ivProfileImage=(ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName=(TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvBody=(TextView)convertView.findViewById(R.id.tvBody);

        tvUserName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#4CB1F4"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                getUser(text);
                            }
                        }).into(tvBody);

        ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(ivProfileImage);

        TextView tvRetweetsCount=(TextView) convertView.findViewById(R.id.tvRetweetsCount);
        tvRetweetsCount.setText(Integer.toString(tweet.getRetweetCount()));

        final TextView tvFavouritesCount=(TextView) convertView.findViewById(R.id.tvFavouritesCount);
        tvFavouritesCount.setText(Integer.toString(tweet.getFavouritesCount()));

        TextView tvTimeStamp=(TextView)convertView.findViewById(R.id.tvTimeStamp);
        tvTimeStamp.setText(tweet.getTimeStamp());

        TextView tvSource=(TextView)convertView.findViewById(R.id.tvName);
        tvSource.setText(General.INITIAL_NAME+tweet.getUser().getScreenName());

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), ProfileActivity.class)
                        .putExtra(General.USER, Parcels.wrap(tweet.getUser()));
                getContext().startActivity(i);
            }
        });

        ImageView ivMedia=(ImageView)convertView.findViewById(R.id.ivMedia);

        Glide.with(getContext()).load(tweet.getMediaURL())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),10,10))
                .into(ivMedia);

        final ImageView btnLike=(ImageView) convertView.findViewById(R.id.btnLike);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), TweetDetailActivity.class)
                        .putExtra(General.TWEET, Parcels.wrap(tweet));
                getContext().startActivity(i);
            }
        });

        if(tweet.getFavorited()){
            btnLike.setImageResource(R.drawable.liked);
            tvFavouritesCount.setTextColor(getContext().getResources().getColor(R.color.likedButtonColor));
        }
        else{
            btnLike.setImageResource(R.drawable.like);
            tvFavouritesCount.setTextColor(getContext().getResources().getColor(R.color.colorLine));
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tweet.getFavorited()){
                    btnLike.setImageResource(R.drawable.liked);
                    tvFavouritesCount.setTextColor(getContext().getResources().getColor(R.color.likedButtonColor));
                    addRemoveFavourite(true,tweet.getId());
                }
                else{
                    btnLike.setImageResource(R.drawable.like);
                    tvFavouritesCount.setTextColor(getContext().getResources().getColor(R.color.colorLine));
                    addRemoveFavourite(false,tweet.getId());
                }
            }
        });
        return convertView;
    }

    private void getUser(String screenName){

        if(!Network.isNetworkAvailable(getContext())){
            Toast.makeText(getContext(),General.INTERNET_CONNECTION_ALERT,Toast.LENGTH_LONG);
        }

        TwitterClient client=TwitterApp.getRestClient();
        client.getUser(screenName.substring(1,screenName.length()),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());
                User user = User.fromJson(response);
                Intent intent=new Intent(getContext(),ProfileActivity.class)
                        .putExtra(General.USER, Parcels.wrap(user));
                getContext().startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(General.DEBUG,errorResponse.toString());
            }
        });
    }

    private void addRemoveFavourite(boolean liked,long tweetId){

        if(!Network.isNetworkAvailable(getContext())){
            Toast.makeText(getContext(),General.INTERNET_CONNECTION_ALERT,Toast.LENGTH_LONG);
        }

        TwitterClient client=TwitterApp.getRestClient();
        client.addRemoveFavourite(tweetId,liked,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d(General.DEBUG,response.toString());
                Tweet tweet = Tweet.fromJson(response);
                //TODO:Refresh required to see
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d(General.DEBUG,errorResponse.toString());
            }
        });
    }
}