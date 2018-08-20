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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
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
public class InstitutionalSupport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    int CAMERA_PIC_REQUEST = 2;
    int CAMERA_PIC_REQUEST_B = 3;
    Spinner spinnerLandscape, spinnerActivity;
    ArrayAdapter landscapeAdpt, singleSpinnerAdpt;
    Button send, save, startGps, previewMap;
    ProgressDialog mProgressDlg;
    Context context = this;
    GPS_TRACKER_FOR_POINT gps;
    String jsonToSend, photoTosend;
    String imagePath, imagePathMonitoring, encodedImage = null, encodedImageMonitoring = null, imageName = "no_photo", imageNameMonitoring = "no_photo";
    ImageButton photo, photoMonitoring;
    ImageView previewImageSite, previewImageSiteMonitoring;

    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";

    boolean isGpsTracking = false;
    boolean isGpsTaken = false;
    double initLat;
    double finalLat;
    double initLong;
    double finalLong;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;
    AlarmManager alarmManager;
    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    List<Location> gpslocation = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    String latLangArray = "", jsonLatLangArray = "";
    String landscape;
    String other_landscape;
    String funding_source;
    String other_equipment;
    String projectCode, activity, specify_activity, agreement_no, grantee_name, fiscal_year,
            district, vdc, name_bz_nf_cf, number, funding_tal, funding_community, other_fund_support,
            others;
    String plantationPolygonArea;
    JSONArray jsonArrayGPS = new JSONArray();
    TextView tvBoundryUsingGps;
    AutoCompleteTextView tvOtherEquipment, tvOtherLandscape, tvFundingSource, tvProjectCode, tvSpecifyActivity, tvAgreementNo, tvGranteeName,
            tvFiscalYear, tvDistrict, tvVdc, tvName_bz_nf_cf, tvNumber, tvFundingTal, tvFundingCommunity, tvOtherFundSupport, tvOthers;

    String userNameToSend, passwordToSend;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    String dataSentStatus = "", dateString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_is);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProjectCode = (AutoCompleteTextView) findViewById(R.id.ProjectCode);
        spinnerLandscape = (Spinner) findViewById(R.id.cs_is_detail_landscape);
        tvOtherLandscape = (AutoCompleteTextView) findViewById(R.id.OtherlandscapeName);
        tvFundingSource = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_fundingSource);
        spinnerActivity = (Spinner) findViewById(R.id.cs_is_spinner_detail_officeEquipmentStatus);
        tvSpecifyActivity = (AutoCompleteTextView) findViewById(R.id.cs_is_specify_others_activity);
        tvAgreementNo = (AutoCompleteTextView) findViewById(R.id.cs_is_Agreement_No);
        tvGranteeName = (AutoCompleteTextView) findViewById(R.id.cs_is_Grantee_Name);
        tvFiscalYear = (AutoCompleteTextView) findViewById(R.id.cs_is_fiscal_Year);
        tvDistrict = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_DistrictName);
        tvVdc = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_Vdc_Municipality_Name);
        tvName_bz_nf_cf = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_nameof_bzForestCorridor);
        tvNumber = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_no);
        tvFundingTal = (AutoCompleteTextView) findViewById(R.id.cs_is_tal);
        tvFundingCommunity = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_community_contribution);
        tvOthers = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_notes);
        tvOtherFundSupport = (AutoCompleteTextView) findViewById(R.id.cs_is_detail_FundOthers);
        setCurrentDateOnView();
        addListenerOnButton();

        photo = (ImageButton) findViewById(R.id.cs_is_detail_Beforephoto);
        previewImageSite = (ImageView) findViewById(R.id.cs_is__detail_PhotographBeforeimageViewPreview);
        previewImageSite.setVisibility(View.GONE);

        photoMonitoring = (ImageButton) findViewById(R.id.cs_is_detail_PhotographIntervention);
        previewImageSiteMonitoring = (ImageView) findViewById(R.id.cs_is__detail_PhotograpInterventionimageViewPreview);
        previewImageSiteMonitoring.setVisibility(View.GONE);

        startGps = (Button) findViewById(R.id.cs_is_GpsStart);
//        endGps = (Button) findViewById(R.id.cs_is_detail_GpsEnd);

        send = (Button) findViewById(R.id.cs_is_detail_send);
        save = (Button) findViewById(R.id.cs_is_detail_save);
        previewMap = (Button) findViewById(R.id.cs_is_preview_map);
        previewMap.setEnabled(false);

        landscapeAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.LANDSCAPE);
        landscapeAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLandscape.setAdapter(landscapeAdpt);
        spinnerLandscape.setOnItemSelectedListener(this);

        singleSpinnerAdpt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.CS_IS_OFFICE_EQUIPMENT);
        singleSpinnerAdpt
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(singleSpinnerAdpt);
        spinnerActivity.setOnItemSelectedListener(this);

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
                            specify_activity = tvSpecifyActivity.getText().toString();
                            agreement_no = tvAgreementNo.getText().toString();
                            grantee_name = tvGranteeName.getText().toString();
                            fiscal_year = tvFiscalYear.getText().toString();
                            district = tvDistrict.getText().toString();
                            vdc = tvVdc.getText().toString();
                            name_bz_nf_cf = tvName_bz_nf_cf.getText().toString();
                            number = tvNumber.getText().toString();
                            funding_tal = tvFundingTal.getText().toString();
                            funding_community = tvFundingCommunity.getText().toString();
                            other_fund_support = tvOtherFundSupport.getText().toString();
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
                        specify_activity = tvSpecifyActivity.getText().toString();
                        agreement_no = tvAgreementNo.getText().toString();
                        grantee_name = tvGranteeName.getText().toString();
                        fiscal_year = tvFiscalYear.getText().toString();
                        district = tvDistrict.getText().toString();
                        vdc = tvVdc.getText().toString();
                        name_bz_nf_cf = tvName_bz_nf_cf.getText().toString();
                        number = tvNumber.getText().toString();
                        funding_tal = tvFundingTal.getText().toString();
                        funding_community = tvFundingCommunity.getText().toString();
                        other_fund_support = tvOtherFundSupport.getText().toString();
                        others = tvOthers.getText().toString();

                        jsonLatLangArray = jsonArrayGPS.toString();

                        convertDataToJson();

                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;

                        final Dialog showDialog = new Dialog(context);
                        showDialog.setContentView(R.layout.date_input_layout);
                        final EditText FormNameToInput = (EditText) showDialog.findViewById(R.id.input_tableName);
                        final EditText dateToInput = (EditText) showDialog.findViewById(R.id.input_date);
                        FormNameToInput.setText("Institutional Support");

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
                                    String[] data = new String[]{"61", formName, dateDataCollected, jsonToSend, jsonLatLangArray,
                                            "" + imageName + "andSecondImage=" + imageNameMonitoring, "Not Sent", "0"};
                                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                                    dataBaseConserVationTracking.open();
                                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);

                                    new PromptDialog(InstitutionalSupport.this)
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

        photoMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST_B);
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
                    startActivity(new Intent(InstitutionalSupport.this, MapPointActivity.class));
                } else {

                    if (GPS_TRACKER_FOR_POINT.GPS_POINT_INITILIZED) {
                        StaticListOfCoordinates.setList(listCf);
                        startActivity(new Intent(InstitutionalSupport.this, MapPointActivity.class));
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

        if (spinnerId == R.id.cs_is_detail_landscape) {
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

        if (spinnerId == R.id.cs_is_spinner_detail_officeEquipmentStatus) {
            switch (position) {
                case 0:
                    activity = "Computer";
                    tvSpecifyActivity.setVisibility(View.GONE);
                    tvSpecifyActivity.setText("");
                    break;
                case 1:
                    activity = "Stationary";
                    tvSpecifyActivity.setVisibility(View.GONE);
                    tvSpecifyActivity.setText("");
                    break;
                case 2:
                    activity = "Books";
                    tvSpecifyActivity.setVisibility(View.GONE);
                    tvSpecifyActivity.setText("");
                    break;
                case 3:
                    activity = "Furniture";
                    tvSpecifyActivity.setVisibility(View.GONE);
                    tvSpecifyActivity.setText("");
                    break;
                case 4:
                    activity = "Others";
                    tvSpecifyActivity.setVisibility(View.VISIBLE);

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

        if (requestCode == CAMERA_PIC_REQUEST_B) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                previewImageSiteMonitoring.setVisibility(View.VISIBLE);
                previewImageSiteMonitoring.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail, CAMERA_PIC_REQUEST_B);
                addImage(CAMERA_PIC_REQUEST_B);
//                Toast.makeText(getApplicationContext(), "" + encodedImage, Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //  ImageView image =(ImageView) findViewById(R.id.Photo);
                // image.setImageBitmap(thumbnail);
                previewImageSite.setVisibility(View.VISIBLE);
                previewImageSite.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail, CAMERA_PIC_REQUEST);
                addImage(CAMERA_PIC_REQUEST);
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


    private void saveToExternalSorage(Bitmap thumbnail, int photoID) {
        // TODO Auto-generated method stub
        //String merocinema="Mero Cinema";
//        String movname=getIntent().getExtras().getString("Title");
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        String iName = null;
        if (photoID == CAMERA_PIC_REQUEST) {
            imageName = "institutional_support_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageName;
        } else if (photoID == CAMERA_PIC_REQUEST_B) {
            imageNameMonitoring = "institutional_support_" + timeInMillis + PhoneUtils.getFormatedId();
            iName = imageNameMonitoring;
        }

        File file1 = new File(ApplicationClass.PHOTO_PATH, iName);
//        if (!file1.mkdirs()) {
//            Toast.makeText(getApplicationContext(), "Not Created", Toast.LENGTH_SHORT).show();
//        }

        if (file1.exists()) file1.delete();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), "Saved " + iName, Toast.LENGTH_SHORT).show();
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


    public void addImage(int photoIdNo) {
        if (photoIdNo == CAMERA_PIC_REQUEST) {
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
        } else if (photoIdNo == CAMERA_PIC_REQUEST_B) {
            File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameMonitoring);
            String path = file1.toString();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            Bitmap bm = BitmapFactory.decodeFile(path, options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            // bitmap object

            byte[] byteImage_photo = baos.toByteArray();

            //generate base64 string of image
            encodedImageMonitoring = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            Log.e("IMAGE STRING", "-" + encodedImageMonitoring);
        }

    }

    private void loadImageFromStorage(String path, int id) {
        if (id == 1) {
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
        if (id == 2) {
            try {
                previewImageSiteMonitoring.setVisibility(View.VISIBLE);
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                previewImageSiteMonitoring.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "invalid path", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
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
            String imageNameall = (String) bundle.get("photo");
            String gpsLocationtoParse = (String) bundle.get("gps");

            String status = (String) bundle.get("status");
            if (status.equals("Sent")) {
                save.setEnabled(false);
                send.setEnabled(false);
            }

            String[] images = imageNameall.split("andSecondImage=");
            imageName = images[0];
            imageNameMonitoring = images[1];
            Log.e("institutional_support_", "i-" + imageName);

            if (imageName.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageName);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 1);

                addImage(CAMERA_PIC_REQUEST);
            }
            if (imageNameMonitoring.equals("no_photo")) {
            } else {
                File file1 = new File(ApplicationClass.PHOTO_PATH, imageNameMonitoring);
                String path = file1.toString();
                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                loadImageFromStorage(path, 2);

                addImage(CAMERA_PIC_REQUEST_B);
            }

            try {
                //new adjustment
                Log.e("institutional_support_", "" + jsonToParse);
                parseArrayGPS(gpsLocationtoParse);
                parseJson(jsonToParse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            gps = new GPS_TRACKER_FOR_POINT(InstitutionalSupport.this);
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

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        JSONObject post_dict = new JSONObject();
        JSONObject photo_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_institutional_support");
            post_dict.put("user_id", "1");

            JSONObject header = new JSONObject();
            header.put("project_code", projectCode);
            header.put("landscape", landscape + ":  " + other_landscape);
            header.put("funding_source", funding_source);
            header.put("specify_activity", specify_activity);
            header.put("agreement_no", agreement_no);
            header.put("grantee_name", grantee_name);
            header.put("fiscal_year", fiscal_year);
            header.put("district", district);
            header.put("vdc", vdc);
            header.put("name_bz_nf_cf", name_bz_nf_cf);
            header.put("activity", activity);
            header.put("number", number);
            header.put("funding_tal", funding_tal);
            header.put("funding_community", funding_community);
            header.put("other_fund_support", other_fund_support);
            header.put("longitude", finalLong);
            header.put("latitude", finalLat);
            header.put("others", others);
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();

            photo_dict.put("photo", encodedImage);
            photo_dict.put("photo2", encodedImageMonitoring);
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
        specify_activity = jsonObj.getString("specify_activity");
        grantee_name = jsonObj.getString("grantee_name");
        fiscal_year = jsonObj.getString("fiscal_year");
        district = jsonObj.getString("district");
        vdc = jsonObj.getString("vdc");
        name_bz_nf_cf = jsonObj.getString("name_bz_nf_cf");
        activity = jsonObj.getString("activity");
        number = jsonObj.getString("number");
        funding_tal = jsonObj.getString("funding_tal");
        funding_community = jsonObj.getString("funding_community");
        other_fund_support = jsonObj.getString("other_fund_support");
        others = jsonObj.getString("others");
        finalLong = Double.parseDouble(jsonObj.getString("longitude"));
        finalLat = Double.parseDouble((jsonObj.getString("latitude")));

        Log.e("Plantationdetail", "Parsed data " + agreement_no + grantee_name + fiscal_year);

        tvProjectCode.setText(projectCode);
        tvFundingSource.setText(funding_source);
        tvSpecifyActivity.setText(specify_activity);
        tvAgreementNo.setText(agreement_no);
        tvGranteeName.setText(grantee_name);
        tvFiscalYear.setText(fiscal_year);
        tvDistrict.setText(district);
        tvVdc.setText(vdc);
        tvName_bz_nf_cf.setText(name_bz_nf_cf);
        tvNumber.setText(number);
        tvFundingTal.setText(funding_tal);
        tvFundingCommunity.setText(funding_community);
        tvOtherFundSupport.setText(other_fund_support);
        tvOthers.setText(others);

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

        int forConPos = singleSpinnerAdpt.getPosition(activity);
        spinnerActivity.setSelection(forConPos);


        if (activity.equals("Others")) {
            tvSpecifyActivity.setVisibility(View.VISIBLE);
        } else {
            tvSpecifyActivity.setVisibility(View.GONE);
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
                    new PromptDialog(InstitutionalSupport.this)
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
                    String[] data = new String[]{"61", "Institutional Support", dateString, jsonToSend, jsonLatLangArray,
                            "" + imageName + "andSecondImage=" + imageNameMonitoring, "Sent", "0"};

                    DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(context);
                    dataBaseConserVationTracking.open();
                    long id = dataBaseConserVationTracking.insertIntoTable_Main(data);
                    Log.e("dbID", "" + id);
                    dataBaseConserVationTracking.close();

                } else {
                    new PromptDialog(InstitutionalSupport.this)
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

        }
    };
}
