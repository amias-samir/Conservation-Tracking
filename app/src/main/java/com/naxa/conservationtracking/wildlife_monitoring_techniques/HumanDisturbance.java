package com.naxa.conservationtracking.wildlife_monitoring_techniques;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtracking.GeoPointActivity;
import com.naxa.conservationtracking.activities.CalculateAreaUsinGPS;
import com.naxa.conservationtracking.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtracking.activities.GpsTracker;
import com.naxa.conservationtracking.activities.MapPolyLineActivity;
import com.naxa.conservationtracking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import com.naxa.conservationtracking.database.DataBaseConserVationTracking;
import com.naxa.conservationtracking.dialog.Default_DIalog;
import com.naxa.conservationtracking.model.CheckValues;
import com.naxa.conservationtracking.model.Constants;
import com.naxa.conservationtracking.model.StaticListOfCoordinates;

import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class HumanDisturbance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, forestFire, spinnerLivestock, spinnerPoaching, spinnerTypeOfPoaching, spinnerHumanPresence;
    ArrayAdapter landscapeAdpt, forestFireAdapter, spinnerLivestockAdapter, adptPoaching, adptTypesOfPoaching, adptHumanPresence;
    Button send, save, startGps, endGps, previewMap, dateBtn, startTimeBtn, endTimeBtn, addBtn;
    ProgressDialog mProgressDlg;
    Context context = this;
    GpsTracker gps;
    String jsonToSend, photoTosend;
    String imagePath, encodedImage = null, imageName = "no_photo";
    ImageButton photo;
    double distance;
    double area_using_Gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    boolean isGpsTaken1 = false;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    ProgressBar tracking;
    ImageView previewImageSite;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";

    String projectCode;
    String landscape;
    String other_landscape;
    String funding_source;
    String district_name;
    String vdc_name;
    String observer_name;
    String forest_fire;
    String livestock_trail;
    String date;
    String location;
    String time_start;
    String time_end;
    String grid_no;
    String spatial_replicate_no;
    String all_segment;
    String segment_no;
    String wood_cut_no;
    String lopping_no;
    String tree_felling_no;
    String construction_no;
    String no_of_segments;
    String human_presence;
    String no_of_human;
    String evidence_of_poaching;
    String types_of_poaching;

    RelativeLayout relativeLayout;
    String other_poaching;
    String livestock_no;
    String others_dialog;
    String remarks;
    String fund_tal, fund_community, fund_others;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    TextView tvdate, tvtime_start, tvtime_end, tvAddNo;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvDistrictname, tvNameOfVdc, tvobserver_name, tvProjectCode,
            tvlocation, tvtree_felling_no, tvconstruction_no, tvlivestock_no,tvFundTal, tvFundCommunity, tvFundOthers,
            tvgrid_no, tvSpatialReplicateNo, tvSegmentNo, tvwood_cut_no, tvlopping_no, tvNotes, tvOtherPoaching, tvNoOfHuman, tvAddSegmentNote;
    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 11;
    static final int TIME_DIALOG_ID1 = 12;
    String dataSentStatus = "", dateString;

    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    List<String> speciesSegmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmt_human_disturbances);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.human_disturbances_ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.wmt_human_disturbances_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.wmt_human_disturbances_detail_fundingSource);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.human_disturbances_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.human_disturbances_Vdc_Municipality_Name);
        tvobserver_name = (AutoCompleteTextView) findViewById(R.id.human_disturbances_observer_name);
        tvlocation = (AutoCompleteTextView) findViewById(R.id.human_disturbances_location);
        tvgrid_no = (AutoCompleteTextView) findViewById(R.id.human_disturbances_grid_no);
        tvSpatialReplicateNo = (AutoCompleteTextView) findViewById(R.id.human_disturbances_Spatial_Replicate_no);
        tvAddNo = (TextView) findViewById(R.id.human_disturbances_added_no);
        addBtn = (Button) findViewById(R.id.human_disturbances_add_btn);
        tvNotes = (AutoCompleteTextView) findViewById(R.id.human_disturbances_remarks);
        tvBoundryUsingGps = (TextView) findViewById(R.id.human_disturbances_boundry_using_gps);

        tvdate = (TextView) findViewById(R.id.human_disturbances_date);
        dateBtn = (Button) findViewById(R.id.date_btn);
        tvtime_start = (TextView) findViewById(R.id.human_disturbances_start_time);
        startTimeBtn = (Button) findViewById(R.id.start_time_btn);
        tvtime_end = (TextView) findViewById(R.id.human_disturbances_end_time);
        endTimeBtn = (Button) findViewById(R.id.end_time_btn);
        setCurrentDateOnView();
        addListenerOnButton();

        setCurrentTimeOnView();
        addListenerOnTimeButton();
        tvFundTal = (AutoCompleteTextView) findViewById(R.id.human_disturbances_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.human_disturbances_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.human_disturbances_FundOthers);
        
        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        send = (Button) findViewById(R.id.human_disturbances_send);
        save = (Button) findViewById(R.id.human_disturbances_save);

        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        startGps = (Button) findViewById(R.id.human_disturbances_GpsStart);
        endGps = (Button) findViewById(R.id.human_disturbances_GpsEnd);

        previewMap = (Button) findViewById(R.id.human_disturbances_preview_map);

        initilizeUI();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSpeciesSeenDialog();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isGpsTracking) {
                    Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
                } else {

                    if (isGpsTaken) {
//                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//                        int width = metrics.widthPixels;
//                        int height = metrics.heightPixels;
//
//                        final Dialog showDialog = new Dialog(context);
//
//                        showDialog.setContentView(R.layout.login_layout);
//                        final EditText userName = (EditText) showDialog.findViewById(R.id.input_userName);
//                        final EditText password = (EditText) showDialog.findViewById(R.id.input_password);
//
//                        AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
//                        showDialog.setTitle("Authentication");
//                        showDialog.setCancelable(true);
//                        showDialog.show();
//                        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                        logIn.setOnClickListener(new View.OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                // TODO Auto-generated method stub
                        String userN = UserNameAndPasswordUtils.getUserNameAndPassword(context).get(0);
                        String passW = UserNameAndPasswordUtils.getUserNameAndPassword(context).get(1);
                                if (userN == null || userN.equals("") || passW == null || passW.equals("")) {
                                    Toast.makeText(context, "Either your user name or password is empty.Please fill the required field. ", Toast.LENGTH_SHORT).show();
                                } else {
//                                    showDialog.dismiss();

                                    projectCode = tvProjectCode.getText().toString();
                                    other_landscape = tvOtherLandscape.getText().toString();
                                    funding_source = tvFundingSource.getText().toString();
                                    district_name = tvDistrictname.getText().toString();
                                    vdc_name = tvNameOfVdc.getText().toString();
                                    observer_name = tvobserver_name.getText().toString();
                                    date = tvdate.getText().toString();
                                    location = tvlocation.getText().toString();
                                    time_start = tvtime_start.getText().toString();
                                    time_end = tvtime_end.getText().toString();
                                    grid_no = tvgrid_no.getText().toString();
                                    spatial_replicate_no = tvSpatialReplicateNo.getText().toString();
                                    no_of_segments = tvAddNo.getText().toString();
                                    remarks = tvNotes.getText().toString();
                                    fund_tal = tvFundTal.getText().toString();
                                    fund_community = tvFundCommunity.getText().toString();
                                    fund_others = tvFundOthers.getText().toString();

                                    userNameToSend = userN;
                                    passwordToSend = passW;

                                    Log.e("SEND", "Clicked");
                                    mProgressDlg = new ProgressDialog(context);
                                    mProgressDlg.setMessage("Please Wait...");
                                    mProgressDlg.setIndeterminate(false);
                                    mProgressDlg.setCancelable(false);
                                    mProgressDlg.show();
                                    convertDataToJson();
                                    sendDatToserver();
                                }
//                            }
//                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You need to take at least one gps cooordinate", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGpsTracking) {
                    Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
                } else {

                    if (isGpsTaken) {
                        projectCode = tvProjectCode.getText().toString();
                        other_landscape = tvOtherLandscape.getText().toString();
                        funding_source = tvFundingSource.getText().toString();
                        district_name = tvDistrictname.getText().toString();
                        vdc_name = tvNameOfVdc.getText().toString();
                        observer_name = tvobserver_name.getText().toString();
                        date = tvdate.getText().toString();
                        location = tvlocation.getText().toString();
                        time_start = tvtime_start.getText().toString();
                        time_end = tvtime_end.getText().toString();
                        grid_no = tvgrid_no.getText().toString();
                        spatial_replicate_no = tvSpatialReplicateNo.getText().toString();
                        no_of_segments = tvAddNo.getText().toString();
                        remarks = tvNotes.getText().toString();
                        fund_tal = tvFundTal.getText().toString();
                        fund_community = tvFundCommunity.getText().toString();
                        fund_others = tvFundOthers.getText().toString();

                        jsonLatLangArray = jsonArrayGPS.toString();

                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("Human Disturbance");

                        long date = System.currentTimeMillis();

                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                        String dateString = sdf.format(date);
                        dateToInput.setText(dateString);

                        AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
                        showDialog.setTitle("Save Data");
                        showDialog.setCancelable(true);
                        showDialog.show();
                        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                        logIn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String dateDataCollected = dateToInput.getText().toString();
                                String formName = FormNameToInput.getText().toString();
                                if (dateDataCollected == null || dateDataCollected.equals("") || formName == null || formName.equals("")) {
                                    Toast.makeText(context, "Please fill the required field. ", Toast.LENGTH_SHORT).show();
                                } else {
                                    String[] data = new String[]{"84", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(HumanDisturbance.this)
                                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                            .setAnimationEnable(true)
                                            .setTitleText(getString(R.string.dialog_success))
                                            .setContentText(getString(R.string.dialog_saved))
                                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                                @Override
                                                public void onClick(PromptDialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                    dataBaseConserVationTracking.close();
                                    showDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You need to take at least one gps cooordinate", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        startGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);

            }
        });
//---------------------------------------------------------------------------------------------------------------------------------

        endGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                unregisterReceiver(mReceiver);
                endGps.setEnabled(false);
                tracking.setVisibility(View.INVISIBLE);
                isGpsTracking = false;
                startGps.setEnabled(true);

                initLat = listCf.get(0).latitude;
                initLong = listCf.get(0).longitude;

                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    String gpsArray = stringBuilder.toString();
                    StringBuilder sb = new StringBuilder();

                    Log.e("gpsarray:", gpsArray);

                    sb.append("[" + gpsArray + "]");
                    latLangArray = sb.toString();
                    Log.e("CFDETAIL", "" + latLangArray);

                    area_using_Gps = 0.0001 * CalculateAreaUsinGPS.calculateAreaOfGPSPolygonOnEarthInSquareMeters(gpslocation);//Area in Hectares
                    tvBoundryUsingGps.setText("Area Using GPS : " + area_using_Gps + " (Hectares)");

                    float[] results = new float[1];
                    Location.distanceBetween(
                            initLat, initLong,
                            finalLat, finalLong, results);
                    distance = 0.001 * results[0];//Distance in Kilometers
                    Default_DIalog.showDefaultDialog(context, R.string.gps_Info, "Distance measured (Kilometers) : " + ( String.format( "Value of a: %.8f", distance ) )  + "\nArea Calculated (Hectares): " + area_using_Gps);

                } else {
                    Default_DIalog.showDefaultDialog(context, R.string.gps_Info, "GPS is not initialized properly");

                }

            }
        });

        previewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpslocation.clear();
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(HumanDisturbance.this, MapPolyLineActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_update) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnerId = parent.getId();
        if (spinnerId == R.id.wmt_human_disturbances_detail_landscape) {
            switch (position) {
                case 0:
                    landscape = "TAL PABZ";
                    break;
                case 1:
                    landscape = "TAL CBRP";
                    break;
                case 2:
                    landscape = "SHL";
                    break;
                case 3:
                    landscape = "CHAL";
                    break;
                case 4:
                    landscape = "NML";
                    break;
                case 5:
                    landscape = "Others";
                    break;

            }
            if (spinnerLandscape.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherLandscape.setVisibility(View.VISIBLE);
            } else {
                tvOtherLandscape.setVisibility(View.GONE);
            }
        }

        if (spinnerId == R.id.human_disturbances_forest_fire) {
            switch (position) {
                case 0:
                    forest_fire = "Yes";
                    break;
                case 1:
                    forest_fire = "No";
                    break;

            }
        }
        if (spinnerId == R.id.human_disturbances_livestock_trail) {
            switch (position) {
                case 0:
                    livestock_trail = "Yes";
                    break;
                case 1:
                    livestock_trail = "No";
                    break;
            }
            if (spinnerLivestock.getItemAtPosition(position).toString().equals("Yes")) {
                tvlivestock_no.setVisibility(View.VISIBLE);
            } else {
                tvlivestock_no.setVisibility(View.GONE);
            }
        }

        if (spinnerId == R.id.human_disturbances_human_presence) {
            switch (position) {
                case 0:
                    human_presence = "Yes";
                    break;
                case 1:
                    human_presence = "No";
                    break;
            }
            if (spinnerHumanPresence.getItemAtPosition(position).toString().equals("Yes")) {
                tvNoOfHuman.setVisibility(View.VISIBLE);
            } else {
                tvNoOfHuman.setVisibility(View.GONE);
            }
        }

        if (spinnerId == R.id.human_disturbances_poaching) {
            switch (position) {
                case 0:
                    evidence_of_poaching = "Yes";
                    break;
                case 1:
                    evidence_of_poaching = "No";
                    break;
            }
            if (spinnerPoaching.getItemAtPosition(position).toString().equals("Yes")) {
                relativeLayout.setVisibility(View.VISIBLE);
                types_of_poaching = "";
            } else {
                relativeLayout.setVisibility(View.GONE);
            }
        }

        if (spinnerId == R.id.human_disturbances_poaching_types) {
            switch (position) {
                case 0:
                    types_of_poaching = "Gun Shot";
                    break;
                case 1:
                    types_of_poaching = "Poacher's Camp";
                    break;
                case 2:
                    types_of_poaching = "Snares";
                    break;
                case 3:
                    types_of_poaching = "Poacher's Activities";
                    break;
                case 4:
                    types_of_poaching = "Others";
                    break;

            }
            if (spinnerTypeOfPoaching.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherPoaching.setVisibility(View.VISIBLE);
            } else {
                tvOtherPoaching.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.close_dialog);
        final Button yes = (Button) showDialog.findViewById(R.id.buttonYes);
        final Button no = (Button) showDialog.findViewById(R.id.buttonNo);

        AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
        showDialog.setTitle("Exit Fomr");
        showDialog.setCancelable(false);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);

//                image_name_tv.setText(filePath);
                imagePath = filePath;
                //  addImage();
                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//                    //FINE
//
//                }
//                else{
//                    //NOT IN REQUIRED FORMAT
//                }
            }
//        if( requestCode == CAMERA_PIC_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                //  ImageView image =(ImageView) findViewById(R.id.Photo);
//                // image.setImageBitmap(thumbnail);
//                previewImageSite.setVisibility(View.VISIBLE);
//                previewImageSite.setImageBitmap(thumbnail);
//                saveToExternalSorage(thumbnail);
//                addImage();
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
//            }
//        }

        if (requestCode == GEOPOINT_RESULT_CODE) {

            switch (resultCode) {
                case RESULT_OK:

                    tracking.setVisibility(View.VISIBLE);
                    isGpsTracking = true;
                    listCf.clear();
                    gpslocation.clear();
                    gps = new GpsTracker(HumanDisturbance.this);
                    if (gps.canGetLocation()) {
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "Your Location is - \nLat: " + initLat
//                                    + "\nLong: " + initLong, Toast.LENGTH_SHORT)
//                            .show();
                    } else {
                        gps.showSettingsAlert();
                    }

                    UpdateData();//Update GPS coordinate background thread //Method

                    endGps.setEnabled(true);
                    startGps.setEnabled(false);
                    //                    Toast.makeText(this.context, location, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    private void saveToExternalSorage(Bitmap thumbnail) {
        // TODO Auto-generated method stub
        //String merocinema="Mero Cinema";
//        String movname=getIntent().getExtras().getString("Title");
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        imageName = "human_disturbances_" + timeInMillis;

        File file1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), imageName);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), "Not Created", Toast.LENGTH_SHORT).show();
//        }

        if (file1.exists()) file1.delete();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), "Saved " + imageName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void initilizeUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            CheckValues.isFromSavedFrom = true;
            listCf.clear();
            isGpsTaken = true;
            startGps.setEnabled(false);
            endGps.setEnabled(false);
            dateBtn.setEnabled(false);
            startTimeBtn.setEnabled(false);
            endTimeBtn.setEnabled(false);
            addBtn.setEnabled(false);
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }

            try {
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GpsTracker(this);
            CheckValues.isFromSavedFrom = false;
            startGps.setEnabled(true);
            endGps.setEnabled(false);
        }
    }


    //new adjustment
    public void parseArrayGPS(String arrayToParse) {
        try {
            JSONArray array = new JSONArray(arrayToParse);
            for (int i = 0; i < array.length(); ++i) {
                JSONObject item1 = null;

                item1 = array.getJSONObject(i);
                LatLng location = new LatLng(item1.getDouble("latitude"), item1.getDouble("longitude"));

                listCf.add(location);

//            mMap.addMarker(new MarkerOptions().position(location).title("Start"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromStorage(String path) {
        try {
            previewImageSite.setVisibility(View.VISIBLE);
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            previewImageSite.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "invalid path", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //same for all
    private void UpdateData() {
        mProgressDlg = new ProgressDialog(context);
        mProgressDlg.setMessage("Acquiring GPS location\nPlease wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

        gps = new GpsTracker(HumanDisturbance.this);
        if (gps.canGetLocation()) {

            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gps = new GpsTracker(HumanDisturbance.this);
                                if (gps.canGetLocation()) {
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();

                                    Log.e("latlang", " lat: " + finalLat + " long: "
                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gps = new GpsTracker(HumanDisturbance.this);
                                if (gps.canGetLocation()) {
                                    gpslocation.add(gps.getLocation());
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();
                                    if (finalLat != 0) {

                                        try {
                                            JSONObject data = new JSONObject();
                                            data.put("latitude", finalLat);
                                            data.put("longitude", finalLong);

                                            jsonArrayGPS.put(data);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        LatLng d = new LatLng(finalLat, finalLong);
                                        isGpsTaken = true;
                                        listCf.add(d);
                                        Log.e("latlang", " lat: " + finalLat + " long: "
                                                + finalLong);
                                        stringBuilder.append("[" + finalLat + "," + finalLong + "]" + ",");

                                    } else {
                                        gps.showSettingsAlert();
                                    }
                                }
                            }
                        }

                    });
                }
            }, 0, 5, TimeUnit.SECONDS);

        } else {
            try {
                gps.showSettingsAlert();
            } catch (NullPointerException e) {

            }

        }
    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_human_disturbance");

            JSONObject header = new JSONObject();

            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("observer_name", observer_name);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("location", location);
            header.put("date", date);
            header.put("time_start", time_start);
            header.put("time_end", time_end);
            header.put("latitude", finalLat);
            header.put("longitude", finalLong);
            header.put("grid_no", grid_no);
            header.put("spatial_replicate_no", spatial_replicate_no);
            header.put("all_species", all_segment);
            header.put("no_of_segments", no_of_segments);
            header.put("others", remarks);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();
            Log.e("convertDataToJsonSave", "convertDataToJsonSave: "+jsonToSend );

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("LATTI", "" + finalLat);
            editor.putString("LONGI", "" + finalLong);
            editor.apply();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendDatToserver() {
        if (jsonToSend.length() > 0) {
            Log.e("Inside Send", "BeforeSending " + jsonToSend);

            RestApii restApii = new RestApii();
            restApii.execute();
        }
    }

    public void parseJson(String jsonToParse) throws JSONException {
        JSONObject jsonOb = new JSONObject(jsonToParse);
        Log.e("PlantationDETAIL", "json : " + jsonOb.toString());
        String data = jsonOb.getString("formdata");
        Log.e("PlantationDETAIL", "formdata : " + jsonOb.toString());
        JSONObject jsonObj = new JSONObject(data);
        Log.e("PlantationDETAIL", "json : " + jsonObj.toString());


        projectCode = jsonObj.getString("project_code");
        landscape = jsonObj.getString("landscape");
        funding_source = jsonObj.getString("funding_source");
        observer_name = jsonObj.getString("observer_name");
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        location = jsonObj.getString("location");
        date = jsonObj.getString("date");
        time_start = jsonObj.getString("time_start");
        time_end = jsonObj.getString("time_end");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        grid_no = jsonObj.getString("grid_no");
        spatial_replicate_no = jsonObj.getString("spatial_replicate_no");

        no_of_segments = jsonObj.getString("no_of_segments");
        all_segment = jsonObj.getString("all_species");

        JSONArray jsonArray = new JSONArray(all_segment);
        Log.e("ALL_SEGMENT_PARSE_JSON", "SAMIR_parseJson: "+jsonArray.length() );

        try {
            for (int i = 0 ; i < jsonArray.length(); i++){
                speciesSegmentList.add(jsonArray.get(i).toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        remarks = jsonObj.getString("others");

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvobserver_name.setText(observer_name);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvlocation.setText(location);
        tvdate.setText(date);
        tvtime_start.setText(time_start);
        tvtime_end.setText(time_end);
        tvgrid_no.setText(grid_no);
        tvSpatialReplicateNo.setText(spatial_replicate_no);
        tvAddNo.setText(no_of_segments);
        tvNotes.setText(remarks);
        tvFundTal.setText(fund_tal);
        tvFundCommunity.setText(fund_community);
        tvFundOthers.setText(fund_others);

        String[] actions = landscape.split(":  ");
        if (actions[0].equals("TAL PABZ") || actions[0].equals("TAL CBRP") ||
                actions[0].equals("SHL") || actions[0].equals("CHAL") || actions[0].equals("NML")) {

            int setlandscape = landscapeAdpt.getPosition(actions[0]);
            spinnerLandscape.setSelection(setlandscape);
            tvOtherLandscape.setVisibility(View.GONE);

        } else {

            int setlandscape = landscapeAdpt.getPosition(actions[0]);
            spinnerLandscape.setSelection(setlandscape);
            tvOtherLandscape.setText(actions[1]);

        }

    }


    private void showAddSpeciesSeenDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.add_humandisturbancesegment_dialog);
        final GPS_TRACKER_FOR_POINT gps1 = null;
        final Button startGps1, previewMap1;

        Button btnOk = (Button) showDialog.findViewById(R.id.human_disturbances_add_button);

        tvtree_felling_no = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_tree_felling_no);
        tvconstruction_no = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_construction_no);
        tvlivestock_no = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_livestock_no);
        spinnerHumanPresence = (Spinner) showDialog.findViewById(R.id.human_disturbances_human_presence);
        tvNoOfHuman = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_human_presence_number);
        spinnerPoaching = (Spinner) showDialog.findViewById(R.id.human_disturbances_poaching);
        spinnerTypeOfPoaching = (Spinner) showDialog.findViewById(R.id.human_disturbances_poaching_types);
        tvOtherPoaching = (AutoCompleteTextView) showDialog.findViewById(R.id.tv_other_poaching);
        tvAddSegmentNote = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_dialog_note);

        relativeLayout = (RelativeLayout) showDialog.findViewById(R.id.relativelayout_poaching_types);

        tvSegmentNo = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_segment);
        tvwood_cut_no = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_woodcut_no);
        tvlopping_no = (AutoCompleteTextView) showDialog.findViewById(R.id.human_disturbances_lopping_no);
        forestFire = (Spinner) showDialog.findViewById(R.id.human_disturbances_forest_fire);
        spinnerLivestock = (Spinner) showDialog.findViewById(R.id.human_disturbances_livestock_trail);

        startGps1 = (Button) showDialog.findViewById(R.id.species_survey_detail_GpsStart1);
        previewMap1 = (Button) showDialog.findViewById(R.id.species_survey_detail_preview_map1);

        forestFireAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.FOREST_FIRE);
        forestFireAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forestFire.setAdapter(forestFireAdapter);
        forestFire.setOnItemSelectedListener(this);

        spinnerLivestockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LIVESTOCK_TRAIL);
        spinnerLivestockAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLivestock.setAdapter(spinnerLivestockAdapter);
        spinnerLivestock.setOnItemSelectedListener(this);

        adptHumanPresence = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.HUMAN_PRESENCE);
        adptHumanPresence
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHumanPresence.setAdapter(adptHumanPresence);
        spinnerHumanPresence.setOnItemSelectedListener(this);

        adptPoaching = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LIVESTOCK_TRAIL);
        adptPoaching
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPoaching.setAdapter(adptPoaching);
        spinnerPoaching.setOnItemSelectedListener(this);

        adptTypesOfPoaching = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.TYPES_OF_POACHING);
        adptTypesOfPoaching
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOfPoaching.setAdapter(adptTypesOfPoaching);
        spinnerTypeOfPoaching.setOnItemSelectedListener(this);

        showDialog.setTitle("Add New Segment");
        showDialog.getActionBar();
        showDialog.show();
        showDialog.getWindow().setLayout((width), LinearLayout.LayoutParams.WRAP_CONTENT);


        startGps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalLat = gps.getLatitude();
                finalLong = gps.getLongitude();
                isGpsTaken1 = true;

                Toast.makeText(
                        getApplicationContext(),
                        "Your Location is - \nLat: " + finalLat
                                + "\nLong: " + finalLong, Toast.LENGTH_SHORT)
                        .show();
            }

        });

        previewMap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpslocation.clear();
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(HumanDisturbance.this, MapPolyLineActivity.class));
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isGpsTaken1) {
                    segment_no = tvSegmentNo.getText().toString();
                    wood_cut_no = tvwood_cut_no.getText().toString();
                    lopping_no = tvlopping_no.getText().toString();
                    tree_felling_no = tvtree_felling_no.getText().toString();
                    construction_no = tvconstruction_no.getText().toString();
                    livestock_trail = tvlivestock_no.getText().toString();
                    no_of_human = tvNoOfHuman.getText().toString();
                    livestock_no = tvlivestock_no.getText().toString();
                    other_poaching = tvOtherPoaching.getText().toString();
                    others_dialog = tvAddSegmentNote.getText().toString();

                    JSONObject memberObj = new JSONObject();
                    try {

                        memberObj.put("segment_no", segment_no);
                        memberObj.put("wood_cut_no", wood_cut_no);
                        memberObj.put("lopping_no", lopping_no);
                        memberObj.put("tree_felling_no", tree_felling_no);
                        memberObj.put("construction_no", construction_no);
                        memberObj.put("livestock_trail", livestock_trail);
                        memberObj.put("human_presence", human_presence);
                        memberObj.put("no_of_human", no_of_human);
                        memberObj.put("livestock_no", livestock_no);
                        memberObj.put("evidence_of_poaching", evidence_of_poaching);
                        memberObj.put("types_of_poaching", types_of_poaching + ":  " + other_poaching);
                        memberObj.put("forest_fire", forest_fire);
                        memberObj.put("latitude", finalLat);
                        memberObj.put("longitude", finalLong);
                        memberObj.put("others", others_dialog);

                        Log.e("all_species: ", memberObj.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    speciesSegmentList.add(memberObj.toString());
                    all_segment = speciesSegmentList.toString();

                    tvAddNo.setText(speciesSegmentList.size() + " Segment added");
                    showDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Take at least one gps coordinate", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        cancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                showDialog.dismiss();
//            }
//        });

    }

    private class RestApii extends AsyncTask<String, Void, String> {
        protected String getASCIIContentFromEntity(HttpURLConnection entity)
                throws IllegalStateException, IOException {
            InputStream in = (InputStream) entity.getContent();


            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);

                if (n > 0)
                    out.append(new String(b, 0, n));
            }

            return out.toString();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String text = null;

            try {
                text = POST(getString(R.string.api_host_url));
                JSONObject jsonObject = new JSONObject(text);
                dataSentStatus = jsonObject.getString("data");


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            mProgressDlg.dismiss();
            if (result != null) {

                //{"code":1,"status":200,"data":"ok"}
                if (dataSentStatus.equals("ok")) {
                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                    dateString = sdf.format(date);
                    new PromptDialog(HumanDisturbance.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText(getString(R.string.dialog_success))
                            .setContentText(getString(R.string.dialog_sent))
                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();

                    String[] data = new String[]{"84", "Human Disturbance", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(HumanDisturbance.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                            .setAnimationEnable(true)
                            .setTitleText(getString(R.string.dialog_error))
                            .setContentText(getString(R.string.dialog_error_desc))
                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }

        }

        public String POST(String urll) {
            InputStream inputStream = null;
            String result = "";
            URL url;

            try {
                url = new URL(urll);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("data", jsonToSend)
                        .appendQueryParameter("username", userNameToSend)
                        .appendQueryParameter("password", passwordToSend);
                //.appendQueryParameter("photo", encodedImage);

                String query = builder.build().getEncodedQuery();

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                } else {
                    result = "";
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    }

    //Date picker
    // display current date
    public void setCurrentDateOnView() {

        //dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvdate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("/").append(month + 1).append("/")
                .append(day).append(""));
    }


    public void addListenerOnButton() {


        dateBtn.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dateBtn.setShowSoftInputOnFocus(false);
                }
                showDialog(DATE_DIALOG_ID);
            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
                        day);

            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
            case TIME_DIALOG_ID1:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener1, hour, minute,
                        false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when com.naxa.conservationtracking.dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvdate.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(""));
        }
    };

    // Time picker code

    // display current time
    public void setCurrentTimeOnView() {

//        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);


    }

    public void addListenerOnTimeButton() {


        startTimeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }

        });

        endTimeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID1);

            }

        });

    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
            tvtime_start.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));

            // set current time into timepicker
//            timePicker1.setCurrentHour(hour);
//            timePicker1.setCurrentMinute(minute);

        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;


            tvtime_end.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));

            // set current time into timepicker
//            timePicker1.setCurrentHour(hour);
//            timePicker1.setCurrentMinute(minute);

        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
