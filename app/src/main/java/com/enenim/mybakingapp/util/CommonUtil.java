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

    public static String getMimeType(String url){
        String ext = url.substring(url.indexOf(".") + 1);

        if("".equalsIgnoreCase(url)){
            return "empty";
        }

        if (ext.equalsIgnoreCase("mp4")
                || ext.equalsIgnoreCase("flv")
                || ext.equalsIgnoreCase("avi")) {
            return  "video";
        }

        if (ext.equalsIgnoreCase("jpg")
                || ext.equalsIgnoreCase("png")
                || ext.equalsIgnoreCase("jpeg")) {
            return  "image";
        }

        return "empty";
    }

}
