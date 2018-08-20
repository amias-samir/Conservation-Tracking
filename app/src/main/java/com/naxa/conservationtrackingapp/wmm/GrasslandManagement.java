package com.naxa.conservationtrackingapp.wmm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtrackingapp.GeoPointActivity;
import com.naxa.conservationtrackingapp.GeoPointPolyLineActivity;
import com.naxa.conservationtrackingapp.PhoneUtils;
import com.naxa.conservationtrackingapp.R;
import com.naxa.conservationtrackingapp.activities.CalculateAreaUsinGPS;
import com.naxa.conservationtrackingapp.activities.GpsTracker;
import com.naxa.conservationtrackingapp.activities.MapPolyLineActivity;
import com.naxa.conservationtrackingapp.application.ApplicationClass;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.forest.ForestProctection;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

import Utls.GPS_Point_For_MapPolyline_Interface;
import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class GrasslandManagement extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GPS_Point_For_MapPolyline_Interface {
    Toolbar toolbar;
    //change for more photos
    int CAMERA_PIC_REQUEST = 2;
    int CAMERA_PIC_REQUEST_B = 3;
    int CAMERA_PIC_REQUEST_C = 4;

    Spinner spinnerLandscape, spinnerWildlifeUse, spinnerGrasslandStatus, forestActionTakenEviction;
    ArrayAdapter landscapeAdpt, spinAddtionInterAdpt, spinWildlifeuseAdpt, spinGrasslandStatAdpt;
    Button send, save, startGps, endGps, previewMap, dateBtn, monitoringBtn, btnManagementPractices, btnAdditionalIntervention;
    ProgressDialog mProgressDlg;
    Context context = this;
    GpsTracker gps;
    String jsonToSend, photoTosend;
    String imagePath, imagePathMonitoring, encodedImage = null, encodedImageCompleted = null, encodedImageMonitoring = null, imageName = "no_photo", imageNameCompleted = "no_photo", imageNameMonitoring = "no_photo";
    ImageButton photo, photoCompleted, photoMonitoring;
    ImageView previewImageSite, previewImageSiteCompleted, previewImageSiteMonitoring;

    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    ProgressBar tracking;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";
    Double finalLatFromGeopoint = 0.0;
    Double finalLongFromGeopoint = 0.0;


    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;

    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";
    double distance;
    double area_using_Gps;

    String projectCode;
    String landscape;
    String other_landscape;
    String funding_source;
    String agreement_no;
    String grantee_name;
    String fiscal_year;
    String name_park_bz_cf;
    String name_of_grassland;
    String district;
    String vdc;
    String date;
    String management_practice = "";
    String other_practices;
    String additional_interventions = "";
    String other_interventions;
    String wildlife_use;
    String grassland_status;
    String monitoring_date;
    String others;
    String fund_tal, fund_community, fund_others, species_name;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvDistance, tvBoundryUsingGps, tvDate, tvMonitoringDate, tvManagementPractice, tvAdditionalInterventions;
    AutoCompleteTextView tvOtherPractice, tvOtherIntervention, tvOtherLandscape, tvFundingSource, tvProjectCode, tvAgreement_no, tvGrantew_name, tvFiscal_year, tvNameOfz,
            tvDistrictname, tvNameOfVdc, tvOthers, tvNameofGrassland, tvFundTal, tvFundCommunity, tvFundOthers, tvSpeciesName;

    CheckBox cbGrassesCut, cbTreesUprooted, cbOldGrassBurnt, cbNewGrassSeeding, cbOthers,
            cbSolarcharger, cbWaterSprinkler, cbOther1;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 11;
    static final int DATE_DIALOG_ID1 = 12;
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    String userNameToSend, passwordToSend;
    String dataSentStatus = "", dateString;

    GeoPointPolyLineActivity geoPointPolyLineActivity;

//    GPS_Point_For_MapPolyline_Interface listner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmm_grassland_management);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        geoPointPolyLineActivity = new GeoPointPolyLineActivity();

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.GrasslandProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.wmm_grassland_management_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_fundingSource);
        tvAgreement_no = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_Agreement_No);
        tvGrantew_name = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_Grantee_Name);
        tvFiscal_year = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_Detail_fiscal_Year);
        tvNameOfz = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_nameof_bzForestCorridor);
        tvNameofGrassland = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_nameofgrassland);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_Vdc_Municipality_Name);
        tvManagementPractice = (TextView) findViewById(R.id.wmm_grassland_management_detail_managementPractices);
        tvAdditionalInterventions = (TextView) findViewById(R.id.wmm_grassland_management_detail_additional_interventions);
        spinnerWildlifeUse = (Spinner) findViewById(R.id.wmm_grassland_management_detail_wildlife_use);
        spinnerGrasslandStatus = (Spinner) findViewById(R.id.wmm_grassland_management_detail_grasslandStatus);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_notes);
        tvDate = (TextView) findViewById(R.id.wmm_grassland_management_Detail_date);
        dateBtn = (Button) findViewById(R.id.wmm_grassland_management_Detail_btn);
        tvMonitoringDate = (TextView) findViewById(R.id.wmm_grassland_management_Detail_monitoring_date);
        monitoringBtn = (Button) findViewById(R.id.wmm_grassland_management_Detail_monitoring_date_btn);
        tvSpeciesName = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_species_name);
        setCurrentDateOnView();
        addListenerOnButton();

        tvBoundryUsingGps = (TextView) findViewById(R.id.wmm_grassland_management_detail_boundry_using_gps);

        tvFundTal = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.wmm_grassland_management_detail_FundOthers);

        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        btnAdditionalIntervention = (Button) findViewById(R.id.select_additional_btn);
        btnManagementPractices = (Button) findViewById(R.id.select_management_practices_btn);
        startGps = (Button) findViewById(R.id.wmm_grassland_management_detail_GpsStart);
        endGps = (Button) findViewById(R.id.wmm_grassland_management_detail_GpsEnd);

        send = (Button) findViewById(R.id.wmm_grassland_management_detail_send);
        save = (Button) findViewById(R.id.wmm_grassland_management_detail_save);
        previewMap = (Button) findViewById(R.id.wmm_grassland_management_detail_preview_map);

        photo = (ImageButton) findViewById(R.id.wmm_grassland_management_detail_before_photo_site);
        previewImageSite = (ImageView) findViewById(R.id.wmm_grassland_management_detail_PhotographimageViewPreview);
        previewImageSite.setVisibility(View.GONE);

        photoCompleted = (ImageButton) findViewById(R.id.wmm_grassland_management_ManagementPhoto);
        previewImageSiteCompleted = (ImageView) findViewById(R.id.wmm_grassland_management_ManageimageViewPreview);
        previewImageSiteCompleted.setVisibility(View.GONE);

        photoMonitoring = (ImageButton) findViewById(R.id.wmm_grassland_management_Monitoring);
        previewImageSiteMonitoring = (ImageView) findViewById(R.id.wmm_grassland_management_detail_imageViewPreviewMonitoring);
        previewImageSiteMonitoring.setVisibility(View.GONE);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        spinWildlifeuseAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMM_GM_STATUS_WILDLIFE_USE);
        spinWildlifeuseAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWildlifeUse.setAdapter(spinWildlifeuseAdpt);
        spinnerWildlifeUse.setOnItemSelectedListener(this);


        spinGrasslandStatAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMM_GM_STATUS_GRASSLAND);
        spinGrasslandStatAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrasslandStatus.setAdapter(spinGrasslandStatAdpt);
        spinnerGrasslandStatus.setOnItemSelectedListener(this);
//
        initilizeUI();

        btnManagementPractices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManagementPracticesDialog();
            }
        });

        btnAdditionalIntervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  showAdditionalDialog();
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
                            agreement_no = tvAgreement_no.getText().toString();
                            grantee_name = tvGrantew_name.getText().toString();
                            fiscal_year = tvFiscal_year.getText().toString();
                            name_park_bz_cf = tvNameOfz.getText().toString();
                            name_of_grassland = tvNameofGrassland.getText().toString();
                            district = tvDistrictname.getText().toString();
                            vdc = tvNameOfVdc.getText().toString();
                            date = tvDate.getText().toString();
                            monitoring_date = tvMonitoringDate.getText().toString();
                            others = tvOthers.getText().toString();
                            fund_tal = tvFundTal.getText().toString();
                            fund_community = tvFundCommunity.getText().toString();
                            fund_others = tvFundOthers.getText().toString();
                            species_name = tvSpeciesName.getText().toString();

                            userNameToSend = userN;
                            passwordToSend = passW;

                            Log.e("SEND", "Clicked");
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
                        agreement_no = tvAgreement_no.getText().toString();
                        grantee_name = tvGrantew_name.getText().toString();
                        fiscal_year = tvFiscal_year.getText().toString();
                        name_park_bz_cf = tvNameOfz.getText().toString();
                        name_of_grassland = tvNameofGrassland.getText().toString();
                        district = tvDistrictname.getText().toString();
                        vdc = tvNameOfVdc.getText().toString();
                        date = tvDate.getText().toString();
                        monitoring_date = tvMonitoringDate.getText().toString();
                        others = tvOthers.getText().toString();
                        fund_tal = tvFundTal.getText().toString();
                        fund_community = tvFundCommunity.getText().toString();
                        fund_others = tvFundOthers.getText().toString();
                        species_name = tvSpeciesName.getText().toString();

                        jsonLatLangArray = jsonArrayGPS.toString();

                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("Grassland Management");

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
                                    String[] data = new String[]{"22", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName + "," + imageNameCompleted + "," + imageNameMonitoring, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(GrasslandManagement.this)
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

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
            }
        });

        photoCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST_B);
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
            }
        });

        photoMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST_C);
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 1);
            }
        });

        startGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ==========================start Gps Tracking ========================================== //

                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);


//                ======================================================================================== //
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
                    Default_DIalog.showDefaultDialog(context, R.string.gps_Info, "Distance measured (Kilometers) : " + (String.format("Value of a: %.8f", distance)) + "\nArea Calculated (Hectares): " + area_using_Gps);

                    scheduleTaskExecutor.shutdown();

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
                startActivity(new Intent(GrasslandManagement.this, MapPolyLineActivity.class));
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

        if (spinnerId == R.id.wmm_grassland_management_detail_landscape) {
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

        if (spinnerId == R.id.wmm_grassland_management_detail_wildlife_use) {
            switch (position) {
                case 0:
                    wildlife_use = "Frequent";
                    break;
                case 1:
                    wildlife_use = "Rare";
                    break;
                case 2:
                    wildlife_use = "Not used";
                    break;
            }
        }
        if (spinnerId == R.id.wmm_grassland_management_detail_grasslandStatus) {
            switch (position) {
                case 0:
                    grassland_status = "Good";
                    break;
                case 1:
                    grassland_status = "Degraded";
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

    //change for photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                previewImageSite.setVisibility(View.VISIBLE);
                previewImageSite.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail, CAMERA_PIC_REQUEST);
                addImage(CAMERA_PIC_REQUEST);
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST_B) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                previewImageSiteCompleted.setVisibility(View.VISIBLE);
                previewImageSiteCompleted.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail, CAMERA_PIC_REQUEST_B);
                addImage(CAMERA_PIC_REQUEST_B);
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST_C) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                previewImageSiteMonitoring.setVisibility(View.VISIBLE);
                previewImageSiteMonitoring.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail, CAMERA_PIC_REQUEST_C);
                addImage(CAMERA_PIC_REQUEST_C);
            }
        }


        if (requestCode == GEOPOINT_RESULT_CODE) {

            switch (resultCode) {
                case RESULT_OK:

                    tracking.setVisibility(View.VISIBLE);
                    isGpsTracking = true;
                    listCf.clear();
                    gpslocation.clear();
                    gps = new GpsTracker(GrasslandManagement.this);
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

    //change here
    private void saveToExternalSorage(Bitmap thumbnail, int photoID) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        String iName = null;
        if (photoID == CAMERA_PIC_REQUEST) {
            imageName = "grassland_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageName;
        } else if (photoID == CAMERA_PIC_REQUEST_B) {
            imageNameCompleted = "grassland_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageNameCompleted;
        } else if (photoID == CAMERA_PIC_REQUEST_C) {
            imageNameMonitoring = "grassland_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageNameMonitoring;
        }

        File file1 = new File(ApplicationClass.PHOTO_PATH, iName);

        if (file1.exists()) file1.delete();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), "Saved " + iName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImage(int photoIdNo) {
        if (photoIdNo == CAMERA_PIC_REQUEST) {
            File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);
//        Bitmap bm = BitmapFactory.decodeFile( imagePath ,options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImage);

        } else if (photoIdNo == CAMERA_PIC_REQUEST_B) {
            File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameCompleted);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImageCompleted = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImageCompleted);

        } else if (photoIdNo == CAMERA_PIC_REQUEST_C) {
            File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameMonitoring);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImageMonitoring = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImageMonitoring);

        }

    }


    private void loadImageFromStorage(String path, int id) {
        if (id == 1) {
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
        if (id == 2) {
            try {
                previewImageSiteCompleted.setVisibility(View.VISIBLE);
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                previewImageSiteCompleted.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "invalid path", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        if (id == 3) {
            try {
                previewImageSiteMonitoring.setVisibility(View.VISIBLE);
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                previewImageSiteMonitoring.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "invalid path", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

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
            String imageNameall = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if (status.equals("Sent")) {
                save.setEnabled(false);
                send.setEnabled(false);
            }

            String[] images = imageNameall.split(",");
            imageName = images[0];
            imageNameCompleted = images[1];
            String[] Second = images[1].split(",");
            imageNameCompleted = Second[0];
            imageNameMonitoring = Second[1];
            Log.e("wetland_", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 1);

                addImage(CAMERA_PIC_REQUEST);
            }
            Log.e("grassland_", "i-" + imageNameCompleted);
            if (imageNameCompleted.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameCompleted);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 2);

                addImage(CAMERA_PIC_REQUEST_B);
            }
            Log.e("grassland_", "i-" + imageNameMonitoring);

            if (imageNameMonitoring.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameMonitoring);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 3);

                addImage(CAMERA_PIC_REQUEST_C);
            }
            try {
                //new adjustment
                Log.e("grassland_", "" + jsonToParse);
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

    private void UpdateData() {
        mProgressDlg = new ProgressDialog(context);
        mProgressDlg.setMessage("Acquiring GPS location\nPlease wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

        gps = new GpsTracker(GrasslandManagement.this);
        if (gps.canGetLocation()) {

            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gps = new GpsTracker(GrasslandManagement.this);
                                if (gps.canGetLocation()) {
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();

                                    Log.e("latlang", " lat: " + finalLat + " long: "
                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gps = new GpsTracker(GrasslandManagement.this);
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

    private void showManagementPracticesDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        management_practice = "";
        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.management_practice_grassland);

        Button btnOk = (Button) showDialog.findViewById(R.id.wmm_grassland_management_ok_btn);

        cbGrassesCut = (CheckBox) showDialog.findViewById(R.id.grasses_cut_id);
        cbTreesUprooted = (CheckBox) showDialog.findViewById(R.id.trees_uprooted_id);
        cbOldGrassBurnt = (CheckBox) showDialog.findViewById(R.id.old_grass_brunt_id);
        cbNewGrassSeeding = (CheckBox) showDialog.findViewById(R.id.new_grass_seeding_id);
        cbSolarcharger = (CheckBox) showDialog.findViewById(R.id.solar_recharger_id);
        cbWaterSprinkler = (CheckBox) showDialog.findViewById(R.id.water_sprinkler_id);
        cbOthers = (CheckBox) showDialog.findViewById(R.id.other_practices_id);

        tvOtherPractice = (AutoCompleteTextView) showDialog.findViewById(R.id.wmm_grassland_management_Detail_OtherPractices);

        showDialog.setTitle("Management Practices");
        showDialog.getActionBar();
        showDialog.show();
        showDialog.getWindow().setLayout((width), LinearLayout.LayoutParams.WRAP_CONTENT);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                other_practices = tvOtherPractice.getText().toString();
                getCheckBoxData();
                tvManagementPractice.setText(management_practice);

                showDialog.dismiss();

            }
        });

    }

    private void showAdditionalDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        additional_interventions = "";
        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.additonal_intervention_grassland);

        Button btnOk = (Button) showDialog.findViewById(R.id.wmm_grassland_management_additional_ok_btn);

        cbSolarcharger = (CheckBox) showDialog.findViewById(R.id.solar_recharger_id);
        cbWaterSprinkler = (CheckBox) showDialog.findViewById(R.id.water_sprinkler_id);
        cbOther1 = (CheckBox) showDialog.findViewById(R.id.other_additional_id);

        tvOtherIntervention = (AutoCompleteTextView) showDialog.findViewById(R.id.wmm_grassland_management_Detail_OtherIntervention);

        showDialog.setTitle("Additional Intervention");
        showDialog.getActionBar();
        showDialog.show();
        showDialog.getWindow().setLayout((width), LinearLayout.LayoutParams.WRAP_CONTENT);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                other_interventions = tvOtherIntervention.getText().toString();
                getCheckBoxData1();
                tvAdditionalInterventions.setText(additional_interventions);

                showDialog.dismiss();

            }
        });

    }

    private void getCheckBoxData() {
        if (cbGrassesCut.isChecked()) {
            management_practice = management_practice + ", " + cbGrassesCut.getText().toString();

        }
        if (cbTreesUprooted.isChecked()) {
            management_practice = management_practice + ", " + cbTreesUprooted.getText().toString();

        }
        if (cbOldGrassBurnt.isChecked()) {
            management_practice = management_practice + ", " + cbOldGrassBurnt.getText().toString();

        }
        if (cbNewGrassSeeding.isChecked()) {
            management_practice = management_practice + ", " + cbNewGrassSeeding.getText().toString();

        }
        if (cbSolarcharger.isChecked()) {
            management_practice = management_practice + ", " + cbSolarcharger.getText().toString();

        }
        if (cbWaterSprinkler.isChecked()) {
            management_practice = management_practice + ", " + cbWaterSprinkler.getText().toString();

        }
        if (cbOthers.isChecked()) {
            management_practice = management_practice + ", " + cbOthers.getText().toString() + ",  " + other_practices;
        }
    }

    private void getCheckBoxData1() {
        if (cbSolarcharger.isChecked()) {
            additional_interventions = additional_interventions + ", " + cbSolarcharger.getText().toString();

        }
        if (cbWaterSprinkler.isChecked()) {
            additional_interventions = additional_interventions + ", " + cbWaterSprinkler.getText().toString();

        }
        if (cbOther1.isChecked()) {
            additional_interventions = additional_interventions + ", " + cbOther1.getText().toString() + ",  " + other_interventions;

        }

    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {

            post_dict.put("tablename", "tbl_grassland_management");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("name_park_bz_cf", name_park_bz_cf);
            header.put("name_of_grassland", name_of_grassland);
            header.put("district", district);
            header.put("date", date);
            header.put("vdc", vdc);
            header.put("management_practice", management_practice);
            header.put("additional_interventions", additional_interventions + ":  " + other_interventions);
            header.put("longitude", finalLong);
            header.put("latitude", finalLat);
            header.put("monitoring_date", monitoring_date);
            header.put("wildlife_use", wildlife_use);
            header.put("grassland_status", grassland_status);
            header.put("others", others);
            header.put("boundary", latLangArray);
            header.put("area_gps", area_using_Gps);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            header.put("species_name", species_name);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            photo_dict.put("photo", encodedImage);
            photo_dict.put("photo2", encodedImageCompleted);
            photo_dict.put("photo3", encodedImageMonitoring);
            photoTosend = photo_dict.toString();


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
        agreement_no = jsonObj.getString("agreement_no");
        grantee_name = jsonObj.getString("grantee_name");
        fiscal_year = jsonObj.getString("fiscal_year");
        name_park_bz_cf = jsonObj.getString("name_park_bz_cf");
        name_of_grassland = jsonObj.getString("name_of_grassland");
        district = jsonObj.getString("district");
        vdc = jsonObj.getString("vdc");
        date = jsonObj.getString("date");
        monitoring_date = jsonObj.getString("monitoring_date");
        management_practice = jsonObj.getString("management_practice");
        additional_interventions = jsonObj.getString("additional_interventions");
        wildlife_use = jsonObj.getString("wildlife_use");
        grassland_status = jsonObj.getString("grassland_status");
        others = jsonObj.getString("others");
        latLangArray = jsonObj.getString("boundary");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        area_using_Gps = Double.parseDouble(jsonObj.getString("area_gps"));
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        species_name = jsonObj.getString("species_name");
        Log.e("Plantationdetail", "Parsed data " + agreement_no + grantee_name + fiscal_year);


        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvAgreement_no.setText(agreement_no);
        tvGrantew_name.setText(grantee_name);
        tvFiscal_year.setText(fiscal_year);
        tvDate.setText(date);
        tvNameOfz.setText(name_park_bz_cf);
        tvNameofGrassland.setText(name_of_grassland);
        tvDistrictname.setText(district);
        tvNameOfVdc.setText(vdc);
        tvMonitoringDate.setText(monitoring_date);
        tvOthers.setText(others);
        tvFundTal.setText(fund_tal);
        tvFundCommunity.setText(fund_community);
        tvFundOthers.setText(fund_others);
        tvSpeciesName.setText(species_name);
        tvManagementPractice.setText(management_practice);

        //
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

        int spinWildlifeUseAdptPosition = spinWildlifeuseAdpt.getPosition(wildlife_use);
        spinnerWildlifeUse.setSelection(spinWildlifeUseAdptPosition);

        int spinWildlifeUseAdptPosition1 = spinGrasslandStatAdpt.getPosition(grassland_status);
        spinnerGrasslandStatus.setSelection(spinWildlifeUseAdptPosition1);

        tvBoundryUsingGps.setText("Area Using GPS : " + area_using_Gps + " (Hectares)");

    }


    @Override
    public void gpsTrackerForPolyLine(Double lat, Double longt) {

        finalLatFromGeopoint = lat;
        finalLongFromGeopoint = longt;

        Log.e("GrassLand_SAMIR", "gpsTrackerForPolyLine: " + finalLatFromGeopoint + " , " + finalLongFromGeopoint);
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
                    new PromptDialog(GrasslandManagement.this)
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
                    String[] data = new String[]{"22", "Grassland Management", dateString, jsonToSend, jsonLatLangArray, "" + imageName + "," + imageNameCompleted + "," + imageNameMonitoring, "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(GrasslandManagement.this)
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
                        .appendQueryParameter("photo", photoTosend)
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

        monitoringBtn.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    monitoringBtn.setShowSoftInputOnFocus(false);
                }
                showDialog(DATE_DIALOG_ID1);
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

            case DATE_DIALOG_ID1:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener1, year, month,
                        day);
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
                    .append(" "));
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when com.naxa.conservationtracking.dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            tvMonitoringDate.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(""));
        }
    };
}
