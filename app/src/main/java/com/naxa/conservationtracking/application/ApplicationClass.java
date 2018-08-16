package com.naxa.conservationtracking.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by samir on 2/11/2018.
 */

public class ApplicationClass extends Application {


    public static String mainFolder = "/ConservationTracking";
    public static String photoFolder = "/Photos";
    public static String dataFolder = "/Data";
    public static String extSdcard = Environment.getExternalStorageDirectory().toString();

    public static String PHOTO_PATH = extSdcard + mainFolder + photoFolder;
    private static Context app;

    public static Context getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = getApplicationContext();

    }


    public static void createFolder() throws Exception {

        Log.i("MapboxApplication", "Trying to create required folders");

//        File dirPhoto = new File(extSdcard + mainFolder + photoFolder);
        File dirData = new File(extSdcard + mainFolder + dataFolder);
        File dirPhoto = new File(PHOTO_PATH);


//        if(!dirPhoto.exists()){
//            if (!dirPhoto.mkdirs()){
//                throw new Exception("Failed to create photo folder");
//            }
//        }


        if (!dirData.exists()) {
            if (!dirData.mkdirs()) {
                throw new Exception("Failed to create database folder");
            }
        }

        if (!dirPhoto.exists()) {
            if (!dirPhoto.mkdirs()) {
                throw new Exception("Failed to create photo folder");
            }
        }


    }
}
