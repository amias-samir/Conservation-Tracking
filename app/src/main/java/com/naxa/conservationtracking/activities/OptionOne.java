package com.naxa.conservationtracking.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.naxa.conservationtracking.R;
import com.naxa.conservationtracking.model.Constants;

/**
 * Created by user1 on 9/29/2015.
 */
public class OptionOne extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner statusOfFench;
    ArrayAdapter spinnerAdapter ;
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        statusOfFench = (Spinner) findViewById(R.id.forestProtectionStatusFenchingTrencing);
        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Constants.statusOfFench);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusOfFench.setAdapter(spinnerAdapter);
        statusOfFench.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
