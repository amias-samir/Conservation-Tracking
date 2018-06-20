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
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.naxa.conservationtracking.activities.GPS_TRACKER_FOR_POINT;
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
public class SpeciesOccupancySurvey extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;

    Spinner spinnerLandscape, spinnerForestNala, spinnerAgeOfSign, spinnerCarnivore, spinnerHerbivore, spinnerSign, spinnerForest, spinnerTerrain;
    ArrayAdapter landscapeAdpt, spinForestNalaAdpt, spinAgeOfSignAdpt, spinCarnivoreAdpt, spinHerbivoreAdpt, spinSignAdpt, spinForestAdpt, spinTerrainAdpt;

    Button send, startGps, endGps, save, previewMap, addSpeciesBtn, dateBtn, timeBtn;

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
    ArrayList<LatLng> listCf1 = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    List<Location> gpslocation1 = new ArrayList<>();
    String latLangArray = "", jsonLatLangArray = "";
    double distance;
    double area_using_Gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    boolean isGpsTaken1 = false;
    ProgressBar tracking;
    double initLat, finalLat, finalLat1, initLong, finalLong, finalLong1;
    StringBuilder stringBuilder = new StringBuilder();
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvDistance, tvBoundryUsingGps, tvNoOfSpeciesAdded,  tvDate, tvTime;
    String landscape;
    String other_landscape;
    String funding_source;
    String district, vdc, projectCode, other_courses,
            observers_name, location, date, time, grid_no, spatial_replicate_no, streamcourses_nala_forest_trail;
    public String all_species;
    public String segment;
    public String species_name;
    //    public String carnivore_species;
//    public String herbivore_species;
    public String sign_type;
    public String number_of_sign;
    public String age_of_sign;
    public String forest_type;
    public String other_forest_type;
    public String terrain_type;
    public String other_terrain_type;
    public String others;
    public String addspeciesothers;
    public String no_of_species_added = "";
    public String fund_tal, fund_community, fund_others;

    AutoCompleteTextView tvOtherCoursesName, tvOtherLandscape, tvFundingSource, tvDistrict, tvVdc, tvProjectCode,
            tvObserversName, tvLocation, tvGridNo, tvSpatialReplicateNo, tvSegmentName, tvNumberOfsign, tvSpeciesName, tvOthers, tvAddSOthers,
            tvOtherSign, tvOtherForest, tvOtherTerrain, tvFundTal, tvFundCommunity, tvFundOthers;

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;


    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 9999;
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    String dataSentStatus = "", dateString;

    List<String> speciesSegmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmt_gmm_species_occupancy_survey);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.wmt_gmm_species_occupancy_survey_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.wmt_gmm_species_occupancy_survey_detail_fundingSource);
        tvDistrict = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_DistrictName);
        tvVdc = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_Vdc_Municipality_Name);
        tvObserversName = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_ObserversName);
        tvLocation = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_LocationName);

        tvDate = (TextView) findViewById(R.id.species_survey_Detail_Date);
        dateBtn = (Button) findViewById(R.id.date_btn);
        tvTime = (TextView) findViewById(R.id.species_survey_Detail_Time);
        timeBtn = (Button) findViewById(R.id.time_btn);
        setCurrentDateOnView();
        addListenerOnButton();

        tvGridNo = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_GridNo);
        spinnerForestNala = (Spinner) findViewById(R.id.species_survey_detail_Stream);
        tvOtherCoursesName = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_other_courses);
        tvSpatialReplicateNo = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_Spatial_Replicate_no);


        tvNoOfSpeciesAdded = (TextView) findViewById(R.id.species_survey_detail_species_added_no);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_notes);

        tvFundTal = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_FundOthers);
        
//        tvSegmentName = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_Segment);
//        tvNumberOfsign = (AutoCompleteTextView) findViewById(R.id.species_survey_detail_no_of_sign_type);
//
//        spinnerCarnivore = (Spinner) findViewById(R.id.species_survey_detail_Carinivore);
//        spinnerHerbivore = (Spinner) findViewById(R.id.species_survey_detail_Herbivore);
//        spinnerSign = (Spinner) findViewById(R.id.species_survey_detail_SignType);
//        spinnerForest = (Spinner) findViewById(R.id.species_survey_detail_ForestType);
//        spinnerTerrain = (Spinner) findViewById(R.id.species_survey_detail_TerrainType);

        tvBoundryUsingGps = (TextView) findViewById(R.id.species_survey_detail_boundry_using_gps);

        addListenerOnButton();
        setCurrentTimeOnView();
        addListenerOnTimeButton();

        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        startGps = (Button) findViewById(R.id.species_survey_detail_GpsStart);
        endGps = (Button) findViewById(R.id.species_survey_detail_GpsEnd);

        addSpeciesBtn = (Button) findViewById(R.id.species_survey_detail_add_species_btn);

        send = (Button) findViewById(R.id.species_survey_detail_send);
        save = (Button) findViewById(R.id.species_survey_detail_save);
        previewMap = (Button) findViewById(R.id.species_survey_detail_preview_map);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        spinForestNalaAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_FOREST_NALA);
        spinForestNalaAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForestNala.setAdapter(spinForestNalaAdpt);
        spinnerForestNala.setOnItemSelectedListener(this);

//        spinCarnivoreAdpt = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_CARNIVORE);
//        spinCarnivoreAdpt
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCarnivore.setAdapter(spinCarnivoreAdpt);
//        spinnerCarnivore.setOnItemSelectedListener(this);
//
//        spinHerbivoreAdpt = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_HERBIVORE);
//        spinHerbivoreAdpt
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerHerbivore.setAdapter(spinHerbivoreAdpt);
//        spinnerHerbivore.setOnItemSelectedListener(this);
////
//        spinSignAdpt = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_SIGN);
//        spinSignAdpt
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSign.setAdapter(spinSignAdpt);
//        spinnerSign.setOnItemSelectedListener(this);
//
//        spinForestAdpt = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_FOREST);
//        spinForestAdpt
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerForest.setAdapter(spinForestAdpt);
//        spinnerForest.setOnItemSelectedListener(this);
//
//        spinTerrainAdpt = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_TERRIAN);
//        spinTerrainAdpt
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTerrain.setAdapter(spinTerrainAdpt);
//        spinnerTerrain.setOnItemSelectedListener(this);

        initilizeUI();

        addSpeciesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSpeciesSeenDialog();
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
                        district = tvDistrict.getText().toString();
                        vdc = tvVdc.getText().toString();
                        observers_name = tvObserversName.getText().toString();
                        location = tvLocation.getText().toString();
                        date = tvDate.getText().toString();
                        time = tvTime.getText().toString();
                        other_courses = tvOtherCoursesName.getText().toString();
                        grid_no = tvGridNo.getText().toString();
                        spatial_replicate_no = tvSpatialReplicateNo.getText().toString();
                        no_of_species_added = tvNoOfSpeciesAdded.getText().toString();
//                        segment = tvSegmentName.getText().toString();
//                        number_of_sign = tvNumberOfsign.getText().toString();
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
                        FormNameToInput.setText("Species Occupancy Survey");

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
                                    String[] data = new String[]{"83", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "", "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(SpeciesOccupancySurvey.this)
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
                                    Toast.makeText(context, "Either your user name or password is empty. Please fill the required field. ", Toast.LENGTH_SHORT).show();
                                } else {
//                                    showDialog.dismiss();

                                    projectCode = tvProjectCode.getText().toString();
                                    other_landscape = tvOtherLandscape.getText().toString();
                                    funding_source = tvFundingSource.getText().toString();
                                    district = tvDistrict.getText().toString();
                                    vdc = tvVdc.getText().toString();
                                    observers_name = tvObserversName.getText().toString();
                                    location = tvLocation.getText().toString();
                                    date = tvDate.getText().toString();
                                    time = tvTime.getText().toString();
                                    other_courses = tvOtherCoursesName.getText().toString();
                                    grid_no = tvGridNo.getText().toString();
                                    spatial_replicate_no = tvSpatialReplicateNo.getText().toString();
//                                    segment = tvSegmentName.getText().toString();
//                                    number_of_sign = tvNumberOfsign.getText().toString();
                                    others = tvOthers.getText().toString();
                                    no_of_species_added = tvNoOfSpeciesAdded.getText().toString();
                                    fund_tal = tvFundTal.getText().toString();
                                    fund_community = tvFundCommunity.getText().toString();
                                    fund_others = tvFundOthers.getText().toString();

                                    userNameToSend = userN;
                                    passwordToSend = passW;

                                    mProgressDlg = new ProgressDialog(context);
                                    mProgressDlg.setMessage("Please wait...");
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
                startActivity(new Intent(SpeciesOccupancySurvey.this, MapPolyLineActivity.class));
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

        if (spinnerId == R.id.wmt_gmm_species_occupancy_survey_detail_landscape) {
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

        if (spinnerId == R.id.species_survey_detail_Stream) {
            switch (position) {
                case 0:
                    streamcourses_nala_forest_trail = "Stream courses";
                    break;
                case 1:
                    streamcourses_nala_forest_trail = "Nala";
                    break;
                case 2:
                    streamcourses_nala_forest_trail = "Forest Trail";
                    break;
                case 3:
                    streamcourses_nala_forest_trail = "Others";
                    break;

            }
            if (spinnerForestNala.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherCoursesName.setVisibility(View.VISIBLE);
            } else {
                tvOtherCoursesName.setVisibility(View.GONE);
            }
        }

//        if (spinnerId == R.id.species_survey_detail_Carinivore) {
//            switch (position) {
//                case 0:
//                    carnivore_species = "बाघ";
//                    break;
//                case 1:
//                    carnivore_species = "चितुवा";
//                    break;
//                case 2:
//                    carnivore_species = "भूइ भालु";
//                    break;
//                case 3:
//                    carnivore_species = "हुँडार";
//                    break;
//                case 4:
//                    carnivore_species = "जंगली कुकुर";
//                    break;
//                case 5:
//                    carnivore_species = "बनबिरालो";
//                    break;
//                case 6:
//                    carnivore_species = "स्याल";
//                    break;
//                case 7:
//                    carnivore_species = "निरबिरालो";
//                    break;
//                case 8:
//                    carnivore_species = "न्यौरिमुसा";
//                    break;
//                case 9:
//                    carnivore_species = "अरु बिरालो प्रजाति";
//                    break;
//
//            }
//        }
//
//        if (spinnerId == R.id.species_survey_detail_Herbivore) {
//            switch (position) {
//                case 0:
//                    herbivore_species = "चित्तल";
//                    break;
//                case 1:
//                    herbivore_species = "लगुना";
//                    break;
//                case 2:
//                    herbivore_species = "बारासिंघा";
//                    break;
//                case 3:
//                    herbivore_species = "गौर";
//                    break;
//                case 4:
//                    herbivore_species = "साम्बर";
//                    break;
//                case 5:
//                    herbivore_species = "बँदेल";
//                    break;
//                case 6:
//                    herbivore_species = "रतुवा";
//                    break;
//                case 7:
//                    herbivore_species = "खरायो";
//                    break;
//                case 8:
//                    herbivore_species = "चौका";
//                    break;
//                case 9:
//                    herbivore_species = "बाँदर";
//                    break;
//                case 10:
//                    herbivore_species = "लंगुर";
//                    break;
//                case 11:
//                    herbivore_species = "दुम्सी";
//                    break;
//                case 12:
//                    herbivore_species = "हात्ती";
//                    break;
//                case 13:
//                    herbivore_species = "गाइबस्तु";
//                    break;
//                case 14:
//                    herbivore_species = "बाख्रा";
//                    break;
//                case 15:
//                    herbivore_species = "अपरिचित";
//                    break;
//
//            }
//        }



        if (spinnerId == R.id.species_survey_detail_SignType) {
            switch (position) {
                case 0:
                    sign_type = "दिसा";
                    break;
                case 1:
                    sign_type = "गोबर";
                    break;
                case 2:
                    sign_type = "जनावरको पाइला";
                    break;
                case 3:
                    sign_type = "कोतारेको निशान";
                    break;
                case 4:
                    sign_type = "मारेको/खाएको संकेत";
                    break;
                case 5:
                    sign_type = "अंग";
                    break;
                case 6:
                    sign_type = "Others";
                    break;
            }
            if (spinnerSign.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherSign.setVisibility(View.VISIBLE);
            } else {
                tvOtherSign.setVisibility(View.GONE);
            }

        }

        if (spinnerId == R.id.species_survey_detail_age_of_sign) {
            switch (position) {
                case 0:
                    age_of_sign = "Old";
                    break;
                case 1:
                    age_of_sign = "Fresh";
                    break;
            }

        }

        if (spinnerId == R.id.species_survey_detail_ForestType) {
            switch (position) {
                case 0:
                    forest_type = "साल जंगल";
                    break;
                case 1:
                    forest_type = "मिश्रित जंगल";
                    break;
                case 2:
                    forest_type = "नदितटीए बन";
                    break;
                case 3:
                    forest_type = "लामो घासेमैदान";
                    break;
                case 4:
                    forest_type = "छोटो घासेमैदान";
                    break;
                case 5:
                    forest_type = "सिमसार";
                    break;
                case 6:
                    forest_type = "Others";
                    break;
            }
            if (spinnerForest.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherForest.setVisibility(View.VISIBLE);
            } else {
                tvOtherForest.setVisibility(View.GONE);
            }
        }

        if (spinnerId == R.id.species_survey_detail_TerrainType) {
            switch (position) {
                case 0:
                    terrain_type = "पहाडी";
                    break;
                case 1:
                    terrain_type = "तराई";
                    break;
                case 2:
                    terrain_type = "खोला किनार";
                    break;
                case 3:
                    terrain_type = "Others";
                    break;
            }
            if (spinnerTerrain.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherTerrain.setVisibility(View.VISIBLE);
            } else {
                tvOtherTerrain.setVisibility(View.GONE);
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
                    gps = new GpsTracker(SpeciesOccupancySurvey.this);
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

    //same for all
    public void initilizeUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            CheckValues.isFromSavedFrom = true;
            listCf.clear();
            isGpsTaken = true;
            startGps.setEnabled(false);
            endGps.setEnabled(false);
            dateBtn.setEnabled(false);
            timeBtn.setEnabled(false);
            addSpeciesBtn.setEnabled(false);
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

        gps = new GpsTracker(SpeciesOccupancySurvey.this);
        if (gps.canGetLocation()) {

            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gps = new GpsTracker(SpeciesOccupancySurvey.this);
                                if (gps.canGetLocation()) {
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();

                                    Log.e("latlang", " lat: " + finalLat + " long: "
                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gps = new GpsTracker(SpeciesOccupancySurvey.this);
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
            post_dict.put("tablename", "tbl_species_occupancy_survey");

            JSONObject header = new JSONObject();

            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("district", district);
            header.put("vdc", vdc);
            header.put("observers_name", observers_name);
            header.put("location", location);
            header.put("date", date);
            header.put("time", time);
            header.put("grid_no", grid_no);
            header.put("spatial_replicate_no", spatial_replicate_no);
            header.put("streamcourses_nala_forest_trail", streamcourses_nala_forest_trail + ":  " + other_courses);
            header.put("all_species", all_species);
            header.put("no_of_species_added", no_of_species_added);
//            header.put("carnivore_species", carnivore_species);
//            header.put("herbivore_species", herbivore_species);
//            header.put("sign_type", sign_type + ":  " + number_of_sign);
//            header.put("forest_type", forest_type);
//            header.put("terrain_type", terrain_type);
            header.put("others", others);
            header.put("longitude", finalLong);
            header.put("latitude", finalLat);
            header.put("boundary", latLangArray);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);

            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.e("ConvertDataToJson", "convertDataToJsonSave: "+jsonToSend );

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("LATTI", "" + finalLat);
            editor.putString("LONGI", "" + finalLong);
            editor.apply();


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
        location = jsonObj.getString("location");
        date = jsonObj.getString("date");
        time = jsonObj.getString("time");
        grid_no = jsonObj.getString("grid_no");
        spatial_replicate_no = jsonObj.getString("spatial_replicate_no");
        streamcourses_nala_forest_trail = jsonObj.getString("streamcourses_nala_forest_trail");

        all_species = jsonObj.getString("all_species");
        no_of_species_added = jsonObj.getString("no_of_species_added");

        JSONArray jsonArray = new JSONArray(all_species);
        Log.e("ALL_SEGMENT_PARSE_JSON", "SAMIR_parseJson: "+jsonArray.length() );

        try {
            for (int i = 0 ; i < jsonArray.length(); i++){
                speciesSegmentList.add(jsonArray.get(i).toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
//        carnivore_species = jsonObj.getString("carnivore_species");
//        herbivore_species = jsonObj.getString("herbivore_species");
//        sign_type = jsonObj.getString("sign_type");
//        forest_type = jsonObj.getString("forest_type");
//        terrain_type = jsonObj.getString("terrain_type");
        others = jsonObj.getString("others");
        latLangArray = jsonObj.getString("boundary");
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        finalLat = Double.parseDouble((jsonObj.getString("latitude")));
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvDistrict.setText(district);
        tvVdc.setText(vdc);
        tvObserversName.setText(observers_name);
        tvLocation.setText(location);
        tvDate.setText(date);
        tvTime.setText(time);
        tvGridNo.setText(grid_no);
        tvSpatialReplicateNo.setText(spatial_replicate_no);
        tvNoOfSpeciesAdded.setText(no_of_species_added);
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

        String[] actions1 = streamcourses_nala_forest_trail.split(":  ");
        if (actions1[0].equals("Stream courses") || actions1[0].equals("Nala") ||
                actions1[0].equals("Forest Trail")) {

            int streamposition = spinForestNalaAdpt.getPosition(actions1[0]);
            spinnerForestNala.setSelection(streamposition);
            tvOtherCoursesName.setVisibility(View.GONE);

        } else {

            int streamposition = spinForestNalaAdpt.getPosition(actions1[0]);
            spinnerForestNala.setSelection(streamposition);
            tvOtherCoursesName.setText(actions1[1]);

        }

        tvBoundryUsingGps.setText("Area Using GPS : " + area_using_Gps + " (Hectares)");
    }

    private void showAddSpeciesSeenDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.add_species_dialog);
        final GPS_TRACKER_FOR_POINT gps1 = null;
        final Button startGps1, previewMap1;

        Button btnOk = (Button) showDialog.findViewById(R.id.species_survey_detail_add_btn);

        tvSegmentName = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_Segment);
        tvAddSOthers = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_notes_species);
        tvNumberOfsign = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_no_of_sign_type);
        tvSpeciesName = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_species_name);

        startGps1 = (Button) showDialog.findViewById(R.id.species_survey_detail_GpsStart1);
        previewMap1 = (Button) showDialog.findViewById(R.id.species_survey_detail_preview_map1);

        spinnerAgeOfSign = (Spinner) showDialog.findViewById(R.id.species_survey_detail_age_of_sign);
        spinnerSign = (Spinner) showDialog.findViewById(R.id.species_survey_detail_SignType);
        tvOtherSign = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_other_sign);
        spinnerForest = (Spinner) showDialog.findViewById(R.id.species_survey_detail_ForestType);
        tvOtherForest = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_other_foresttype);
        spinnerTerrain = (Spinner) showDialog.findViewById(R.id.species_survey_detail_TerrainType);
        tvOtherTerrain = (AutoCompleteTextView) showDialog.findViewById(R.id.species_survey_detail_other_terraintype);

        spinSignAdpt = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_SIGN);
        spinSignAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSign.setAdapter(spinSignAdpt);
        spinnerSign.setOnItemSelectedListener(this);

        spinAgeOfSignAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_AGE_OF_SIGN);
        spinAgeOfSignAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeOfSign.setAdapter(spinAgeOfSignAdpt);
        spinnerAgeOfSign.setOnItemSelectedListener(this);

        spinForestAdpt = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_FOREST);
        spinForestAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForest.setAdapter(spinForestAdpt);
        spinnerForest.setOnItemSelectedListener(this);

        spinTerrainAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.SPECIES_SURVEY_TERRIAN);
        spinTerrainAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerrain.setAdapter(spinTerrainAdpt);
        spinnerTerrain.setOnItemSelectedListener(this);

        showDialog.setTitle("Add New Species");
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
                startActivity(new Intent(SpeciesOccupancySurvey.this, MapPolyLineActivity.class));
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isGpsTaken1) {
                    segment = tvSegmentName.getText().toString();
                    species_name = tvSpeciesName.getText().toString();
                    number_of_sign = tvNumberOfsign.getText().toString();
                    age_of_sign = tvNumberOfsign.getText().toString();
                    other_forest_type = tvOtherForest.getText().toString();
                    other_terrain_type = tvOtherTerrain.getText().toString();
                    addspeciesothers = tvAddSOthers.getText().toString();

                    JSONObject memberObj = new JSONObject();
                    try {

                        memberObj.put("segment_no", segment);
                        memberObj.put("species_name", species_name);
                        memberObj.put("sign_type", sign_type + ":  " + number_of_sign);
                        memberObj.put("age_of_sign", age_of_sign);
                        memberObj.put("forest_type", forest_type + ":  " + other_forest_type);
                        memberObj.put("terrain_type", terrain_type + ":  " + other_terrain_type);
                        memberObj.put("latitude", finalLat);
                        memberObj.put("longitude", finalLong);
                        memberObj.put("others", addspeciesothers);

                        Log.e("all_species: ", memberObj.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    speciesSegmentList.add(memberObj.toString());
                    all_species = speciesSegmentList.toString();

                    tvNoOfSpeciesAdded.setText(speciesSegmentList.size() + " Species added");
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
                    new PromptDialog(SpeciesOccupancySurvey.this)
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
                    String[] data = new String[]{"83", "Species Occupancy Survey", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(SpeciesOccupancySurvey.this)
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

        // set current time into timepicker
//        timePicker1.setCurrentHour(hour);
//        timePicker1.setCurrentMinute(minute);

    }

    public void addListenerOnTimeButton() {


        timeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

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