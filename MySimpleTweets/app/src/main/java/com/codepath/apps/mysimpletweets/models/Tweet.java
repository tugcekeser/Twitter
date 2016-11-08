package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import com.codepath.apps.mysimpletweets.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tugce on 10/29/2016.
 */

@Parcel
public class Tweet {
    private long id;
    private String body;
    private long uid;
    private User user;
    private int retweetCount=0;
    private boolean retweeted;
    private int favouritesCount=0;
    private String timeStamp;
    private String mediaURL;
    private boolean favorited=false;

    public Tweet() {
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public long getId() {
        return id;
    }

    public String getMediaURL() {
        return mediaURL;
    }

   public boolean getFavorited(){
       return favorited;
   }

    public static Tweet fromJson(JSONObject jsonObject){
        Tweet tweet=new Tweet();
        try {
            tweet.id=jsonObject.getLong("id");
            tweet.body=jsonObject.getString("text");
            tweet.uid=jsonObject.getLong("id");
            tweet.timeStamp= Helper.getRelativeTimeAgo(jsonObject.getString("created_at"));
            tweet.user=User.fromJson(jsonObject.getJSONObject("user"));
            tweet.retweetCount=jsonObject.getInt("retweet_count");
           // tweet.retweeted=jsonObject.getBoolean("retweeted");
            if (jsonObject.has("entities")) {
                if (jsonObject.getJSONObject("entities").has("media")){
                    if(jsonObject.getJSONObject("entities").getJSONArray("media").length() > 0) {
                    tweet.mediaURL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
                    }
                }
            }
            tweet.favorited=jsonObject.getBoolean("favorited");
            tweet.favouritesCount=jsonObject.getInt("favourites_count");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets=new ArrayList<Tweet>();
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject tweetJson=jsonArray.getJSONObject(i);
                Tweet tweet=Tweet.fromJson(tweetJson);
                if(tweet!=null) {
                    tweets.add(tweet);
                    continue;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tweets;
    }


}
