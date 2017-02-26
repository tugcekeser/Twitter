package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.ProfilePagerAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileActivity extends AppCompatActivity {
    private User user;
    private ProfilePagerAdapter adapter;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvScreenName)
    TextView tvScreenName;
    @BindView(R.id.tvCountFollowing)
    TextView tvFollowingCount;
    @BindView(R.id.tvCountFollowers)
    TextView tvFollowerCount;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvWebsite)
    TextView tvWebsite;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tabsProfile)
    PagerSlidingTabStrip tabsProfile;
    @BindView(R.id.vpProfile)
    ViewPager vpProfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarLayout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
       // toolbarLayout.setBackgroundResource(R.drawable.twitter_background);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("User"));
        //binding.ivBackgroundImage.setImageResource(0);
        ivProfileImage.setImageResource(0);

        Glide.with(ProfileActivity.this).load(user.getProfileImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(this,5,5))
                .into(ivProfileImage);

        /*Glide.with(ProfileActivity.this).load(user.getProfileBackgroundImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(this,40,40))
                .into(binding.ivBackgroundImage);*/

        tvName.setText(user.getName());
        tvScreenName.setText(General.INITIAL_NAME+user.getScreenName());
        tvFollowingCount.setText(Integer.toString(user.getFriendsCount()));
        tvFollowingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProfileActivity.this, FollowingActivity.class)
                        .putExtra("User", Parcels.wrap(user));
               startActivity(i);
            }
        });
        tvFollowerCount.setText(Integer.toString(user.getFollowersCount()));
        tvFollowerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProfileActivity.this, FollowersActivity.class)
                        .putExtra("User", Parcels.wrap(user));
                startActivity(i);
            }
        });

        if(user.getDescription()==null || user.getDescription().equals(""))
            tvDescription.setVisibility(View.GONE);
        else
            tvDescription.setText(user.getDescription());

        if(user.getLocation()==null || user.getLocation().equals(""))
            tvLocation.setVisibility(View.GONE);
        else
            tvLocation.setText(user.getLocation());

        if(user.getUrl()==null || user.getUrl().equals(""))
            tvWebsite.setVisibility(View.GONE);
        else
            tvWebsite.setText(user.getUrl());


        adapter=new ProfilePagerAdapter(getSupportFragmentManager(),user);
        vpProfile.setAdapter(adapter);
        tabsProfile.setViewPager(vpProfile);
    }
}
