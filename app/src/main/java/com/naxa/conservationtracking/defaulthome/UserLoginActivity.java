package com.naxa.conservationtracking.defaulthome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.naxa.conservationtracking.MainActivity;
import com.naxa.conservationtracking.R;
import com.naxa.conservationtracking.database.DataBaseConserVationTracking;
import com.naxa.conservationtracking.forest.Cf_Detail;

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

import javax.net.ssl.HttpsURLConnection;

import Utls.SharedPreferenceUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;

public class UserLoginActivity extends AppCompatActivity {
    private static final String TAG = "UserLoginActivity";

    Toolbar toolbar;
    ProgressDialog mProgressDlg;
    @BindView(R.id.login_form_User_Name)
    AutoCompleteTextView tvUserName;
    @BindView(R.id.login_form_User_password)
    AutoCompleteTextView tvUserPassword;
    @BindView(R.id.login_form_Button_User_Login)
    Button loginFormButtonUserLogin;


    String jsonToSend, dataSentStatus="";

    SharedPreferenceUtils sharedPreferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
        initTolbar();

        sharedPreferenceUtils = new SharedPreferenceUtils(this);

    }

    private void initTolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.login_form_Button_User_Login)
    public void onViewClicked() {

        if(TextUtils.isEmpty(tvUserName.getText().toString()) && TextUtils.isEmpty(tvUserPassword.getText().toString())){
            Toast.makeText(this, "User Name/Password field cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
            showProgressDialog();
            convertDataToJson();
            sendDatToserver();

        }

    }

    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button
        try {
            JSONObject header = new JSONObject();
            header.put(SharedPreferenceUtils.KEY_USER_NAME, tvUserName.getText().toString());
            header.put(SharedPreferenceUtils.KEY_USER_PASSWORD, tvUserPassword.getText().toString());

            jsonToSend = header.toString();
            Log.d(TAG, "convertDataToJson: "+ jsonToSend);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //same for all
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
                text = POST(getString(R.string.api_login_url));
                JSONObject jsonObject = new JSONObject(text);
                dataSentStatus = jsonObject.getString("status");


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            dissmissPregressDialog();
            if (result != null) {

                //{"status":200,"data":"Login Success"}
                if (dataSentStatus.equals("200")) {

                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.KEY_USER_NAME, tvUserName.getText().toString());
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.KEY_USER_PASSWORD, tvUserPassword.getText().toString());
                    sharedPreferenceUtils.setValue(SharedPreferenceUtils.KEY_IS_USER_LOGGED_IN, true);

                    new PromptDialog(UserLoginActivity.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText(getString(R.string.success))
                            .setContentText(getString(R.string.login_success))
                            .setPositiveListener("okay", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                    startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).show();


                } else {
                    new PromptDialog(UserLoginActivity.this)
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
                        .appendQueryParameter("data", jsonToSend);

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

    private void showProgressDialog(){
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Please wait...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
    }

    private void dissmissPregressDialog(){
        if(mProgressDlg!= null && mProgressDlg.isShowing()){
            mProgressDlg.dismiss();
        }
    }

}
