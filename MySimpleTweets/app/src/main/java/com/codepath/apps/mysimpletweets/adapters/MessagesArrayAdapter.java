package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.activities.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.models.Message;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.PatternEditableBuilder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Tugce on 11/7/2016.
 */
public class MessagesArrayAdapter extends ArrayAdapter<Message>{

    public MessagesArrayAdapter(Context context, List<Message> messages) {
        super(context,0, messages);
    }

    //TODO: viewleri ButterKnife yap!
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message message=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_message,parent,false);
        }

        CircleImageView ivProfileImage=(CircleImageView)convertView.findViewById(R.id.ivProfileImage);
        TextView tvScreenName=(TextView)convertView.findViewById(R.id.tvScreenName);
        TextView tvName=(TextView)convertView.findViewById(R.id.tvName);
        TextView tvText=(TextView)convertView.findViewById(R.id.tvText);

        tvScreenName.setText(General.INITIAL_NAME+message.getSender().getScreenName());
        tvName.setText(message.getSender().getName());
        tvText.setText(message.getText());


        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), R.color.linkColor,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                //TODO:
                            }
                        }).into(tvText);

        ivProfileImage.setImageResource(android.R.color.transparent);

        Glide.with(getContext()).load(message.getSender().getProfileImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(ivProfileImage);

        TextView tvTimeStamp=(TextView)convertView.findViewById(R.id.tvTimeStamp);
        tvTimeStamp.setText(message.getCreatedAt());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }
}
