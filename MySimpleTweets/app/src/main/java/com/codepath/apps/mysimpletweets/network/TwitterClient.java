package com.codepath.apps.mysimpletweets.network;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.apps.mysimpletweets.constants.Client;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {

	public TwitterClient(Context context) {
		super(context, Client.REST_API_CLASS, Client.REST_URL, Client.REST_CONSUMER_KEY, Client.REST_CONSUMER_SECRET, Client.REST_CALLBACK_URL);
	}

	//Get Home Timeline Tweets
	public void getHomeTimeline(AsyncHttpResponseHandler handler, long max){
		String apiUrl = getApiUrl(Client.GET_HOME_TIMELINE);
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 25);

		if(max==1)
		    params.put(Client.SINCE_ID, 1);
		else
			params.put(Client.MAX_ID, max-1);
		getClient().get(apiUrl,params,handler);
	}

	// Compose tweet
	public void postTweet(String tweet, String inReplyToStatusId, AsyncHttpResponseHandler handler) {
		String api_url = getApiUrl(Client.POST_TWEET);
		RequestParams params = new RequestParams();
		params.put(Client.STATUS, tweet);
		if (inReplyToStatusId != null)
			params.put(Client.IN_REPLY_TO_REQUEST, inReplyToStatusId);
		getClient().post(api_url, params, handler);
	}

	//Get Autorized User
	public void getAutorizedUser(AsyncHttpResponseHandler handler) {
		String api_url = getApiUrl(Client.GET_AUTORIZED_USER);
		RequestParams params = new RequestParams();
		getClient().get(api_url, params, handler);
	}

	//Get Mentions Tweets
	public void getMentionsTimeLine( AsyncHttpResponseHandler handler,long max) {
		String api_url = getApiUrl(Client.MENTIONS_TIMELINE);
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 25);
		if (max == 1)
			params.put(Client.SINCE_ID, 1);
		else
			params.put(Client.MAX_ID, max - 1);

		getClient().get(api_url, params, handler);
	}


	public void getUserTimeLine(long userId, AsyncHttpResponseHandler handler, long max) {
		String api_url = getApiUrl(Client.GET_USER_TIMELINE);
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 25);
		params.put("user_id", userId);
		if (max == 1)
			params.put(Client.SINCE_ID, 1);
		else
			params.put(Client.MAX_ID, max - 1);
		getClient().get(api_url, params, handler);
	}

	/*public void getUserPhotos(long userId, AsyncHttpResponseHandler handler,long max) {
		String api_url = getApiUrl(Client.GET_USER_TIMELINE);
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 25);
		params.put("user_id", userId);
		params.put("exclude_replies", "true");
		params.put("include_rts", "false");
		if (max == 1)
			params.put(Client.SINCE_ID, 1);
		else
			params.put(Client.MAX_ID, max - 1);
		getClient().get(api_url, params, handler);
	}*/

	public void getUserLikes(long userId, AsyncHttpResponseHandler handler, long max) {
		String api_url = getApiUrl(Client.GET_FAVORITES_LIST);
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 25);
		params.put("user_id", userId);
		if (max == 1)
			params.put(Client.SINCE_ID, 1);
		else
			params.put(Client.MAX_ID, max - 1);
		getClient().get(api_url, params, handler);
	}

	public void getFollowers(String screenName, long cursorId, AsyncHttpResponseHandler handler) {
		String api_url = getApiUrl(Client.GET_FOLLOWERS);
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 25);
		params.put(Client.SCREEN_NAME, screenName);
		params.put("skip_status", "true");
		params.put("cursor", cursorId);
		getClient().get(api_url, params, handler);
	}

	public void getFollowing(String screenName, long cursorId, AsyncHttpResponseHandler handler) {
		String api_url = getApiUrl(Client.GET_FOLLOWING);
		RequestParams params = new RequestParams();
		params.put(Client.COUNT_TAG, 10);
		params.put(Client.SCREEN_NAME, screenName);
		params.put("skip_status", "true");
		params.put("cursor", cursorId);
		getClient().get(api_url, params, handler);
	}
}
