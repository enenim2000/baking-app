package com.enenim.mybakingapp.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.support.v7.app.AlertDialog;

public class InternetUtil{
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static AlertDialog.Builder showDialog(Context context, int icon, int message) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setIcon(icon);
    }
}
