package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.ProfilePagerAdapter;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.databinding.ActivityProfileBinding;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private User user;
    private ProfilePagerAdapter adapter;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvCountFollowing) TextView tvFollowingCount;
    @BindView(R.id.tvCountFollowers) TextView tvFollowerCount;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.tvWebsite) TextView tvWebsite;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tabsProfile) PagerSlidingTabStrip tabsProfile;
    @BindView(R.id.vpProfile) ViewPager vpProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setBackgroundResource(R.drawable.twitter_background);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("User"));
       // getSupportActionBar().setTitle(General.INITIAL_NAME+user.getScreenName());
        binding.ivBackgroundImage.setImageResource(0);
        binding.ivProfileImage.setImageResource(0);

        Glide.with(ProfileActivity.this).load(user.getProfileImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(this,5,5))
                .into(binding.ivProfileImage);

        Glide.with(ProfileActivity.this).load(user.getProfileBackgroundImageUrl())
                .bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(this,40,40))
                .into(binding.ivBackgroundImage);

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
        tvDescription.setText(user.getDescription());
        tvLocation.setText(user.getLocation());
        tvWebsite.setText(user.getUrl());
        adapter=new ProfilePagerAdapter(getSupportFragmentManager(),user);
        vpProfile.setAdapter(adapter);
        tabsProfile.setViewPager(vpProfile);
    }
}
