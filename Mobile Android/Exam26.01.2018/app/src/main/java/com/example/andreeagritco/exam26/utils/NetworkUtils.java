package com.example.andreeagritco.exam26.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

public class NetworkUtils {

    public static Boolean isNetworkAvailable(Object connectivityManager2) {
        ConnectivityManager connectivityManager = (ConnectivityManager) connectivityManager2;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null & activeNetworkInfo.isConnected();
    }
}
