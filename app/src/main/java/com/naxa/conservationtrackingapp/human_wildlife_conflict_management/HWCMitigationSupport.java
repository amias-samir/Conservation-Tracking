package com.naxa.conservationtrackingapp.human_wildlife_conflict_management;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.support.v7.widget.CardView;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtrackingapp.GeoPointActivity;
import com.naxa.conservationtrackingapp.PhoneUtils;
import com.naxa.conservationtrackingapp.activities.CalculateAreaUsinGPS;
import com.naxa.conservationtrackingapp.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtrackingapp.activities.GpsTracker;
import com.naxa.conservationtrackingapp.activities.MapPointActivity;
import com.naxa.conservationtrackingapp.activities.MapPolyLineActivity;
import com.naxa.conservationtrackingapp.R;

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

import com.naxa.conservationtrackingapp.application.ApplicationClass;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.dialog.Multiple_Selection_Dialog_HECMitigationSupport;
import com.naxa.conservationtrackingapp.forest.Cf_Detail;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;
import com.naxa.conservationtrackingapp.wildlife_monitoring_techniques.HumanDisturbance;

import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class HWCMitigationSupport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;

    //change for more photos
    int CAMERA_PIC_REQUEST = 2;
    int CAMERA_PIC_REQUEST_B = 3;
    int CAMERA_PIC_REQUEST_C = 4;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    Spinner spinnerLandscape, activityName, fenchStatus;
    ArrayAdapter landscapeAdpt, activityNameAdapter, fenchStatusAdapter;
    Button send, save, startGps, endGps , previewMap, startTrackingGps, endTrackingGps, trackingPreviewMap;
    ProgressDialog mProgressDlg;
    ProgressBar tracking;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    GpsTracker gpsTracking;
    double distance;
    String jsonToSend, photoTosend;

    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    Constants constants;

    //change for more photos
    String imagePath, imagePathMonitoring, encodedImage = null, encodedImageCompleted = null, encodedImageMonitoring = null, imageName = "no_photo", imageNameCompleted = "no_photo", imageNameMonitoring = "no_photo";
    ImageButton photo, photoCompleted, photoMonitoring;
    ImageView previewImageSite, previewImageSiteCompleted, previewImageSiteMonitoring;

    double area_using_Gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    boolean isPaused = true;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;
    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";

    String projectCode;
    String landscape;
    String other_landscape;
    String funding_source;
    String agreement_no;
    String grantee_name;
    String fiscal_year;
    String name_of_bz;
    String district_name;
    String vdc_name;
    String activity_name;
    //    String unit;
    String value;
    String total_benificiary_hhs;
    String funding_tal;
    String funding_community;
    String completion_date;
    String follow_up_monitoring_date;
    String recommendation;
    String fench_status;
    String implementation_recommendation;
    String remarks;
    String other_fund;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvProjectCode, tvAgreement_no, tvGrantew_name, tvFiscal_year, tvNameOfz, tvDistrictname, tvNameOfVdc,
            tvValue, tvTotal_benificiary_hhs, tvFunding_tal, tvFunding_community, tvOthers_fund,
            tvCompletion_date, tvFollow_up_monitoring_date, tvRecommendation,
            tvImplementation_recommendation, tvNotes;

   public static CardView cvGpsPoint , cvGpsTracking ;

    public static EditText tvNameAndNumber;
    RelativeLayout rlNumberOfItems;
    public static String fromMultipleSelectionDialog = "";

    public static String gpsPointBoolean ="", gpsTrackingBoolean = "";

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 11;
    static final int DATE_DIALOG_ID1 = 12;

    String userNameToSend, passwordToSend;
    String dataSentStatus = "", dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwc_mitigation_support);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.hwc_mitigation_support_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_fundingSource);
        tvAgreement_no = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_Agreement_No);
        tvGrantew_name = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_Grantee_Name);
        tvFiscal_year = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_fiscal_Year);
        tvNameOfz = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_name_of_bz_corridor_location);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_Vdc_Municipality_Name);
//        tvUnit = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_unit);
        tvTotal_benificiary_hhs = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_total_beneficiary);
        tvFunding_tal = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_tal);
        tvFunding_community = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_Community_Contribution);
        tvCompletion_date = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_date_of_completion);
        tvFollow_up_monitoring_date = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_follow_up_monitoring_date);
        tvRecommendation = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_recommendation);
        tvImplementation_recommendation = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_implementation_of_recommendation);
        tvNotes = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_remarks);
        tvOthers_fund = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_otherfund);

        //checkbox selection
        rlNumberOfItems = (RelativeLayout) findViewById(R.id.hwc_mitigation_support_name_of_activity);
        tvNameAndNumber = (EditText) findViewById(R.id.nameAndNumber);
        tvValue = (AutoCompleteTextView) findViewById(R.id.hwc_mitigation_support_value);

        fenchStatus = (Spinner) findViewById(R.id.hwc_mitigation_support_status_of_fence);
        setCurrentDateOnView();
        addListenerOnButton();

        photo = (ImageButton) findViewById(R.id.hwc_mitigation_support_photoOfsite);
        previewImageSite = (ImageView) findViewById(R.id.hwc_mitigation_support_PhotoSiteimageViewPreview);
        previewImageSite.setVisibility(View.GONE);

        photoCompleted = (ImageButton) findViewById(R.id.hwc_mitigation_support_photo_completed);
        previewImageSiteCompleted = (ImageView) findViewById(R.id.hwc_mitigation_support_CompletedimageViewPreview);
        previewImageSiteCompleted.setVisibility(View.GONE);

        photoMonitoring = (ImageButton) findViewById(R.id.hwc_mitigation_support_photo_monitoring);
        previewImageSiteMonitoring = (ImageView) findViewById(R.id.hwc_mitigation_support_Monitoring_imageViewPreview);
        previewImageSiteMonitoring.setVisibility(View.GONE);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        fenchStatusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.STATUS_OF_FENCE);
        fenchStatusAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fenchStatus.setAdapter(fenchStatusAdapter);
        fenchStatus.setOnItemSelectedListener(this);

        send = (Button) findViewById(R.id.hwc_mitigation_support_send);
        save = (Button) findViewById(R.id.hwc_mitigation_support_save);
        startGps = (Button) findViewById(R.id.hwc_mitigation_support_GpsPointStart);
//         endGps = (Button) findViewById(R.id.hwc_mitigation_support_GpsEnd);
        previewMap = (Button) findViewById(R.id.hwc_mitigation_support_preview_map);
        previewMap.setEnabled(false);

//===============tracking initiliazition=======================================//

        startTrackingGps = (Button) findViewById(R.id.hwc_mitigation_support_GpsTrackingStart);
        endTrackingGps = (Button) findViewById(R.id.hwc_mitigation_support_GpsTrackingEnd);
        trackingPreviewMap = (Button) findViewById(R.id.hwc_mitigation_support_Tracking_preview_map);
        tvBoundryUsingGps = (TextView) findViewById(R.id.hwc_mitigation_support_areaUsinggps);

        trackingPreviewMap.setEnabled(false);
        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        cvGpsPoint = (CardView) findViewById(R.id.gpsPointLayout);
        cvGpsTracking = (CardView) findViewById(R.id.gpsTrackingLayout);

//        constants = new Constants();
//        gpsPointBoolean = constants.hwcEndomentGPSPointKey;
//        gpsTrackingBoolean = constants.hwcEndomentGPSTrakingKey;





//================================================================================================//

        initilizeUI();

        rlNumberOfItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Multiple_Selection_Dialog_HECMitigationSupport.multipleChoice_MitigationSupport(context, R.string.select_choices_mitigation_support, "Choose and Put unit ");


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
                                    name_of_bz = tvNameOfz.getText().toString();
                                    district_name = tvDistrictname.getText().toString();
                                    vdc_name = tvNameOfVdc.getText().toString();
                                    activity_name = tvNameAndNumber.getText().toString();
                                    value = tvValue.getText().toString();
                                    total_benificiary_hhs = tvTotal_benificiary_hhs.getText().toString();
                                    funding_tal = tvFunding_tal.getText().toString();
                                    funding_community = tvFunding_community.getText().toString();
                                    other_fund = tvOthers_fund.getText().toString();
                                    completion_date = tvCompletion_date.getText().toString();
                                    follow_up_monitoring_date = tvFollow_up_monitoring_date.getText().toString();
                                    recommendation = tvRecommendation.getText().toString();
                                    implementation_recommendation = tvImplementation_recommendation.getText().toString();
                                    remarks = tvNotes.getText().toString();

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
                        agreement_no = tvAgreement_no.getText().toString();
                        grantee_name = tvGrantew_name.getText().toString();
                        fiscal_year = tvFiscal_year.getText().toString();
                        name_of_bz = tvNameOfz.getText().toString();
                        district_name = tvDistrictname.getText().toString();
                        vdc_name = tvNameOfVdc.getText().toString();
                        activity_name = tvNameAndNumber.getText().toString();
                        value = tvValue.getText().toString();
                        total_benificiary_hhs = tvTotal_benificiary_hhs.getText().toString();
                        funding_tal = tvFunding_tal.getText().toString();
                        funding_community = tvFunding_community.getText().toString();
                        other_fund = tvOthers_fund.getText().toString();
                        completion_date = tvCompletion_date.getText().toString();
                        follow_up_monitoring_date = tvFollow_up_monitoring_date.getText().toString();
                        recommendation = tvRecommendation.getText().toString();
                        implementation_recommendation = tvImplementation_recommendation.getText().toString();
                        remarks = tvNotes.getText().toString();

                        jsonLatLangArray = jsonArrayGPS.toString();

                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("HWC Mitigation Support");

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
                                    String[] data = new String[]{"47", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName + "," + imageNameCompleted + "," + imageNameMonitoring, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(HWCMitigationSupport.this)
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

        // for multiple photo action click
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
                if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                    SharedPreferences wmbPreference = PreferenceManager
                            .getDefaultSharedPreferences(context);

                    String latitu = wmbPreference.getString("LAT", "");
                    String longtitu = wmbPreference.getString("LONG", "");

                    if (gps.canGetLocation()) {
                        gpslocation.add(gps.getLocation());
                        finalLat = gps.getLatitude();
                        finalLong = gps.getLongitude();
                        if (finalLat != 0) {
                            previewMap.setEnabled(true);
                            try {
                                JSONObject data = new JSONObject();
                                data.put("latitude", finalLat);
                                data.put("longitude", finalLong);

                                jsonArrayGPS.put(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            LatLng d = new LatLng(finalLat, finalLong);

                            listCf.add(d);
                            isGpsTaken = true;
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Your Location is - \nLat: " + finalLat
                                            + "\nLong: " + finalLong, Toast.LENGTH_SHORT)
                                    .show();
                            stringBuilder.append("[" + finalLat + "," + finalLong + "]" + ",");
                        }

                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
                    gps = new GPS_TRACKER_FOR_POINT(HWCMitigationSupport.this);
                    Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");
                }
            }

        });

        previewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckValues.isFromSavedFrom) {
                    StaticListOfCoordinates.setList(listCf);
                    startActivity(new Intent(HWCMitigationSupport.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(HWCMitigationSupport.this, MapPointActivity.class));
                    } else {
                        Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

                    }
                }
            }
        });



        //============================================== start tracking code starts here ======================================//

//        startTrackingGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tracking.setVisibility(View.VISIBLE);
//                isGpsTracking = true;
//                listCf.clear();
//                gpslocation.clear();
//                gpsTracking = new GpsTracker(HWCMitigationSupport.this);
//                if (gpsTracking.canGetLocation()) {
//
////                    Toast.makeText(
////                            getApplicationContext(),
////                            "Your Location is - \nLat: " + initLat
////                                    + "\nLong: " + initLong, Toast.LENGTH_SHORT)
////                            .show();
//                } else {
//                    gpsTracking.showSettingsAlert();
//                }
//
//                UpdateData();//Update GPS coordinate background thread //Method
//
//                endTrackingGps.setEnabled(true);
//                startTrackingGps.setEnabled(false);
//            }
//        });


        startTrackingGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws NullPointerException {


                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
            }
        });




        endTrackingGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                unregisterReceiver(mReceiver);
                endTrackingGps.setEnabled(false);
                trackingPreviewMap.setEnabled(true);
                tracking.setVisibility(View.INVISIBLE);
                isGpsTracking = false;
                startTrackingGps.setEnabled(true);
                initLat = gpsTracking.getLatitude();
                initLong = gpsTracking.getLongitude();

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
                    Default_DIalog.showDefaultDialog(context, R.string.gps_Info, "Distance measured (Kilometers) : " + distance + "\nArea Calculated (Hectares): " + area_using_Gps);

                } else {
                    Default_DIalog.showDefaultDialog(context, R.string.gps_Info, "GPS is not initialized properly");

                }

            }
        });

        trackingPreviewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(HWCMitigationSupport.this, MapPolyLineActivity.class));
            }
        });


        //============================================end of tracking code here =================================================//

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

        if (spinnerId == R.id.hwc_mitigation_support_landscape) {
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

        if (spinnerId == R.id.hwc_mitigation_support_status_of_fence) {
            switch (position) {
                case 0:
                    fench_status = "Good";
                    break;
                case 1:
                    fench_status = "Damaged";
                    break;
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


    // change here for photo
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
                    gpsTracking = new GpsTracker(HWCMitigationSupport.this);
                    if (gpsTracking.canGetLocation()) {
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "Your Location is - \nLat: " + initLat
//                                    + "\nLong: " + initLong, Toast.LENGTH_SHORT)
//                            .show();
                    } else {
                        gpsTracking.showSettingsAlert();
                    }

                    UpdateData();//Update GPS coordinate background thread //Method

                    endTrackingGps.setEnabled(true);
                    startTrackingGps.setEnabled(false);
                    //                    Toast.makeText(this.context, location, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

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

    }

    //change here
    private void saveToExternalSorage(Bitmap thumbnail, int photoID) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        String iName = null;
        if (photoID == CAMERA_PIC_REQUEST) {
            imageName = "hwc_mitigation_support_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageName;
        } else if (photoID == CAMERA_PIC_REQUEST_B) {
            imageNameCompleted = "hwc_mitigation_complete_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageNameCompleted;
        } else if (photoID == CAMERA_PIC_REQUEST_C) {
            imageNameMonitoring = "hwc_mitigation_monitoring_" + timeInMillis + PhoneUtils.getFormatedId();
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
            startGps.setEnabled(false);
            isGpsTaken=true;
            previewMap.setEnabled(true);
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            String imageNameall = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }

            String[] images = imageNameall.split(",");
            imageName = images[0];
            imageNameCompleted = images[1];
            imageNameMonitoring = images[2];
            Log.e("mitigation_support_", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 1);

                addImage(CAMERA_PIC_REQUEST);
            }
            Log.e("mitigation_complete_", "i-" + imageNameCompleted);
            if (imageNameCompleted.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameCompleted);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 2);

                addImage(CAMERA_PIC_REQUEST_B);
            }
            Log.e("mitigation_monitoring_", "i-" + imageNameMonitoring);

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
                Log.e("mitigation_support_", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//            gps = new GPS_TRACKER_FOR_POINT(HWCMitigationSupport.this);
//            gps.canGetLocation();
//            startGps.setEnabled(true);
            gpsTracking = new GpsTracker(HWCMitigationSupport.this);
            gpsTracking.canGetLocation();
            startTrackingGps.setEnabled(true);
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

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_hwc_mitigation_support");


            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("boundary", latLangArray);
            header.put("name_bz_corridor", name_of_bz);
            header.put("activity_name", activity_name + "   " + value);
            header.put("status_fence_trench", fench_status);
            header.put("total_benificiary_hhs", total_benificiary_hhs);
            header.put("latitude", finalLat);
            header.put("longitude", finalLong);
            header.put("funding_tal", funding_tal);
            header.put("funding_community", funding_community);
            header.put("other_fund", other_fund);
            header.put("completion_date", completion_date);
            header.put("follow_up_monitoring_date", follow_up_monitoring_date);
            header.put("recommendation", recommendation);
            header.put("implementation_recommendation", implementation_recommendation);
            header.put("others", remarks);

//            header.put("area_gps", area_using_Gps);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            photo_dict.put("photo", encodedImage);
            photo_dict.put("photo2", encodedImageCompleted);
            photo_dict.put("photo3", encodedImageMonitoring);
            photoTosend = photo_dict.toString();

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
        Log.e("MITIGATIONSUPPORT", "json : " + jsonOb.toString());
        String data = jsonOb.getString("formdata");
        Log.e("MITIGATIONSUPPORT", "formdata : " + jsonOb.toString());
        JSONObject jsonObj = new JSONObject(data);
        Log.e("MITIGATIONSUPPORT", "json : " + jsonObj.toString());

        projectCode = jsonObj.getString("project_code");
        landscape = jsonObj.getString("landscape");
        funding_source = jsonObj.getString("funding_source");
        agreement_no = jsonObj.getString("agreement_no");
        grantee_name = jsonObj.getString("grantee_name");
        fiscal_year = jsonObj.getString("fiscal_year");
        name_of_bz = jsonObj.getString("name_bz_corridor");
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        latLangArray = jsonObj.getString("boundary");
        activity_name = jsonObj.getString("activity_name");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        total_benificiary_hhs = jsonObj.getString("total_benificiary_hhs");
        funding_tal = jsonObj.getString("funding_tal");
        funding_community = jsonObj.getString("funding_community");
        other_fund = jsonObj.getString("other_fund");
        completion_date = jsonObj.getString("completion_date");
        follow_up_monitoring_date = jsonObj.getString("follow_up_monitoring_date");
        fench_status = jsonObj.getString("status_fence_trench");
        recommendation = jsonObj.getString("recommendation");
        implementation_recommendation = jsonObj.getString("implementation_recommendation");
        remarks = jsonObj.getString("others");

//        area_using_Gps = Double.parseDouble(jsonObj.getString("area_gps"));

        Log.e("MITIGATIONSUPPORT", "Parsed data " + agreement_no + grantee_name + fiscal_year);

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvAgreement_no.setText(agreement_no);
        tvGrantew_name.setText(grantee_name);
        tvFiscal_year.setText(fiscal_year);
        tvNameOfz.setText(name_of_bz);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvTotal_benificiary_hhs.setText(total_benificiary_hhs);
        tvFunding_tal.setText(funding_tal);
        tvFunding_community.setText(funding_community);
        tvOthers_fund.setText(other_fund);
        tvCompletion_date.setText(completion_date);
        tvFollow_up_monitoring_date.setText(follow_up_monitoring_date);
        tvRecommendation.setText(recommendation);
        tvImplementation_recommendation.setText(implementation_recommendation);
        tvNotes.setText(remarks);

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

        if (activity_name.equals("   ")) {
            tvNameAndNumber.setText("");
            tvValue.setText("");
        } else {
            String[] actions1 = activity_name.split("   ");
            tvNameAndNumber.setText(actions1[0]);
            tvValue.setText(actions1[1]);
        }

        int forTypePos = fenchStatusAdapter.getPosition(fench_status);
        fenchStatus.setSelection(forTypePos);
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
                    new PromptDialog(HWCMitigationSupport.this)
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
                    String[] data = new String[]{"47", "HWC Mitigation Support", dateString, jsonToSend, jsonLatLangArray,
                            "" + imageName + "," + imageNameCompleted + "," + imageNameMonitoring, "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(HWCMitigationSupport.this)
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


        tvCompletion_date.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvCompletion_date.setShowSoftInputOnFocus(false);
                }
                showDialog(DATE_DIALOG_ID);
            }

        });

        tvFollow_up_monitoring_date.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvFollow_up_monitoring_date.setShowSoftInputOnFocus(false);
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
            tvCompletion_date.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(year).append("-").append(month + 1).append("-")
                    .append(day).append(""));
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when com.naxa.conservationtracking.dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            tvFollow_up_monitoring_date.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(year).append("-").append(month + 1).append("-")
                    .append(day).append(""));
        }
    };



    private void UpdateData() {
        mProgressDlg = new ProgressDialog(context);
        mProgressDlg.setMessage("Acquiring GPS location\nPlease wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

        gpsTracking = new GpsTracker(HWCMitigationSupport.this);
        if (gpsTracking.canGetLocation()) {

            finalLat = gpsTracking.getLatitude();
            finalLong = gpsTracking.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gpsTracking = new GpsTracker(HWCMitigationSupport.this);
                                if (gpsTracking.canGetLocation()) {
                                    finalLat = gpsTracking.getLatitude();
                                    finalLong = gpsTracking.getLongitude();

                                    Log.e("latlang", " lat: " + finalLat + " long: "
                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gpsTracking = new GpsTracker(HWCMitigationSupport.this);
                                if (gpsTracking.canGetLocation()) {
                                    gpslocation.add(gpsTracking.getLocation());
                                    finalLat = gpsTracking.getLatitude();
                                    finalLong = gpsTracking.getLongitude();
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
                                        gpsTracking.showSettingsAlert();
                                    }
                                }
                            }
                        }

                    });
                }
            }, 0, 5, TimeUnit.SECONDS);

        } else {
            try {
                gpsTracking.showSettingsAlert();
            } catch (NullPointerException e) {

            }

        }
    }
}
