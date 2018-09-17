package com.naxa.conservationtrackingapp.human_wildlife_conflict_management;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtrackingapp.GeoPointActivity;
import com.naxa.conservationtrackingapp.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtrackingapp.activities.MapPointActivity;
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

import javax.net.ssl.HttpsURLConnection;

import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;
import com.naxa.conservationtrackingapp.wildlife_monitoring_techniques.HumanDisturbance;

import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class HWCEndowmentFund extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "HWCEndowmentFund";

    Toolbar toolbar;
    //    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape;
    ArrayAdapter landscapeAdpt;
    Button send, save, startGps, previewMap;
    ProgressDialog mProgressDlg;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    String jsonToSend, photoTosend;
    String imagePath, encodedImage = null, imageName = "no_photo";
    ImageButton photo;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

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

    String projectCode;
    String landscape;
    String other_landscape;
    String funding_source;
    String agreement_no;
    String grantee_name;
    String fiscal_year;
    String bzuc_cfcc_name;
    String district_name;
    String vdc_name;
    double area_using_Gps;
    String establishment_year;
    String fund_tal;
    String fund_bzuc_cfugs;
    String total_fund;
    String support_hwc_endowment;
//    String reason;
    String post_total_fund;
    String total_fund_cumulative;
    String fund_others;
    String remarks;

    String death_no, injured_no, others_name, others_no ;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvProjectCode, tvAgreement_no, tvGrantew_name, tvFiscal_year, tvBzuc_cfcc_name, tvDistrictname, tvNameOfVdc,
            tvEstablishment_year, tvFund_tal, tvFund_bzuc_cfugs, tvTotal_fund,
            tvSupport_hwc_endowment, tvPost_total_fund, tvFundOthers, tvTotal_fund_cumulative, tvNotes;
//    EditText tvReason ;

    EditText tvDeathNo, tvInjuredNo, tvOthersNo ;
    AutoCompleteTextView  tvOthersName;

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    String dataSentStatus = "", dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hwc_endowment_fund);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.HWCEndoFundProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.hwc_endowment_fund_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_fundingSource);
        tvAgreement_no = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_Agreement_No);
        tvGrantew_name = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_Grantee_Name);
        tvFiscal_year = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_fiscal_Year);
        tvBzuc_cfcc_name = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_name_of_bzuc_cfcc);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_Vdc_Municipality_Name);
        tvEstablishment_year = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_establishment_year);
        tvFund_tal = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_tal);
        tvFund_bzuc_cfugs = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_bzuc_cfcc);
        tvTotal_fund = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_total_fund);
        tvSupport_hwc_endowment = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_support_through_hwc_fund);
//        tvReason = (EditText) findViewById(R.id.hwc_endowment_fund_others_name);
        tvPost_total_fund = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_post_total_fund);
        tvTotal_fund_cumulative = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_total_fund_in_cumulative);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_FundOthers);
        tvNotes = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_remarks);

        tvDeathNo = (EditText) findViewById(R.id.hwc_endowment_fund_death_no);
        tvInjuredNo = (EditText) findViewById(R.id.hwc_endowment_fund_injured_no);
        tvOthersName = (AutoCompleteTextView) findViewById(R.id.hwc_endowment_fund_others_name);
        tvOthersNo = (EditText) findViewById(R.id.hwc_endowment_fund_others_no);


        send = (Button) findViewById(R.id.hwc_endowment_fund_send);
        save = (Button) findViewById(R.id.hwc_endowment_fund_save);

        startGps = (Button) findViewById(R.id.hwc_endowment_fund_GpsStart);
        previewMap = (Button) findViewById(R.id.hwc_endowment_fund_preview_map);
        previewMap.setEnabled(false);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

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
                                    bzuc_cfcc_name = tvBzuc_cfcc_name.getText().toString();
                                    district_name = tvDistrictname.getText().toString();
                                    vdc_name = tvNameOfVdc.getText().toString();
                                    establishment_year = tvEstablishment_year.getText().toString();
                                    fund_tal = tvFund_tal.getText().toString();
                                    fund_bzuc_cfugs = tvFund_bzuc_cfugs.getText().toString();
                                    total_fund = tvTotal_fund.getText().toString();
                                    support_hwc_endowment = tvSupport_hwc_endowment.getText().toString();
//                                    reason = tvReason.getText().toString();
                                    post_total_fund = tvPost_total_fund.getText().toString();
                                    total_fund_cumulative = tvTotal_fund_cumulative.getText().toString();
                                    fund_others = tvFundOthers.getText().toString();
                                    remarks = tvNotes.getText().toString();

                                    death_no = tvDeathNo.getText().toString();
                                    injured_no = tvInjuredNo.getText().toString();
                                    others_name = tvOthersName.getText().toString();
                                    others_no = tvOthersNo.getText().toString();

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
                        agreement_no = tvAgreement_no.getText().toString();
                        grantee_name = tvGrantew_name.getText().toString();
                        fiscal_year = tvFiscal_year.getText().toString();
                        bzuc_cfcc_name = tvBzuc_cfcc_name.getText().toString();
                        district_name = tvDistrictname.getText().toString();
                        vdc_name = tvNameOfVdc.getText().toString();
                        establishment_year = tvEstablishment_year.getText().toString();
                        fund_tal = tvFund_tal.getText().toString();
                        fund_bzuc_cfugs = tvFund_bzuc_cfugs.getText().toString();
                        total_fund = tvTotal_fund.getText().toString();
                        support_hwc_endowment = tvSupport_hwc_endowment.getText().toString();
//                        reason = tvReason.getText().toString();
                        post_total_fund = tvPost_total_fund.getText().toString();
                        total_fund_cumulative = tvTotal_fund_cumulative.getText().toString();
                        fund_others = tvFundOthers.getText().toString();
                        remarks = tvNotes.getText().toString();

                        death_no = tvDeathNo.getText().toString();
                        injured_no = tvInjuredNo.getText().toString();
                        others_name = tvOthersName.getText().toString();
                        others_no = tvOthersNo.getText().toString();

                        jsonLatLangArray = jsonArrayGPS.toString();

                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("HWC Endowment Fund");

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
                                    String[] data = new String[]{"46", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(HWCEndowmentFund.this)
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

        previewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckValues.isFromSavedFrom) {
                    StaticListOfCoordinates.setList(listCf);
                    startActivity(new Intent(HWCEndowmentFund.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(HWCEndowmentFund.this, MapPointActivity.class));
                    } else {
                        Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

                    }
                }
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

        if (spinnerId == R.id.hwc_endowment_fund_landscape) {
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

                imagePath = filePath;

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
            startGps.setEnabled(false);
            isGpsTaken=true;
            previewMap.setEnabled(true);
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }

            try {
                //new adjustment
                Log.e("hwc_endowment_fund", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(HWCEndowmentFund.this);
            gps.canGetLocation();
            startGps.setEnabled(true);
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

    private void RegisterAlarmBroadcast() {
        mReceiver = new BroadcastReceiver() {
            // private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent) {
                // Toast.makeText(context, "Alarm time has been reached",
                // Toast.LENGTH_LONG).show();
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
                        listCf.add(d);
                        Log.e("latlang", " lat: " + finalLat + " long: "
                                + finalLong);
                        stringBuilder.append("[" + finalLat + "," + finalLong + "]" + ",");
                    }

                } else {
                    gps.showSettingsAlert();
                }

            }

        };

        registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(this, 0,
                new Intent("sample"), 0);
        alarmManager = (AlarmManager) (this
                .getSystemService(Context.ALARM_SERVICE));
    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_hwc_endowment_fund");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("bzuc_cfcc_name", bzuc_cfcc_name);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("establishment_year", establishment_year);
            header.put("fund_tal", fund_tal);
            header.put("fund_bzuc_cfugs", fund_bzuc_cfugs);
            header.put("total_fund", total_fund);
            header.put("latitude", finalLat);
            header.put("longitude", finalLong);
            header.put("support_hwc_endowment", support_hwc_endowment);
//            header.put("reason", reason);
            header.put("post_total_fund", post_total_fund);
            header.put("total_fund_cumulative", total_fund_cumulative);
            header.put("fund_others", fund_others);
            header.put("others", remarks);

            header.put("death_no", death_no);
            header.put("injured_no", injured_no);
            header.put("others_name", others_name);
            header.put("others_no", others_no);

            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.e(TAG, "convertDataToJson: "+jsonToSend.toString() );

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
        Log.e("HWCEndowmentFund", "json : " + jsonOb.toString());
        String data = jsonOb.getString("formdata");
        Log.e("HWCEndowmentFund", "formdata : " + jsonOb.toString());
        JSONObject jsonObj = new JSONObject(data);
        Log.e("HWCEndowmentFund", "json : " + jsonObj.toString());

        projectCode = jsonObj.getString("project_code");
        landscape = jsonObj.getString("landscape");
        funding_source = jsonObj.getString("funding_source");
        agreement_no = jsonObj.getString("agreement_no");
        grantee_name = jsonObj.getString("grantee_name");
        fiscal_year = jsonObj.getString("fiscal_year");
        bzuc_cfcc_name = jsonObj.getString("bzuc_cfcc_name");
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        establishment_year = jsonObj.getString("establishment_year");
        fund_tal = jsonObj.getString("fund_tal");
        fund_bzuc_cfugs = jsonObj.getString("fund_bzuc_cfugs");
        total_fund = jsonObj.getString("total_fund");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        support_hwc_endowment = jsonObj.getString("support_hwc_endowment");
//        reason = jsonObj.getString("reason");
        post_total_fund = jsonObj.getString("post_total_fund");
        total_fund_cumulative = jsonObj.getString("total_fund_cumulative");
        fund_others = jsonObj.getString("fund_others");
        remarks = jsonObj.getString("others");

        death_no = jsonObj.getString("death_no");
        injured_no = jsonObj.getString("injured_no");
        others_name = jsonObj.getString("others_name");
        others_no = jsonObj.getString("others_no");


        Log.e("HWCEndowmentFund", "Parsed data " + agreement_no + grantee_name + fiscal_year);

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvAgreement_no.setText(agreement_no);
        tvGrantew_name.setText(grantee_name);
        tvFiscal_year.setText(fiscal_year);
        tvBzuc_cfcc_name.setText(bzuc_cfcc_name);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvEstablishment_year.setText(establishment_year);
        tvFund_tal.setText(fund_tal);
        tvFund_bzuc_cfugs.setText(fund_bzuc_cfugs);
        tvTotal_fund.setText(total_fund);
        tvSupport_hwc_endowment.setText(support_hwc_endowment);
//        tvReason.setText(reason);
        tvPost_total_fund.setText(post_total_fund);
        tvTotal_fund_cumulative.setText(total_fund_cumulative);
        tvNotes.setText(remarks);
        tvFundOthers.setText(fund_others);

        tvDeathNo.setText(death_no);
        tvInjuredNo.setText(injured_no);
        tvOthersName.setText(others_name);
        tvOthersNo.setText(others_no);

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
                Log.e("HWC_ENDOME", "doInBackground: "+ jsonObject );
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
                    new PromptDialog(HWCEndowmentFund.this)
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
                    String[] data = new String[]{"46", "HWC Endowment Fund", dateString, jsonToSend, jsonLatLangArray,
                            "" + imageName, "Sent", "0"};
                    Log.e("HWC_ENDOMENT_FUND", "onPostExecute: "+ data );

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(HWCEndowmentFund.this)
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
