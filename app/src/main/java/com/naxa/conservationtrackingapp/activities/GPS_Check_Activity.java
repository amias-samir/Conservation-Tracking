package com.naxa.conservationtrackingapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.naxa.conservationtrackingapp.R;

public class GPS_Check_Activity extends AppCompatActivity {

    GPS_TRACKER_FOR_POINT gpsTrackerForPoint;
    GpsTracker gps;
    TextView tv;
    double finalLat, finalLong;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_check_activity);

        tv = (TextView) findViewById(R.id.shimmer_tv);

        gpsTrackerForPoint = new GPS_TRACKER_FOR_POINT(this);

        GpsTracer gpsTracer = new GpsTracer(this);

        gps = new GpsTracker(GPS_Check_Activity.this);

//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (finalLat !=0 || finalLong != 0 || gps.canGetLocation()) {
            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            gps = new GpsTracker(GPS_Check_Activity.this);
            gpsTrackerForPoint = new GPS_TRACKER_FOR_POINT(this);

            Intent intent = new Intent(GPS_Check_Activity.this, SplashActivity.class);
            startActivity(intent);
            finish();

        } else{
//
            new AlertDialog.Builder(this)
                    .setMessage("Trun on your GPS")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);

                            // Setting Dialog Title
                            alertDialog.setTitle("Use Location?");

                            //Setting Diaglog Message
                            alertDialog.setMessage("This app wants to change your device settings:" +
                                    "\n\nUse GPS, Wi-Fi and mobile network for high accuracy location." +
                                    "\n\nEnable your GPS using SETTINGS BUTTON");

                            // On pressing Settings button
                            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    context.startActivity(intent);
                                    finish();
                                }
                            });

                            // on pressing cancel button
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    finish();
                                }
                            });

                            // Showing Alert Message
                            try{
                                alertDialog.show();
                            }catch (NullPointerException e){

                            }
//
                        }

                    })
                    .show();

        }
    }
}

