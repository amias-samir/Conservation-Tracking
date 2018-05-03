package com.naxa.conservationtracking.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naxa.conservationtracking.MainActivity;
import com.naxa.conservationtracking.R;
import com.naxa.conservationtracking.application.ApplicationClass;
import com.naxa.conservationtracking.application.BaseActivity;

public class SplashActivity extends BaseActivity {
    private static String TAG = "SplashActivity";
    GpsTracker gps;
    GPS_TRACKER_FOR_POINT gpsTrackerForPoint;

    TextView tv;

    TextView tvWWFNepal;
    private static final int PERMISSION_WRITE = 5654556;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        ApplicationClass.createFolder();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to create ConservationTracking Directories, Forms can't be saved ", Toast.LENGTH_SHORT).show();
//                        Default_DIalog.showDefaultDialog(this, "Unexpected Error Occurred", "Failed to create ConservationTracking Directories, Forms can't be saved ");
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }

                    break;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        tvWWFNepal = (TextView)findViewById(R.id.textView2);
        tv         = (TextView)findViewById(R.id.shimmer_tv);

        Typeface face= Typeface.createFromAsset(getAssets(), "font/junegull.ttf");
        tvWWFNepal.setTypeface(face);

        Typeface face1= Typeface.createFromAsset(getAssets(), "font/abugslife.ttf");
        tv.setTypeface(face1);

        gpsTrackerForPoint = new GPS_TRACKER_FOR_POINT(this);

        GpsTracer gps2 = new GpsTracer(this);

        gps = new GpsTracker(SplashActivity.this);

        Thread pause = new Thread() {
            public void run() {
                try {

                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeBackground);
                    Animation relativeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    relativeLayout.startAnimation(relativeAnim);

                    ImageView image = (ImageView)findViewById(R.id.imageView);
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slow);
                    image.startAnimation(animation1);

                    Animation textViewAnimate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slow);
                    tvWWFNepal.startAnimation(textViewAnimate);

                    Animation textViewAnimate1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slow);
                    tv.startAnimation(textViewAnimate1);

                    sleep(3500);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent stuff = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(stuff);
                }
                finish();
            }
        };
        pause.start();


        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            try {
                ApplicationClass.createFolder();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to create ConservationTracking Directories, Forms can't be saved ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

        } else {

            requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, PERMISSION_WRITE);

        }


    }

}
