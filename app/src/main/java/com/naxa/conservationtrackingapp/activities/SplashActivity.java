package com.naxa.conservationtrackingapp.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naxa.conservationtrackingapp.MainActivity;
import com.naxa.conservationtrackingapp.PhoneUtils;
import com.naxa.conservationtrackingapp.R;
import com.naxa.conservationtrackingapp.application.ApplicationClass;
import com.naxa.conservationtrackingapp.application.BaseActivity;
import com.naxa.conservationtrackingapp.defaulthome.DefaultHomeActivity;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import Utls.SharedPreferenceUtils;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking.IMEI;

public class SplashActivity extends BaseActivity {
    private static final int MULTIPLE_PERMISSION_CODE = 22;
    private static String TAG = "SplashActivity";
    GpsTracker gps;
    GPS_TRACKER_FOR_POINT gpsTrackerForPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        gpsTrackerForPoint = new GPS_TRACKER_FOR_POINT(this);
        gps = new GpsTracker(SplashActivity.this);


        if (isPermissionAllowed() || Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            toDashBoard();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPermissionRationale();
                }
            }, TimeUnit.SECONDS.toMillis(3));

        }
    }

    private boolean isPermissionAllowed() {

        boolean hasCameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_GRANTED;
        boolean hasStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;

        //perform sanity check
        boolean hasIMEI = !TextUtils.isEmpty(SharedPreferenceUtils.getInstance(getApplicationContext()).getStringValue(IMEI, ""));
        boolean hasFolders = ApplicationClass.hasCTAfolders();


        return hasCameraPermission && hasLocationPermission && hasIMEI && hasFolders;
    }

    private void requestMultiplePermission() {


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, MULTIPLE_PERMISSION_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION_CODE:
                if (grantResults.length == 0) {
                    Toast.makeText(getApplicationContext(), " Required permission were not given\napp may function improperly", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean hasCameraPermission = grantResults[0] == PERMISSION_GRANTED;
                boolean hasStoragePermission = grantResults[1] == PERMISSION_GRANTED;
                boolean hasLocationPermission = grantResults[2] == PERMISSION_GRANTED;
                boolean hasPhonePermission = grantResults[3] == PERMISSION_GRANTED;

                if (hasPhonePermission) {

                    SharedPreferenceUtils.getInstance(getApplicationContext()).setValue(IMEI, PhoneUtils.getDeviceId());
                }

                if (hasLocationPermission) {

                }

                if (hasStoragePermission) {
                    try {
                        ApplicationClass.createFolder();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to create folders ", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                //perform sanity check
                boolean hasIMEI = !TextUtils.isEmpty(SharedPreferenceUtils.getInstance(getApplicationContext()).getStringValue(IMEI, ""));
                boolean hasFolders = ApplicationClass.hasCTAfolders();

                if (!hasIMEI) {
                    Toast.makeText(this, "Failed to read IMEI", Toast.LENGTH_LONG).show();
                }

                if (hasCameraPermission && hasStoragePermission && hasLocationPermission && hasIMEI && hasFolders) {

                    toDashBoard();

                } else {
                    showPermissionRationale();
                }
                break;
            default:
                Toast.makeText(this, "A unhandled error occurred", Toast.LENGTH_SHORT).show();

        }

    }

    private void toDashBoard() {

        boolean isLoggedIn = (new SharedPreferenceUtils(this).getBoolanValue(SharedPreferenceUtils.KEY_IS_USER_LOGGED_IN, false));
        Intent intent;

        if (isLoggedIn) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, DefaultHomeActivity.class);
        }


        startActivity(intent);
        finish();

    }

    private void showPermissionRationale() {
        new AlertDialog.Builder(this).setMessage(String.format("%s requires consent to access on device gps, storage, camera and imei", getString(R.string.app_name)))
                .setTitle("Setup")
                .setPositiveButton("Start setup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestMultiplePermission();
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else {
                            finish();
                        }
                    }
                }).show()
        ;
    }

}
