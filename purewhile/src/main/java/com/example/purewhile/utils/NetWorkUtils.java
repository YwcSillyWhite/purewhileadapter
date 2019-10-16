package com.example.purewhile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;




/**
 * @author yuwenchao
 */
public class NetWorkUtils {

    //判断是否存在网络
    public static boolean isConnected() {
        Context context = AdapterUtils.getContext();
        if (context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           if (connectivityManager != null){
               NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
               if (networkInfo != null) {
                   return networkInfo.isConnected();
               }
           }
           return false;
        }
        return true;
    }

}
