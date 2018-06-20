package com.naxa.conservationtracking.wildlife_monitoring_techniques;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.naxa.conservationtracking.GeoPointActivity;
import com.naxa.conservationtracking.R;
import com.naxa.conservationtracking.activities.GPS_TRACKER_FOR_POINT;
import com.naxa.conservationtracking.activities.MapPointActivity;
import com.naxa.conservationtracking.database.DataBaseConserVationTracking;
import com.naxa.conservationtracking.dialog.Default_DIalog;
import com.naxa.conservationtracking.model.CheckValues;
import com.naxa.conservationtracking.model.Constants;
import com.naxa.conservationtracking.model.StaticListOfCoordinates;

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

public class ScatCollectionDetailsActivity extends AppCompatActivity {

    @BindView(R.id.scat_collection_details_project_code)
    AutoCompleteTextView tvProjectCode;
    private String TAG = "ScatCollectionDetailsActivity";

    @BindView(R.id.scat_collection_details_name_of_collector)
    AutoCompleteTextView tvNameOfCollector;
    @BindView(R.id.scat_collection_details_block_name)
    AutoCompleteTextView tvBlockName;
    @BindView(R.id.scat_collection_details_grid_no)
    AutoCompleteTextView tvGridNo;
    @BindView(R.id.scat_collection_details_date)
    AutoCompleteTextView tvDate;
    @BindView(R.id.scat_collection_details_sample_id)
    AutoCompleteTextView tvSampleId;
    @BindView(R.id.scat_collection_details_diameter)
    AutoCompleteTextView tvDiameter;
    @BindView(R.id.scat_collection_details_segmentation)
    Spinner spinnerSegmentation;
    @BindView(R.id.scat_collection_details_age)
    Spinner spinnerAge;
    @BindView(R.id.scat_collection_details_site_type)
    Spinner spinnerSiteType;
    @BindView(R.id.scat_collection_details_GpsStart)
    Button btnGpsStart;
    @BindView(R.id.scat_collection_details_preview_map)
    Button btnPreviewMap;
    @BindView(R.id.scat_collection_details_elevation)
    AutoCompleteTextView tvElevation;
    @BindView(R.id.scat_collection_details_associated_sign)
    Spinner spinnerAssociatedSign;
    @BindView(R.id.scat_collection_details_comments)
    AutoCompleteTextView tvComments;
    @BindView(R.id.scat_collection_details_save)
    Button btnSave;
    @BindView(R.id.scat_collection_details_send)
    Button btnSend;
    @BindView(R.id.scat_collection_details_confidencce)
    AutoCompleteTextView tvConfidencce;


    Toolbar toolbar;

    ProgressDialog mProgressDlg;
    Context context = this;
    String jsonToSend, photoTosend;
    String imagePath, encodedImage = null, imageName = "no_photo";

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    GPS_TRACKER_FOR_POINT gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    double finalLat;
    double finalLong;

    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    String latLangArray = "", jsonLatLangArray = "";

    String dataSentStatus = "", dateString;
    String userNameToSend, passwordToSend;
    JSONArray jsonArrayGPS = new JSONArray();

    ArrayAdapter segmentationAdpt, ageAdpt, siteTypeAdpt, associatedSignAdpt;

    String KEY_COLLECTOR_NAME = "collector_name",
            KEY_BLOCK_NAME = "block_name",
            KEY_GRID_NO = "grid_no",
            KEY_DATE = "date",
            KEY_SAMPLE_ID = "sample_id",
            KEY_DIMAETER = "diameter",
            KEY_SEGMENTATION = "segmentation",
            KEY_AGE = "age",
            KEY_SITE_TYPE = "site_type",
            KEY_ELEVATION = "elevation",
            KEY_FINAL_LAT = "latitude",
            KEY_FINAL_LONG = "longitude",
            KEY_ASSOCITED_SIGN = "associated_sign",
            KEY_CONFIDENCE = "confidence",
            KEY_COMMENTS = "comments",
            KEY_PROJECT_CODE = "project_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scat_collection_details);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setCurrentDateOnView();
        addListenerOnButton();

//        Segmentation spinner
        segmentationAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SCAT_COLLECTION_SEGMENTATION);
        segmentationAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSegmentation.setAdapter(segmentationAdpt);
//        spinnerSegmentation.setOnItemSelectedListener(this);

//        Age spinner
        ageAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SCAT_COLLECTION_AGE);
        ageAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setAdapter(ageAdpt);
//        spinnerAge.setOnItemSelectedListener(this);

//        site type spinner
        siteTypeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SCAT_COLLECTION_SITE_TYPE);
        siteTypeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSiteType.setAdapter(siteTypeAdpt);
//        spinnerSiteType.setOnItemSelectedListener(this);

//        associated sign spinner
        associatedSignAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.WMT_SCAT_COLLECTION_ASSOCIATED_SIGN);
        associatedSignAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociatedSign.setAdapter(associatedSignAdpt);
//        spinnerAssociatedSign.setOnItemSelectedListener(this);


        btnPreviewMap.setEnabled(false);

        initilizeUI();
    }


    @OnClick({R.id.scat_collection_details_date, R.id.scat_collection_details_GpsStart, R.id.scat_collection_details_preview_map, R.id.scat_collection_details_save, R.id.scat_collection_details_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scat_collection_details_date:
                break;

            case R.id.scat_collection_details_GpsStart:
                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
                break;

            case R.id.scat_collection_details_preview_map:
                mapPREVIEW();
                break;

            case R.id.scat_collection_details_save:
                saveFORMDATA();
                break;

            case R.id.scat_collection_details_send:
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

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month,
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
                    .append("/").append(month + 1).append("/").append(day)
                    .append(""));
        }
    };


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
            startActivity(new Intent(ScatCollectionDetailsActivity.this, MapPointActivity.class));
        } else {

            if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(ScatCollectionDetailsActivity.this, MapPointActivity.class));
            } else {
                Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

            }
        }
    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_scat_collect");

            JSONObject header = new JSONObject();

            header.put(KEY_PROJECT_CODE, tvProjectCode.getText().toString());
            header.put(KEY_COLLECTOR_NAME, tvNameOfCollector.getText().toString());
            header.put(KEY_BLOCK_NAME, tvBlockName.getText().toString());
            header.put(KEY_GRID_NO, tvGridNo.getText().toString());
            header.put(KEY_DATE, tvDate.getText().toString());
            header.put(KEY_SAMPLE_ID, tvSampleId.getText().toString());
            header.put(KEY_DIMAETER, tvDiameter.getText().toString());

            header.put(KEY_SEGMENTATION, spinnerSegmentation.getSelectedItem().toString());
            header.put(KEY_AGE, spinnerAge.getSelectedItem().toString());
            header.put(KEY_SITE_TYPE, spinnerSiteType.getSelectedItem().toString());

            header.put(KEY_FINAL_LAT, finalLat);
            header.put(KEY_FINAL_LONG, finalLong);
            header.put(KEY_ELEVATION, tvElevation.getText().toString());

            header.put(KEY_ASSOCITED_SIGN, spinnerAssociatedSign.getSelectedItem().toString());

            header.put(KEY_CONFIDENCE, tvConfidencce.getText().toString());
            header.put(KEY_COMMENTS, tvComments.getText().toString());


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
                FormNameToInput.setText("Scat Collection Details");

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
                            String[] data = new String[]{"88", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                    "" + imageName, "Not Sent", "0"};

                            DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                            dataBaseConserVationTracking.open();
                            long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                            new PromptDialog(ScatCollectionDetailsActivity.this)
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
                Log.e(TAG, "doInBackground: "+text );
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
                    new PromptDialog(ScatCollectionDetailsActivity.this)
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
                    String[] data = new String[]{"88", "Scat Collection Details", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(ScatCollectionDetailsActivity.this)
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
                Log.e("scat_collection_details", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(ScatCollectionDetailsActivity.this);
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

        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));

        tvProjectCode.setText(jsonObj.getString(KEY_PROJECT_CODE));
        tvNameOfCollector.setText(jsonObj.getString(KEY_COLLECTOR_NAME));
        tvBlockName.setText(jsonObj.getString(KEY_BLOCK_NAME));
        tvGridNo.setText(jsonObj.getString(KEY_GRID_NO));
        tvDate.setText(jsonObj.getString(KEY_DATE));
        tvSampleId.setText(jsonObj.getString(KEY_SAMPLE_ID));
        tvDiameter.setText(jsonObj.getString(KEY_DIMAETER));
        tvElevation.setText(jsonObj.getString(KEY_ELEVATION));
        tvConfidencce.setText(jsonObj.getString(KEY_CONFIDENCE));
        tvComments.setText(jsonObj.getString(KEY_COMMENTS));


        //set spinner
        int segmentationPos = segmentationAdpt.getPosition(jsonObj.getString(KEY_SEGMENTATION));
        spinnerSegmentation.setSelection(segmentationPos);

        int agePos = ageAdpt.getPosition(jsonObj.getString(KEY_AGE));
        spinnerAge.setSelection(agePos);

        int siteTypePos = siteTypeAdpt.getPosition(jsonObj.getString(KEY_SITE_TYPE));
        spinnerSiteType.setSelection(siteTypePos);

        int associatedSignPos = associatedSignAdpt.getPosition(jsonObj.getString(KEY_ASSOCITED_SIGN));
        spinnerAssociatedSign.setSelection(associatedSignPos);
    }


}
