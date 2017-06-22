package com.enenim.mybakingapp.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.enenim.mybakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class CommonUtil {
    public static void playVideo(String key, Context context){
    }

    public static boolean isXLargeScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

}
