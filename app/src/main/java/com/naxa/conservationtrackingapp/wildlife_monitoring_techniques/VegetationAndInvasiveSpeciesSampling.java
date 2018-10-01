package com.naxa.conservationtrackingapp.wildlife_monitoring_techniques;

import android.annotation.TargetApi;
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
import android.graphics.Color;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import java.io.File;
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
import com.naxa.conservationtrackingapp.adapter.RecyclerListAdapterSpeciesNN;
import com.naxa.conservationtrackingapp.application.ApplicationClass;
import com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking;
import com.naxa.conservationtrackingapp.dialog.Default_DIalog;
import com.naxa.conservationtrackingapp.forest.Cf_Detail;
import com.naxa.conservationtrackingapp.model.CheckValues;
import com.naxa.conservationtrackingapp.model.Constants;
import com.naxa.conservationtrackingapp.model.SpeciesNaNumModel;
import com.naxa.conservationtrackingapp.model.StaticListOfCoordinates;

import Utls.UserNameAndPasswordUtils;
import cn.refactor.lib.colordialog.PromptDialog;

/**
 * Created by ramaan on 1/18/2016.
 */
public class VegetationAndInvasiveSpeciesSampling extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    Spinner spinnerLandscape, habitatType, terrainType, invasiveSpp;
    ArrayAdapter landscapeAdpt, habitatTypeAdapter, terrainTypeAdapter, invasiveSppAdapter;
    Button send, save, startGps, previewMap;
    ProgressDialog mProgressDlg;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    String jsonToSend, photoTosend;
    String imagePath, encodedImage = null, imageName = "no_photo";
    ImageButton photo;

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
    String observer_name;
    String canopy_cover;
    String location;
    String date;
    String grid_id;
    String transect_id;
    String plot_no;
    String habitat_type;
    String terrain_type;
    String remarks;

    //10 meter plot
    String total_no_10m;

    //5 meter plot
    String total_no__5m;
    String invasive_spp_5m;
    String wildlife_usage_5m;

    //1 meter plot
    String herbs_seedling_per_1m;
    String grass_per_1m;
    String leaf_litter_per_1m;
    String bare_soil_boulders_per_1m;
    String fund_tal, fund_community, fund_others;
    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    AutoCompleteTextView tvOtherLandscape, tvFundingSource, tvDistrictname, tvNameOfVdc, tvobserver_name, tvProjectCode,
            tvdate, tvcanopy_cover, tvgrid_id, tvtransect_id, tvplot_no, tvlocation, tvNotes,
            tvtotal_no_10m, tvtotal_no_5m, tvherbs_seedling_per_1m, tvgrass_per_1m,
            tvleaf_litter_per_1m, tvbaresoil_boulders_per_1m, tvFundTal, tvFundCommunity, tvFundOthers;

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    String dataSentStatus = "", dateString;

    //For dynamic entry on species name and number
    ScrollView scrl, scrl5m, scrl1m;
    LinearLayout ll, ll2,
            ll5m, ll25m,
            ll1m, ll21m;
    LinearLayout.LayoutParams params, params1, params2;
    EditText et10mSpecies, et5mSpecies, et1mSpecies,
            et10mNumber, et5mNumber, et1mNumber;

    Button add_btn, add_btn5m, add_btn1m,
            clear_btn, clear_btn5m, clear_btn1m;

    int count_list10 = 0;
    int edittext_id10 = 1;

    int count_list5 = 0;
    int edittext_id5 = 1;

    int count_list1 = 0;
    int edittext_id1 = 1;

    String etSpeciesId10m = "", etSpeciesId5m = "", etSpeciesId1m = "",
            etSpeciesString10m = "", etSpeciesString5m = "", etSpeciesString1m = "",
            etNoId10m = "", etNoId5m = "", etNoId1m = "",
            etNoString10m = "", etNoString5m = "", etNoString1m = "",
            all_species_list_10m = "", all_species_list_5m = "", all_species_list_1m = "";

    List<String> speciesList = new ArrayList<>();
    List<String> speciesList5m = new ArrayList<>();
    List<String> speciesList1m = new ArrayList<>();
    private RecyclerView mRVSpecies10m, mRVSpecies5m, mRVSpecies1m;
    private LinearLayout LLSpecies10m, LLVSpecies5m, LLVSpecies1m;
    private RecyclerListAdapterSpeciesNN mAdapterSpecies10m, mAdapterSpecies5m, mAdapterSpecies1m;
    private TextView tvSpecieslistinfo, tvSpecieslistinfo1, tvSpecieslistinfo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wmt_vegitation_n_invasive_species_sampling);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckValues.isFromSavedFrom = false;


        /**
         * @Author Susan Lama: Adding Dynamic EditText View initialization
         */
        //10m plot
        mRVSpecies10m = (RecyclerView) findViewById(R.id.NewsList1);
        LLSpecies10m = (LinearLayout) findViewById(R.id.specieslabel_10m);
        mRVSpecies10m.setVisibility(View.GONE);
        mRVSpecies10m.setEnabled(false);
        LLSpecies10m.setVisibility(View.GONE);
        LLSpecies10m.setEnabled(false);

        //5m plot
        mRVSpecies5m = (RecyclerView) findViewById(R.id.NewsList2);
        LLVSpecies5m = (LinearLayout) findViewById(R.id.specieslabel_5m);
        mRVSpecies5m.setVisibility(View.GONE);
        mRVSpecies5m.setEnabled(false);
        LLVSpecies5m.setVisibility(View.GONE);
        LLVSpecies5m.setEnabled(false);

        //1m plot
        mRVSpecies1m = (RecyclerView) findViewById(R.id.NewsList3);
        LLVSpecies1m = (LinearLayout) findViewById(R.id.specieslabel_1m);
        mRVSpecies1m.setVisibility(View.GONE);
        mRVSpecies1m.setEnabled(false);
        LLVSpecies1m.setVisibility(View.GONE);
        LLVSpecies1m.setEnabled(false);

        tvSpecieslistinfo = new TextView(this);
        tvSpecieslistinfo1 = new TextView(this);
        tvSpecieslistinfo2 = new TextView(this);

        /**
         * First time Content view initialization 10 m plot
         */
        add_btn = new Button(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            add_btn.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }

        setFirstContentView10m();
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method
                clear_btn.setEnabled(true);

                JSONObject memberObj = new JSONObject();
                try {
                    memberObj.put("species_10m", et10mSpecies.getText());
                    memberObj.put("gbh_10m", et10mNumber.getText());
                    Log.e("all_species_list_10m: ", memberObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speciesList.add(memberObj.toString());
                all_species_list_10m = speciesList.toString();

                Log.e("all_species_list_10m: ", all_species_list_10m);

                count_list10++;
                edittext_id10++;
                editTextFieldDynamic10m();
            }
        });
        //Dynamic Button
        clear_btn = new Button(this);
        clear_btn.setEnabled(false);
        clear_btn.setText("Clear All Species Data");
        clear_btn.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clear_btn.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        clear_btn.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        clear_btn.setLayoutParams(params);
        //Add LinearLayout view
        ll.addView(clear_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_list10 = 0;
                speciesList.clear();
                ll.removeAllViews();
                scrl.removeAllViews();
                setFirstContentView10m();
                ll.addView(clear_btn);
                scrl.addView(ll);
                clear_btn.setEnabled(false);
            }
        });
        scrl.addView(ll);

        /**
         * First time Content view initialization 5m plot dynamic edittext
         */
        add_btn5m = new Button(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            add_btn5m.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        setFirstContentView5m();
        add_btn5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method
                clear_btn5m.setEnabled(true);

                JSONObject memberObj = new JSONObject();
                try {
                    memberObj.put("species_5m", et5mSpecies.getText());
                    memberObj.put("number_5m", et5mNumber.getText());
                    Log.e("all_species_list_5m: ", memberObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speciesList5m.add(memberObj.toString());
                all_species_list_5m = speciesList5m.toString();

                Log.e("all_species_list_5m: ", all_species_list_5m);

                count_list5++;
                edittext_id5++;
                editTextFieldDynamic5m();
            }
        });
        //Dynamic Button
        clear_btn5m = new Button(this);
        clear_btn5m.setEnabled(false);
        clear_btn5m.setText("Clear All Species Data");
        clear_btn5m.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clear_btn5m.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        clear_btn5m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        clear_btn5m.setLayoutParams(params);
        //Add LinearLayout view
        ll5m.addView(clear_btn5m);
        clear_btn5m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_list5 = 0;
                speciesList5m.clear();
                ll5m.removeAllViews();
                scrl5m.removeAllViews();
                setFirstContentView5m();
                ll5m.addView(clear_btn5m);
                scrl5m.addView(ll5m);
                clear_btn5m.setEnabled(false);
            }
        });
        scrl5m.addView(ll5m);

        /**
         * First time Content view initialization 1m plot dynamic edittext
         */
        add_btn1m = new Button(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            add_btn1m.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        setFirstContentView1m();
        add_btn1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method
                clear_btn1m.setEnabled(true);

                JSONObject memberObj = new JSONObject();
                try {
                    memberObj.put("species_1m", et1mSpecies.getText());
                    memberObj.put("number_1m", et1mNumber.getText());
                    Log.e("all_species_list_1m: ", memberObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speciesList1m.add(memberObj.toString());
                all_species_list_1m = speciesList1m.toString();

                Log.e("all_species_list_1m: ", all_species_list_1m);

                count_list1++;
                edittext_id1++;
                editTextFieldDynamic1m();
            }
        });
        //Dynamic Button
        clear_btn1m = new Button(this);
        clear_btn1m.setEnabled(false);
        clear_btn1m.setText("Clear All Species Data");
        clear_btn1m.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clear_btn1m.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        clear_btn1m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        clear_btn1m.setLayoutParams(params);
        //Add LinearLayout view
        ll1m.addView(clear_btn1m);
        clear_btn1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_list1 = 0;
                speciesList1m.clear();
                ll1m.removeAllViews();
                scrl1m.removeAllViews();
                setFirstContentView5m();
                ll1m.addView(clear_btn1m);
                scrl1m.addView(ll1m);
                clear_btn1m.setEnabled(false);
            }
        });
        scrl1m.addView(ll1m);
        /**
         * @End of the dynamic edit text view ***************************************************
         */

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.vis_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.vis_fundingSource);
        tvDistrictname = (AutoCompleteTextView) findViewById(R.id.vis_discrict);
        tvNameOfVdc = (AutoCompleteTextView) findViewById(R.id.vis_vdc);
        tvobserver_name = (AutoCompleteTextView) findViewById(R.id.vis_observer_name);
        tvcanopy_cover = (AutoCompleteTextView) findViewById(R.id.vis_canopy_cover);
        tvgrid_id = (AutoCompleteTextView) findViewById(R.id.vis_gird_id);
        tvtransect_id = (AutoCompleteTextView) findViewById(R.id.vis_transect_id);
        tvplot_no = (AutoCompleteTextView) findViewById(R.id.vis_plot_no);
        tvlocation = (AutoCompleteTextView) findViewById(R.id.vis_location);
        tvdate = (AutoCompleteTextView) findViewById(R.id.vis_date);
        habitatType = (Spinner) findViewById(R.id.vis_habitat_type);
        terrainType = (Spinner) findViewById(R.id.vis_terrain_type);
        tvNotes = (AutoCompleteTextView) findViewById(R.id.vis_remarks);

        //10 meter plot............................
        tvtotal_no_10m = (AutoCompleteTextView) findViewById(R.id.vis_10m_plot_total_no);

        //5 meter plot.............................
//        tvwildlife_usage_5m = (AutoCompleteTextView) findViewById(R.id.vis_5m_plot_wildlife_usage);
        tvtotal_no_5m = (AutoCompleteTextView) findViewById(R.id.vis_5m_plot_total_no);
        invasiveSpp = (Spinner) findViewById(R.id.vis_5m_plot_invasive_spp);

        //1 meter plot...............................
        tvherbs_seedling_per_1m = (AutoCompleteTextView) findViewById(R.id.vis_1m_plot_herb_per);
        tvgrass_per_1m = (AutoCompleteTextView) findViewById(R.id.vis_1m_plot_grass_per);
        tvleaf_litter_per_1m = (AutoCompleteTextView) findViewById(R.id.vis_1m_plot_leaf_per);
        tvbaresoil_boulders_per_1m = (AutoCompleteTextView) findViewById(R.id.vis_1m_plot_bare_soil_per);
        tvFundTal = (AutoCompleteTextView) findViewById(R.id.vis_Tal);
        tvFundCommunity = (AutoCompleteTextView) findViewById(R.id.vis_CommunityContribution);
        tvFundOthers = (AutoCompleteTextView) findViewById(R.id.vis_FundOthers);

        setCurrentDateOnView();
        addListenerOnButton();

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        invasiveSppAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.INVASIVE_SPP);
        invasiveSppAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        invasiveSpp.setAdapter(invasiveSppAdapter);
        invasiveSpp.setOnItemSelectedListener(this);

        habitatTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.HABITAT_TYPE);
        habitatTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitatType.setAdapter(habitatTypeAdapter);
        habitatType.setOnItemSelectedListener(this);

        terrainTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.TERRAIN);
        terrainTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        terrainType.setAdapter(terrainTypeAdapter);
        terrainType.setOnItemSelectedListener(this);

        send = (Button) findViewById(R.id.vis_send);
        save = (Button) findViewById(R.id.vis_save);
        startGps = (Button) findViewById(R.id.vis_GpsStart);
        //endGps = (Button) findViewById(R.id.vis_1m_plot_GpsEnd);
        previewMap = (Button) findViewById(R.id.vis_preview_map);
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
                                    observer_name = tvobserver_name.getText().toString();
                                    date = tvdate.getText().toString();
                                    canopy_cover = tvcanopy_cover.getText().toString();
                                    grid_id = tvgrid_id.getText().toString();
                                    transect_id = tvtransect_id.getText().toString();
                                    plot_no = tvplot_no.getText().toString();
                                    location = tvlocation.getText().toString();
                                    remarks = tvNotes.getText().toString();

                                    //10 meter plot...............................................
                                    total_no_10m = tvtotal_no_10m.getText().toString();

                                    //5 meter plot................................................
//                                    wildlife_usage_5m = tvwildlife_usage_5m.getText().toString();
                                    total_no__5m = tvtotal_no_5m.getText().toString();

                                    //1 meter plot................................................
                                    herbs_seedling_per_1m = tvherbs_seedling_per_1m.getText().toString();
                                    grass_per_1m = tvgrass_per_1m.getText().toString();
                                    leaf_litter_per_1m = tvleaf_litter_per_1m.getText().toString();
                                    bare_soil_boulders_per_1m = tvbaresoil_boulders_per_1m.getText().toString();

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
                        observer_name = tvobserver_name.getText().toString();
                        date = tvdate.getText().toString();
                        canopy_cover = tvcanopy_cover.getText().toString();
                        grid_id = tvgrid_id.getText().toString();
                        transect_id = tvtransect_id.getText().toString();
                        plot_no = tvplot_no.getText().toString();
                        location = tvlocation.getText().toString();
                        remarks = tvNotes.getText().toString();

                        //10 meter plot...............................................
                        total_no_10m = tvtotal_no_10m.getText().toString();

                        //5 meter plot................................................
//                        wildlife_usage_5m = tvwildlife_usage_5m.getText().toString();
                        total_no__5m = tvtotal_no_5m.getText().toString();

                        //1 meter plot................................................
                        herbs_seedling_per_1m = tvherbs_seedling_per_1m.getText().toString();
                        grass_per_1m = tvgrass_per_1m.getText().toString();
                        leaf_litter_per_1m = tvleaf_litter_per_1m.getText().toString();
                        bare_soil_boulders_per_1m = tvbaresoil_boulders_per_1m.getText().toString();

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
                        FormNameToInput.setText("Vegetation And Invasive Species Sampling");

                        if (CheckValues.isFromSavedFrom) {
                            if (formNameSavedForm == null | formNameSavedForm.equals("")) {
                                FormNameToInput.setText("Vegetation And Invasive Species Sampling");
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
                                    String[] data = new String[]{"87", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
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

                                    new PromptDialog(VegetationAndInvasiveSpeciesSampling.this)
                                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                            .setAnimationEnable(true)
                                            .setTitleText(getString(R.string.dialog_success))
                                            .setContentText(getString(R.string.dialog_saved))
                                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                                @Override
                                                public void onClick(PromptDialog dialog) {
                                                    if (CheckValues.isFromSavedFrom) {
                                                        showDialog.dismiss();
                                                        startActivity(new Intent(VegetationAndInvasiveSpeciesSampling.this, SavedFormsActivity.class));
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
                    startActivity(new Intent(VegetationAndInvasiveSpeciesSampling.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(VegetationAndInvasiveSpeciesSampling.this, MapPointActivity.class));
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

        if (spinnerId == R.id.vis_landscape) {
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

        if (spinnerId == R.id.vis_habitat_type) {
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

            }
        }
        if (spinnerId == R.id.vis_terrain_type) {
            switch (position) {
                case 0:
                    terrain_type = "Hilly";
                    break;
                case 1:
                    terrain_type = "Flat";
                    break;
                case 2:
                    terrain_type = "Streambed";
                    break;
            }
        }

        if (spinnerId == R.id.vis_5m_plot_invasive_spp) {
            switch (position) {
                case 0:
                    invasive_spp_5m = "0-25%";
                    break;
                case 1:
                    invasive_spp_5m = "25-50%";
                    break;
                case 2:
                    invasive_spp_5m = "50-75%";
                    break;
                case 3:
                    invasive_spp_5m = "75-100%";
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
        showDialog.setTitle("Exit Data");
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

        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        imageName = "vis_1m_plot_" + timeInMillis + PhoneUtils.getFormatedId();

        File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);

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

    public void initilizeUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("JSON1")) {
            CheckValues.isFromSavedFrom = true;
            startGps.setEnabled(false);
            previewMap.setEnabled(true);

            add_btn.setEnabled(false);
            clear_btn.setEnabled(false);

            add_btn5m.setEnabled(false);
            clear_btn5m.setEnabled(false);

            add_btn1m.setEnabled(false);
            clear_btn1m.setEnabled(false);

            mRVSpecies10m.setVisibility(View.VISIBLE);
            mRVSpecies10m.setEnabled(true);
            mRVSpecies5m.setVisibility(View.VISIBLE);
            mRVSpecies5m.setEnabled(true);
            mRVSpecies1m.setVisibility(View.VISIBLE);
            mRVSpecies1m.setEnabled(true);

            LLSpecies10m.setVisibility(View.VISIBLE);
            LLSpecies10m.setEnabled(true);
            LLVSpecies5m.setVisibility(View.VISIBLE);
            LLVSpecies5m.setEnabled(true);
            LLVSpecies1m.setVisibility(View.VISIBLE);
            LLVSpecies1m.setEnabled(true);

            isGpsTaken = true;
            Bundle bundle = intent.getExtras();
            String jsonToParse = (String) bundle.get("JSON1");
            imageName = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");
            String status = (String) bundle.get("status");

            formid = (String) bundle.get("dbID");
            formNameSavedForm = (String) bundle.get("formName");

            if(status.equals("Sent")){
                save.setEnabled(false);
                send.setEnabled(false);
            }

            Log.e("PLANYTATIONDETAIL", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

//                loadImageFromStorage(path);

                // addImage();
            }
            try {
                //new adjustment
                Log.e("vis_1m_plot_", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(VegetationAndInvasiveSpeciesSampling.this);
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
            post_dict.put("tablename", "tbl_vegetation_invasive_species");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("observer_name", observer_name);
            header.put("date", date);
            header.put("canopy_cover", canopy_cover);
            header.put("grid_id", grid_id);
            header.put("transect_id", transect_id);
            header.put("plot_no", plot_no);
            header.put("location", location);
            header.put("district", district_name);
            header.put("vdc", vdc_name);
            header.put("habitat_type", habitat_type);
            header.put("terrain_type", terrain_type);
            header.put("latitude", finalLat);
            header.put("longitude", finalLong);

            //10 meter plot...........................................................
            header.put("all_species_list_10m", all_species_list_10m);
            header.put("total_no_10m", total_no_10m);

            //5 meter plot............................................................
            header.put("all_species_list_5m", all_species_list_5m);
//            header.put("wildlife_usage_5m", wildlife_usage_5m);
            header.put("invasive_spp_5m", invasive_spp_5m);
            header.put("total_no__5m", total_no__5m);

            //1 meter plot...........................................................
            header.put("all_species_list_1m", all_species_list_1m);
            header.put("herbs_seedling_per_1m", herbs_seedling_per_1m);
            header.put("grass_per_1m", grass_per_1m);
            header.put("leaf_litter_per_1m", leaf_litter_per_1m);
            header.put("bare_soil_boulders_per_1m", bare_soil_boulders_per_1m);

            header.put("fund_tal", fund_tal);
            header.put("fund_community", fund_community);
            header.put("fund_others", fund_others);
            header.put("others", remarks);

            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

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
        observer_name = jsonObj.getString("observer_name");
        date = jsonObj.getString("date");
        canopy_cover = jsonObj.getString("canopy_cover");
        grid_id = jsonObj.getString("grid_id");
        transect_id = jsonObj.getString("transect_id");
        plot_no = jsonObj.getString("plot_no");
        location = jsonObj.getString("location");
        district_name = jsonObj.getString("district");
        vdc_name = jsonObj.getString("vdc");
        finalLat = Double.parseDouble(jsonObj.getString("latitude"));
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        habitat_type = jsonObj.getString("habitat_type");
        terrain_type = jsonObj.getString("terrain_type");

        /**
         * @Start dynamic view data fetch***************************************************************
         */
        /**
         * Dynamic data on RecyclerView10 meter plot.................................
         */
        all_species_list_10m = jsonObj.getString("all_species_list_10m");
        Log.e("SUSAN", "all_species_list_10m: " + all_species_list_10m.toString());
        List<SpeciesNaNumModel> speciesNaNumModelsList = new ArrayList<>();
        try {
            JSONArray all_species_array1 = new JSONArray(all_species_list_10m);
            Log.e("SUSAN", "parseJson: " + all_species_array1.toString());

            //Loop to get list object of array
            for (int i = 0; i < all_species_array1.length(); i++) {

                //list each object
                JSONObject listObj = all_species_array1.getJSONObject(i);

                SpeciesNaNumModel speciesNaNumModel = new SpeciesNaNumModel();
                speciesNaNumModel.setSpecies_10m(listObj.getString("species_10m"));
                speciesNaNumModel.setGbh_10m(listObj.getString("gbh_10m"));
                speciesNaNumModelsList.add(speciesNaNumModel);

                Log.e("SUSAN", "species_10m: " + listObj.getString("species_10m"));
                Log.e("SUSAN", "gbh_10m: " + listObj.getString("gbh_10m"));
            }

            // Setup and Handover data to recyclerview
            mAdapterSpecies10m = new RecyclerListAdapterSpeciesNN(speciesNaNumModelsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRVSpecies10m.setLayoutManager(mLayoutManager);
            mRVSpecies10m.setItemAnimator(new DefaultItemAnimator());
            mRVSpecies10m.setAdapter(mAdapterSpecies10m);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        total_no_10m = jsonObj.getString("total_no_10m");

        /**
         * Dynamic data on RecyclerView 5 meter plot......................................
         */
        all_species_list_5m = jsonObj.getString("all_species_list_5m");
        List<SpeciesNaNumModel> species5mModelsList = new ArrayList<>();
        try {
            JSONArray all_species_array2 = new JSONArray(all_species_list_5m);
            Log.e("SUSAN", "parseJson: " + all_species_array2.toString());

            //Loop to get list object of array
            for (int i = 0; i < all_species_array2.length(); i++) {

                //list each object
                JSONObject listObj = all_species_array2.getJSONObject(i);

                SpeciesNaNumModel speciesNaNumModel = new SpeciesNaNumModel();
                speciesNaNumModel.setSpecies_10m(listObj.getString("species_5m"));
                speciesNaNumModel.setGbh_10m(listObj.getString("number_5m"));
                species5mModelsList.add(speciesNaNumModel);

                Log.e("SUSAN", "species_5m: " + listObj.getString("species_5m"));
                Log.e("SUSAN", "gbh_5m: " + listObj.getString("number_5m"));
            }

            // Setup and Handover data to recyclerview
            mAdapterSpecies5m = new RecyclerListAdapterSpeciesNN(species5mModelsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRVSpecies5m.setLayoutManager(mLayoutManager);
            mRVSpecies5m.setItemAnimator(new DefaultItemAnimator());
            mRVSpecies5m.setAdapter(mAdapterSpecies5m);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        total_no__5m = jsonObj.getString("total_no__5m");
        invasive_spp_5m = jsonObj.getString("invasive_spp_5m");
//        wildlife_usage_5m = jsonObj.getString("wildlife_usage_5m");

        /**
         * Dynamic data on RecyclerView 5 meter plot......................................
         */
        //1 meter plot.......................................
        all_species_list_1m = jsonObj.getString("all_species_list_1m");
        List<SpeciesNaNumModel> species1mModelsList = new ArrayList<>();
        try {
            JSONArray all_species_array3 = new JSONArray(all_species_list_1m);
            Log.e("SUSAN", "parseJson: " + all_species_array3.toString());

            //Loop to get list object of array
            for (int i = 0; i < all_species_array3.length(); i++) {

                //list each object
                JSONObject listObj = all_species_array3.getJSONObject(i);

                SpeciesNaNumModel speciesNaNumModel = new SpeciesNaNumModel();
                speciesNaNumModel.setSpecies_10m(listObj.getString("species_1m"));
                speciesNaNumModel.setGbh_10m(listObj.getString("number_1m"));
                species1mModelsList.add(speciesNaNumModel);

                Log.e("SUSAN", "species_1m: " + listObj.getString("species_1m"));
                Log.e("SUSAN", "number_1m: " + listObj.getString("number_1m"));
            }

            // Setup and Handover data to recyclerview
            mAdapterSpecies1m = new RecyclerListAdapterSpeciesNN(species1mModelsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRVSpecies1m.setLayoutManager(mLayoutManager);
            mRVSpecies1m.setItemAnimator(new DefaultItemAnimator());
            mRVSpecies1m.setAdapter(mAdapterSpecies1m);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        herbs_seedling_per_1m = jsonObj.getString("herbs_seedling_per_1m");
        grass_per_1m = jsonObj.getString("grass_per_1m");
        leaf_litter_per_1m = jsonObj.getString("leaf_litter_per_1m");
        bare_soil_boulders_per_1m = jsonObj.getString("bare_soil_boulders_per_1m");

        /**
         * @End of Dynamic view data fetch **********************************************************************
         */


        remarks = jsonObj.getString("others");

        fund_tal = jsonObj.getString("fund_tal");
        fund_community = jsonObj.getString("fund_community");
        fund_others = jsonObj.getString("fund_others");

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvobserver_name.setText(observer_name);
        tvdate.setText(date);
        tvcanopy_cover.setText(canopy_cover);
        tvgrid_id.setText(grid_id);
        tvtransect_id.setText(transect_id);
        tvplot_no.setText(plot_no);
        tvlocation.setText(location);
        tvDistrictname.setText(district_name);
        tvNameOfVdc.setText(vdc_name);
        tvNotes.setText(remarks);

        //10 meter plot....................................
        tvtotal_no_10m.setText(total_no_10m);

        //5 meter plot.....................................
        tvtotal_no_5m.setText(total_no__5m);
//        tvwildlife_usage_5m.setText(wildlife_usage_5m);

        //1 meter plot..........................................
        tvherbs_seedling_per_1m.setText(herbs_seedling_per_1m);
        tvgrass_per_1m.setText(grass_per_1m);
        tvleaf_litter_per_1m.setText(leaf_litter_per_1m);
        tvbaresoil_boulders_per_1m.setText(bare_soil_boulders_per_1m);

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

        int forTypePos = terrainTypeAdapter.getPosition(terrain_type);
        terrainType.setSelection(forTypePos);

        int forInvasive = invasiveSppAdapter.getPosition(invasive_spp_5m);
        invasiveSpp.setSelection(forInvasive);

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
                    new PromptDialog(VegetationAndInvasiveSpeciesSampling.this)
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
                    String[] data = new String[]{"87", "Vegetation And Invasive Species Sampling", dateString, jsonToSend, jsonLatLangArray, "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(VegetationAndInvasiveSpeciesSampling.this)
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

    /**
     * @Susan Dynamic First Content View 10 m plot method
     */
    public void setFirstContentView10m() {

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);

        params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(10, 30, 10, 10);

        params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params2.setMargins(10, 10, 10, 30);

        scrl = (ScrollView) findViewById(R.id.dynamic_scrollview_10m);

        ll = new LinearLayout(this);
        ll.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(params);

        ll2 = new LinearLayout(this);
        ll2.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll2.setOrientation(LinearLayout.VERTICAL);
        ll2.setLayoutParams(params);

        //Editext Field properties
        editTextFieldDynamic10m();

        ll.addView(ll2);
        tvSpecieslistinfo.setTextColor(Color.RED);
        tvSpecieslistinfo.setText("Note: \nPlease you must click 'Add New Species Button' each time after typing the species name and number");
        tvSpecieslistinfo.setLayoutParams(params);
        tvSpecieslistinfo.setTextSize(15);
        ll.addView(tvSpecieslistinfo);
        add_btn.setText("Add New Species");

        add_btn.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            add_btn.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        add_btn.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        add_btn.setLayoutParams(params);
        ll.addView(add_btn);
    }

    /**
     * @Susan Dynamic edit text method
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void editTextFieldDynamic10m() {
        //Species Name
        et10mSpecies = new EditText(getApplicationContext());
        et10mSpecies.setHint("Species ");
        et10mSpecies.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et10mSpecies.setTextColor(Color.BLACK);
        et10mSpecies.setBackground(getResources().getDrawable(R.drawable.edit_text_boarder_recg));
        et10mSpecies.setHintTextColor(Color.GRAY);
        et10mSpecies.setPadding(15, 15, 15, 15);
        et10mSpecies.setCursorVisible(true);
        et10mSpecies.setId(count_list10);
        et10mSpecies.setLayoutParams(params1);
        etSpeciesString10m = et10mSpecies.getText().toString();
        etSpeciesId10m = String.valueOf(et10mSpecies.getId());
        Log.e("SUSAN", "tvSpeciesString: " + etSpeciesString10m);
        Log.e("SUSAN", "speciesId: " + etSpeciesId10m);
        ll2.addView(et10mSpecies);

        //Species Number
        et10mNumber = new EditText(getApplicationContext());
        et10mNumber.setHint("GBH ");
        et10mNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        et10mNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et10mNumber.setTextColor(Color.BLACK);
        et10mNumber.setBackground(getResources().getDrawable(R.drawable.edit_text_boarder_recg));
        et10mNumber.setHintTextColor(Color.GRAY);
        et10mNumber.setPadding(15, 15, 15, 15);
        et10mNumber.setCursorVisible(true);
        et10mNumber.setId(count_list10);
        et10mNumber.setLayoutParams(params2);
        etNoString10m = et10mNumber.getText().toString();
        etNoId10m = String.valueOf(et10mNumber.getId());
        Log.e("SUSAN", "tvNoString: " + etNoString10m);
        Log.e("SUSAN", "noId: " + etNoId10m);
        ll2.addView(et10mNumber);
    }

    /**
     * @Susan Dynamic First Content View 5 m plot method
     */
    public void setFirstContentView5m() {

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);

        params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(10, 30, 10, 10);

        params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params2.setMargins(10, 10, 10, 30);

        scrl5m = (ScrollView) findViewById(R.id.dynamic_scrollview_5m);

        ll5m = new LinearLayout(this);
        ll5m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll5m.setOrientation(LinearLayout.VERTICAL);
        ll5m.setLayoutParams(params);

        ll25m = new LinearLayout(this);
        ll25m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll25m.setOrientation(LinearLayout.VERTICAL);
        ll25m.setLayoutParams(params);

        //Editext Field properties
        editTextFieldDynamic5m();

        ll5m.addView(ll25m);

        tvSpecieslistinfo1.setTextColor(Color.RED);
        tvSpecieslistinfo1.setText("Note: \nPlease you must click 'Add New Species Button' each time after typing the species name and number");
        tvSpecieslistinfo1.setLayoutParams(params);
        tvSpecieslistinfo1.setTextSize(15);
        ll5m.addView(tvSpecieslistinfo1);

        add_btn5m.setText("Add New Species");
        add_btn5m.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            add_btn5m.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        add_btn5m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        add_btn5m.setLayoutParams(params);
        ll5m.addView(add_btn5m);
    }

    /**
     * @Susan Dynamic edit text method
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void editTextFieldDynamic5m() {
        //Species Name
        et5mSpecies = new EditText(getApplicationContext());
        et5mSpecies.setHint("Species ");
        et5mSpecies.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et5mSpecies.setTextColor(Color.BLACK);
        et5mSpecies.setBackground(getResources().getDrawable(R.drawable.edit_text_boarder_recg));
        et5mSpecies.setHintTextColor(Color.GRAY);
        et5mSpecies.setPadding(15, 15, 15, 15);
        et5mSpecies.setCursorVisible(true);
        et5mSpecies.setId(count_list5);
        et5mSpecies.setLayoutParams(params1);
        etSpeciesString5m = et5mSpecies.getText().toString();
        etSpeciesId5m = String.valueOf(et5mSpecies.getId());
        Log.e("SUSAN", "tvSpeciesString: " + etSpeciesString5m);
        Log.e("SUSAN", "speciesId: " + etSpeciesId5m);
        ll25m.addView(et5mSpecies);

        //Species Number
        et5mNumber = new EditText(getApplicationContext());
        et5mNumber.setHint("Number ");
        et5mNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        et5mNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et5mNumber.setTextColor(Color.BLACK);
        et5mNumber.setBackground(getResources().getDrawable(R.drawable.edit_text_boarder_recg));
        et5mNumber.setHintTextColor(Color.GRAY);
        et5mNumber.setPadding(15, 15, 15, 15);
        et5mNumber.setCursorVisible(true);
        et5mNumber.setId(count_list5);
        et5mNumber.setLayoutParams(params2);
        etNoString5m = et5mNumber.getText().toString();
        etNoId5m = String.valueOf(et5mNumber.getId());
        Log.e("SUSAN", "tvNoString: " + etNoString5m);
        Log.e("SUSAN", "noId: " + etNoId5m);
        ll25m.addView(et5mNumber);
    }

    /**
     * @Susan Dynamic First Content View 1 m plot method
     */
    public void setFirstContentView1m() {

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);

        params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params1.setMargins(10, 30, 10, 10);

        params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params2.setMargins(10, 10, 10, 30);

        scrl1m = (ScrollView) findViewById(R.id.dynamic_scrollview_1m);

        ll1m = new LinearLayout(this);
        ll1m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll1m.setOrientation(LinearLayout.VERTICAL);
        ll1m.setLayoutParams(params);

        ll21m = new LinearLayout(this);
        ll21m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll21m.setOrientation(LinearLayout.VERTICAL);
        ll21m.setLayoutParams(params);

        //Editext Field properties
        editTextFieldDynamic1m();

        ll1m.addView(ll21m);

        tvSpecieslistinfo2.setTextColor(Color.RED);
        tvSpecieslistinfo2.setText("Note: \nPlease you must click 'Add New Species Button' each time after typing the species name and number");
        tvSpecieslistinfo2.setLayoutParams(params);
        tvSpecieslistinfo2.setTextSize(15);
        ll1m.addView(tvSpecieslistinfo2);

        add_btn1m.setText("Add New Species");
        add_btn1m.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            add_btn1m.setBackground(getResources().getDrawable(R.drawable.button_pressed_wmt_light));
        }
        add_btn1m.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        add_btn1m.setLayoutParams(params);
        ll1m.addView(add_btn1m);
    }

    /**
     * @Susan Dynamic edit text method
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void editTextFieldDynamic1m() {
        //Species Name
        et1mSpecies = new EditText(getApplicationContext());
        et1mSpecies.setHint("Species ");
        et1mSpecies.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et1mSpecies.setTextColor(Color.BLACK);
        et1mSpecies.setBackground(getResources().getDrawable(R.drawable.edit_text_boarder_recg));
        et1mSpecies.setHintTextColor(Color.GRAY);
        et1mSpecies.setPadding(15, 15, 15, 15);
        et1mSpecies.setCursorVisible(true);
        et1mSpecies.setId(count_list5);
        et1mSpecies.setLayoutParams(params1);
        etSpeciesString1m = et1mSpecies.getText().toString();
        etSpeciesId1m = String.valueOf(et1mSpecies.getId());
        Log.e("SUSAN", "tvSpeciesString: " + etSpeciesString1m);
        Log.e("SUSAN", "speciesId: " + etSpeciesId1m);
        ll21m.addView(et1mSpecies);

        //Species Number
        et1mNumber = new EditText(getApplicationContext());
        et1mNumber.setHint("Number ");
        et1mNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        et1mNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et1mNumber.setTextColor(Color.BLACK);
        et1mNumber.setBackground(getResources().getDrawable(R.drawable.edit_text_boarder_recg));
        et1mNumber.setHintTextColor(Color.GRAY);
        et1mNumber.setPadding(15, 15, 15, 15);
        et1mNumber.setCursorVisible(true);
        et1mNumber.setId(count_list1);
        et1mNumber.setLayoutParams(params2);
        etNoString1m = et1mNumber.getText().toString();
        etNoId1m = String.valueOf(et1mNumber.getId());
        Log.e("SUSAN", "tvNoString: " + etNoString1m);
        Log.e("SUSAN", "noId: " + etNoId1m);
        ll21m.addView(et1mNumber);
    }
}
