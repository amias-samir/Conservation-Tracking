package com.naxa.conservationtrackingapp.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.naxa.conservationtrackingapp.R;

/**
 * Created by ramaan on 1/26/2016.
 */
public class MapActivity extends Activity  {
//    static LatLng HAMBURG;
//    private GoogleMap map;
    Button tv, start, stop;
    GpsTracker gps;
//    static ArrayList<LatLng> list = new ArrayList<LatLng>();
//    static ArrayList<LatLng> livelist = new ArrayList<LatLng>();

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver mReceiver;
//    Marker hamburg;
    Boolean firstPass=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gps = new GpsTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            Location l = gps.getLocation();
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Log.e("latlang", " lat: " + latitude + " long: " + longitude+" location "+l);
//            HAMBURG = new LatLng(latitude, longitude);
            // \n is for new line
            Toast.makeText(
                    getApplicationContext(),
                    "Your Location is - \nLat: " + latitude + "\nLong: "
                            + longitude, Toast.LENGTH_SHORT).show();
        } else {
            gps.showSettingsAlert();
        }
    }


}
