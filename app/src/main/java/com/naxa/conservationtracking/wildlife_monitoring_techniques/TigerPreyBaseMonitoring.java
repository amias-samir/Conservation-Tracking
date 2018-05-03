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
import android.content.IntentFilter;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtracking.GeoPointActivity;
import com.naxa.conservationtracking.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtracking.activities.MapPointActivity;
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

import javax.net.ssl.HttpsURLConnection;

import com.naxa.conservationtracking.database.DataBaseConserVationTracking;
import com.naxa.conservationtracking.dialog.Default_DIalog;
import com.naxa.conservationtracking.model.CheckValues;
import com.naxa.conservationtracking.model.Constants;
import com.naxa.conservationtracking.model.StaticListOfCoordinates;

import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class TigerPreyBaseMonitoring extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, habitatType;
    ArrayAdapter landscapeAdpt, habitatTypeAdapter;
    Button send, save, startGps, previewMap, btnAddSegment;
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
    String district_name;
    String vdc_name;
    String observer_name;
    String block_no;
    String grid_no;
    String transect_no;
    String transect_bearing;
    String location;
    String weather;
    String date;
    String time_start;
    String time_end;
    String species;
    String male;
    String female;
    String young;
    String total_no;
    String animal_bearing;
    String angular_sighting_distance;
    String habitat_type;
    String remarks;
    String fund_tal, fund_community, fund_others;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps, tvNoOfSegmentAdded;
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvDistrictname, tvNameOfVdc, tvobserver_name, tvblock_no, tvProjectCode,
            tvgrid_no, tvtransect_no, tvtransect_bearing, tvlocation, tvweather, tvdate, tvtime_start, tvtime_end, tvspecies,
            tvmale, tvfemale, tvyoung, tvtotal_no, tvanimal_bearing, tvangular_sighting_distance, tvNotes, tvFundTal, tvFundCommunity, tvFundOthers;
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

    List<String> speciesSegmentList = new ArrayList<>();
    public String all_segment, no_of_segment_added = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmt_tiger_prey_base_monitoring);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.tiger_prey_base_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_detail_fundingSource);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_Vdc_Municipality_Name);
        tvobserver_name = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_observer_name);
        tvblock_no = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_block_no);
        tvgrid_no = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_grid_no);
        tvtransect_no = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_transect_no);
        tvtransect_bearing = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_transect_bearing);
        tvlocation = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_location);
        tvweather = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_weather);

        btnAddSegment = (Button) findViewById(R.id.tiger_prey_base_add_segment_btn);
        tvNoOfSegmentAdded = (TextView) findViewById(R.id.tiger_prey_base_segment_added_no);


//        tvspecies = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_species);
//        tvmale = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_male);
//        tvfemale = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_female);
//        tvyoung = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_young);
//        tvtotal_no = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_total_no);
//        tvanimal_bearing = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_animal_bearing);
//        tvangular_sighting_distance = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_angular_sighting_distance);

        tvNotes = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_remarks);
        habitatType = (Spinner) findViewById(R.id.tiger_prey_base_habitat_type);
        tvdate = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_date);
        tvtime_start = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_time_start);
        tvtime_end = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_time_end);
        setCurrentDateOnView();
        addListenerOnButton();
        setCurrentTimeOnView();
        setCurrentTimeOnView();
        addListenerOnTimeButton();
        tvFundTal = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.tiger_prey_base_FundOthers);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        habitatTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.HABITAT_TYPE);
        habitatTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitatType.setAdapter(habitatTypeAdapter);
        habitatType.setOnItemSelectedListener(this);

        send = (Button) findViewById(R.id.tiger_prey_base_send);
        save = (Button) findViewById(R.id.tiger_prey_base_save);
        startGps = (Button) findViewById(R.id.tiger_prey_base_GpsStart);
        previewMap = (Button) findViewById(R.id.tiger_prey_base_preview_map);
        previewMap.setEnabled(false);

        initilizeUI();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isGpsTracking) {
                    Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
                } else {

                    if (isGpsTaken) {
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);

                        showDialog.setContentView(R.layout.login_layout);
                        final EditText userName = (EditText) showDialog.findViewById(R.id.input_userName);
                        final EditText password = (EditText) showDialog.findViewById(R.id.input_password);

                        AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
                        showDialog.setTitle("Authentication");
                        showDialog.setCancelable(true);
                        showDialog.show();
                        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                        logIn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                String userN = userName.getText().toString();
                                String passW = password.getText().toString();
                                if (userN == null || userN.equals("") || passW == null || passW.equals("")) {
                                    Toast.makeText(context, "Either your user name or password is empty.Please fill the required field. ", Toast.LENGTH_SHORT).show();
                                } else {
                                    showDialog.dismiss();

                                    projectCode = tvProjectCode.getText().toString();
                                    other_landscape = tvOtherLandscape.getText().toString();
                                    funding_source = tvFundingSource.getText().toString();
                                    district_name = tvDistrictname.getText().toString();
                                    vdc_name = tvNameOfVdc.getText().toString();
                                    observer_name = tvobserver_name.getText().toString();
                                    block_no = tvblock_no.getText().toString();
                                    grid_no = tvgrid_no.getText().toString();
                                    transect_no = tvtransect_no.getText().toString();
                                    transect_bearing = tvtransect_bearing.getText().toString();
                                    location = tvlocation.getText().toString();
                                    weather = tvweather.getText().toString();
                                    date = tvdate.getText().toString();
                                    time_start = tvtime_start.getText().toString();
                                    time_end = tvtime_end.getText().toString();


                                    no_of_segment_added = tvNoOfSegmentAdded.getText().toString();
//                                    species = tvspecies.getText().toString();
//                                    male = tvmale.getText().toString();
//                                    female = tvfemale.getText().toString();
//                                    young = tvyoung.getText().toString();
//                                    total_no = tvtotal_no.getText().toString();
//                                    animal_bearing = tvanimal_bearing.getText().toString();
//                                    angular_sighting_distance = tvangular_sighting_distance.getText().toString();
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
                            }
                        });
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
                        block_no = tvblock_no.getText().toString();
                        grid_no = tvgrid_no.getText().toString();
                        transect_no = tvtransect_no.getText().toString();
                        transect_bearing = tvtransect_bearing.getText().toString();
                        location = tvlocation.getText().toString();
                        weather = tvweather.getText().toString();
                        date = tvdate.getText().toString();
                        time_start = tvtime_start.getText().toString();
                        time_end = tvtime_end.getText().toString();


                        no_of_segment_added = tvNoOfSegmentAdded.getText().toString();
//                        species = tvspecies.getText().toString();
//                        male = tvmale.getText().toString();
//                        female = tvfemale.getText().toString();
//                        young = tvyoung.getText().toString();
//                        total_no = tvtotal_no.getText().toString();
//                        animal_bearing = tvanimal_bearing.getText().toString();
//                        angular_sighting_distance = tvangular_sighting_distance.getText().toString();
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
                        FormNameToInput.setText("Tiger Prey Base Monitoring");

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
                                    String[] data = new String[]{"86", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(TigerPreyBaseMonitoring.this)
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
                    startActivity(new Intent(TigerPreyBaseMonitoring.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(TigerPreyBaseMonitoring.this, MapPointActivity.class));
                    } else {
                        Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

                    }
                }
            }
        });

        btnAddSegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddSegmentSeenDialog();
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

        if (spinnerId == R.id.tiger_prey_base_detail_landscape) {
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

        if (spinnerId == R.id.tiger_prey_base_habitat_type) {
            switch (position) {
                case 0:
                    habitat_type = "SF - Sal com.naxa.conservationtracking.forest";
                    break;
                case 1:
                    habitat_type = "MF - Mixed Forest";
                    break;
                case 2:
                    habitat_type = "RF -  Riverine Forest";
                    break;
                case 3:
                    habitat_type = "TG - Tall Grassland";
                    break;
                case 4:
                    habitat_type = "SG – Short Grassland";
                    break;
                case 5:
                    habitat_type = "W-Wetland";
                    break;
                case 6:
                    habitat_type = "S - Streambed";
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

        imageName = "tiger_prey_base_" + timeInMillis;

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

    //    public void addImage(){
//        File file1 = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES),imageName);
//        String path=file1.toString();
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        options.inSampleSize = 1;
//        options.inPurgeable = true;
//        Bitmap bm = BitmapFactory.decodeFile( path ,options);
////        Bitmap bm = BitmapFactory.decodeFile( imagePath ,options);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        bm.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
//
//
//        // bitmap object
//
//        byte[] byteImage_photo = baos.toByteArray();
//
//        //generate base64 string of image
//        encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
//        Log.e("IMAGE STRING" ,"-"+encodedImage);
//
//    }
    public void initilizeUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            CheckValues.isFromSavedFrom = true;
            startGps.setEnabled(false);
            isGpsTaken = true;
            previewMap.setEnabled(true);
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            imageName = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }

            Log.e("PLANYTATIONDETAIL", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), imageName);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path);

                // addImage();
            }
            try {
                //new adjustment
                Log.e("tiger_prey_base", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(TigerPreyBaseMonitoring.this);
            gps.canGetLocation();
            startGps.setEnabled(true);
//            endGps.setEnabled(false);
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

    private void RegisterAlarmBroadcast() {
        mReceiver = new BroadcastReceiver() {
            // private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent) {
                gps = new GPS_TRACKER_FOR_POINT(TigerPreyBaseMonitoring.this);
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
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_tiger_prey_base_monitoring");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("observer_name", observer_name);
            header.put("block_no", block_no);
            header.put("grid_no", grid_no);
            header.put("transect_no", transect_no);
            header.put("latitude", finalLat);
            header.put("longitude", finalLong);
            header.put("transect_bearing", transect_bearing);
            header.put("location", location);
            header.put("weather", weather);
            header.put("date", date);
            header.put("habitat_type", habitat_type);
            header.put("time_start", time_start);
            header.put("time_end", time_end);


            header.put("no_of_segment_added", no_of_segment_added);
            header.put("all_species", all_segment);
//            header.put("species", species);
//            header.put("male", male);
//            header.put("female", female);
//            header.put("young", young);
//            header.put("total_no", total_no);
//            header.put("animal_bearing", animal_bearing);
//            header.put("angular_sighting_distance", angular_sighting_distance);
            header.put("others", remarks);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.e("ConvertDataToJSON", "BeforeSending " + jsonToSend);


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
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        observer_name = jsonObj.getString("observer_name");
        block_no = jsonObj.getString("block_no");
        grid_no = jsonObj.getString("grid_no");
        transect_no = jsonObj.getString("transect_no");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        transect_bearing = jsonObj.getString("transect_bearing");
        location = jsonObj.getString("location");
        weather = jsonObj.getString("weather");
        date = jsonObj.getString("date");
        habitat_type = jsonObj.getString("habitat_type");
        time_start = jsonObj.getString("time_start");
        time_end = jsonObj.getString("time_end");


        no_of_segment_added = jsonObj.getString("no_of_segment_added");
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
//        species = jsonObj.getString("species");
//        male = jsonObj.getString("male");
//        female = jsonObj.getString("female");
//        young = jsonObj.getString("young");
//        total_no = jsonObj.getString("total_no");
//        animal_bearing = jsonObj.getString("animal_bearing");
//        angular_sighting_distance = jsonObj.getString("angular_sighting_distance");
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        remarks = jsonObj.getString("others");


        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvobserver_name.setText(observer_name);
        tvblock_no.setText(block_no);
        tvgrid_no.setText(grid_no);
        tvtransect_no.setText(transect_no);
        tvtransect_bearing.setText(transect_bearing);
        tvlocation.setText(location);
        tvweather.setText(weather);
        tvdate.setText(date);
        tvtime_start.setText(time_start);
        tvtime_end.setText(time_end);


        tvNoOfSegmentAdded.setText(no_of_segment_added);
//        tvspecies.setText(species);
//        tvmale.setText(male);
//        tvfemale.setText(female);
//        tvyoung.setText(young);
//        tvtotal_no.setText(total_no);
//        tvanimal_bearing.setText(animal_bearing);
//        tvangular_sighting_distance.setText(angular_sighting_distance);
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


        int forConPos = habitatTypeAdapter.getPosition(habitat_type);
        habitatType.setSelection(forConPos);

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
                    new PromptDialog(TigerPreyBaseMonitoring.this)
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
                    String[] data = new String[]{"86", "Tiger Prey Base Monitoring", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(TigerPreyBaseMonitoring.this)
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


        tvdate.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvdate.setShowSoftInputOnFocus(false);
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


        tvtime_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }

        });
        tvtime_end.setOnClickListener(new View.OnClickListener() {

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


        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;


            tvtime_end.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));


        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private void showAddSegmentSeenDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.tiger_prey_base_add_species_segment_layout);
        final GPS_TRACKER_FOR_POINT gps1 = null;
        final Button startGps1, previewMap1;

        Button btnOk = (Button) showDialog.findViewById(R.id.tiger_prey_base_dialog_segment_add_btn);

        tvspecies = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_species);
        tvmale = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_male);
        tvfemale = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_female);
        tvyoung = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_young);
        tvtotal_no = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_total_no);
        tvanimal_bearing = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_animal_bearing);
        tvangular_sighting_distance = (AutoCompleteTextView) showDialog.findViewById(R.id.tiger_prey_base_angular_sighting_distance);


        showDialog.setTitle("ADD SPECIES SEEN");
        showDialog.getActionBar();
        showDialog.show();
        showDialog.getWindow().setLayout((width), LinearLayout.LayoutParams.WRAP_CONTENT);


        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                species = tvspecies.getText().toString();
                male = tvmale.getText().toString();
                female = tvfemale.getText().toString();
                young = tvyoung.getText().toString();
                total_no = tvtotal_no.getText().toString();
                animal_bearing = tvanimal_bearing.getText().toString();
                angular_sighting_distance = tvangular_sighting_distance.getText().toString();

                JSONObject memberObj = new JSONObject();
                try {

                    memberObj.put("species", species);
                    memberObj.put("male", male);
                    memberObj.put("female", female);
                    memberObj.put("young", young);
                    memberObj.put("total_no", total_no);
                    memberObj.put("animal_bearing", animal_bearing);
                    memberObj.put("angular_sighting_distance", angular_sighting_distance);

                    Log.e("segment added : ", memberObj.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speciesSegmentList.add(memberObj.toString());
                all_segment = speciesSegmentList.toString();
                Log.e("all_species: ", all_segment);


                tvNoOfSegmentAdded.setText(speciesSegmentList.size() + " Species added");
                showDialog.dismiss();
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

}
