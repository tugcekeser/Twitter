package com.codepath.apps.mysimpletweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.constants.General;
import com.codepath.apps.mysimpletweets.fragments.MentionsFragment;
import com.codepath.apps.mysimpletweets.fragments.TimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.UserLikesFragment;
import com.codepath.apps.mysimpletweets.fragments.UserMediaFragment;
import com.codepath.apps.mysimpletweets.fragments.UserTweetsFragment;
import com.codepath.apps.mysimpletweets.models.User;

/**
 * Created by Tugce on 11/4/2016.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {
    private static int PAGE_COUNT = 3;
    private User user;
    private String tabTitles[] = new String[] {General.TWEETS, General.MEDIA, General.LIKES };

    public ProfilePagerAdapter(FragmentManager fragmentManager,User user) {
        super(fragmentManager);
        this.user=user;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return UserTweetsFragment.newInstance(position,user);
            case 1:
                return UserMediaFragment.newInstance(position,user);
            case 2:
                return UserLikesFragment.newInstance(position,user);
            default:
                return UserTweetsFragment.newInstance(position,user);
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}