package com.naxa.conservationtrackingapp.wildlife_monitoring_techniques;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtrackingapp.GeoPointActivity;
import com.naxa.conservationtrackingapp.R;
import com.naxa.conservationtrackingapp.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtrackingapp.activities.MapPointActivity;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;

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

import javax.net.ssl.HttpsURLConnection;

import Utls.UserNameAndPasswordUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;
/**
 * Created by samir on 1/28/2018.
 */
public class SnowLeopardPreyBaseMonitoringActivity extends AppCompatActivity {

    @BindView(R.id.snow_leopard_prey_base_project_code)
    AutoCompleteTextView tvProjectCode;
    private String TAG = "ScatCollectionDetailsActivity";

    @BindView(R.id.snow_leopard_prey_base_observer_name)
    AutoCompleteTextView tvObserverName;
    @BindView(R.id.snow_leopard_prey_base_date)
    AutoCompleteTextView tvDate;
    @BindView(R.id.snow_leopard_prey_base_group_no)
    AutoCompleteTextView tvGroupNo;
    @BindView(R.id.snow_leopard_prey_base_observer_no)
    AutoCompleteTextView tvObserverNo;
    @BindView(R.id.snow_leopard_prey_base_destination)
    AutoCompleteTextView tvDestination;
    @BindView(R.id.snow_leopard_prey_base_start_time)
    AutoCompleteTextView tvStartTime;
    @BindView(R.id.snow_leopard_prey_base_end_time)
    AutoCompleteTextView tvEndTime;
    @BindView(R.id.snow_leopard_prey_base_GpsStart)
    Button btnGpsStart;
    @BindView(R.id.snow_leopard_prey_base_preview_map)
    Button btnPreviewMap;
    @BindView(R.id.scat_collection_details_elevation)
    AutoCompleteTextView tvElevation;
    @BindView(R.id.snow_leopard_prey_base_district)
    AutoCompleteTextView tvDistrict;
    @BindView(R.id.snow_leopard_prey_base_rural_municipality)
    AutoCompleteTextView tvRuralMunicipality;
    @BindView(R.id.snow_leopard_prey_base_block)
    AutoCompleteTextView tvBlock;
    @BindView(R.id.snow_leopard_prey_base_use_of_grazing_land)
    Spinner spinnerUseOfGrazingLand;
    @BindView(R.id.snow_leopard_prey_base_grid_id)
    AutoCompleteTextView tvGridId;
    @BindView(R.id.snow_leopard_prey_base_site)
    AutoCompleteTextView tvSite;
    @BindView(R.id.snow_leopard_prey_base_female_no)
    AutoCompleteTextView tvFemaleNo;
    @BindView(R.id.snow_leopard_prey_base_young_no)
    AutoCompleteTextView tvYoungNo;
    @BindView(R.id.snow_leopard_prey_base_yearling_no)
    AutoCompleteTextView tvYearlingNo;
    @BindView(R.id.snow_leopard_prey_base_young_male_no)
    AutoCompleteTextView tvYoungMaleNo;
    @BindView(R.id.snow_leopard_prey_base_sub_adult_male_no)
    AutoCompleteTextView tvSubAdultMaleNo;
    @BindView(R.id.snow_leopard_prey_base_adult_male_no)
    AutoCompleteTextView tvAdultMaleNo;
    @BindView(R.id.snow_leopard_prey_base_unidentified_no)
    AutoCompleteTextView tvUnidentifiedNo;
    @BindView(R.id.snow_leopard_prey_base_total_numbers)
    AutoCompleteTextView tvTotalNumbers;
    @BindView(R.id.snow_leopard_prey_base_habitat_type)
    Spinner spinnerHabitatType;
    @BindView(R.id.snow_leopard_prey_base_aspect)
    Spinner spinnerAspect;
    @BindView(R.id.snow_leopard_prey_base_distance_to_cliff)
    AutoCompleteTextView tvDistanceToCliff;
    @BindView(R.id.snow_leopard_prey_base_slope)
    Spinner spinnerSlope;
    @BindView(R.id.snow_leopard_prey_base_save)
    Button btnSave;
    @BindView(R.id.snow_leopard_prey_base_send)
    Button btnSend;


    Toolbar toolbar;

    ProgressDialog mProgressDlg;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    String jsonToSend, photoTosend;
    String imagePath, encodedImage = null, imageName = "no_photo";

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 11;
    static final int TIME_DIALOG_ID1 = 12;


    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";

    String dataSentStatus = "", dateString;
    String userNameToSend, passwordToSend;
    JSONArray jsonArrayGPS = new JSONArray();

    ArrayAdapter useOfGrazingLandAdpt, habitatTypeAdpt, aspectAdpt, slopeAdpt;

    String KEY_OBSERVER_NAME = "observer_name",
            KEY_PROJECT_CODE = "project_code",
            KEY_DATE = "date",
            KEY_GROUP_NO = "group_no",
            KEY_OBSERVER_NO = "observer_no",
            KEY_DESTINATION = "destination",
            KEY_START_TIME = "start_time",
            KEY_END_TIME = "end_time",

    KEY_FINAL_LAT = "latitude",
            KEY_FINAL_LONG = "longitude",
            KEY_ELEVATION = "elevation",

    KEY_DISTRICT = "district",
            KEY_RURAL_MUNICIPALITY = "rural_municipality",
            KEY_BLOCK = "block",
            KEY_USE_OF_GRAZING_LAND = "use_of_grazing_land",
            KEY_GRID_ID = "grid_id",

    KEY_SITE = "site",
            KEY_FEMALE_NO = "female_no",
            KEY_YOUNG_NO = "young_no",
            KEY_YEARLING_NO = "yearling_no",
            KEY_YOUNG_MALE_NO = "young_male_no",
            KEY_SUB_ADULT_MALE_NO = "sub_adult_male_no",
            KEY_ADULT_MALE_NO = "adult_male_no",
            KEY_UNIDENTIFIED_NO = "unidentified_no",
            KEY_TOTAL_NO = "total_no",
            KEY_HABITAT_TYPE = "habitat_type",
            KEY_ASPECT = "aspect",
            KEY_DISTANCE_TO_CLIFF = "distance_to_cliff",
            KEY_SLOPE = "slope";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snow_leopard_prey_base_monitoring);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        set date and time
        setCurrentDateOnView();
        setStarTimeOnView();
        setEndTimeOnView();
        addListenerOnButton();

        //        useOfGrazingLandAdpt spinner
        useOfGrazingLandAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SNOW_LEOPARD_USE_OF_GRAZING_LAND);
        useOfGrazingLandAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUseOfGrazingLand.setAdapter(useOfGrazingLandAdpt);

        //        habitat spinner
        habitatTypeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SNOW_LEOPARD_HABITAT_TYPE);
        habitatTypeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHabitatType.setAdapter(habitatTypeAdpt);

        //        aspect spinner
        aspectAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SNOW_LEOPARD_ASPECT);
        aspectAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAspect.setAdapter(aspectAdpt);

        //        slope spinner
        slopeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SNOW_LEOPARD_SLOPE);
        slopeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSlope.setAdapter(slopeAdpt);

        btnPreviewMap.setEnabled(false);

//        initialize UI
        initilizeUI();

    }

    @OnClick({R.id.snow_leopard_prey_base_GpsStart, R.id.snow_leopard_prey_base_preview_map, R.id.snow_leopard_prey_base_save, R.id.snow_leopard_prey_base_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.snow_leopard_prey_base_GpsStart:
                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
                break;

            case R.id.snow_leopard_prey_base_preview_map:
                mapPREVIEW();
                break;

            case R.id.snow_leopard_prey_base_save:
                saveFORMDATA();
                break;

            case R.id.snow_leopard_prey_base_send:
                sendFORMDATA();
                break;
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


        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID1);
            }
        });

        tvTotalNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countTotalSnowLeopardNumber();
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
            tvDate.setText(new StringBuilder().append(year)
                    .append("/").append(month + 1).append("/").append(day)
                    .append(""));
        }
    };

    // Time picker code

    // display current time
    public void setStarTimeOnView() {

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        // set current time into textview
        tvStartTime.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)));
    }

    // display current time
    public void setEndTimeOnView() {

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        // set current time into textview
        tvEndTime.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)));
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
            tvStartTime.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            tvEndTime.setText(new StringBuilder().append(pad(hour))
                    .append(":").append(pad(minute)));
        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
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

        if (requestCode == GEOPOINT_RESULT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String location = data.getStringExtra(LOCATION_RESULT);

                    String string = location;
                    String[] parts = string.split(" ");
                    String split_lat = parts[0]; // 004
                    String split_lon = parts[1]; // 034556
                    String split_altitude = parts[2]; // altitude

                    finalLat = Double.parseDouble(split_lat);
                    finalLong = Double.parseDouble(split_lon);

                    LatLng d = new LatLng(finalLat, finalLong);
                    //
                    listCf.add(d);
                    if (!split_lat.equals("") && !split_lon.equals("")) {

                        isGpsTaken = true;

                        GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED = true;
                        try {
                            JSONObject locationData = new JSONObject();
                            locationData.put(KEY_FINAL_LAT, finalLat);
                            locationData.put(KEY_FINAL_LONG, finalLong);

                            jsonArrayGPS.put(locationData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tvElevation.setText(split_altitude);
                        btnPreviewMap.setEnabled(true);
                        btnGpsStart.setText("Location Recorded");
                    }
                    //                    Toast.makeText(this.context, location, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void mapPREVIEW() {

        if (CheckValues.isFromSavedFrom) {
            StaticListOfCoordinates.setList(listCf);
            startActivity(new Intent(SnowLeopardPreyBaseMonitoringActivity.this, MapPointActivity.class));
        } else {

            if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(SnowLeopardPreyBaseMonitoringActivity.this, MapPointActivity.class));
            } else {
                Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

            }
        }
    }

    public void countTotalSnowLeopardNumber() {
        int female_no, young_no, yearling_no, young_male_no, sub_adult_male_no, adult_male_no, unidentified_no, total_no = 0;

        if (TextUtils.isEmpty(tvFemaleNo.getText().toString())) {
            female_no = 0;
        } else {
            female_no = Integer.parseInt(tvFemaleNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvYoungNo.getText().toString())) {
            young_no = 0;
        } else {
            young_no = Integer.parseInt(tvYoungNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvYearlingNo.getText().toString())) {
            yearling_no = 0;
        } else {
            yearling_no = Integer.parseInt(tvYearlingNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvYoungMaleNo.getText().toString())) {
            young_male_no = 0;
        } else {
            young_male_no = Integer.parseInt(tvYoungMaleNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvSubAdultMaleNo.getText().toString())) {
            sub_adult_male_no = 0;
        } else {
            sub_adult_male_no = Integer.parseInt(tvSubAdultMaleNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvAdultMaleNo.getText().toString())) {
            adult_male_no = 0;
        } else {
            adult_male_no = Integer.parseInt(tvAdultMaleNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvUnidentifiedNo.getText().toString())) {
            unidentified_no = 0;
        } else {
            unidentified_no = Integer.parseInt(tvUnidentifiedNo.getText().toString());
        }

        total_no = female_no + young_no + yearling_no + young_male_no + sub_adult_male_no + adult_male_no + unidentified_no;
        tvTotalNumbers.setText("" + total_no);

    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_snow_leopard_prey_base");

            JSONObject header = new JSONObject();

            header.put(KEY_PROJECT_CODE, tvProjectCode.getText().toString());
            header.put(KEY_OBSERVER_NAME, tvObserverName.getText().toString());
            header.put(KEY_DATE, tvDate.getText().toString());
            header.put(KEY_GROUP_NO, tvGroupNo.getText().toString());
            header.put(KEY_OBSERVER_NO, tvObserverNo.getText().toString());
            header.put(KEY_DESTINATION, tvDestination.getText().toString());
            header.put(KEY_START_TIME, tvStartTime.getText().toString());
            header.put(KEY_END_TIME, tvEndTime.getText().toString());

            header.put(KEY_FINAL_LAT, finalLat);
            header.put(KEY_FINAL_LONG, finalLong);
            header.put(KEY_ELEVATION, tvElevation.getText().toString());

            header.put(KEY_DISTRICT, tvDistrict.getText().toString());
            header.put(KEY_RURAL_MUNICIPALITY, tvRuralMunicipality.getText().toString());
            header.put(KEY_BLOCK, tvBlock.getText().toString());
            header.put(KEY_USE_OF_GRAZING_LAND, spinnerUseOfGrazingLand.getSelectedItem().toString());
            header.put(KEY_GRID_ID, tvGridId.getText().toString());

            header.put(KEY_SITE, tvSite.getText().toString());
            header.put(KEY_FEMALE_NO, tvFemaleNo.getText().toString());
            header.put(KEY_YOUNG_NO, tvYoungNo.getText().toString());
            header.put(KEY_YEARLING_NO, tvYearlingNo.getText().toString());
            header.put(KEY_YOUNG_MALE_NO, tvYoungMaleNo.getText().toString());
            header.put(KEY_SUB_ADULT_MALE_NO, tvSubAdultMaleNo.getText().toString());
            header.put(KEY_ADULT_MALE_NO, tvAdultMaleNo.getText().toString());
            header.put(KEY_UNIDENTIFIED_NO, tvUnidentifiedNo.getText().toString());
            header.put(KEY_TOTAL_NO, tvTotalNumbers.getText().toString());
            header.put(KEY_HABITAT_TYPE, spinnerHabitatType.getSelectedItem().toString());
            header.put(KEY_ASPECT, spinnerAspect.getSelectedItem().toString());
            header.put(KEY_DISTANCE_TO_CLIFF, tvDistanceToCliff.getText().toString());
            header.put(KEY_SLOPE, spinnerSlope.getSelectedItem().toString());


            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.d(TAG, "convertDataToJsonSave: " + jsonToSend);

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

    public void saveFORMDATA() {
        if (isGpsTracking) {
            Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
        } else {
            if (isGpsTaken) {
                jsonLatLangArray = jsonArrayGPS.toString();

                convertDataToJson();

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                final Dialog showDialog = new Dialog(context);
                showDialog.setContentView(R.layout.date_input_layout);
                final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                FormNameToInput.setText("Snow Leopard Prey Base Monitoring");

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
                            String[] data = new String[]{"89", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                    "" + imageName, "Not Sent", "0"};

                            DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                            dataBaseConserVationTracking.open();
                            long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                            new PromptDialog(SnowLeopardPreyBaseMonitoringActivity.this)
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

    public void sendFORMDATA() {
        if (isGpsTracking) {
            Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
        } else {

            if (isGpsTaken) {
//                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//                int width = metrics.widthPixels;
//                int height = metrics.heightPixels;
//
//                final Dialog showDialog = new Dialog(context);
//
//                showDialog.setContentView(R.layout.login_layout);
//                final EditText userName = (EditText) showDialog.findViewById(R.id.input_userName);
//                final EditText password = (EditText) showDialog.findViewById(R.id.input_password);
//
//                AppCompatButton logIn = (AppCompatButton) showDialog.findViewById(R.id.login_button);
//                showDialog.setTitle("Authentication");
//                showDialog.setCancelable(true);
//                showDialog.show();
//                showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                logIn.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
                String userN = UserNameAndPasswordUtils.getUserNameAndPassword(context).get(0);
                String passW = UserNameAndPasswordUtils.getUserNameAndPassword(context).get(1);
                        if (userN == null || userN.equals("") || passW == null || passW.equals("")) {
                            Toast.makeText(context, "Either your user name or password is empty.Please fill the required field. ", Toast.LENGTH_SHORT).show();
                        } else {
//                            showDialog.dismiss();

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
//                    }
//                });
            } else {
                Toast.makeText(getApplicationContext(), "You need to take at least one gps cooordinate", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void sendDatToserver() {
        if (jsonToSend.length() > 0) {
            Log.e("Inside Send", "BeforeSending " + jsonToSend);

            RestApii restApii = new RestApii();
            restApii.execute();
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
                Log.e(TAG, "doInBackground: "+text );
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
                    new PromptDialog(SnowLeopardPreyBaseMonitoringActivity.this)
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
                    String[] data = new String[]{"89", "Snow Leopard Prey Base Monitoring", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(SnowLeopardPreyBaseMonitoringActivity.this)
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


    public void initilizeUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            CheckValues.isFromSavedFrom = true;
            btnGpsStart.setEnabled(false);
            isGpsTaken = true;
            btnPreviewMap.setEnabled(true);
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            String gpsLocationtoParse = (String) bundle.get("gps");


            try {
                //new adjustment
                Log.e("snow_leopard_prey_base", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(SnowLeopardPreyBaseMonitoringActivity.this);
            gps.canGetLocation();
            btnGpsStart.setEnabled(true);
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
                LatLng location = new LatLng(item1.getDouble(KEY_FINAL_LAT), item1.getDouble(KEY_FINAL_LONG));

                listCf.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseJson(String jsonToParse) throws JSONException {
        JSONObject jsonOb = new JSONObject(jsonToParse);
        Log.e(TAG, "json : " + jsonOb.toString());
        String data = jsonOb.getString("formdata");
        Log.e(TAG, "formdata : " + jsonOb.toString());
        JSONObject jsonObj = new JSONObject(data);
        Log.e(TAG, "json : " + jsonObj.toString());

        finalLat = Double.parseDouble(jsonObj.getString(KEY_FINAL_LAT));
        finalLong = Double.parseDouble(jsonObj.getString(KEY_FINAL_LONG));
        tvElevation.setText(jsonObj.getString(KEY_ELEVATION));

        tvProjectCode.setText(jsonObj.getString(KEY_PROJECT_CODE));
        tvObserverName.setText(jsonObj.getString(KEY_OBSERVER_NAME));
        tvDate.setText(jsonObj.getString(KEY_DATE));
        tvGridId.setText(jsonObj.getString(KEY_GRID_ID));
        tvGroupNo.setText(jsonObj.getString(KEY_GROUP_NO));
        tvObserverNo.setText(jsonObj.getString(KEY_OBSERVER_NO));
        tvDestination.setText(jsonObj.getString(KEY_DESTINATION));
        tvStartTime.setText(jsonObj.getString(KEY_START_TIME));
        tvEndTime.setText(jsonObj.getString(KEY_END_TIME));

        tvDistrict.setText(jsonObj.getString(KEY_DISTRICT));
        tvRuralMunicipality.setText(jsonObj.getString(KEY_RURAL_MUNICIPALITY));
        tvBlock.setText(jsonObj.getString(KEY_BLOCK));
        tvGridId.setText(jsonObj.getString(KEY_GRID_ID));

        tvSite.setText(jsonObj.getString(KEY_SITE));
        tvFemaleNo.setText(jsonObj.getString(KEY_FEMALE_NO));
        tvYoungNo.setText(jsonObj.getString(KEY_YOUNG_NO));
        tvYearlingNo.setText(jsonObj.getString(KEY_YEARLING_NO));
        tvYoungMaleNo.setText(jsonObj.getString(KEY_YOUNG_MALE_NO));
        tvSubAdultMaleNo.setText(jsonObj.getString(KEY_SUB_ADULT_MALE_NO));
        tvAdultMaleNo.setText(jsonObj.getString(KEY_ADULT_MALE_NO));
        tvUnidentifiedNo.setText(jsonObj.getString(KEY_UNIDENTIFIED_NO));
        tvTotalNumbers.setText(jsonObj.getString(KEY_TOTAL_NO));
        tvDistanceToCliff.setText(jsonObj.getString(KEY_DISTANCE_TO_CLIFF));

        //set spinner
        int useOfGrazingLandPos = useOfGrazingLandAdpt.getPosition(jsonObj.getString(KEY_USE_OF_GRAZING_LAND));
        spinnerUseOfGrazingLand.setSelection(useOfGrazingLandPos);

        int habitatPos = habitatTypeAdpt.getPosition(jsonObj.getString(KEY_HABITAT_TYPE));
        spinnerHabitatType.setSelection(habitatPos);

        int aspectPos = aspectAdpt.getPosition(jsonObj.getString(KEY_ASPECT));
        spinnerAspect.setSelection(aspectPos);

        int slopePos = slopeAdpt.getPosition(jsonObj.getString(KEY_SLOPE));
        spinnerSlope.setSelection(slopePos);
    }
}
