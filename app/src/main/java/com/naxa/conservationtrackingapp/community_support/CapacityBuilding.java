package com.naxa.conservationtrackingapp.community_support;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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

import com.naxa.conservationtrackingapp.application.ApplicationClass;
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
public class CapacityBuilding extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, spinnerActivity;
    ArrayAdapter landscapeAdpt, singleSpinnerAdpt;
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
    String district;
    String vdc;
    String activity_type;
    String other_activity;
    String location;
    String date_start;
    String date_end;
    String participant_male;
    String participant_female;
    String participant_total;
    String affiliation_government;
    String affiliation_civil_society;
    String affiliation_others;
    String funding_tal;
    String funding_community;
    String others;
    String fund_others;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    AutoCompleteTextView tvOtherActivity, tvOtherLandscape, tvFundingSource, tvProjectCode, tvAgreement_no, tvGrantew_name, tvFiscal_year,
            tvDistrictname, tvNameOfVdc, tvDateStart, tvDateEnd,
            tvLocation, tvParticipantMale, tvParticipantFemale, tvParticipantTotal, tvAffiliationGovernment,
            tvAffiliationCivilSociety, tvAffiliationOthers, tvFundingTal, tvFundingCommunity, tvOthers,
            tvFundOthers;

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 11;
    static final int DATE_DIALOG_ID1 = 12;
    String dataSentStatus = "", dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_cb);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.cs_cb_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_fundingSource);
        tvAgreement_no = (AutoCompleteTextView) findViewById(R.id.cs_cb_Agreement_No);
        tvGrantew_name = (AutoCompleteTextView) findViewById(R.id.cs_cb_Grantee_Name);
        tvFiscal_year = (AutoCompleteTextView) findViewById(R.id.cs_cb_fiscal_Year);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_Vdc_Municipality_Name);
        spinnerActivity = (Spinner) findViewById(R.id.cs_cb_spinner_detail_typeStatus);
        tvOtherActivity = (AutoCompleteTextView) findViewById(R.id.cs_cb_otheractivity);
        tvLocation = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_LocationtName);
        tvParticipantMale = (AutoCompleteTextView) findViewById(R.id.cs_cb_male);
        tvParticipantFemale = (AutoCompleteTextView) findViewById(R.id.cs_cb_female);
        tvParticipantTotal = (AutoCompleteTextView) findViewById(R.id.cs_cb_totalparticipant);
        tvAffiliationGovernment = (AutoCompleteTextView) findViewById(R.id.cs_cb_AffiliationGovt);
        tvAffiliationCivilSociety = (AutoCompleteTextView) findViewById(R.id.cs_cb_AffiliationCivilSociety);
        tvAffiliationOthers = (AutoCompleteTextView) findViewById(R.id.cs_cb_AffiliationOtherst);
        tvFundingTal = (AutoCompleteTextView) findViewById(R.id.cs_cb_tal);
        tvFundingCommunity = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_community_contribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_FundOthers);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.cs_cb_detail_notes);
        tvDateStart = (AutoCompleteTextView) findViewById(R.id.cs_cb_Startdate);
        tvDateEnd = (AutoCompleteTextView) findViewById(R.id.cs_cb_Enddate);
        setCurrentDateOnView();
        addListenerOnButton();

        startGps = (Button) findViewById(R.id.cs_cb_GpsStart);

        send = (Button) findViewById(R.id.cs_cb_detail_send);
        save = (Button) findViewById(R.id.cs_cb_detail_save);
        previewMap = (Button) findViewById(R.id.cs_cb_preview_map);
        previewMap.setEnabled(false);
        photo = (ImageButton) findViewById(R.id.cs_cb_detail_photo);
        previewImageSite = (ImageView) findViewById(R.id.cs_cb_detail_PhotographSiteimageViewPreview);
        previewImageSite.setVisibility(View.GONE);
//
        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        singleSpinnerAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.CS_CB_ACTIVITY_TYPE);
        singleSpinnerAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(singleSpinnerAdpt);
        spinnerActivity.setOnItemSelectedListener(this);
//
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
//                                String userN = userName.getText().toString();
//                                String passW = password.getText().toString();
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
                            district = tvDistrictname.getText().toString();
                            vdc = tvNameOfVdc.getText().toString();
                            other_activity = tvOtherActivity.getText().toString();
                            location = tvLocation.getText().toString();
                            date_start = tvDateStart.getText().toString();
                            date_end = tvDateEnd.getText().toString();
                            participant_male = tvParticipantMale.getText().toString();
                            participant_female = tvParticipantFemale.getText().toString();
                            participant_total = tvParticipantTotal.getText().toString();
                            affiliation_government = tvAffiliationGovernment.getText().toString();
                            affiliation_civil_society = tvAffiliationCivilSociety.getText().toString();
                            affiliation_others = tvAffiliationOthers.getText().toString();
                            funding_tal = tvFundingTal.getText().toString();
                            funding_community = tvFundingCommunity.getText().toString();
                            fund_others = tvFundOthers.getText().toString();
                            others = tvOthers.getText().toString();

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
                        district = tvDistrictname.getText().toString();
                        vdc = tvNameOfVdc.getText().toString();
                        other_activity = tvOtherActivity.getText().toString();
                        location = tvLocation.getText().toString();
                        date_start = tvDateStart.getText().toString();
                        date_end = tvDateEnd.getText().toString();
                        participant_male = tvParticipantMale.getText().toString();
                        participant_female = tvParticipantFemale.getText().toString();
                        participant_total = tvParticipantTotal.getText().toString();
                        affiliation_government = tvAffiliationGovernment.getText().toString();
                        affiliation_civil_society = tvAffiliationCivilSociety.getText().toString();
                        affiliation_others = tvAffiliationOthers.getText().toString();
                        funding_tal = tvFundingTal.getText().toString();
                        funding_community = tvFundingCommunity.getText().toString();
                        fund_others = tvFundOthers.getText().toString();
                        others = tvOthers.getText().toString();

                        jsonLatLangArray = jsonArrayGPS.toString();
//
                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("Capacity Building");

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
                                    String[] data = new String[]{"66", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName, "Not Sent", "0"};

                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(CapacityBuilding.this)
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
                    startActivity(new Intent(CapacityBuilding.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(CapacityBuilding.this, MapPointActivity.class));
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

        if (spinnerId == R.id.cs_cb_detail_landscape) {
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


        if (spinnerId == R.id.cs_cb_spinner_detail_typeStatus) {
            switch (position) {
                case 0:
                    activity_type = "Training";
                    break;
                case 1:
                    activity_type = "Orientation";
                    break;
                case 2:
                    activity_type = "Workshop";
                    break;
                case 3:
                    activity_type = "Meeting";
                    break;
                case 4:
                    activity_type = "Study Tour";
                    break;
                case 5:
                    activity_type = "Others";
                    break;

            }
            if (spinnerActivity.getItemAtPosition(position).toString().equals("Others")) {
                tvOtherActivity.setVisibility(View.VISIBLE);
            } else {
                tvOtherActivity.setVisibility(View.GONE);
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

        imageName = "Capacity_building_" + timeInMillis + PhoneUtils.getFormatedId();

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
            isGpsTaken = true;
            previewMap.setEnabled(true);
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            imageName = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if (status.equals("Sent")) {
                save.setEnabled(false);
                send.setEnabled(false);
            }

            Log.e("PLANYTATIONDETAIL", "i-" + imageName);

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
                Log.e("plantation_detail", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(CapacityBuilding.this);
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
                gps = new GPS_TRACKER_FOR_POINT(CapacityBuilding.this);
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

            post_dict.put("tablename", "tbl_capacity_building");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("district", district);
            header.put("vdc", vdc);
            header.put("activity_type", activity_type + ":  " + other_activity);
            header.put("location", location);
            header.put("date_start", date_start);
            header.put("date_end", date_end);
            header.put("participant_male", participant_male);
            header.put("participant_female", participant_female);
            header.put("participant_total", participant_total);
            header.put("affiliation_government", affiliation_government);
            header.put("affiliation_civil_society", affiliation_civil_society);
            header.put("affiliation_others", affiliation_others);
            header.put("funding_tal", funding_tal);
            header.put("funding_community", funding_community);
            header.put("fund_others", fund_others);
            header.put("others", others);

            header.put("longitude", finalLong);
            header.put("latitude", finalLat);

            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();
            Log.d("CapacityBuilding", "convertDataToJson: " + jsonToSend);

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
        funding_source = jsonObj.getString("funding_source");
        agreement_no = jsonObj.getString("agreement_no");
        grantee_name = jsonObj.getString("grantee_name");
        fiscal_year = jsonObj.getString("fiscal_year");
        district = jsonObj.getString("district");
        vdc = jsonObj.getString("vdc");
        activity_type = jsonObj.getString("activity_type");
        location = jsonObj.getString("location");
        date_start = jsonObj.getString("date_start");
        date_end = jsonObj.getString("date_end");
        participant_male = jsonObj.getString("participant_male");
        participant_female = jsonObj.getString("participant_female");
        participant_total = jsonObj.getString("participant_total");
        affiliation_government = jsonObj.getString("affiliation_government");
        affiliation_civil_society = jsonObj.getString("affiliation_civil_society");
        affiliation_others = jsonObj.getString("affiliation_others");
        funding_tal = jsonObj.getString("funding_tal");
        funding_community = jsonObj.getString("funding_community");
        fund_others = jsonObj.getString("fund_others");
        others = jsonObj.getString("others");

        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));

        Log.e("Plantationdetail", "Parsed data " + agreement_no + grantee_name + fiscal_year);


        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvAgreement_no.setText(agreement_no);
        tvGrantew_name.setText(grantee_name);
        tvFiscal_year.setText(fiscal_year);
        tvDistrictname.setText(district);
        tvNameOfVdc.setText(vdc);
        tvLocation.setText(location);
        tvDateStart.setText(date_start);
        tvDateEnd.setText(date_end);
        tvParticipantMale.setText(participant_male);
        tvParticipantFemale.setText(participant_female);
        tvParticipantTotal.setText(participant_total);
        tvAffiliationGovernment.setText(affiliation_government);
        tvAffiliationCivilSociety.setText(affiliation_civil_society);
        tvAffiliationOthers.setText(affiliation_others);
        tvFundingTal.setText(funding_tal);
        tvFundingCommunity.setText(funding_community);
        tvOthers.setText(others);
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

        String[] actions1 = activity_type.split(":  ");
        if (actions1[0].equals("Training") || actions1[0].equals("Orientation") ||
                actions1[0].equals("Workshop") || actions1[0].equals("Meeting") || actions1[0].equals("Study Tour")) {

            int singleSpinnerAdptPosition = singleSpinnerAdpt.getPosition(actions1[0]);
            spinnerActivity.setSelection(singleSpinnerAdptPosition);
            tvOtherActivity.setVisibility(View.GONE);

        } else {

            int singleSpinnerAdptPosition = singleSpinnerAdpt.getPosition(actions1[0]);
            spinnerActivity.setSelection(singleSpinnerAdptPosition);
            tvOtherActivity.setText(actions1[1]);

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
                    new PromptDialog(CapacityBuilding.this)
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
                    String[] data = new String[]{"66", "Capacity Building", dateString, jsonToSend, jsonLatLangArray,
                            "" + imageName, "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(CapacityBuilding.this)
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


        tvDateStart.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvDateStart.setShowSoftInputOnFocus(false);
                }
                showDialog(DATE_DIALOG_ID);
            }

        });

        tvDateEnd.setOnClickListener(new View.OnClickListener() {

            //            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvDateEnd.setShowSoftInputOnFocus(false);
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
            tvDateStart.setText(new StringBuilder().append(year)
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


            // set selected date into textview
            tvDateEnd.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(""));
        }
    };

}