package com.naxa.conservationtrackingapp.wildlife_monitoring_techniques;

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
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.net.ssl.HttpsURLConnection;

import com.naxa.conservationtrackingapp.activities.SavedFormsActivity;
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
public class TigerTrappingDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "TigerTrappingDetail";
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, habitatType, terrian, spinnerSlope, spinnerAspect;
    ArrayAdapter landscapeAdpt, habitatTypeAdapter, terrianAdapter, slopeAdpt, aspectAdpt;
    Button send, save, startGps, previewMap;
    ProgressDialog mProgressDlg;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    String jsonToSend, photoTosend;

    String formNameSavedForm, formid;


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
    String district_name;
    String vdc_name;
    String block_id;
    String grid_id;
    String group_no;
    String team_leader;
    String group_members;
    String deployed_date;
    String ended_date;
    String camera_id;
    String site_name;
    String elevation;
    String habitat_type;
    String land_terrian;
    String slope;
    String aspect;
    String remarks;
    String fund_tal, fund_community, fund_others;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvDistrictname, tvNameOfVdc, tvblock_id, tvgrid_id, tvgroup_no, tvProjectCode,
            tvteam_leader, tvgroup_members, tvdeployed_date, tvended_date, tvcamera_id, tvsite_name, tvelevation,
            tvNotes, tvFundTal, tvFundCommunity, tvFundOthers;
    String userNameToSend, passwordToSend;

//    CheckBox cbSafeHeaven;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 11;
    static final int DATE_DIALOG_ID1 = 12;
    String dataSentStatus = "", dateString;

    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    boolean isPaused = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmt_tiger_camera_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckValues.isFromSavedFrom = false;


        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.tiger_camera_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_fundingSource);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_Vdc_Municipality_Name);
        tvblock_id = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_block_id);
        tvgrid_id = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_grid_id);
        tvgroup_no = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_group_no);
        tvteam_leader = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_team_leader);
        tvgroup_members = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_group_member);
        tvcamera_id = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_camera_id);
        tvsite_name = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_site_name);
        tvelevation = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_elevation);
        tvdeployed_date = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_deployed_date);
        tvended_date = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_ended_date);
        habitatType = (Spinner) findViewById(R.id.tiger_camera_detail_habitat_type);
        terrian = (Spinner) findViewById(R.id.tiger_camera_detail_terrain);
        spinnerSlope = (Spinner) findViewById(R.id.tiger_camera_detail_slope);
        spinnerAspect = (Spinner) findViewById(R.id.tiger_camera_detail_aspect);
        tvNotes = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_remarks);

        setCurrentDateOnView();
        addListenerOnButton();

        tvFundTal = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.tiger_camera_detail_FundOthers);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        habitatTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.CAMERA_TRAPPING_HABITAT_TYPE);
        habitatTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitatType.setAdapter(habitatTypeAdapter);
        habitatType.setOnItemSelectedListener(this);

        terrianAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.TERRAIN);
        terrianAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        terrian.setAdapter(terrianAdapter);
        terrian.setOnItemSelectedListener(this);

        slopeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.SLOPE);
        slopeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSlope.setAdapter(slopeAdpt);
        spinnerSlope.setOnItemSelectedListener(this);

        aspectAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.ASPECT);
        aspectAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAspect.setAdapter(aspectAdpt);
        spinnerAspect.setOnItemSelectedListener(this);

        send = (Button) findViewById(R.id.tiger_camera_detail_send);
        save = (Button) findViewById(R.id.tiger_camera_detail_save);
        startGps = (Button) findViewById(R.id.tiger_camera_detail_GpsStart);
        previewMap = (Button) findViewById(R.id.tiger_camera_detail_preview_map);
        previewMap.setEnabled(false);

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
                                    district_name = tvDistrictname.getText().toString();
                                    vdc_name = tvNameOfVdc.getText().toString();
                                    block_id = tvblock_id.getText().toString();
                                    grid_id = tvgrid_id.getText().toString();
                                    group_no = tvgroup_no.getText().toString();
                                    team_leader = tvteam_leader.getText().toString();
                                    group_members = tvgroup_members.getText().toString();
                                    deployed_date = tvdeployed_date.getText().toString();
                                    ended_date = tvended_date.getText().toString();
                                    camera_id = tvcamera_id.getText().toString();
                                    site_name = tvsite_name.getText().toString();
                                    elevation = tvelevation.getText().toString();
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
                        block_id = tvblock_id.getText().toString();
                        grid_id = tvgrid_id.getText().toString();
                        group_no = tvgroup_no.getText().toString();
                        team_leader = tvteam_leader.getText().toString();
                        group_members = tvgroup_members.getText().toString();
                        deployed_date = tvdeployed_date.getText().toString();
                        ended_date = tvended_date.getText().toString();
                        camera_id = tvcamera_id.getText().toString();
                        site_name = tvsite_name.getText().toString();
                        elevation = tvelevation.getText().toString();
                        remarks = tvNotes.getText().toString();
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
                        FormNameToInput.setText("Camera Trapping Details");

                        if (CheckValues.isFromSavedFrom) {
                            if (formNameSavedForm == null | formNameSavedForm.equals("")) {
                                FormNameToInput.setText("Camera Trapping Details");
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
                                    String[] data = new String[]{"85", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "", "Not Sent", "0"};

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

                                    new PromptDialog(TigerTrappingDetail.this)
                                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                            .setAnimationEnable(true)
                                            .setTitleText(getString(R.string.dialog_success))
                                            .setContentText(getString(R.string.dialog_saved))
                                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                                @Override
                                                public void onClick(PromptDialog dialog) {
                                                    if (CheckValues.isFromSavedFrom) {
                                                        showDialog.dismiss();
                                                        startActivity(new Intent(TigerTrappingDetail.this, SavedFormsActivity.class));
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

        //Edited for GPS
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
                    startActivity(new Intent(TigerTrappingDetail.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(TigerTrappingDetail.this, MapPointActivity.class));
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

        if (spinnerId == R.id.tiger_camera_detail_landscape) {
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

        if (spinnerId == R.id.tiger_camera_detail_habitat_type) {
            switch (position) {
                case 0:
                    habitat_type = "SF-Sal Forest";
                    break;
                case 1:
                    habitat_type = "MF-Mixed Forest";
                    break;
                case 2:
                    habitat_type = "RF-Riverine Forest";
                    break;
                case 3:
                    habitat_type = "TG-Tall Grassland";
                    break;
                case 4:
                    habitat_type = "SG–Short Grassland";
                    break;
                case 5:
                    habitat_type = "W-Wetland";
                    break;
                case 6:
                    habitat_type = "S-Streambed";
                    break;
                case 7:
                    habitat_type = "Alpine Grassland";
                    break;
                    case 8:
                    habitat_type = "Shurbland";
                    break;
                    case 9:
                    habitat_type = "Barren Land";
                    break;
                    case 10:
                    habitat_type = "Sub Alpine Forest";
                    break;
            }
        }
        if (spinnerId == R.id.tiger_camera_detail_terrain) {
            switch (position) {
                case 0:
                    land_terrian = "Hilly";
                    break;
                case 1:
                    land_terrian = "Flat";
                    break;
                case 2:
                    land_terrian = "Streambed";
                    break;
            }
        }
        if (spinnerId == R.id.tiger_camera_detail_slope) {
            switch (position) {
                case 0:
                    slope = "0-30";
                    break;
                case 1:
                    slope = "30-60";
                    break;
                case 2:
                    slope = "60-90";
                    break;
            }
        }
        if (spinnerId == R.id.tiger_camera_detail_aspect) {
            switch (position) {
                case 0:
                    aspect = "NE";
                    break;
                case 1:
                    aspect = "NW";
                    break;
                case 2:
                    aspect = "SE";
                    break;
                case 3:
                    aspect = "SW";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

            formid = (String) bundle.get("dbID");
            formNameSavedForm = (String) bundle.get("formName");

            String status = (String) bundle.get("status");
            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }


            try {
                //new adjustment
                Log.e("tiger_camera_detail", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(TigerTrappingDetail.this);
            gps.canGetLocation();
            startGps.setEnabled(true);
//            endGps.setEnabled(false);
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

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_tiger_camera_details");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("group_no", group_no);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("block_id", block_id);
            header.put("grid_id", grid_id);
            header.put("team_leader", team_leader);
            header.put("group_members", group_members);
            header.put("latitude", finalLat);
            header.put("longitude", finalLong);
            header.put("deployed_date", deployed_date);
            header.put("ended_date", ended_date);
            header.put("camera_id", camera_id);
            header.put("site_name", site_name);
            header.put("habitat_type", habitat_type);
            header.put("elevation", elevation);
            header.put("terrain", land_terrian);
            header.put("slope", slope);
            header.put("aspect", aspect);
            header.put("others", remarks);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.d(TAG, "convertDataToJsonSave: "+jsonToSend);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("LATTI", "" + finalLat);
            editor.putString("LONGI", "" + finalLong);
            editor.apply();

            //   photo_dict.put("data", encodedImage );

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
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        block_id = jsonObj.getString("block_id");
        grid_id = jsonObj.getString("grid_id");
        group_no = jsonObj.getString("group_no");
        team_leader = jsonObj.getString("team_leader");
        group_members = jsonObj.getString("group_members");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        deployed_date = jsonObj.getString("deployed_date");
        ended_date = jsonObj.getString("ended_date");
        camera_id = jsonObj.getString("camera_id");
        site_name = jsonObj.getString("site_name");
        habitat_type = jsonObj.getString("habitat_type");
        elevation = jsonObj.getString("elevation");
        land_terrian = jsonObj.getString("terrain");
        slope = jsonObj.getString("slope");
        aspect = jsonObj.getString("aspect");
        remarks = jsonObj.getString("others");
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvblock_id.setText(block_id);
        tvgrid_id.setText(grid_id);
        tvgroup_no.setText(group_no);
        tvteam_leader.setText(team_leader);
        tvgroup_members.setText(group_members);
        tvdeployed_date.setText(deployed_date);
        tvended_date.setText(ended_date);
        tvcamera_id.setText(camera_id);
        tvsite_name.setText(site_name);
        tvelevation.setText(elevation);
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

        //set spinner
        int forConPos = habitatTypeAdapter.getPosition(habitat_type);
        habitatType.setSelection(forConPos);

        int forTypePos = terrianAdapter.getPosition(land_terrian);
        terrian.setSelection(forTypePos);

        int setslope = slopeAdpt.getPosition(slope);
        spinnerSlope.setSelection(setslope);

        int setaspect = aspectAdpt.getPosition(aspect);
        spinnerAspect.setSelection(setaspect);
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
                    new PromptDialog(TigerTrappingDetail.this)
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
                    String[] data = new String[]{"85", "Camera Details", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(TigerTrappingDetail.this)
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

    }


    public void addListenerOnButton() {


        tvdeployed_date.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvdeployed_date.setShowSoftInputOnFocus(false);
                }
                showDialog(DATE_DIALOG_ID);
            }

        });

        tvended_date.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvended_date.setShowSoftInputOnFocus(false);
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
            tvdeployed_date.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(""));

        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when com.naxa.conservationtracking.dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;


            tvended_date.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(""));
        }
    };
}
