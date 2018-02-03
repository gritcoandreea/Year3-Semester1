package com.example.andreeagritco.exam25.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

public class NetworkUtils {

    public static Boolean isNetworkAvailable(Object connectivityManager2) {
        ConnectivityManager connectivityManager = (ConnectivityManager) connectivityManager2;
        NetworkInfo activeNewtorkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNewtorkInfo != null && activeNewtorkInfo.isConnected();
    }

}
