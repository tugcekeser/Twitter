package com.codepath.apps.mysimpletweets.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Tugce on 11/7/2016.
 */
public class Helper  {

    public static final String TWITTER_FORMAT="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = TWITTER_FORMAT ;
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getTwitterFormat(relativeDate);
    }

    //covert to 12m, 13h, ... format
    private static String getTwitterFormat(String relativeDate){
        String tweetRelativeDate="";
        int charCounter=0;

        if(!Character.isDigit(relativeDate.charAt(0)))
            return relativeDate;

        for(int i=0; i<relativeDate.length();i++){
            if(!Character.isDigit(relativeDate.charAt(i)) && relativeDate.charAt(i)!=' '){
                charCounter++;
                if(charCounter==2)
                    break;
            }
            tweetRelativeDate=tweetRelativeDate+relativeDate.charAt(i);
        }
        return tweetRelativeDate;
    }
}
