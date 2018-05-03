package com.naxa.conservationtracking.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.naxa.conservationtracking.MainActivity;
import com.naxa.conservationtracking.R;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ramaan on 1/23/2016.
 */
public class ServiceTest extends AppCompatActivity  {
    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    JSONArray data = null;
    ArrayList<String> question;
    Context context = this ;
    ProgressDialog mProgressDlg ;
    static int backPressed = 0;
    String jsonToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        senddatatoserver();

        mProgressDlg = new ProgressDialog(this);


        mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
        RestApii restApii = new RestApii();
        restApii.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        finish();
    }

    public void senddatatoserver() {
        //function in the activity that corresponds to the layout button

        JSONObject post_dict = new JSONObject();

        try {
            post_dict.put("tablename", "tbl_cf_detail");
            post_dict.put("user_id", "1");
            post_dict.put("cat_id", "1");

            JSONObject header = new JSONObject();
            header.put("name_bz", "ramesh");
            header.put("name_cf", "test");
            header.put("cf_longitude", "87.56");
            header.put("boundry", "[[27.0656,85.255],[27.3564, 85.3564],[27.98998, 85.6898]]");
            header.put("area", "989.948");
            header.put("forest_conditon", "High");
            header.put("natural_regeneration", "High");
            header.put("grazing_pressure", "High");
            header.put("wild_species_list", "elephant,monkey,snake");
            post_dict.put("formdata", header);

            jsonToSend = post_dict.toString();



        } catch (JSONException e) {
            e.printStackTrace();
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
            String urll = "http://naxa.com.np/cta/webapi/enter_record";


            String text = null;
            String txtDisp = null;
            StringBuffer sb = new StringBuffer();
            question = new ArrayList<>();

            try {
                text = POST(urll);

                Log.e("tagg", "Before parsing");
                Log.e("error", "" + text);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result != null) {
                mProgressDlg.dismiss();
                Toast.makeText(getApplicationContext(),"SERVICE "+result ,Toast.LENGTH_SHORT).show();
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
//                writer.write(getPostDataString(postDataParams));




                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("data", jsonToSend);
//                        .appendQueryParameter("password", "mcq123");
                String query = builder.build().getEncodedQuery();

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result+=line;
                    }
                }
                else {
                    result="";
                }


            }  catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

    }

}

