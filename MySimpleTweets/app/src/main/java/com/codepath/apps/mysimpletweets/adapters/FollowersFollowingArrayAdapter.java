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
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.network.TwitterApp;
import com.codepath.apps.mysimpletweets.network.TwitterClient;
import com.codepath.apps.mysimpletweets.utils.Network;
import com.codepath.apps.mysimpletweets.utils.PatternEditableBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Tugce on 11/6/2016.
 */
public class FollowersFollowingArrayAdapter extends ArrayAdapter<User> {

    public FollowersFollowingArrayAdapter(Context context, List<User> users) {
        super(context,0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_follower_following,parent,false);
        }
        ImageView ivProfileImage=(ImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvName=(TextView)convertView.findViewById(R.id.tvName);
        TextView tvUserName=(TextView)convertView.findViewById(R.id.tvUserName);
        TextView tvDescription=(TextView)convertView.findViewById(R.id.tvDescription);


        tvName.setText(General.INITIAL_NAME+user.getScreenName());
        tvUserName.setText(user.getName());
       // tvDescription.setText(user.getDescription());
        tvDescription.setText(user.getDescription());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#4CB1F4"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                getUser(text);
                            }
                        }).into(tvDescription);

        Glide.with(getContext()).load(user.getProfileImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(ivProfileImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), ProfileActivity.class)
                        .putExtra(General.USER, Parcels.wrap(user));
                getContext().startActivity(i);
            }
        });
        return convertView;
    }

    private void getUser(String screenName){

        if(!Network.isNetworkAvailable(getContext())){
            Toast.makeText(getContext(),General.INTERNET_CONNECTION_ALERT,Toast.LENGTH_LONG);
        }

        TwitterClient client= TwitterApp.getRestClient();
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
}
