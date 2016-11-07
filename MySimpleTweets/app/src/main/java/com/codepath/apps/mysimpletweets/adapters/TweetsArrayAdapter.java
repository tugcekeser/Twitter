package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.activities.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Tugce on 10/29/2016.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public static final String TWEET="Tweet";
    public static final String INITIAL_NAME="@";

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,0, tweets);
    }

    //TODO: viewleri ButterKnife yap!
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
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(ivProfileImage);

        TextView tvRetweetsCount=(TextView) convertView.findViewById(R.id.tvRetweetsCount);
        tvRetweetsCount.setText(Integer.toString(tweet.getRetweetCount()));

        TextView tvFavouritesCount=(TextView) convertView.findViewById(R.id.tvFavouritesCount);
        tvFavouritesCount.setText(Integer.toString(tweet.getFavouritesCount()));

        TextView tvTimeStamp=(TextView)convertView.findViewById(R.id.tvTimeStamp);
        tvTimeStamp.setText(tweet.getTimeStamp());

        TextView tvSource=(TextView)convertView.findViewById(R.id.tvName);
        tvSource.setText("@"+tweet.getUser().getScreenName());

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), ProfileActivity.class)
                        .putExtra("User", Parcels.wrap(tweet.getUser()));
                getContext().startActivity(i);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), TweetDetailActivity.class)
                        .putExtra(TWEET, Parcels.wrap(tweet));
                getContext().startActivity(i);
            }
        });
        return convertView;

    }
}