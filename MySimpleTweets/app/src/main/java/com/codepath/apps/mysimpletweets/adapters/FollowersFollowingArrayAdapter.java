package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

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
        tvDescription.setText(user.getDescription());
        Glide.with(getContext()).load(user.getProfileImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),10,10))
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
}
