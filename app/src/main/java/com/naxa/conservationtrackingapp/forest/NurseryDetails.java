package com.naxa.conservationtrackingapp.forest;

import android.app.Activity;
import android.app.AlarmManager;
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
import android.os.Bundle;
import android.os.Environment;
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
import com.naxa.conservationtrackingapp.PhoneUtils;
import com.naxa.conservationtrackingapp.activities.CalculateAreaUsinGPS;
import com.naxa.conservationtrackingapp.activities.General_Form;
import com.naxa.conservationtrackingapp.activities.GpsTracker;
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

import com.naxa.conservationtrackingapp.activities.SavedFormsActivity;
import com.naxa.conservationtrackingapp.application.ApplicationClass;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;

import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class NurseryDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, spinnerSeedingRaised, spinnerGrassSpecies;
    ArrayAdapter landscapeAdpt, spinSeedingRaisedAdpt, spinGrassSpeciesAdpt;
    Button send, save, startGps, endGps, previewMap, btnAddSegment;
    ProgressDialog mProgressDlg;
    Context context = this;
    GpsTracker gps;
    String jsonToSend, photoTosend;
    String imagePath, encodedImage = null, imageName = "no_photo";
    ImageButton photo;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    ProgressBar tracking;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    ImageView previewImageSite;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";

    String formNameSavedForm, formid;

    ArrayList<String> selection = new ArrayList<String>();
    TextView final_text;
    EditText editText1, editText2, editText3, editText4;
    String edit_text1, edit_text2, edit_text3, edit_text4;
    boolean isCheckedOne = false, isCheckedTwo = false, isCheckedThree = false;

    String projectCode;
    String landscape;
    String other_landscape;
    String funding_source;
    String agreement_no;
    String grantee_name;
    String fiscal_year;
    String year_of_establishment;
    String district_name;
    String vdc_name;
    String institution_name;
    String bzug_name;
    String location_address;
    double distance;
    double area_using_Gps;
    String seedling_raised;
    String no;
    String grass_species;
    String other_species;
    String fund_tal, fund_community, fund_others;
    String others;

    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvDistance, tvBoundryUsingGps, tvNoOfSegmentAdded;
    AutoCompleteTextView tvOtherSpecies, tvOtherLandscape, tvFundingSource, tvYearOfEstablishment, tvProjectCode, tvAgreement_no, tvGrantew_name, tvFiscal_year, tvDistrictname, tvNameOfVdc,
            tvInstitutionName, tvBzugName, tvLocationAddress, tvNo, tvFundTal, tvFundCommunity, tvFundOthers, tvOthers;
    //checkbox selection
    public static TextView tvNameAndNumber;
    CardView rlNumberOfItems;
    public static String fromMultipleSelectionDialog = "";

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    String dataSentStatus = "", dateString;

    List<String> speciesSegmentList = new ArrayList<>();
    public String all_species, no_of_segment_added = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forest_nursery);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.NurseryProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.nursery_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.nursery_detail_fundingSource);
        tvAgreement_no = (AutoCompleteTextView) findViewById(R.id.nursery_detail_Agreement_No);
        tvGrantew_name = (AutoCompleteTextView) findViewById(R.id.nursery_detail_Grantee_Name);
        tvFiscal_year = (AutoCompleteTextView) findViewById(R.id.nursery_Detail_fiscal_Year);
        tvYearOfEstablishment = (AutoCompleteTextView) findViewById(R.id.nursery_detail_yearofestablishment);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.nursery_detail_DistrictName);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.nursery_detail_Vdc_Municipality_Name);
        tvInstitutionName = (AutoCompleteTextView) findViewById(R.id.nursery_detail_Institution);
        tvBzugName = (AutoCompleteTextView) findViewById(R.id.nursery_detail_nameof_BZUG);
        tvLocationAddress = (AutoCompleteTextView) findViewById(R.id.nursery_detail_LocationName);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.nursery_detail_notes);
        tvBoundryUsingGps = (TextView) findViewById(R.id.nursery_detail_boundry_using_gps);


//        spinnerSeedingRaised = (Spinner) findViewById(R.id.nursery_detail_spinner_SeddingRaised);
//        tvNo = (AutoCompleteTextView) findViewById(R.id.nursery_detail_No);

        btnAddSegment = (Button) findViewById(R.id.nursery_details_add_segment_btn);
        tvNoOfSegmentAdded = (TextView) findViewById(R.id.nursery_details_segment_added_no);

//        rlNumberOfItems = (CardView) findViewById(R.id.nursery_detail_spinner_GrassSpecies);
//        tvNameAndNumber = (TextView) findViewById(R.id.nursery_nameAndNumber);
//        tvOtherSpecies = (AutoCompleteTextView) findViewById(R.id.nursery_other_activity);

        tvFundTal = (AutoCompleteTextView) findViewById(R.id.nursery_detail_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.nursery_detail_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.nursery_detail_FundOthers);

        photo = (ImageButton) findViewById(R.id.nursery_detail_Photograph);
        previewImageSite = (ImageView) findViewById(R.id.nursery_detail_PhotographSiteimageViewPreview);
        previewImageSite.setVisibility(View.GONE);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

//        spinSeedingRaisedAdpt = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, Constants.NURSERY_SEEDING_RAISED);
//        spinSeedingRaisedAdpt
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSeedingRaised.setAdapter(spinSeedingRaisedAdpt);
//        spinnerSeedingRaised.setOnItemSelectedListener(this);

        send = (Button) findViewById(R.id.nursery_detail_send);
        save = (Button) findViewById(R.id.nursery_detail_save);

        tracking = (ProgressBar) findViewById(R.id.progressBar);
        tracking.setVisibility(View.INVISIBLE);

        startGps = (Button) findViewById(R.id.nursery_detail_GpsStart);
        endGps = (Button) findViewById(R.id.nursery_detail_GpsEnd);
        previewMap = (Button) findViewById(R.id.nursery_detail_preview_map);

        initilizeUI();

//        rlNumberOfItems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Multiple_Selection_Dialog_Nurserry1.multipleChoice_NurseryDetail(context, R.string.select_choices_nursery1, "Choose and Put Number ");
//            }
//        });

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
                                    year_of_establishment = tvYearOfEstablishment.getText().toString();
                                    district_name = tvDistrictname.getText().toString();
                                    vdc_name = tvNameOfVdc.getText().toString();
                                    institution_name = tvInstitutionName.getText().toString();
                                    bzug_name = tvBzugName.getText().toString();
                                    location_address = tvLocationAddress.getText().toString();

                                    no_of_segment_added = tvNoOfSegmentAdded.getText().toString();
//                                    grass_species = tvNameAndNumber.getText().toString();
//                                    other_species = tvOtherSpecies.getText().toString();
//                                    no = tvNo.getText().toString();
                                    fund_tal = tvFundTal.getText().toString();
                                    fund_community = tvFundCommunity.getText().toString();
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
                        year_of_establishment = tvYearOfEstablishment.getText().toString();
                        district_name = tvDistrictname.getText().toString();
                        vdc_name = tvNameOfVdc.getText().toString();
                        institution_name = tvInstitutionName.getText().toString();
                        bzug_name = tvBzugName.getText().toString();
                        location_address = tvLocationAddress.getText().toString();

                        no_of_segment_added = tvNoOfSegmentAdded.getText().toString();
//                        grass_species = tvNameAndNumber.getText().toString();
//                        other_species = tvOtherSpecies.getText().toString();
//                        no = tvNo.getText().toString();
                        fund_tal = tvFundTal.getText().toString();
                        fund_community = tvFundCommunity.getText().toString();
                        fund_others = tvFundOthers.getText().toString();
                        others = tvOthers.getText().toString();

                        if(!CheckValues.isFromSavedFrom) {
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
                        FormNameToInput.setText("Nursery Details");

                        if (CheckValues.isFromSavedFrom) {
                            if (formNameSavedForm == null | formNameSavedForm.equals("")) {
                                FormNameToInput.setText("Nursery Details");
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
                                    String[] data = new String[]{"2", formName, dateDataCollected, jsonToSend, jsonLatLangArray, "" + imageName, "Not Sent", "0"};

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

                                    new PromptDialog(NurseryDetails.this)
                                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                            .setAnimationEnable(true)
                                            .setTitleText(getString(R.string.dialog_success))
                                            .setContentText(getString(R.string.dialog_saved))
                                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                                @Override
                                                public void onClick(PromptDialog dialog) {
                                                    if (CheckValues.isFromSavedFrom) {
                                                        showDialog.dismiss();
                                                        startActivity(new Intent(NurseryDetails.this, SavedFormsActivity.class));
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

        startGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
            }
        });

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
                startActivity(new Intent(NurseryDetails.this, MapPolyLineActivity.class));
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

        if (spinnerId == R.id.nursery_detail_landscape) {
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

//        if (spinnerId == R.id.nursery_detail_spinner_SeddingRaised) {
//            switch (position) {
//                case 0:
//                    seedling_raised = "Amala";
//                    break;
//                case 1:
//                    seedling_raised = "Bans";
//                    break;
//                case 2:
//                    seedling_raised = "Bet";
//                    break;
//                case 3:
//                    seedling_raised = "Bel";
//                    break;
//                case 4:
//                    seedling_raised = "Amriso";
//                    break;
//                case 5:
//                    seedling_raised = "Harro";
//                    break;
//                case 6:
//                    seedling_raised = "Kurilo";
//                    break;
//                case 7:
//                    seedling_raised = "Nim";
//                    break;
//                case 8:
//                    seedling_raised = "Raktachandan";
//                    break;
//                case 9:
//                    seedling_raised = "Rittha";
//                    break;
//                case 10:
//                    seedling_raised = "Sarpagandha";
//                    break;
//                case 11:
//                    seedling_raised = "Tejpat";
//                    break;
//                case 12:
//                    seedling_raised = "Bakaino";
//                    break;
//                case 13:
//                    seedling_raised = "Badhahar";
//                    break;
//                case 14:
//                    seedling_raised = "Kavro";
//                    break;
//                case 15:
//                    seedling_raised = "Kimbu";
//                    break;
//                case 16:
//                    seedling_raised = "Koiralo";
//                    break;
//                case 17:
//                    seedling_raised = "Nimaro";
//                    break;
//                case 18:
//                    seedling_raised = "Ranki";
//                    break;
//                case 19:
//                    seedling_raised = "Asna";
//                    break;
//                case 20:
//                    seedling_raised = "Ashok";
//                    break;
//                case 21:
//                    seedling_raised = "Bijaysal";
//                    break;
//                case 22:
//                    seedling_raised = "Chiuri";
//                    break;
//                case 23:
//                    seedling_raised = "Dhupi";
//                    break;
//                case 24:
//                    seedling_raised = "Haldu";
//                    break;
//                case 25:
//                    seedling_raised = "Jamun";
//                    break;
//                case 26:
//                    seedling_raised = "Khayer";
//                    break;
//                case 27:
//                    seedling_raised = "Kusum";
//                    break;
//                case 28:
//                    seedling_raised = "Mahuwa";
//                    break;
//                case 29:
//                    seedling_raised = "Nimaro";
//                    break;
//                case 30:
//                    seedling_raised = "Rajbrikssha";
//                    break;
//                case 31:
//                    seedling_raised = "Satisal";
//                    break;
//                case 32:
//                    seedling_raised = "Siris";
//                    break;
//                case 33:
//                    seedling_raised = "Sissoo";
//                    break;
//                case 34:
//                    seedling_raised = "Simal";
//                    break;
//                case 35:
//                    seedling_raised = "Sindhure";
//                    break;
//                case 36:
//                    seedling_raised = "Bayer";
//                    break;
//                case 37:
//                    seedling_raised = "Lapsi";
//                    break;
//                case 38:
//                    seedling_raised = "Katahar";
//                    break;
//                case 39:
//                    seedling_raised = "Mewa";
//                    break;
//                case 40:
//                    seedling_raised = "Others";
//                    break;
//            }
//        }


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

                    tracking.setVisibility(View.VISIBLE);
                    isGpsTracking = true;
                    listCf.clear();
                    gpslocation.clear();
                    gps = new GpsTracker(NurseryDetails.this);
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

    private void saveToExternalSorage(Bitmap thumbnail) {
        // TODO Auto-generated method stub
        //String merocinema="Mero Cinema";
//        String movname=getIntent().getExtras().getString("Title");
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        imageName = "Plantation_detail_" + timeInMillis + PhoneUtils.getFormatedId();

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
//            Toast.makeText(getApplicationContext(), "Saved "+imageName, Toast.LENGTH_SHORT).show();
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
            listCf.clear();
            isGpsTaken = true;
            startGps.setEnabled(false);
            endGps.setEnabled(false);
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

            Log.e("nursery_details_", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
                String path = file1.toString();
//                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path);

                addImage();
            }
            try {
                //new adjustment
                Log.e("nursery_details_", "" + jsonToParse);
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

    private void UpdateData() {
        mProgressDlg = new ProgressDialog(context);
        mProgressDlg.setMessage("Acquiring GPS location\nPlease wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

        gps = new GpsTracker(NurseryDetails.this);
        if (gps.canGetLocation()) {

            finalLat = gps.getLatitude();
            finalLong = gps.getLongitude();

            //Background thread
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {

                public void run() {

                    runOnUiThread(new Runnable() {

                        public void run() {

                            if (finalLat == 0.0 || finalLong == 0.0) {

                                gps = new GpsTracker(NurseryDetails.this);
                                if (gps.canGetLocation()) {
                                    finalLat = gps.getLatitude();
                                    finalLong = gps.getLongitude();

                                    Log.e("latlang", " lat: " + finalLat + " long: "
                                            + finalLong);

                                }

                            } else if (finalLat != 0 || finalLong != 0) {
                                mProgressDlg.dismiss();
                                gps = new GpsTracker(NurseryDetails.this);
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


    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_nursery_detail");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("year_of_establishment", year_of_establishment);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("institution_name", institution_name);
            header.put("bzug_name", bzug_name);
            header.put("location_address", location_address);

            header.put("no_of_segment_added", no_of_segment_added);
            header.put("all_species", all_species);
//            header.put("seedling_raised", seedling_raised);
//            header.put("no", no);
//            header.put("grass_species", grass_species + "   " + other_species);

            header.put("latitude", finalLat);
            header.put("longitude", finalLong);
            header.put("boundary", latLangArray);
            header.put("area_gps", area_using_Gps);
            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            header.put("others", others);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();
            Log.e("convertDataToJsonSave", "convertDataToJsonSave: "+jsonToSend );


            photo_dict.put("photo", encodedImage);
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
        year_of_establishment = jsonObj.getString("year_of_establishment");
        agreement_no = jsonObj.getString("agreement_no");
        grantee_name = jsonObj.getString("grantee_name");
        fiscal_year = jsonObj.getString("fiscal_year");
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        latLangArray = jsonObj.getString("boundary");
        institution_name = jsonObj.getString("institution_name");
        bzug_name = jsonObj.getString("bzug_name");
        location_address = jsonObj.getString("location_address");


        no_of_segment_added = jsonObj.getString("no_of_segment_added");
        all_species = jsonObj.getString("all_species");

        JSONArray jsonArray = new JSONArray(all_species);
        Log.e("ALL_SEGMENT_PARSE_JSON", "SAMIR_parseJson: "+jsonArray.length() );

        try {
            for (int i = 0 ; i < jsonArray.length(); i++){
                speciesSegmentList.add(jsonArray.get(i).toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


//        seedling_raised = jsonObj.getString("seedling_raised");
//        no = jsonObj.getString("no");
//        grass_species = jsonObj.getString("grass_species");

        others = jsonObj.getString("others");
        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        area_using_Gps = Double.parseDouble(jsonObj.getString("area_gps"));

        Log.e("Plantationdetail", "Parsed data " + agreement_no + grantee_name + fiscal_year);

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvYearOfEstablishment.setText(year_of_establishment);
        tvAgreement_no.setText(agreement_no);
        tvGrantew_name.setText(grantee_name);
        tvFiscal_year.setText(fiscal_year);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvInstitutionName.setText(institution_name);
        tvBzugName.setText(bzug_name);
        tvLocationAddress.setText(location_address);
        tvOthers.setText(others);
        tvFundTal.setText(fund_tal);
        tvFundCommunity.setText(fund_community);
        tvFundOthers.setText(fund_others);

        //Do rest of work here
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

        tvNoOfSegmentAdded.setText(no_of_segment_added);

//        int forConPos = spinSeedingRaisedAdpt.getPosition(seedling_raised);
//        spinnerSeedingRaised.setSelection(forConPos);


//        if (grass_species.equals("   ")) {
//            tvNameAndNumber.setText("");
//            tvOtherSpecies.setText("");
//        } else {
//            String[] actions1 = grass_species.split("   ");
//            tvNameAndNumber.setText(actions1[0]);
//            tvOtherSpecies.setText(actions1[1]);
//        }
        //        tvNo.setText(no);



        tvBoundryUsingGps.setText("Area Using GPS : " + area_using_Gps + " (Hectares)");

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
                    new PromptDialog(NurseryDetails.this)
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
                    String[] data = new String[]{"2", "Nursery Details", dateString, jsonToSend, jsonLatLangArray, "" + imageName, "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(NurseryDetails.this)
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


    private void showAddSegmentSeenDialog() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.forest_nursery_add_species_dialog_layout);

        Button btnOk = (Button) showDialog.findViewById(R.id.nursery_details_dialog_segment_add_btn);

        spinnerSeedingRaised = (Spinner)showDialog. findViewById(R.id.nursery_detail_spinner_SeddingRaised);
        tvNo = (AutoCompleteTextView)showDialog. findViewById(R.id.nursery_detail_No);


        spinSeedingRaisedAdpt = new ArrayAdapter<String>(NurseryDetails.this,
                android.R.layout.simple_spinner_item, Constants.NURSERY_SEEDING_RAISED);
        spinSeedingRaisedAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeedingRaised.setAdapter(spinSeedingRaisedAdpt);
//        spinnerSeedingRaised.setOnItemSelectedListener(this);
//        spinnerSeedingRaised.setOnItemSelectedListener(new OnSpinnerItemClicked());

        showDialog.setTitle("ADD SPECIES SEEN");
        showDialog.getActionBar();
        showDialog.show();
        showDialog.getWindow().setLayout((width), LinearLayout.LayoutParams.WRAP_CONTENT);


        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                no = tvNo.getText().toString();
                seedling_raised = spinnerSeedingRaised.getSelectedItem().toString();

                JSONObject memberObj = new JSONObject();
                try {

                    memberObj.put("seedling_raised", seedling_raised);
                    memberObj.put("no", no);

                    Log.e("species added : ", memberObj.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speciesSegmentList.add(memberObj.toString());
                all_species = speciesSegmentList.toString();
                Log.e("all_species: ", all_species);


                tvNoOfSegmentAdded.setText(speciesSegmentList.size() + " Species added");
                showDialog.dismiss();
            }
        });
    }

    public class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            int spinnerId = parent.getId();
            if (spinnerId == R.id.nursery_detail_spinner_SeddingRaised) {
                switch (pos) {
                    case 0:
                        seedling_raised = "Amala";
                        break;
                    case 1:
                        seedling_raised = "Bans";
                        break;
                    case 2:
                        seedling_raised = "Bet";
                        break;
                    case 3:
                        seedling_raised = "Bel";
                        break;
                    case 4:
                        seedling_raised = "Amriso";
                        break;
                    case 5:
                        seedling_raised = "Harro";
                        break;
                    case 6:
                        seedling_raised = "Kurilo";
                        break;
                    case 7:
                        seedling_raised = "Nim";
                        break;
                    case 8:
                        seedling_raised = "Raktachandan";
                        break;
                    case 9:
                        seedling_raised = "Rittha";
                        break;
                    case 10:
                        seedling_raised = "Sarpagandha";
                        break;
                    case 11:
                        seedling_raised = "Tejpat";
                        break;
                    case 12:
                        seedling_raised = "Bakaino";
                        break;
                    case 13:
                        seedling_raised = "Badhahar";
                        break;
                    case 14:
                        seedling_raised = "Kavro";
                        break;
                    case 15:
                        seedling_raised = "Kimbu";
                        break;
                    case 16:
                        seedling_raised = "Koiralo";
                        break;
                    case 17:
                        seedling_raised = "Nimaro";
                        break;
                    case 18:
                        seedling_raised = "Ranki";
                        break;
                    case 19:
                        seedling_raised = "Asna";
                        break;
                    case 20:
                        seedling_raised = "Ashok";
                        break;
                    case 21:
                        seedling_raised = "Bijaysal";
                        break;
                    case 22:
                        seedling_raised = "Chiuri";
                        break;
                    case 23:
                        seedling_raised = "Dhupi";
                        break;
                    case 24:
                        seedling_raised = "Haldu";
                        break;
                    case 25:
                        seedling_raised = "Jamun";
                        break;
                    case 26:
                        seedling_raised = "Khayer";
                        break;
                    case 27:
                        seedling_raised = "Kusum";
                        break;
                    case 28:
                        seedling_raised = "Mahuwa";
                        break;
                    case 29:
                        seedling_raised = "Nimaro";
                        break;
                    case 30:
                        seedling_raised = "Rajbrikssha";
                        break;
                    case 31:
                        seedling_raised = "Satisal";
                        break;
                    case 32:
                        seedling_raised = "Siris";
                        break;
                    case 33:
                        seedling_raised = "Sissoo";
                        break;
                    case 34:
                        seedling_raised = "Simal";
                        break;
                    case 35:
                        seedling_raised = "Sindhure";
                        break;
                    case 36:
                        seedling_raised = "Bayer";
                        break;
                    case 37:
                        seedling_raised = "Lapsi";
                        break;
                    case 38:
                        seedling_raised = "Katahar";
                        break;
                    case 39:
                        seedling_raised = "Mewa";
                        break;
                    case 40:
                        seedling_raised = "Others";
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
}
