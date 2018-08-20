package com.naxa.conservationtrackingapp.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.naxa.conservationtrackingapp.R;

/**
 * Created by user1 on 9/29/2015.
 */
public class OptionTwo extends AppCompatActivity{
    TextInputLayout a,b,c,d;
    Button btn;
    AutoCompleteTextView aa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter);
        btn = (Button) findViewById(R.id.button3);
        a = (TextInputLayout) findViewById(R.id.to_text_input_layout);
        b = (TextInputLayout) findViewById(R.id.garenteeName);
        aa = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewGarentee);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setVisibility(View.GONE);
                b.setHint("SecondHin");
                String wor = aa.getText().toString();
                Log.e("TAGG" , wor);
            }
        });
    }
}
