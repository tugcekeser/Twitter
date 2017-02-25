package com.codepath.apps.mysimpletweets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.MentionsFragment;
import com.codepath.apps.mysimpletweets.fragments.MessageFragment;
import com.codepath.apps.mysimpletweets.fragments.TimelineFragment;

/**
 * Created by Tugce on 11/4/2016.
 */
public class HomePagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private static int PAGE_COUNT = 2;
    private int tabIcons[] = {R.drawable.home,R.drawable.mentions};//,R.drawable.messages};

    public HomePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return TimelineFragment.newInstance(position);
        else //if(position==1)
            return MentionsFragment.newInstance(position);
       /* else
            return MessageFragment.newInstance(position);*/
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}
