package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Tugce on 10/29/2016.
 */
@Parcel
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String createdAt;
    private String description;
    private int favouritesCount;
    private int followersCount;
    private int friendsCount;
    private int statusesCount;
    private String location;
    private String profileBackgroundImageUrl;
    private String url;

    public User() {
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getUrl() {
        return url;
    }

    public static User fromJson(JSONObject jsonObject){
        User user=new User();
        try {
            user.name=jsonObject.getString("name");
            user.uid=jsonObject.getLong("id");
            user.screenName=jsonObject.getString("screen_name");
            user.profileImageUrl=jsonObject.getString("profile_image_url");
            user.createdAt=jsonObject.getString("created_at");
            user.description=jsonObject.getString("description");
            user.favouritesCount=jsonObject.getInt("favourites_count");
            user.followersCount=jsonObject.getInt("followers_count");
            user.friendsCount=jsonObject.getInt("friends_count");
            user.location=jsonObject.getString("location");
            user.profileBackgroundImageUrl=jsonObject.getString("profile_background_image_url");
            user.statusesCount=jsonObject.getInt("statuses_count");
            user.url=jsonObject.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
