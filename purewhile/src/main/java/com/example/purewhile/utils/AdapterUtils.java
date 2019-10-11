package com.example.purewhile.utils;

import android.app.Application;
import android.content.Context;

public class AdapterUtils {

    private static Application application;
    public static void initAdapter(Application app){
        application=app;
    }

    public static Context getContext(){
        if (application!=null){
            return application.getApplicationContext();
        }
        return null;
    }

}
