package com.enenim.mybakingapp.util;

import android.content.Context;
import android.content.res.Configuration;

public class CommonUtil {
    public static void playVideo(String key, Context context){
    }

    public static boolean isXLargeScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}
