package com.codepath.apps.mysimpletweets.constants;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created by Tugce on 11/4/2016.
 */
public class Client {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "3QI55C4WH9YbBBKZhArhdbAsM";       // Change this
    public static final String REST_CONSUMER_SECRET = "pi5uimTgeA5NQkYHgFvBqP0CmN6eP8xXx4aKfrLvC0W2jaqWwl"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

    public static final String GET_HOME_TIMELINE="statuses/home_timeline.json";
    public static final String COUNT_TAG="count";
    public static final String SINCE_ID="since_id";
    public static final String MAX_ID="max_id";

    public static final String POST_TWEET="statuses/update.json";
    public static final String STATUS="status";
    public static final String IN_REPLY_TO_REQUEST="in_reply_to_status_id";

    public static final String GET_AUTORIZED_USER="account/verify_credentials.json";
    public static final String MENTIONS_TIMELINE = "statuses/mentions_timeline.json";
    public static final String GET_FOLLOWERS = "followers/list.json";
    public static final String GET_FOLLOWING = "friends/list.json";
    public static final String GET_USER_TIMELINE = "statuses/user_timeline.json";
    public static final String SCREEN_NAME = "screen_name";
    public static final String GET_FAVORITES_LIST = "favorites/list.json";
}
