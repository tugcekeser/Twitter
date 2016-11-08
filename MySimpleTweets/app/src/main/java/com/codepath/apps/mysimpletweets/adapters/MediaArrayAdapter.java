package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Tugce on 11/6/2016.
 */
public class MediaArrayAdapter  extends ArrayAdapter<Tweet> {

    Context mContext;
    public MediaArrayAdapter(Context context, List<Tweet> tweets) {
        super(context,0, tweets);
        mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_media, parent, false);
        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivMedia);
        Glide.with(getContext()).load(tweet.getMediaURL())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),10,10))
                .into(ivImage);
        return convertView;
    }}
