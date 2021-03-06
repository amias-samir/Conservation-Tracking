package com.naxa.conservationtrackingapp.defaulthome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.naxa.conservationtrackingapp.PhoneUtils;
import com.naxa.conservationtrackingapp.R;
import com.naxa.conservationtrackingapp.activities.General_Form;
import com.naxa.conservationtrackingapp.activities.SavedFormsActivity;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_ClimateChange;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_CommunitySupport;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_Conservation_Education;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_Forest;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_Human_Wildlife_Conflict_Management;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_WMM;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_WildlifeMonitoringTechniques;
import com.naxa.conservationtrackingapp.fragment_main.Fragment_Wildlife_Trade_Control;

import Utls.SharedPreferenceUtils;
import br.liveo.Model.HelpLiveo;
import br.liveo.Model.Navigation;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.naxa.conservationtrackingapp.database.DataBaseConserVationTracking.IMEI;

public class DefaultHomeActivity extends NavigationLiveo implements OnItemClickListener {

    Context context = this;

    //Susan
    private final int MULTIPLE_PERMISSION_CODE = 22;
    static final Integer LOCATION = 0x1;
    static final Integer GPS_SETTINGS = 0x7;
    static final String TAG = "MainActivity";
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;

    private HelpLiveo mHelpLiveo;
    Context con = this;
    //        ConnectonDetector connectonDetector;
    FragmentManager mFragmentManager = getSupportFragmentManager();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


}

    @Override
    public void onInt(Bundle savedInstanceState) {

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();
//        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);

        // User Information
//        this.userName.setText("Ramesh Parajuli");
//        this.userEmail.setText("ramesh.parajuli575@gmail.com");
//        this.userPhoto.setImageResource(R.drawable.face);
        this.userBackground.setImageResource(R.drawable.profile_image);
//
        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.addSeparator();
        mHelpLiveo.add(getString(R.string.home), R.mipmap.home);
        mHelpLiveo.addSeparator();
        mHelpLiveo.addSubHeader("PROJECT CATEGORIES");
        mHelpLiveo.add(getString(R.string.forest), R.mipmap.forest);
//            mHelpLiveo.addSeparator();
        mHelpLiveo.add(getString(R.string.wildlife_mgmt_n_monitoring), R.mipmap.wmm);
//            mHelpLiveo.addSeparator(); // Iteem separator
        mHelpLiveo.add(getString(R.string.wildlife_trade_control), R.mipmap.wildlifetrade_control);
//            mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.human_wildlife_conflict_mgmt), R.mipmap.conflict);
//            mHelpLiveo.addSeparator();
//        mHelpLiveo.add(getString(R.string.climate_change), R.mipmap.climate);
////            mHelpLiveo.addSeparator();
//        mHelpLiveo.add(getString(R.string.community_support), R.mipmap.community_support);
////            mHelpLiveo.addSeparator();
//        mHelpLiveo.add(getString(R.string.conservation_education), R.mipmap.conservation_education);
////            mHelpLiveo.addSeparator();
//        mHelpLiveo.add(getString(R.string.wildlife_monitoring_techniques), R.mipmap.monitoring_techniquies);

        mHelpLiveo.add(getString(R.string.general_form), R.mipmap.general_form);

        mHelpLiveo.add(getString(R.string.save_form), R.mipmap.saved_forms);

        mHelpLiveo.addSeparator();

//        with(this, Navigation.THEME_DARK). add theme dark
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this, Navigation.THEME_DARK) // default theme is dark
                .startingPosition(1) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .colorItemSelected(R.color.teal)
                .colorNameSubHeader(R.color.light_teal)
                .footerItem(R.string.about_us, R.drawable.about_us)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };
    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//                Default_DIalog.showDefaultDialog(con, R.string.app_name, "चाडै आउँदैछ। कृपया प्रतिक्षा गर्नुहोस् ।। ");
        }
    };

    @Override //The "R.id.container" should be used in "beginTransaction (). Replace"
    public void onItemClick(int position) {
        Fragment mFragment = null;
//        FragmentManager mFragmentManager = getSupportFragmentManager();
        String title = getString(R.string.app_name);
        int color = R.color.teal;
//            connectonDetector = new ConnectonDetector(this);
//            boolean isConnected = connectonDetector.isConnectedToInternet();
//            Toast.makeText(getApplicationContext(),"clicked : "+position ,Toast.LENGTH_SHORT ).show();
        switch (position) {
            case 1:

                mFragment = new Fragment_Grid_Home_Default();
                title = getString(R.string.home);
                color = R.color.teal;
                break;

            case 4:
                mFragment = new Fragment_Forest();
                title = getString(R.string.forest);
                color = R.color.forest;
                break;
            case 5:
                mFragment = new Fragment_WMM();
                title = getString(R.string.wildlife_mgmt_n_monitoring);
                color = R.color.wmm;
                break;
            case 6:
                mFragment = new Fragment_Wildlife_Trade_Control();
                title = getString(R.string.wildlife_trade_control);
                color = R.color.wild_life_trade_control;
                break;
            case 7:
                mFragment = new Fragment_Human_Wildlife_Conflict_Management();
                title = getString(R.string.human_wildlife_conflict_mgmt);
                color = R.color.human_wildLife_conflict;
                break;
//            case 8:
//                mFragment = new Fragment_ClimateChange();
//                title = getString(R.string.climate_change);
//                color = R.color.climate_change;
//                break;
//            case 9:
//                mFragment = new Fragment_CommunitySupport();
//                title = getString(R.string.community_support);
//                color = R.color.community_support;
//                break;
//            case 10:
//                mFragment = new Fragment_Conservation_Education();
//                title = getString(R.string.conservation_education);
//                color = R.color.conservation_education;
//                break;
//            case 11:
//                mFragment = new Fragment_WildlifeMonitoringTechniques();
//                title = getString(R.string.wildlife_monitoring_techniques);
//                color = R.color.wildlife_monitoring_technique;
//                break;
            case 8:
                startActivity(new Intent(DefaultHomeActivity.this, General_Form.class));
                title = getString(R.string.general_form);
                color = R.color.general_frorm;
                break;
            case 9:
                startActivity(new Intent(DefaultHomeActivity.this, SavedFormsActivity.class));
//                mFragment = new Fragment_SavedForms();
//                title = getString(R.string.save_form);
//                color = R.color.saved_form;
                break;

        }

        if (mFragment != null) {
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, mFragment)
                    .commit();
        }
        getSupportActionBar().setTitle(title);
        getToolbar().setBackgroundResource(color);

        setElevationToolBar(position != 2 ? 15 : 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menu, menu);
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

//            updateApp();
            showAlertDialogBeforeUpdate();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.user_login) {

//            updateApp();
            startActivity(new Intent(DefaultHomeActivity.this, UserLoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateApp() {
        String url = "http://naxa.com.np/apps/cta.apk";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        this.startActivity(browserIntent);

    }

    public void showAlertDialogBeforeUpdate() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.alert_dialog_before_action_layout);
        final TextView tvWarningDetails = (TextView) showDialog.findViewById(R.id.textViewWarning);
        final Button btnYes = (Button) showDialog.findViewById(R.id.alertButtonYes);
        final Button btnNo = (Button) showDialog.findViewById(R.id.alertButtonNo);

        tvWarningDetails.setText("Please send your data first before App update");
        btnYes.setText("Update Anyway");
        btnNo.setText("Send Data");


        showDialog.setTitle("WARNING !!!");
        showDialog.setCancelable(false);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
                updateApp();
//                                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
                Intent intent = new Intent(DefaultHomeActivity.this, SavedFormsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(DefaultHomeActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    private boolean isPermissionAllowed() {

        boolean hasCameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_GRANTED;
        boolean hasStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        boolean hasLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
        boolean hasPhonePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;

        return hasCameraPermission && hasStoragePermission && hasLocationPermission && hasPhonePermission;
    }


    //Requesting permission
    private void requestMultiplePermission() {


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, MULTIPLE_PERMISSION_CODE);

    }

    //This method will be called when the user will tap on allow or deny
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MULTIPLE_PERMISSION_CODE:
                if (grantResults.length == 0) {
                    Toast.makeText(context, " Required permission were not given\napp may function improperly", Toast.LENGTH_LONG).show();

                    return;
                }

                boolean hasCameraPermission = grantResults[0] == PERMISSION_GRANTED;
                boolean hasStoragePermission = grantResults[1] == PERMISSION_GRANTED;
                boolean hasLocationPermission = grantResults[2] == PERMISSION_GRANTED;
                boolean hasPhonePermission = grantResults[3] == PERMISSION_GRANTED;

                if (hasPhonePermission) {
                    SharedPreferenceUtils.getInstance(context).setValue(IMEI, PhoneUtils.getDeviceId());
                }


                if (hasLocationPermission) {
                    askForGPS();
                }

                if (hasCameraPermission && hasStoragePermission && hasLocationPermission & hasPhonePermission) {
                    //unhandled
                } else {
                    Toast.makeText(context, "All required permission were not given\napp may function improperly", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                //unhandled
        }

    }
}
