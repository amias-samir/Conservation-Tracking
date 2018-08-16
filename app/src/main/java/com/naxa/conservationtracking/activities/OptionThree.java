package com.naxa.conservationtracking.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.naxa.conservationtracking.R;
import com.naxa.conservationtracking.application.ApplicationClass;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ramaan on 1/5/2016.
 */
public class OptionThree extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton camSite , camFenchOne, camFenchTwo;
    int CAMERA_PIC_REQUEST = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_option);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        camSite = (ImageButton) findViewById(R.id.forestProtectionPhotographSite);
        camFenchOne = (ImageButton) findViewById(R.id.forestProtectionPhotographFench);
        camFenchTwo = (ImageButton) findViewById(R.id.forestProtectionPhotographMonitoring);
        camFenchOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        camFenchTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        camSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == CAMERA_PIC_REQUEST)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //  ImageView image =(ImageView) findViewById(R.id.Photo);
            // image.setImageBitmap(thumbnail);
            saveToExternalSorage(thumbnail);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Picture Not taken", Toast.LENGTH_LONG).show();
        }
    }
    private void saveToExternalSorage(Bitmap thumbnail) {
        // TODO Auto-generated method stub
        //String merocinema="Mero Cinema";
        //todo: Nishon
        String movname= "com/naxa/conservationtracking/forest";

        File file1 = new File(ApplicationClass.PHOTO_PATH, movname);
        if (!file1.mkdirs()) {
            Toast.makeText(getApplicationContext(), "Not Created", Toast.LENGTH_SHORT).show();
        }

        if (file1.exists ()) file1.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), "Saved "+movname, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
