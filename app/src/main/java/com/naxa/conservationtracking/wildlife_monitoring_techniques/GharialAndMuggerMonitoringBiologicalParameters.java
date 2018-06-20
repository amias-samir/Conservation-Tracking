package com.naxa.conservationtracking.wildlife_monitoring_techniques;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtracking.GeoPointActivity;
import com.naxa.conservationtracking.activities.CalculateAreaUsinGPS;
import com.naxa.conservationtracking.activities.GpsTracker;
import com.naxa.conservationtracking.activities.MapPolyLineActivity;
import com.naxa.conservationtracking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
 * Created by susan on 1/21/2016.
 */
public class GharialAndMuggerMonitoringBiologicalParameters extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;

    Spinner spinnerLandscape, spinnerHabitatType, spinnerActivity, spinnerHabitatSubStratum, spinnerSpeciesName;
    ArrayAdapter landscapeAdpt, spinHabitatTypeAdpt, spinActivityAdpt, spinHabitatSubstratumAdpt, spinSpeciesNameAdpt;

    Button send, startGps, endGps, save, previewMap;
    String jsonToSend = null;
    ProgressDialog mProgressDlg;
    Context context = this;
    GpsTracker gps;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    String latLangArray = "", jsonLatLangArray = "";
    double distance;
    double area_using_Gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    ProgressBar tracking;
    double initLat, finalLat, initLong, finalLong;
    TextView tvDistance, tvBoundryUsingGps;

    String landscape;
    String other_landscape;
    String funding_source;
    String district, vdc, projectCode,
            observers_name, date, weather, river_name, segment_name, location_name,
            habitat_type, species_name, time_start, time_end, time, male, female, sub_adult,
            juvenile, yearling, unknown, total_animals, activity, habitat_substratum,
            others, fund_tal, fund_community, fund_others;

    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvDistrict, tvVdc, tvProjectCode,
            tvObserversName, tvDate, tvWeather, tvRiverName, tvSegmentName, tvLocationName,
            tvTimeStart, tvTimeEnd, tvTime, tvMale, tvFemale, tvSubAdult, tvJuvenile,
            tvYearling, tvUnknown, tvTotalAnimal, tvOthers, tvFundTal, tvFundCommunity, tvFundOthers;

    String userNameToSend, passwordToSend;


    StringBuilder stringBuilder = new StringBuilder();
    JSONArray jsonArrayGPS = new JSONArray();

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;


    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 11;
    static final int TIME_DIALOG_ID1 = 12;
    static final int TIME_DIALOG_ID2 = 13;
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    String dataSentStatus = "", dateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmt_gmm_biological_parameters);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.wmt_gmm_biological_parameters_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_fundingSource);
        tvDistrict = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_DistrictName);
        tvVdc = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Vdc_Municipality_Name);
        tvObserversName = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_ObserverName);
        tvDate = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_Detail_date);
        tvWeather = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_weather);
        tvRiverName = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_RiverName);
        tvSegmentName = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_SegmentName);
        tvLocationName = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_LocationName);
        tvTimeStart = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_Detail_StartTime);
        tvTimeEnd = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_Detail_EndTime);
        tvTime = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_Detail_Time);
        spinnerSpeciesName = (Spinner) findViewById(R.id.wmt_gmm_biological_parameters_detail_SpeciesName);
        tvMale = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Male);
        tvFemale = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Female);
        tvSubAdult = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_subAdult);
        tvJuvenile = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Juvenile);
        tvYearling = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Yearling);
        tvUnknown = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Unknown);
        tvTotalAnimal = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_TotalAnimals);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_notes);
        spinnerHabitatType = (Spinner) findViewById(R.id.wmt_gmm_biological_parameters_detail_habitatType);
        spinnerActivity = (Spinner) findViewById(R.id.wmt_gmm_biological_parameters_detail_Activity);
        spinnerHabitatSubStratum = (Spinner) findViewById(R.id.wmt_gmm_biological_parameters_detail_HabitatSubstratum);
        tvBoundryUsingGps = (TextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_boundry_using_gps);
        setCurrentDateOnView();
        addListenerOnButton();
        setCurrentTimeOnView();
        addListenerOnTimeButton();
        tvFundTal = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_biological_parameters_detail_FundOthers);

        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        startGps = (Button) findViewById(R.id.wmt_gmm_biological_parameters_detail_GpsStart);
        endGps = (Button) findViewById(R.id.wmt_gmm_biological_parameters_detail_GpsEnd);

        send = (Button) findViewById(R.id.wmt_gmm_biological_parameters_detail_send);
        save = (Button) findViewById(R.id.wmt_gmm_biological_parameters_detail_save);
        previewMap = (Button) findViewById(R.id.wmt_gmm_biological_parameters_detail_preview_map);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        spinHabitatTypeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_GMM_BIOLOGICAL_HABITAT_TYPE);
        spinHabitatTypeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHabitatType.setAdapter(spinHabitatTypeAdpt);
        spinnerHabitatType.setOnItemSelectedListener(this);
//
        spinActivityAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_GMM_BIOLOGICAL_HABITAT_SUBSTRATUM);
        spinActivityAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(spinActivityAdpt);
        spinnerActivity.setOnItemSelectedListener(this);

        spinHabitatSubstratumAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_GMM_BIOLOGICAL_ACTIVITY);
        spinHabitatSubstratumAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHabitatSubStratum.setAdapter(spinHabitatSubstratumAdpt);
        spinnerHabitatSubStratum.setOnItemSelectedListener(this);

        spinSpeciesNameAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_GMM_BIOLOGICAL_SPECIES_NAME);
        spinSpeciesNameAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpeciesName.setAdapter(spinSpeciesNameAdpt);
        spinnerSpeciesName.setOnItemSelectedListener(this);

        initilizeUI();

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
                        district = tvDistrict.getText().toString();
                        vdc = tvVdc.getText().toString();
                        observers_name = tvObserversName.getText().toString();
                        date = tvDate.getText().toString();
                        weather = tvWeather.getText().toString();
                        river_name = tvRiverName.getText().toString();
                        segment_name = tvSegmentName.getText().toString();
                        location_name = tvLocationName.getText().toString();
                        time_start = tvTimeStart.getText().toString();
                        time_end = tvTimeEnd.getText().toString();
                        time = tvTime.getText().toString();
                        male = tvMale.getText().toString();
                        female = tvFemale.getText().toString();
                        sub_adult = tvSubAdult.getText().toString();
                        juvenile = tvJuvenile.getText().toString();
                        yearling = tvYearling.getText().toString();
                        unknown = tvUnknown.getText().toString();
                        total_animals = tvTotalAnimal.getText().toString();
                        others = tvOthers.getText().toString();
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
                        FormNameToInput.setText("Crocodile Monitoring Details");

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
                                    String[] data = new String[]{"81", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "", "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(GharialAndMuggerMonitoringBiologicalParameters.this)
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
                        Toast.makeText(getApplicationContext(), "Take at least one gps coordinate", Toast.LENGTH_SHORT).show();

                    }
                }

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
                                    district = tvDistrict.getText().toString();
                                    vdc = tvVdc.getText().toString();
                                    observers_name = tvObserversName.getText().toString();
                                    date = tvDate.getText().toString();
                                    weather = tvWeather.getText().toString();
                                    river_name = tvRiverName.getText().toString();
                                    segment_name = tvSegmentName.getText().toString();
                                    location_name = tvLocationName.getText().toString();
                                    time_start = tvTimeStart.getText().toString();
                                    time_end = tvTimeEnd.getText().toString();
                                    time = tvTime.getText().toString();
                                    male = tvMale.getText().toString();
                                    female = tvFemale.getText().toString();
                                    sub_adult = tvSubAdult.getText().toString();
                                    juvenile = tvJuvenile.getText().toString();
                                    yearling = tvYearling.getText().toString();
                                    unknown = tvUnknown.getText().toString();
                                    total_animals = tvTotalAnimal.getText().toString();
                                    others = tvOthers.getText().toString();
                                    fund_tal = tvFundTal.getText().toString();
                                    fund_community = tvFundCommunity.getText().toString();
                                    fund_others = tvFundOthers.getText().toString();

                                    jsonLatLangArray = jsonArrayGPS.toString();

                                    userNameToSend = userN;
                                    passwordToSend = passW;

                                    mProgressDlg = new ProgressDialog(context);
                                    mProgressDlg.setMessage("Please wait....");
                                    mProgressDlg.setIndeterminate(false);
                                    mProgressDlg.setCancelable(false);
                                    mProgressDlg.show();

                                    convertDataToJson();
                                    sendDatToserver();
                                }
//                            }
//                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Take at least one gps coordinate", Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(GharialAndMuggerMonitoringBiologicalParameters.this, MapPolyLineActivity.class));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GEOPOINT_RESULT_CODE) {

            switch (resultCode) {
                case RESULT_OK:

                    tracking.setVisibility(View.VISIBLE);
                    isGpsTracking = true;
                    listCf.clear();
                    gpslocation.clear();
                    gps = new GpsTracker(GharialAndMuggerMonitoringBiologicalParameters.this);
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
        if (spinnerId == R.id.wmt_gmm_biological_parameters_detail_landscape) {
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

        if (spinnerId == R.id.wmt_gmm_biological_parameters_detail_habitatType) {
            switch (position) {
                case 0:
                    habitat_type = "Sal com.naxa.conservationtracking.forest";
                    break;
                case 1:
                    habitat_type = "Mixed Forest";
                    break;
                case 2:
                    habitat_type = "Riverine Forest";
                    break;
                case 3:
                    habitat_type = "Tall Grassland";
                    break;
                case 4:
                    habitat_type = "Short Grassland";
                    break;
                case 5:
                    habitat_type = "Wetland";
                    break;
                case 6:
                    habitat_type = "Streambed";
                    break;

            }
        }
        if (spinnerId == R.id.wmt_gmm_biological_parameters_detail_Activity) {
            switch (position) {
                case 0:
                    activity = "Sandy bank";
                    break;
                case 1:
                    activity = "Grassy bank";
                    break;
                case 2:
                    activity = "Muddy soil";
                    break;
                case 3:
                    activity = "Gravel";
                    break;
                case 4:
                    activity = "Pebbles";
                    break;
                case 5:
                    activity = "Rocky area";
                    break;
            }
        }
        if (spinnerId == R.id.wmt_gmm_biological_parameters_detail_HabitatSubstratum) {
            switch (position) {
                case 0:
                    habitat_substratum = "Basking";
                    break;
                case 1:
                    habitat_substratum = "Fishing";
                    break;
                case 2:
                    habitat_substratum = "Swimming";
                    break;
            }
        }

        if (spinnerId == R.id.wmt_gmm_biological_parameters_detail_SpeciesName) {
            switch (position) {
                case 0:
                    species_name = "Gharial";
                    break;
                case 1:
                    species_name = "Mugger";
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        listCf.clear();
        gpslocation.clear();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.close_dialog);
        final Button yes = (Button) showDialog.findViewById(R.id.buttonYes);
        final Button no = (Button) showDialog.findViewById(R.id.buttonNo);

        AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
        showDialog.setTitle("Exit Form");
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

    //same for all
    public void initilizeUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            CheckValues.isFromSavedFrom = true;
            listCf.clear();
            isGpsTaken = true;
            startGps.setEnabled(false);
            endGps.setEnabled(false);
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
//same for all

    private void UpdateData() {
        mProgressDlg = new ProgressDialog(context);
        mProgressDlg.setMessage("Acquiring GPS location\nPlease wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

        gps = new GpsTracker(GharialAndMuggerMonitoringBiologicalParameters.this);
        if (gps.canGetLocation()) {

            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gps = new GpsTracker(GharialAndMuggerMonitoringBiologicalParameters.this);
                                if (gps.canGetLocation()) {
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();

                                    Log.e("latlang", " lat: " + finalLat + " long: "
                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gps = new GpsTracker(GharialAndMuggerMonitoringBiologicalParameters.this);
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

        try {
            post_dict.put("tablename", "tbl_gharial_biological_parameters");

            JSONObject header = new JSONObject();

            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("district", district);
            header.put("vdc", vdc);
            header.put("observers_name", observers_name);
            header.put("date", date);
            header.put("weather", weather);
            header.put("river_name", river_name);
            header.put("segment_name", segment_name);
            header.put("location_name", location_name);
            header.put("habitat_type", habitat_type);
            header.put("time_start", time_start);
            header.put("time_end", time_end);
            header.put("area_gps", area_using_Gps);
            header.put("time", time);
            header.put("species_name", species_name);
            header.put("male", male);
            header.put("female", female);
            header.put("sub_adult", sub_adult);
            header.put("juvenile", juvenile);
            header.put("yearling", yearling);
            header.put("unknown", unknown);
            header.put("total_animals", total_animals);
            header.put("activity", activity);
            header.put("habitat_substratum", habitat_substratum);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            header.put("others", others);
            header.put("boundary", latLangArray);
            header.put("longitude", finalLong);
            header.put("latitude", finalLat);
            post_dict.put("formdata", header);


            jsonToSend = post_dict.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //same for all
    public void sendDatToserver() {
        if (jsonToSend.length() > 0) {
            Log.e("Inside Send", "BeforeSending " + jsonToSend);

            RestApii restApii = new RestApii();
            restApii.execute();
        }
    }

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


    public void parseJson(String jsonToParse) throws JSONException {
        JSONObject jsonOb = new JSONObject(jsonToParse);
        Log.e("CFDETAIL", "json : " + jsonOb.toString());
        String data = jsonOb.getString("formdata");
        Log.e("CFDETAIL", "formdata : " + jsonOb.toString());
        JSONObject jsonObj = new JSONObject(data);
        Log.e("CFDETAIL Json", "json : " + jsonObj.toString());


        projectCode = jsonObj.getString("project_code");
        landscape = jsonObj.getString("landscape");
        funding_source = jsonObj.getString("funding_source");
        district = jsonObj.getString("district");
        vdc = jsonObj.getString("vdc");
        observers_name = jsonObj.getString("observers_name");
        date = jsonObj.getString("date");
        weather = jsonObj.getString("weather");
        river_name = jsonObj.getString("river_name");
        segment_name = jsonObj.getString("segment_name");
        location_name = jsonObj.getString("location_name");
        habitat_type = jsonObj.getString("habitat_type");
        time_start = jsonObj.getString("time_start");
        time_end = jsonObj.getString("time_end");
        time = jsonObj.getString("time");
        species_name = jsonObj.getString("species_name");
        male = jsonObj.getString("male");
        female = jsonObj.getString("female");
        sub_adult = jsonObj.getString("sub_adult");
        juvenile = jsonObj.getString("juvenile");
        yearling = jsonObj.getString("yearling");
        unknown = jsonObj.getString("unknown");
        total_animals = jsonObj.getString("total_animals");
        activity = jsonObj.getString("activity");
        habitat_substratum = jsonObj.getString("habitat_substratum");
        others = jsonObj.getString("others");
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        latLangArray = jsonObj.getString("boundary");
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        finalLat = Double.parseDouble((jsonObj.getString("latitude")));
        area_using_Gps = Double.parseDouble((jsonObj.getString("area_gps")));

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvDistrict.setText(district);
        tvVdc.setText(vdc);
        tvObserversName.setText(observers_name);
        tvDate.setText(date);
        tvWeather.setText(weather);
        tvRiverName.setText(river_name);
        tvSegmentName.setText(segment_name);
        tvLocationName.setText(location_name);
        tvTimeStart.setText(time_start);
        tvTimeEnd.setText(time_end);
        tvTime.setText(time);
        tvMale.setText(male);
        tvFemale.setText(female);
        tvSubAdult.setText(sub_adult);
        tvJuvenile.setText(juvenile);
        tvYearling.setText(yearling);
        tvUnknown.setText(unknown);
        tvTotalAnimal.setText(total_animals);
        tvOthers.setText(others);
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

        int forConPos = spinHabitatTypeAdpt.getPosition(habitat_type);
        spinnerHabitatType.setSelection(forConPos);

        int forTypePos = spinActivityAdpt.getPosition(activity);
        spinnerActivity.setSelection(forTypePos);

        int speciesName = spinSpeciesNameAdpt.getPosition(species_name);
        spinnerSpeciesName.setSelection(speciesName);

        int graPressurePos = spinHabitatSubstratumAdpt.getPosition(habitat_substratum);
        spinnerHabitatSubStratum.setSelection(graPressurePos);

        tvBoundryUsingGps.setText("Area Using GPS : " + area_using_Gps + " (Hectares)");

    }

    //same for all
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
                    new PromptDialog(GharialAndMuggerMonitoringBiologicalParameters.this)
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
                    String[] data = new String[]{"81", "Crocodile Monitoring Details", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(GharialAndMuggerMonitoringBiologicalParameters.this)
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
        tvDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(year).append("/").append(month + 1).append("/")
                .append(day).append(""));
    }


    public void addListenerOnButton() {


        tvDate.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvDate.setShowSoftInputOnFocus(false);
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

            case TIME_DIALOG_ID2:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener2, hour, minute,
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
            tvDate.setText(new StringBuilder().append(year)
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

        // set current time into textview
        tvTime.setText(new StringBuilder().append(pad(hour)).append(":")
                .append(pad(minute)));


    }

    public void addListenerOnTimeButton() {


        tvTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }

        });

        tvTimeStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID1);

            }

        });
        tvTimeEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID2);

            }

        });

    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
            tvTime.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));


        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;


            tvTimeStart.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));


        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;


            tvTimeEnd.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));

        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}