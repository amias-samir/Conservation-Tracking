package com.naxa.conservationtrackingapp.human_wildlife_conflict_management;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import javax.net.ssl.HttpsURLConnection;

import Utls.UserNameAndPasswordUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;

public class ElephantActivityRecordingCSUActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    @BindView(R.id.elephant_recording_GpsStart)
    Button btnGpsStart;
    @BindView(R.id.elephant_recording_preview_map)
    Button btnPreviewMap;
    private String TAG = "ElephantActivityRecordingCSU";

    @BindView(R.id.elephant_recording_project_code)
    AutoCompleteTextView tvProjectCode;
    @BindView(R.id.elephant_recording_date)
    AutoCompleteTextView tvDate;
    @BindView(R.id.elephant_recording_time)
    AutoCompleteTextView tvTime;
    @BindView(R.id.elephant_recording_potential_attractants)
    Spinner spinnerPotentialAttractants;
    @BindView(R.id.elephant_recording_herd_size)
    AutoCompleteTextView tvHerdSize;
    @BindView(R.id.elephant_recording_tuskers_no)
    AutoCompleteTextView tvTuskersNo;
    @BindView(R.id.elephant_recording_female_no)
    AutoCompleteTextView tvFemaleNo;
    @BindView(R.id.elephant_recording_sub_adult_no)
    AutoCompleteTextView tvSubAdultNo;
    @BindView(R.id.elephant_recording_calves_no)
    AutoCompleteTextView tvCalvesNo;
    @BindView(R.id.elephant_recording_total_no)
    AutoCompleteTextView tvTotalNo;
    @BindView(R.id.elephatns_recording_elephants_reaction_during_entry)
    Spinner spinnerElephantsReactionDuringEntry;
    @BindView(R.id.elephant_recording_cb_making_noise)
    CheckBox cbMakingNoise;
    @BindView(R.id.elephant_recording_cb_throwing_flame)
    CheckBox cbThrowingFlame;
    @BindView(R.id.elephant_recording_cb_throwing_stones)
    CheckBox cbThrowingStones;
    @BindView(R.id.elephant_recording_cb_using_mega_torches)
    CheckBox cbUsingMegaTorches;
    @BindView(R.id.elephant_recording_cb_inform_park_authorities)
    CheckBox cbInformParkAuthorities;
    @BindView(R.id.elephant_recording_cb_others)
    CheckBox cbOthers;
    @BindView(R.id.elephatns_recording_elephants_reaction_after_entry)
    Spinner spinnerElephantsReactionAfterEntry;
    @BindView(R.id.elephant_recording_elephant_entered_from_area)
    AutoCompleteTextView tvElephantEnteredFromArea;

    @BindView(R.id.elephant_recording_potential_attractants_other)
    AutoCompleteTextView tvPotentialAttractantsOther;
    @BindView(R.id.elephant_recording_elephants_reaction_during_entry_other)
    AutoCompleteTextView tvElephantsReactionDuringEntryOther;
    @BindView(R.id.elephant_recording_elephants_reaction_after_entry_specify_other)
    AutoCompleteTextView tvElephantsReactionAfterEntrySpecifyOther;
    @BindView(R.id.elephant_recording_people_reaction_specify_other)
    AutoCompleteTextView tvPeopleReactionSpecifyOther;

    @BindView(R.id.elephant_recording_csu_save)
    Button btnSave;
    @BindView(R.id.elephant_recording_csu_send)
    Button btnSend;

    ProgressDialog mProgressDlg;
    Context context = this;
    String jsonToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    private TimePicker timePicker1;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 11;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    GPS_TRACKER_FOR_POINT gps;
    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    double finalLat;
    double finalLong;

    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    String latLangArray = "", jsonLatLangArray = "";
    JSONArray jsonArrayGPS = new JSONArray();


    StringBuilder stringBuilder = new StringBuilder();
    String dataSentStatus = "", dateString;
    String userNameToSend, passwordToSend;

    ArrayAdapter potentialAttractantsAdpt, elephantsReactionDuringEntryAdpt, elephantsReactionAfterEnryAdpt;
    String potentialAttractants = "", elephntsReactionDuring = "", elephantsReactionAfter = "";

    String making_noise = "no", throwing_flame_torches = "no", throwing_stones = "no", using_mega_torches = "no", inform_park = "no", other_reaction_by_people = "no";

    String KEY_PROJECT_CODE = "project_code",
            KEY_DATE = "date",
            KEY_TIME = "time",
            KEY_POTENTIAL_ATTRACTANTS = "potential_attractants",
            KEY_POTENTIAL_ATTRACTANTS_OTHER = "potential_attractants_other",
            KEY_HERD_SIZE = "herd_size",
            KEY_NO_OF_TUSKERS = "no_of_tuskers",
            KEY_NO_OF_FEMALES = "no_of_females",
            KEY_NO_OF_SUB_ADULTS = "no_of_sub_adults",
            KEY_NO_OF_CALVES = "no_of_calves",
            KEY_TOTAL_NUMBER = "total_number",

    KEY_FINAL_LAT = "latitude",
            KEY_FINAL_LONG = "longitude",

    KEY_ELEPHANTS_REACTION_DURING_ENTRY = "elephants_reaction_during_entry",
            KEY_ELEPHANTS_REACTION_DURING_ENTRY_OTHER = "elephants_reaction_during_entry_other",

    KEY_REACTION_BY_PEOPLE = "reaction_by_people",
            KEY_PEOPLES_REACTION_MAKING_NOISE = "making_noise_shouting_or_beating_items",
            KEY_PEOPLES_REACTION_THROWING_FLAME_TORCHES = "throwing_flame_torches",
            KEY_PEOPLES_REACTION_THROWING_STONES = "throwing_stones",
            KEY_PEOPLES_REACTION_USING_MEGA_TORCHES = "using_mega_torches",
            KEY_PEOPLES_REACTION_INFORM_PARK_FOR_HELP = "inform_park_authorities_for_help",
            KEY_PEOPLES_REACTION_OTHER = "peoples_reaction_other",
            KEY_PEOPLES_REACTION_SPECIFY_OTHER = "peoples_reaction_specify_other",

    KEY_ELEPHANTS_REACTION_AFTER_ENTRY = "elephants_reaction_after_entry",
            KEY_ELEPHANTS_REACTION_AFTER_ENTRY_SPECIFY_OTHER = "elephants_reaction_after_entry_other",
            KEY_ELEPHANT_ENTERED_FROM_AREA_ARE_NOT_COVERED = "elephant_entered_from_area_not_covered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elephant_recording_csu);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //        set date and time
        setCurrentDateOnView();
        setStarTimeOnView();
        addListenerOnButton();

        //        potential attractants spinner
        potentialAttractantsAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.HWC_ELEPHANT_ACTIVITY_POTENTIAL_ATTRACTANTS);
        potentialAttractantsAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPotentialAttractants.setAdapter(potentialAttractantsAdpt);
        spinnerPotentialAttractants.setOnItemSelectedListener(this);

//        elephants reaction during entry spinner
        elephantsReactionDuringEntryAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.HWC_ELEPHANT_ACTIVITY_REACTION_DURING_ENTRY);
        elephantsReactionDuringEntryAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerElephantsReactionDuringEntry.setAdapter(elephantsReactionDuringEntryAdpt);
        spinnerElephantsReactionDuringEntry.setOnItemSelectedListener(this);

        //        elephants reaction after entry spinner
        elephantsReactionAfterEnryAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.HWC_ELEPHANT_ACTIVITY_REACTION_AFTER_ENTRY);
        elephantsReactionAfterEnryAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerElephantsReactionAfterEntry.setAdapter(elephantsReactionAfterEnryAdpt);
        spinnerElephantsReactionAfterEntry.setOnItemSelectedListener(this);

        tvPotentialAttractantsOther.setVisibility(View.GONE);
        tvElephantsReactionDuringEntryOther.setVisibility(View.GONE);
        tvPeopleReactionSpecifyOther.setVisibility(View.GONE);
        tvElephantsReactionAfterEntrySpecifyOther.setVisibility(View.GONE);

        cbMakingNoise.setOnCheckedChangeListener(this);
        cbThrowingFlame.setOnCheckedChangeListener(this);
        cbThrowingStones.setOnCheckedChangeListener(this);
        cbUsingMegaTorches.setOnCheckedChangeListener(this);
        cbInformParkAuthorities.setOnCheckedChangeListener(this);
        cbOthers.setOnCheckedChangeListener(this);

        btnPreviewMap.setEnabled(false);
        initilizeUI();

    }


    @OnClick({R.id.elephant_recording_GpsStart, R.id.elephant_recording_preview_map, R.id.elephant_recording_total_no, R.id.elephant_recording_csu_save, R.id.elephant_recording_csu_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.elephant_recording_GpsStart:
                Intent toGeoPointActivity = new Intent(context, GeoPointActivity.class);
                startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
                break;

            case R.id.elephant_recording_preview_map:
                mapPREVIEW();
                break;

            case R.id.elephant_recording_total_no:
                countTotalElephantNumber();
                break;

            case R.id.elephant_recording_csu_save:
                saveFORMDATA();
                break;

            case R.id.elephant_recording_csu_send:
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


        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
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
        tvTime.setText(new StringBuilder().append(pad(hour))
                .append(":").append(pad(minute)));
    }


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour,
                              int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;

            // set current time into textview
            tvTime.setText(new StringBuilder().append(pad(hour))
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


    public void countTotalElephantNumber() {
        int female_no, tuskers_no, sub_adult_no, calves_no, total_no = 0;

        if (TextUtils.isEmpty(tvFemaleNo.getText().toString())) {
            female_no = 0;
        } else {
            female_no = Integer.parseInt(tvFemaleNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvTuskersNo.getText().toString())) {
            tuskers_no = 0;
        } else {
            tuskers_no = Integer.parseInt(tvTuskersNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvSubAdultNo.getText().toString())) {
            sub_adult_no = 0;
        } else {
            sub_adult_no = Integer.parseInt(tvSubAdultNo.getText().toString());
        }

        if (TextUtils.isEmpty(tvCalvesNo.getText().toString())) {
            calves_no = 0;
        } else {
            calves_no = Integer.parseInt(tvCalvesNo.getText().toString());
        }


        total_no = female_no + tuskers_no + sub_adult_no + calves_no;
        tvTotalNo.setText("" + total_no);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.elephant_recording_cb_making_noise:
                if (cbMakingNoise.isChecked() == true) {
                    making_noise = "yes";
                } else {
                    making_noise = "no";
                }
                break;

            case R.id.elephant_recording_cb_throwing_flame:
                if (cbThrowingFlame.isChecked() == true) {
                    throwing_flame_torches = "yes";
                } else {
                    throwing_flame_torches = "no";
                }
                break;

            case R.id.elephant_recording_cb_throwing_stones:
                if (cbThrowingStones.isChecked() == true) {
                    throwing_stones = "yes";
                } else {
                    throwing_stones = "no";
                }
                break;

            case R.id.elephant_recording_cb_using_mega_torches:
                if (cbUsingMegaTorches.isChecked() == true) {
                    using_mega_torches = "yes";
                } else {
                    using_mega_torches = "no";
                }
                break;

            case R.id.elephant_recording_cb_inform_park_authorities:
                if (cbInformParkAuthorities.isChecked() == true) {
                    inform_park = "yes";
                } else {
                    inform_park = "no";
                }
                break;

            case R.id.elephant_recording_cb_others:
                if (cbOthers.isChecked() == true) {
                    other_reaction_by_people = "yes";
                    tvPeopleReactionSpecifyOther.setVisibility(View.VISIBLE);

                } else {
                    other_reaction_by_people = "no";
                    tvPeopleReactionSpecifyOther.setVisibility(View.GONE);
                    tvPeopleReactionSpecifyOther.setText("");

                }
                break;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int SpinnerID = parent.getId();
        if (SpinnerID == R.id.elephant_recording_potential_attractants) {
            potentialAttractants = Constants.HWC_ELEPHANT_ACTIVITY_POTENTIAL_ATTRACTANTS[position];
            Log.d(TAG, "onItemSelected: " + potentialAttractants);

            if (potentialAttractants.equals("Others")) {
                tvPotentialAttractantsOther.setVisibility(View.VISIBLE);
            } else {
                tvPotentialAttractantsOther.setVisibility(View.GONE);
                tvPotentialAttractantsOther.setText("");
            }
        }

        if (SpinnerID == R.id.elephatns_recording_elephants_reaction_during_entry) {
            elephntsReactionDuring = Constants.HWC_ELEPHANT_ACTIVITY_REACTION_DURING_ENTRY[position];
            Log.d(TAG, "onItemSelected: " + elephntsReactionDuring);

            if (elephntsReactionDuring.equals("Others")) {
                tvElephantsReactionDuringEntryOther.setVisibility(View.VISIBLE);
            } else {
                tvElephantsReactionDuringEntryOther.setVisibility(View.GONE);
                tvElephantsReactionDuringEntryOther.setText("");
            }
        }

        if (SpinnerID == R.id.elephatns_recording_elephants_reaction_after_entry) {
            elephantsReactionAfter = Constants.HWC_ELEPHANT_ACTIVITY_REACTION_AFTER_ENTRY[position];
            Log.d(TAG, "onItemSelected: " + elephantsReactionAfter);

            if (elephantsReactionAfter.equals("Any others")) {
                tvElephantsReactionAfterEntrySpecifyOther.setVisibility(View.VISIBLE);
            } else {
                tvElephantsReactionAfterEntrySpecifyOther.setVisibility(View.GONE);
                tvElephantsReactionAfterEntrySpecifyOther.setText("");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void convertDataToJsonSave() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        try {
            post_dict.put("tablename", "tbl_elephant_activity_recording");

            JSONObject header = new JSONObject();

            header.put(KEY_PROJECT_CODE, tvProjectCode.getText().toString());
            header.put(KEY_DATE, tvDate.getText().toString());
            header.put(KEY_TIME, tvTime.getText().toString());
            header.put(KEY_POTENTIAL_ATTRACTANTS, spinnerPotentialAttractants.getSelectedItem().toString());
            header.put(KEY_POTENTIAL_ATTRACTANTS_OTHER, tvPotentialAttractantsOther.getText().toString());
            header.put(KEY_HERD_SIZE, tvHerdSize.getText().toString());

            header.put(KEY_NO_OF_TUSKERS, tvTuskersNo.getText().toString());
            header.put(KEY_NO_OF_FEMALES, tvFemaleNo.getText().toString());
            header.put(KEY_NO_OF_SUB_ADULTS, tvSubAdultNo.getText().toString());
            header.put(KEY_NO_OF_CALVES, tvCalvesNo.getText().toString());
            header.put(KEY_TOTAL_NUMBER, tvTotalNo.getText().toString());

            header.put(KEY_FINAL_LAT, finalLat);
            header.put(KEY_FINAL_LONG, finalLong);

            header.put(KEY_ELEPHANTS_REACTION_DURING_ENTRY, spinnerElephantsReactionDuringEntry.getSelectedItem().toString());
            header.put(KEY_ELEPHANTS_REACTION_DURING_ENTRY_OTHER, tvElephantsReactionDuringEntryOther.getText().toString());

            header.put(KEY_PEOPLES_REACTION_MAKING_NOISE, making_noise);
            header.put(KEY_PEOPLES_REACTION_THROWING_FLAME_TORCHES, throwing_flame_torches);
            header.put(KEY_PEOPLES_REACTION_THROWING_STONES, throwing_stones);
            header.put(KEY_PEOPLES_REACTION_USING_MEGA_TORCHES, using_mega_torches);
            header.put(KEY_PEOPLES_REACTION_INFORM_PARK_FOR_HELP, inform_park);
            header.put(KEY_PEOPLES_REACTION_OTHER, other_reaction_by_people);
            header.put(KEY_PEOPLES_REACTION_SPECIFY_OTHER, tvPeopleReactionSpecifyOther.getText().toString());

            header.put(KEY_ELEPHANTS_REACTION_AFTER_ENTRY, spinnerElephantsReactionAfterEntry.getSelectedItem().toString());
            header.put(KEY_ELEPHANTS_REACTION_AFTER_ENTRY_SPECIFY_OTHER, tvElephantsReactionAfterEntrySpecifyOther.getText().toString());
            header.put(KEY_ELEPHANT_ENTERED_FROM_AREA_ARE_NOT_COVERED, tvElephantEnteredFromArea.getText().toString());
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.d(TAG, "convertDataToJsonSave: " + jsonToSend);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void convertDataToJsonSend() {

        String reactionByPeople = "";

        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        try {
            post_dict.put("tablename", "tbl_elephant_activity_recording");

            JSONObject header = new JSONObject();

            header.put(KEY_PROJECT_CODE, tvProjectCode.getText().toString());
            header.put(KEY_DATE, tvDate.getText().toString());
            header.put(KEY_TIME, tvTime.getText().toString());
            header.put(KEY_POTENTIAL_ATTRACTANTS, spinnerPotentialAttractants.getSelectedItem().toString());
            header.put(KEY_POTENTIAL_ATTRACTANTS_OTHER, tvPotentialAttractantsOther.getText().toString());
            header.put(KEY_HERD_SIZE, tvHerdSize.getText().toString());

            header.put(KEY_NO_OF_TUSKERS, tvTuskersNo.getText().toString());
            header.put(KEY_NO_OF_FEMALES, tvFemaleNo.getText().toString());
            header.put(KEY_NO_OF_SUB_ADULTS, tvSubAdultNo.getText().toString());
            header.put(KEY_NO_OF_CALVES, tvCalvesNo.getText().toString());
            header.put(KEY_TOTAL_NUMBER, tvTotalNo.getText().toString());

            header.put(KEY_FINAL_LAT, finalLat);
            header.put(KEY_FINAL_LONG, finalLong);

            header.put(KEY_ELEPHANTS_REACTION_DURING_ENTRY, spinnerElephantsReactionDuringEntry.getSelectedItem().toString());
            header.put(KEY_ELEPHANTS_REACTION_DURING_ENTRY_OTHER, tvElephantsReactionDuringEntryOther.getText().toString());

//            header.put(KEY_PEOPLES_REACTION_MAKING_NOISE, making_noise);
//            header.put(KEY_PEOPLES_REACTION_THROWING_FLAME_TORCHES, throwing_flame_torches);
//            header.put(KEY_PEOPLES_REACTION_THROWING_STONES, throwing_stones);
//            header.put(KEY_PEOPLES_REACTION_USING_MEGA_TORCHES, using_mega_torches);
//            header.put(KEY_PEOPLES_REACTION_INFORM_PARK_FOR_HELP, inform_park);
//            header.put(KEY_PEOPLES_REACTION_OTHER, other_reaction_by_people);

            if (cbMakingNoise.isChecked()) {
                reactionByPeople = reactionByPeople + "Making  noise - shouting or beating items, ";
            }
            if (cbThrowingFlame.isChecked()) {
                reactionByPeople = reactionByPeople + "Throwing flame torches, ";
            }
            if (cbThrowingStones.isChecked()) {
                reactionByPeople = reactionByPeople + "Throwing stones, ";
            }
            if (cbUsingMegaTorches.isChecked()) {
                reactionByPeople = reactionByPeople + "Using mega torch, ";
            }
            if (cbInformParkAuthorities.isChecked()) {
                reactionByPeople = reactionByPeople + "Informed park authorities for help, ";
            }
            if (cbOthers.isChecked()) {
                reactionByPeople = reactionByPeople + "Others, ";
            }
            header.put(KEY_REACTION_BY_PEOPLE, reactionByPeople);
            header.put(KEY_PEOPLES_REACTION_SPECIFY_OTHER, tvPeopleReactionSpecifyOther.getText().toString());

            header.put(KEY_ELEPHANTS_REACTION_AFTER_ENTRY, spinnerElephantsReactionAfterEntry.getSelectedItem().toString());
            header.put(KEY_ELEPHANTS_REACTION_AFTER_ENTRY_SPECIFY_OTHER, tvElephantsReactionAfterEntrySpecifyOther.getText().toString());
            header.put(KEY_ELEPHANT_ENTERED_FROM_AREA_ARE_NOT_COVERED, tvElephantEnteredFromArea.getText().toString());
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            Log.d(TAG, "convertDataToJsonSend: " + jsonToSend);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            Log.e(TAG, "onActivityResult: " + jsonArrayGPS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        tvElevation.setText(split_altitude);
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
            startActivity(new Intent(ElephantActivityRecordingCSUActivity.this, MapPointActivity.class));
        } else {

            if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(ElephantActivityRecordingCSUActivity.this, MapPointActivity.class));
            } else {
                Default_DIalog.showDefaultDialog(context, R.string.app_name, "Please Wait until Gps is Initilized");

            }
        }
    }


    public void saveFORMDATA() {
        if (isGpsTracking) {
            Toast.makeText(getApplicationContext(), "Please end GPS Tracking.", Toast.LENGTH_SHORT).show();
        } else {
            if (isGpsTaken) {
                jsonLatLangArray = jsonArrayGPS.toString();

                convertDataToJsonSave();

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                final Dialog showDialog = new Dialog(context);
                showDialog.setContentView(R.layout.date_input_layout);
                final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                FormNameToInput.setText("Elephant Activity Recording");

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
                            String[] data = new String[]{"91", formName, dateDataCollected, jsonToSend, "",
                                    "", "Not Sent", "0"};

                            DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                            dataBaseConserVationTracking.open();
                            long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                            new PromptDialog(ElephantActivityRecordingCSUActivity.this)
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
                            convertDataToJsonSend();
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
                Log.e(TAG, "doInBackground: " + text);
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
                    new PromptDialog(ElephantActivityRecordingCSUActivity.this)
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
                    String[] data = new String[]{"91", "Elephant Activity Recording", dateString, jsonToSend, "", "", "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(ElephantActivityRecordingCSUActivity.this)
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
                Log.e("elephantStatusRecording", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(ElephantActivityRecordingCSUActivity.this);
            gps.canGetLocation();
            btnGpsStart.setEnabled(true);
            Log.d(TAG, "initilizeUI: " + "No JSON to parse");
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

        tvProjectCode.setText(jsonObj.getString(KEY_PROJECT_CODE));
        tvDate.setText(jsonObj.getString(KEY_DATE));
        tvTime.setText(jsonObj.getString(KEY_TIME));
        tvPotentialAttractantsOther.setText(jsonObj.getString(KEY_POTENTIAL_ATTRACTANTS_OTHER));
        tvHerdSize.setText(jsonObj.getString(KEY_HERD_SIZE));

        tvTuskersNo.setText(jsonObj.getString(KEY_NO_OF_TUSKERS));
        tvFemaleNo.setText(jsonObj.getString(KEY_NO_OF_FEMALES));
        tvSubAdultNo.setText(jsonObj.getString(KEY_NO_OF_SUB_ADULTS));
        tvCalvesNo.setText(jsonObj.getString(KEY_NO_OF_CALVES));
        tvTotalNo.setText(jsonObj.getString(KEY_TOTAL_NUMBER));

        tvElephantsReactionDuringEntryOther.setText(jsonObj.getString(KEY_ELEPHANTS_REACTION_DURING_ENTRY_OTHER));

        tvPeopleReactionSpecifyOther.setText(jsonObj.getString(KEY_PEOPLES_REACTION_SPECIFY_OTHER));

        tvElephantsReactionAfterEntrySpecifyOther.setText(jsonObj.getString(KEY_ELEPHANTS_REACTION_AFTER_ENTRY_SPECIFY_OTHER));
        tvElephantEnteredFromArea.setText(jsonObj.getString(KEY_ELEPHANT_ENTERED_FROM_AREA_ARE_NOT_COVERED));


        //set spinner
        int potentialAttractantsPos = potentialAttractantsAdpt.getPosition(jsonObj.getString(KEY_POTENTIAL_ATTRACTANTS));
        spinnerPotentialAttractants.setSelection(potentialAttractantsPos);
        if (jsonObj.getString(KEY_POTENTIAL_ATTRACTANTS).equals("Others")) {
            tvPotentialAttractantsOther.setVisibility(View.VISIBLE);
        }

        int elephantsReactionDuringPos = elephantsReactionDuringEntryAdpt.getPosition(jsonObj.getString(KEY_ELEPHANTS_REACTION_DURING_ENTRY));
        spinnerElephantsReactionDuringEntry.setSelection(elephantsReactionDuringPos);
        if (jsonObj.getString(KEY_ELEPHANTS_REACTION_DURING_ENTRY).equals("Others")) {
            tvElephantsReactionDuringEntryOther.setVisibility(View.VISIBLE);
        }

        int elephantsReactionAfterPos = elephantsReactionAfterEnryAdpt.getPosition(jsonObj.getString(KEY_ELEPHANTS_REACTION_AFTER_ENTRY));
        spinnerElephantsReactionAfterEntry.setSelection(elephantsReactionAfterPos);
        if (jsonObj.getString(KEY_ELEPHANTS_REACTION_AFTER_ENTRY).equals("Any others")) {
            tvElephantsReactionAfterEntrySpecifyOther.setVisibility(View.VISIBLE);
        }

//        set Checkboxes
        if (jsonObj.getString(KEY_PEOPLES_REACTION_MAKING_NOISE).equals("yes")) {
            cbMakingNoise.setChecked(true);
        }
        if (jsonObj.getString(KEY_PEOPLES_REACTION_THROWING_FLAME_TORCHES).equals("yes")) {
            cbThrowingFlame.setChecked(true);
        }
        if (jsonObj.getString(KEY_PEOPLES_REACTION_THROWING_STONES).equals("yes")) {
            cbThrowingStones.setChecked(true);
        }
        if (jsonObj.getString(KEY_PEOPLES_REACTION_USING_MEGA_TORCHES).equals("yes")) {
            cbUsingMegaTorches.setChecked(true);
        }
        if (jsonObj.getString(KEY_PEOPLES_REACTION_INFORM_PARK_FOR_HELP).equals("yes")) {
            cbInformParkAuthorities.setChecked(true);
        }
        if (jsonObj.getString(KEY_PEOPLES_REACTION_OTHER).equals("yes")) {
            cbOthers.setChecked(true);
            tvPeopleReactionSpecifyOther.setVisibility(View.VISIBLE);
        }

    }

    //new adjustment
    public void parseArrayGPS(String arrayToParse) {
        Log.e(TAG, "parseArrayGPS: " + arrayToParse);
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

}
