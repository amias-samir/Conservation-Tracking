package com.naxa.conservationtrackingapp.wmm;

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
import android.support.design.widget.TextInputLayout;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtrackingapp.GeoPointActivity;
import com.naxa.conservationtrackingapp.PhoneUtils;
import com.naxa.conservationtrackingapp.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtrackingapp.activities.MapPointActivity;
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

import javax.net.ssl.HttpsURLConnection;

import com.naxa.conservationtrackingapp.activities.SavedFormsActivity;
import com.naxa.conservationtrackingapp.application.ApplicationClass;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.forest.Cf_Detail;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;

import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class WildlifeSightingDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, spinnerSex, spinnerAge, spinnerHabitType, spinnerRhinoActivity;
    ArrayAdapter landscapeAdpt, spinSexAdpt, spinAgeAdpt, spinHabitTypeAdpt, spinRhinoActivityAdpt;
    Button send, save, startGps, previewMap;
    ProgressDialog mProgressDlg;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    String jsonToSend, photoTosend;

    String formNameSavedForm, formid;

    String imagePath, encodedImage = null, imageName = "no_photo";
    ImageButton photo;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    ImageView previewImageSite;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;
    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";


    String projectCode;
    String landscape;
    String other_landscape;
    String funding_source;
    String agreement_no;
    String grantee_name;
    String fiscal_year;
    String district;
    String vdc;
    String date;
    String time;
    String wildlife_spp;
    String rhino_id;
    String identification_features;
    String sex;
    String age;
    String total_no;
    String name_bz_forest_cf;
    String habitat_type;
    String rhino_activity;
    String specified_rhino_activity;
    String others;
    String male_number = "0";
    String female_number = "0";
    String unknown_number = "0";
    String adult_number = "0";
    String sub_adult_number = "0";
    String calf_cub_number = "0";
    String fund_tal, fund_community, fund_others;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    TextInputLayout tvSpecifiedRhinoActivityLBL;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvProjectCode, tvAgreement_no, tvGrantew_name, tvFiscal_year, tvNameOfz,
            tvDistrictname, tvNameOfVdc, tvWildlifeSpp, tvSpecifiedRhinoActivity,
            tvDate, tvTime, tvMonitoringDate, tvTotalNo, tvOthers, tvmale_number, tvfemale_number, tvunknown_number, tvadult_number,
            tvsub_adult_number, tvcalf_cub_number, tvFundTal, tvFundCommunity, tvFundOthers, tvRhinoID, tvIdentificationFeatures;


    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 9999;

    String userNameToSend, passwordToSend;
    String dataSentStatus = "", dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmm_wildlife_sighting_management);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckValues.isFromSavedFrom = false;


        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.wmm_wildlife_sighting_management_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_fundingSource);
        tvAgreement_no = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_Agreement_No);
        tvGrantew_name = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_Grantee_Name);
        tvFiscal_year = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_Detail_fiscal_Year);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_Vdc_Municipality_Name);
        tvTime = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_Detail_time);
        tvWildlifeSpp = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_spp_name);
        tvRhinoID = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_rhino_id);
        tvSpecifiedRhinoActivity = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_rhino_activity_specify_others);
        tvSpecifiedRhinoActivityLBL = (TextInputLayout) findViewById(R.id.wmm_wildlife_sighting_management_detail_rhino_activitySpecifyOthersLBL);
        tvIdentificationFeatures = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_identification_features);
       // spinnerSex = (Spinner) findViewById(R.id.wmm_wildlife_sighting_management_detail_sexStatus);
     //   spinnerAge = (Spinner) findViewById(R.id.wmm_wildlife_sighting_management_detail_age);
        tvTotalNo = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_total);
        tvNameOfz = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_nameof_bzForestCorridor);
        spinnerHabitType = (Spinner) findViewById(R.id.wmm_wildlife_sighting_management_detail_habitat_typeStatus);
        spinnerRhinoActivity = (Spinner) findViewById(R.id.wmm_wildlife_sighting_management_detail_rhino_activity);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_notes);
        tvDate = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_Detail_date);
        tvmale_number=(AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_male_number);
        tvfemale_number=(AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_female_number);
        tvunknown_number= (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_unknown_number);
        tvadult_number= (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_adult_number);
        tvsub_adult_number= (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_sub_adult_number);
        tvcalf_cub_number= (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_calf_cub_number);

        setCurrentDateOnView();
        addListenerOnButton();
        setCurrentTimeOnView();
        addListenerOnTimeButton();

        tvFundTal = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.wmm_wildlife_sighting_management_detail_FundOthers);

        startGps = (Button) findViewById(R.id.wmm_wildlife_sighting_management_detail_GpsStart);

        send = (Button) findViewById(R.id.wmm_wildlife_sighting_management_detail_send);
        save = (Button) findViewById(R.id.wmm_wildlife_sighting_management_detail_save);
        previewMap = (Button) findViewById(R.id.wmm_wildlife_sighting_management_detail_preview_map);
        previewMap.setEnabled(false);

        photo = (ImageButton) findViewById(R.id.wmm_wildlife_sighting_management_detail_photo);
        previewImageSite = (ImageView) findViewById(R.id.wmm_wildlife_sighting_management_detail_PhotographSiteimageViewPreview);
        previewImageSite.setVisibility(View.GONE);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

      //  spinSexAdpt = new ArrayAdapter<String>(this,
       //         android.R.layout.simple_spinner_item, Constants.WMM_WS_SEX);
       // spinSexAdpt
       //         .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSex.setAdapter(spinSexAdpt);
//        spinnerSex.setOnItemSelectedListener(this);

        /*
        spinAgeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMM_WILDLIFEM_AGE);
        spinAgeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(spinAgeAdpt);
        spinnerAge.setOnItemSelectedListener(this);
        */

        spinHabitTypeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMM_WS_HABITAT_TYPE);
        spinHabitTypeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHabitType.setAdapter(spinHabitTypeAdpt);
        spinnerHabitType.setOnItemSelectedListener(this);

        spinRhinoActivityAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMM_WS_ACTIVITY);
        spinRhinoActivityAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRhinoActivity.setAdapter(spinRhinoActivityAdpt);
        spinnerRhinoActivity.setOnItemSelectedListener(this);
//
        initilizeUI();

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
                                    name_bz_forest_cf = tvNameOfz.getText().toString();
                                    district = tvDistrictname.getText().toString();
                                    vdc = tvNameOfVdc.getText().toString();
                                    date = tvDate.getText().toString();
                                    time = tvTime.getText().toString();
                                    wildlife_spp = tvWildlifeSpp.getText().toString();
                                    rhino_id = tvRhinoID.getText().toString();
                                    specified_rhino_activity = tvSpecifiedRhinoActivity.getText().toString();
                                    identification_features = tvIdentificationFeatures.getText().toString();
                                    date = tvDate.getText().toString();
                                    total_no = tvTotalNo.getText().toString();
                                    others = tvOthers.getText().toString();
                                    male_number= tvmale_number.getText().toString();
                                    female_number=tvfemale_number.getText().toString();
                                    unknown_number= tvunknown_number.getText().toString();
                                    adult_number= tvsub_adult_number.getText().toString();
                                    sub_adult_number= tvsub_adult_number.getText().toString();
                                    calf_cub_number= tvcalf_cub_number.getText().toString();
                                    fund_tal = tvFundTal.getText().toString();
                                    fund_community = tvFundCommunity.getText().toString();
                                    fund_others = tvFundOthers.getText().toString();

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
                        name_bz_forest_cf = tvNameOfz.getText().toString();
                        district = tvDistrictname.getText().toString();
                        vdc = tvNameOfVdc.getText().toString();
                        date = tvDate.getText().toString();
                        time = tvTime.getText().toString();
                        wildlife_spp = tvWildlifeSpp.getText().toString();
                        rhino_id = tvRhinoID.getText().toString();
                        specified_rhino_activity = tvSpecifiedRhinoActivity.getText().toString();
                        identification_features = tvIdentificationFeatures.getText().toString();
                        date = tvDate.getText().toString();
                        total_no = tvTotalNo.getText().toString();
                        others = tvOthers.getText().toString();
                        male_number= tvmale_number.getText().toString();
                        female_number=tvfemale_number.getText().toString();
                        unknown_number= tvunknown_number.getText().toString();
                        adult_number= tvsub_adult_number.getText().toString();
                        sub_adult_number= tvsub_adult_number.getText().toString();
                        calf_cub_number= tvcalf_cub_number.getText().toString();
                        fund_tal = tvFundTal.getText().toString();
                        fund_community = tvFundCommunity.getText().toString();
                        fund_others = tvFundOthers.getText().toString();

                        if (!CheckValues.isFromSavedFrom) {
                            jsonLatLangArray = jsonArrayGPS.toString();
                        }

                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("Wildlife Sighting Details");

                        if (CheckValues.isFromSavedFrom) {
                            if (formNameSavedForm == null | formNameSavedForm.equals("")) {
                                FormNameToInput.setText("Wildlife Sighting Details");
                            } else {
                                FormNameToInput.setText(formNameSavedForm);
                            }
                        }

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
                                    String[] data = new String[]{"26", formName, dateDataCollected, jsonToSend, jsonLatLangArray, "" + imageName, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                                    dataBaseConserVationTracking.close();


                                    if (CheckValues.isFromSavedFrom) {

                                        DataBaseConserVationTracking dataBaseConserVationTracking1 = new DataBaseConserVationTracking(context);
                                        dataBaseConserVationTracking1.open();
                                        int updated_id = (int) dataBaseConserVationTracking1.updateTable_DeleteFlag(formid);
                                        dataBaseConserVationTracking.close();
                                    }

                                    new PromptDialog(WildlifeSightingDetails.this)
                                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                            .setAnimationEnable(true)
                                            .setTitleText(getString(R.string.dialog_success))
                                            .setContentText(getString(R.string.dialog_saved))
                                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                                @Override
                                                public void onClick(PromptDialog dialog) {
                                                    if (CheckValues.isFromSavedFrom) {
                                                        showDialog.dismiss();
                                                        startActivity(new Intent(WildlifeSightingDetails.this, SavedFormsActivity.class));
                                                        finish();
                                                    } else {
                                                        dialog.dismiss();
                                                    }                                                }
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


        startGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
            }

        });

        previewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckValues.isFromSavedFrom) {
                    StaticListOfCoordinates.setList(listCf);
                    startActivity(new Intent(WildlifeSightingDetails.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(WildlifeSightingDetails.this, MapPointActivity.class));
                    } else {
                        Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

                    }
                }
            }
        });
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
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int spinnerId = parent.getId();

        if (spinnerId == R.id.wmm_wildlife_sighting_management_detail_landscape) {
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
//                tvOtherLandscape.setText(other_landscape);
            } else {
                tvOtherLandscape.setVisibility(View.GONE);
            }
        }


        /*
        if (spinnerId == R.id.wmm_wildlife_sighting_management_detail_sexStatus) {
            switch (position) {
                case 0:
                    sex = "Male";
                    break;
                case 1:
                    sex = "Female";
                    break;
            }
        }
        */

        /*
        if (spinnerId == R.id.wmm_wildlife_sighting_management_detail_age) {
            switch (position) {
                case 0:
                    age = "Adult";
                    break;
                case 1:
                    age = "Sub-Adult";
                    break;
                case 2:
                    age = "Calf/Cub";
                    break;
            }
        }
        */

        if (spinnerId == R.id.wmm_wildlife_sighting_management_detail_habitat_typeStatus) {
            switch (position) {
                case 0:
                    habitat_type = "Sal Forest";
                    break;
                case 1:
                    habitat_type = "Rivarine Forest";
                    break;
                case 2:
                    habitat_type = "Mixed Forest";
                    break;
                case 3:
                    habitat_type = "Short Grassland";
                    break;
                case 4:
                    habitat_type = "Tall Grassland";
                    break;
            }
        }

        if (spinnerId == R.id.wmm_wildlife_sighting_management_detail_rhino_activity) {
            switch (position) {
                case 0:
                    rhino_activity = "Grazing";
                    tvSpecifiedRhinoActivity.setText("");
                    tvSpecifiedRhinoActivityLBL.setVisibility(View.GONE);
                    break;
                case 1:
                    rhino_activity = "Walking";
                    tvSpecifiedRhinoActivity.setText("");
                    tvSpecifiedRhinoActivityLBL.setVisibility(View.GONE);
                    break;
                case 2:
                    rhino_activity = "Wallowing";
                    tvSpecifiedRhinoActivity.setText("");
                    tvSpecifiedRhinoActivityLBL.setVisibility(View.GONE);
                    break;
                case 3:
                    rhino_activity = "Sleeping";
                    tvSpecifiedRhinoActivity.setText("");
                    tvSpecifiedRhinoActivityLBL.setVisibility(View.GONE);
                    break;
                case 4:
                    rhino_activity = "Resting";
                    tvSpecifiedRhinoActivity.setText("");
                    tvSpecifiedRhinoActivityLBL.setVisibility(View.GONE);
                    break;
                case 5:
                    rhino_activity = "Other with specify";
                    tvSpecifiedRhinoActivityLBL.setVisibility(View.VISIBLE);
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
                addImage();
//                Toast.makeText(getApplicationContext(),""+encodedImage,Toast.LENGTH_SHORT).show();
//                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//                    //FINE
//
//                }
//                else{
//                    //NOT IN REQUIRED FORMAT
//                }
            }
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                previewImageSite.setVisibility(View.VISIBLE);
                previewImageSite.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail);
                addImage();
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GEOPOINT_RESULT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String location = data.getStringExtra(LOCATION_RESULT);

                    String string = location;
                    String[] parts = string.split(" ");
                    String split_lat = parts[0]; // 004
                    String split_lon = parts[1]; // 034556

                    finalLat = Double.parseDouble(split_lat);
                    finalLong = Double.parseDouble(split_lon);

                    LatLng d = new LatLng(finalLat, finalLong);
                    //
                    listCf.add(d);
                    isGpsTaken = true;

                    if (!split_lat.equals("") && !split_lon.equals("")) {
                        GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED = true;
                        try {
                            JSONObject locationData = new JSONObject();
                            locationData.put("latitude", finalLat);
                            locationData.put("longitude", finalLong);

                            jsonArrayGPS.put(locationData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        previewMap.setEnabled(true);
                        startGps.setText("Location Recorded");
                    }


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

        imageName = "wildlife_sighting_" + timeInMillis + PhoneUtils.getFormatedId();

        File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
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

    public void addImage() {
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
            imageName = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");

            formid = (String) bundle.get("dbID");
            formNameSavedForm = (String) bundle.get("formName");

            String status = (String) bundle.get("status");
            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }

            Log.e("wildlife_sighting_", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path);

                addImage();
            }
            try {
                //new adjustment
                Log.e("wildlife_sighting_", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(WildlifeSightingDetails.this);
            gps.canGetLocation();
            startGps.setEnabled(true);
        }
    }


    //new adjustment
    public void parseArrayGPS(String arrayToParse) {
        try {
            jsonLatLangArray = arrayToParse;

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

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {

            post_dict.put("tablename", "tbl_wildlife_sighting");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("district", district);
            header.put("vdc", vdc);
            header.put("date", date);
            header.put("time", time);
            header.put("wildlife_spp", wildlife_spp);
            header.put("rhino_id", rhino_id);
            header.put("specified_rhino_activity", specified_rhino_activity);
            header.put("identification_features", identification_features);
//            header.put("sex", sex);
//            header.put("age", age);
            header.put("longitude", finalLong);
            header.put("latitude", finalLat);
            header.put("name_bz_forest_cf", name_bz_forest_cf);
            header.put("habitat_type", habitat_type);
//            header.put("rhino_activity", rhino_activity);
            header.put("others", others);
            header.put("male_number", male_number);
            header.put("female_number", female_number);
            header.put("unknown_number", unknown_number);
            header.put("adult_number", adult_number);
            header.put("sub_adult_number", sub_adult_number);
            header.put("calf_cub_number", calf_cub_number);
            header.put("total_no", total_no);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.e("convertDataToJsonSave", "convertDataToJsonSave: "+jsonToSend );

            photo_dict.put("photo", encodedImage);
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
        Log.e("PlantationDETAIL", "json : " + jsonOb.toString());
        String data = jsonOb.getString("formdata");
        Log.e("PlantationDETAIL", "formdata : " + jsonOb.toString());
        JSONObject jsonObj = new JSONObject(data);
        Log.e("PlantationDETAIL", "json : " + jsonObj.toString());

        projectCode = jsonObj.getString("project_code");
        landscape = jsonObj.getString("landscape");
        agreement_no = jsonObj.getString("agreement_no");
        grantee_name = jsonObj.getString("grantee_name");
        funding_source = jsonObj.getString("funding_source");
        fiscal_year = jsonObj.getString("fiscal_year");
        date = jsonObj.getString("date");
        time = jsonObj.getString("time");
        name_bz_forest_cf = jsonObj.getString("name_bz_forest_cf");
        district = jsonObj.getString("district");
        vdc = jsonObj.getString("vdc");
        wildlife_spp = jsonObj.getString("wildlife_spp");
        rhino_id = jsonObj.getString("rhino_id");
        specified_rhino_activity = jsonObj.getString("specified_rhino_activity");
        identification_features = jsonObj.getString("identification_features");
        habitat_type = jsonObj.getString("habitat_type");
        rhino_activity = jsonObj.getString("rhino_activity");
        others = jsonObj.getString("others");
        male_number= jsonObj.getString("male_number");
        female_number= jsonObj.getString("female_number");
        unknown_number= jsonObj.getString("unknown_number");
        adult_number= jsonObj.getString("adult_number");
        sub_adult_number= jsonObj.getString("sub_adult_number");
        calf_cub_number= jsonObj.getString("calf_cub_number");
        total_no = jsonObj.getString("total_no");
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));


        Log.e("Plantationdetail", "Parsed data " + agreement_no + grantee_name + fiscal_year);


        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvAgreement_no.setText(agreement_no);
        tvGrantew_name.setText(grantee_name);
        tvFiscal_year.setText(fiscal_year);
        tvDate.setText(date);
        tvTime.setText(time);
        tvNameOfz.setText(name_bz_forest_cf);
        tvDistrictname.setText(district);
        tvNameOfVdc.setText(vdc);
        tvWildlifeSpp.setText(wildlife_spp);
        tvRhinoID.setText(rhino_id);
        tvSpecifiedRhinoActivity.setText(specified_rhino_activity);
        tvIdentificationFeatures.setText(identification_features);
        tvTotalNo.setText(total_no);
        tvOthers.setText(others);
        tvmale_number.setText(male_number);
        tvfemale_number.setText(female_number);
        tvunknown_number.setText(unknown_number);
        tvadult_number.setText(adult_number);
        tvsub_adult_number.setText(sub_adult_number);
        tvcalf_cub_number.setText(calf_cub_number);
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

//        int sexPosition = spinSexAdpt.getPosition(sex);
//        spinnerSex.setSelection(sexPosition);
//
//        int singleSpinnerAdptPosition2 = spinAgeAdpt.getPosition(age);
//        spinnerAge.setSelection(singleSpinnerAdptPosition2);

        int habitPosition = spinHabitTypeAdpt.getPosition(habitat_type);
        spinnerHabitType.setSelection(habitPosition);

        int rhinoActivityPosition = spinRhinoActivityAdpt.getPosition(rhino_activity);
        spinnerRhinoActivity.setSelection(rhinoActivityPosition);

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
                    new PromptDialog(WildlifeSightingDetails.this)
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
                    String[] data = new String[]{"26", "Wildlife Sighting Details", dateString, jsonToSend, jsonLatLangArray, "" + imageName, "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(WildlifeSightingDetails.this)
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
                    .append("-").append(month).append("-").append(day)
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


        tvTime.setOnClickListener(new View.OnClickListener() {

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
