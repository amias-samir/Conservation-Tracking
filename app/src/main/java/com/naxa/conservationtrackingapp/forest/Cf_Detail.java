package com.naxa.conservationtrackingapp.forest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtrackingapp.GeoPointActivity;
import com.naxa.conservationtrackingapp.activities.CalculateAreaUsinGPS;
import com.naxa.conservationtrackingapp.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtrackingapp.activities.GpsTracker;
import com.naxa.conservationtrackingapp.activities.MapPolyLineActivity;
import com.naxa.conservationtrackingapp.R;

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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import com.naxa.conservationtrackingapp.climate_change.BiogasDetail;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;
import com.naxa.conservationtrackingapp.wildlife_monitoring_techniques.HumanDisturbance;

import Utls.SharedPreferenceUtils;
import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/21/2016.
 */
public class Cf_Detail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Cf_Detail";
    Toolbar toolbar;
    boolean isSpinner0 = false;
    Spinner spinnerLandscape, forestCondition, naturalRegeneration, grazingPressure, forestType;
    ArrayAdapter landscapeAdpt, forestConditionAdapter, natural_regeneration_Adapter, grazing_pressure_Adapter, forestTypeAdapter;

    String other_landscape, landscape, funding_source, no_of_beneficiaries, location_name, forest_condition, forest_type, grazing_pressure, natural_regeneration,
            name_of_cf, list_OfWildLife, remarks, district, projectCode, vdc, dalits, janajatis, other_social_group,
            hwc_affected, nrm_dependant, other_target_groups, fund_tal, fund_community, fund_others;

    Button send, startGps, endGps, save, previewMap;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvNoOfBeneficiaries, tvLocationName, nameOfCf, listOfSpecies, remarks_Others, tvDistrict,
            tvVdc, tvProjectCode, tvDalits, tvJanajatis, tvOtherSocialGroup, tvHWC_Affected, tvNRM_Dependant, tvOtherTargetGroup,
            tvFundTal, tvFundCommunity, tvFundOthers;

    String jsonToSend = null;
    ProgressDialog mProgressDlg;
    Context context = this;
    GpsTracker gps;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;
    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    Location initial;
    String latLangArray = "", jsonLatLangArray = "";

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    double distance;
    double area_using_Gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    boolean isPaused = true;
    ProgressBar tracking;
    MapPolyLineActivity mapPolyLineActivity;

    double initLat, finalLat, initLong, finalLong;
    StringBuilder stringBuilder = new StringBuilder();
    JSONArray jsonArrayGPS = new JSONArray();

    TextView tvDistance, tvBoundryUsingGps;

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    String dataSentStatus = "", dateString;
    String dateDataCollected;
    String formName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forest_cf_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setTitle("Cf Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.cf_detail_landscape);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.cf_detail_fundingSource);
        tvNoOfBeneficiaries = (AutoCompleteTextView) findViewById(R.id.cf_detail_No_of_beneficiaries);
        forestCondition = (Spinner) findViewById(R.id.cf_detail_forest_condition);
        naturalRegeneration = (Spinner) findViewById(R.id.cf_detail_natural_regeneration);
        grazingPressure = (Spinner) findViewById(R.id.cf_detail_grazing_pressure);
        forestType = (Spinner) findViewById(R.id.cf_detail_forest_type);
        tvLocationName = (AutoCompleteTextView) findViewById(R.id.cf_detail_location_name);
        nameOfCf = (AutoCompleteTextView) findViewById(R.id.cf_detail_Name_Of_CF);
        tvBoundryUsingGps = (TextView) findViewById(R.id.cf_detail_boundry_using_gps);
        listOfSpecies = (AutoCompleteTextView) findViewById(R.id.cf_detail_list_Of_species_seen);
        remarks_Others = (AutoCompleteTextView) findViewById(R.id.cf_detail_notes);
        tvDistrict = (AutoCompleteTextView) findViewById(R.id.cf_detail_DistrictName);
        tvVdc = (AutoCompleteTextView) findViewById(R.id.cf_detail_Vdc_Municipality_Name);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);

        tvDalits = (AutoCompleteTextView) findViewById(R.id.cf_detail_dalits);
        tvJanajatis = (AutoCompleteTextView) findViewById(R.id.cf_detail_Janajatis);
        tvOtherSocialGroup = (AutoCompleteTextView) findViewById(R.id.cf_detail_other_social_group);
        tvNRM_Dependant = (AutoCompleteTextView) findViewById(R.id.cf_detail_HWC_Affected);
        tvHWC_Affected = (AutoCompleteTextView) findViewById(R.id.cf_detail_NRM_Dependant);
        tvOtherTargetGroup = (AutoCompleteTextView) findViewById(R.id.cf_detail_other_target_group);

        tvFundTal = (AutoCompleteTextView) findViewById(R.id.cf_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.cf_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.cf_detail_FundOthers);

        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        startGps = (Button) findViewById(R.id.cf_detail_GpsStart);
        endGps = (Button) findViewById(R.id.cf_detail_GpsEnd);

        send = (Button) findViewById(R.id.cf_detail_send);
        save = (Button) findViewById(R.id.cf_detail_save);
        previewMap = (Button) findViewById(R.id.cf_detail_preview_map);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        forestConditionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.FOREST_CONDITION);
        forestConditionAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forestCondition.setAdapter(forestConditionAdapter);
        forestCondition.setOnItemSelectedListener(this);
//
        natural_regeneration_Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.NATURAL_REGENERATION);
        natural_regeneration_Adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        naturalRegeneration.setAdapter(natural_regeneration_Adapter);
        naturalRegeneration.setOnItemSelectedListener(this);

        forestTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.FOREST_TYPE);
        forestTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forestType.setAdapter(forestTypeAdapter);
        forestType.setOnItemSelectedListener(this);

        grazing_pressure_Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.GRAZING_PRESSURE);
        grazing_pressure_Adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grazingPressure.setAdapter(grazing_pressure_Adapter);
        grazingPressure.setOnItemSelectedListener(this);

        initilizeUI();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGpsTracking) {
                    Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
                } else {

                    if (isGpsTaken) {
                        projectCode = tvProjectCode.getText().toString();
                        funding_source = tvFundingSource.getText().toString();
                        no_of_beneficiaries = tvNoOfBeneficiaries.getText().toString();
                        dalits = tvDalits.getText().toString();
                        janajatis = tvJanajatis.getText().toString();
                        other_social_group = tvOtherSocialGroup.getText().toString();
                        hwc_affected = tvHWC_Affected.getText().toString();
                        nrm_dependant = tvNRM_Dependant.getText().toString();
                        other_target_groups = tvOtherTargetGroup.getText().toString();
                        other_landscape = tvOtherLandscape.getText().toString();
                        location_name = tvLocationName.getText().toString();
                        name_of_cf = nameOfCf.getText().toString();
                        list_OfWildLife = listOfSpecies.getText().toString();
                        remarks = remarks_Others.getText().toString();
                        district = tvDistrict.getText().toString();
                        vdc = tvVdc.getText().toString();
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
                        FormNameToInput.setText("Cf Details");

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
                                    String[] data = new String[]{"1", formName, dateDataCollected, jsonToSend, jsonLatLangArray, "", "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(Cf_Detail.this)
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
//                                String userN = userName.getText().toString();
//                                String passW = password.getText().toString();
                        String userN = UserNameAndPasswordUtils.getUserNameAndPassword(context).get(0);
                        String passW = UserNameAndPasswordUtils.getUserNameAndPassword(context).get(1);

                                if (userN == null || userN.equals("") || passW == null || passW.equals("")) {
                                    Toast.makeText(context, "Either your user name or password is empty. \nPlease fill the required field. ", Toast.LENGTH_SHORT).show();
                                } else {
//                                    showDialog.dismiss();
                                    projectCode = tvProjectCode.getText().toString();
                                    other_landscape = tvOtherLandscape.getText().toString();
                                    funding_source = tvFundingSource.getText().toString();
                                    no_of_beneficiaries = tvNoOfBeneficiaries.getText().toString();
                                    dalits = tvDalits.getText().toString();
                                    janajatis = tvJanajatis.getText().toString();
                                    other_social_group = tvOtherSocialGroup.getText().toString();
                                    hwc_affected = tvHWC_Affected.getText().toString();
                                    nrm_dependant = tvNRM_Dependant.getText().toString();
                                    other_target_groups = tvOtherTargetGroup.getText().toString();
                                    location_name = tvLocationName.getText().toString();
                                    name_of_cf = nameOfCf.getText().toString();
                                    list_OfWildLife = listOfSpecies.getText().toString();
                                    remarks = remarks_Others.getText().toString();
                                    district = tvDistrict.getText().toString();
                                    vdc = tvVdc.getText().toString();
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

        //------------------------------------------------------ edited by susan -------------------------------------
        startGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws NullPointerException {


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

                isPaused = true;
                MapPolyLineActivity.isPausedInPreview = true;

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
                startActivity(new Intent(Cf_Detail.this, MapPolyLineActivity.class));


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
                    isPaused = false;
                    MapPolyLineActivity.isPausedInPreview = false;
//                    listCf.clear();
//                    gpslocation.clear();
                    gps = new GpsTracker(Cf_Detail.this);
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

        if (spinnerId == R.id.cf_detail_landscape) {
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


        if (spinnerId == R.id.cf_detail_forest_condition) {
            switch (position) {
                case 0:
                    forest_condition = "Poor";
                    break;
                case 1:
                    forest_condition = "Degraded";
                    break;
                case 2:
                    forest_condition = "Average";
                    break;
                case 3:
                    forest_condition = "Good";
                    break;
            }
        }
        if (spinnerId == R.id.cf_detail_natural_regeneration) {
            switch (position) {
                case 0:
                    natural_regeneration = "High";
                    break;
                case 1:
                    natural_regeneration = "Medium";
                    break;
                case 2:
                    natural_regeneration = "Low";
                    break;
            }
        }
        if (spinnerId == R.id.cf_detail_grazing_pressure) {
            switch (position) {
                case 0:
                    grazing_pressure = "High";
                    break;
                case 1:
                    grazing_pressure = "Medium";
                    break;
                case 2:
                    grazing_pressure = "Low";
                    break;
            }
        }
        if (spinnerId == R.id.cf_detail_forest_type) {
            switch (position) {
                case 0:
                    forest_type = "Natural";
                    break;
                case 1:
                    forest_type = "Plantation";
                    break;
                case 2:
                    forest_type = "Mixed";
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
        isPaused = true;
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
            startGps.setEnabled(true);
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

    //------------------------------------------------------------------edited by susan------------------------
    //same for all
    private void UpdateData() {
        mProgressDlg = new ProgressDialog(context);
        mProgressDlg.setMessage("Acquiring GPS location\nPlease wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

        gps = new GpsTracker(Cf_Detail.this);
        if (gps.canGetLocation()) {

            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gps = new GpsTracker(Cf_Detail.this);
                                if (gps.canGetLocation()) {
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();
//                                    Log.e("latlang", " lat: " + finalLat + " long: "
//                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gps = new GpsTracker(Cf_Detail.this);
                                if (gps.canGetLocation()) {
                                    if (!isPaused) {
                                        gpslocation.add(gps.getLocation());
                                    }
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();
                                    if (finalLat != 0) {

                                        try {
                                            JSONObject data = new JSONObject();
                                            data.put("latitude", finalLat);
                                            data.put("longitude", finalLong);
                                            if (!isPaused) {
                                                jsonArrayGPS.put(data);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        LatLng d = new LatLng(finalLat, finalLong);
                                        isGpsTaken = true;
                                        if (!isPaused) {
                                            listCf.add(d);
                                            Log.e("latlang", " lat: " + finalLat + " long: "
                                                    + finalLong);
                                            stringBuilder.append("[" + finalLat + "," + finalLong + "]" + ",");
                                        }
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
//----------------------------------------------------------------------------------------------------------

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button

        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_cf_details");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("no_of_beneficiaries", no_of_beneficiaries);
            header.put("location_name", location_name);
            header.put("name_cf", name_of_cf);
            header.put("district", district);
            header.put("vdc", vdc);
            header.put("dalits", dalits);
            header.put("janajatis", janajatis);
            header.put("other_social_group", other_social_group);
            header.put("hwc_affected", hwc_affected);
            header.put("nrm_dependant", nrm_dependant);
            header.put("other_target_groups", other_target_groups);
            header.put("longitude", finalLong);
            header.put("latitude", finalLat);
            header.put("boundary", latLangArray);
            header.put("area_gps", area_using_Gps);
            header.put("forest_condition", forest_condition);
            header.put("natural_regeneration", natural_regeneration);
            header.put("grazing_pressure", grazing_pressure);
            header.put("forest_type", forest_type);
            header.put("wild_species_list", list_OfWildLife);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            header.put("others", remarks);
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
        no_of_beneficiaries = jsonObj.getString("no_of_beneficiaries");
        dalits = jsonObj.getString("dalits");
        janajatis = jsonObj.getString("janajatis");
        other_social_group = jsonObj.getString("other_social_group");
        hwc_affected = jsonObj.getString("hwc_affected");
        nrm_dependant = jsonObj.getString("nrm_dependant");
        other_target_groups = jsonObj.getString("other_target_groups");
        location_name = jsonObj.getString("location_name");
        name_of_cf = jsonObj.getString("name_cf");
        district = jsonObj.getString("district");
        vdc = jsonObj.getString("vdc");
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        finalLat = Double.parseDouble((jsonObj.getString("latitude")));
        latLangArray = jsonObj.getString("boundary");
        area_using_Gps = Double.parseDouble(jsonObj.getString("area_gps"));
        forest_condition = jsonObj.getString("forest_condition");
        natural_regeneration = jsonObj.getString("natural_regeneration");
        grazing_pressure = jsonObj.getString("grazing_pressure");
        list_OfWildLife = jsonObj.getString("wild_species_list");
        forest_type = jsonObj.getString("forest_type");
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        remarks = jsonObj.getString("others");

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvNoOfBeneficiaries.setText(no_of_beneficiaries);
        tvDalits.setText(dalits);
        tvJanajatis.setText(janajatis);
        tvOtherSocialGroup.setText(other_social_group);
        tvHWC_Affected.setText(hwc_affected);
        tvNRM_Dependant.setText(nrm_dependant);
        tvOtherTargetGroup.setText(other_target_groups);
        tvLocationName.setText(location_name);
        nameOfCf.setText(name_of_cf);
        tvDistrict.setText(district);
        tvVdc.setText(vdc);
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

        int forConPos = forestConditionAdapter.getPosition(forest_condition);
        forestCondition.setSelection(forConPos);

        int forTypePos = forestTypeAdapter.getPosition(forest_type);
        forestType.setSelection(forTypePos);

        int graPressurePos = grazing_pressure_Adapter.getPosition(grazing_pressure);
        grazingPressure.setSelection(graPressurePos);

        int naturalRegerationPos = natural_regeneration_Adapter.getPosition(natural_regeneration);
        naturalRegeneration.setSelection(naturalRegerationPos);

        listOfSpecies.setText(list_OfWildLife);
        tvBoundryUsingGps.setText("Area Using Gps : " + area_using_Gps + " (Hectares)");
        remarks_Others.setText(remarks);
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
                    new PromptDialog(Cf_Detail.this)
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
                    String[] data = new String[]{"1", "Cf Details", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(Cf_Detail.this)
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

}