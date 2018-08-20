package com.naxa.conservationtrackingapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naxa.conservationtrackingapp.R;

import java.util.ArrayList;

import com.naxa.conservationtrackingapp.forest.NurseryDetails;

/**
 * Created by ramaan on 4/9/2016.
 */
public class Multiple_Selection_Dialog_Nurserry1 {

    public static void multipleChoice_NurseryDetail(Context context, int title, String displayText) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.multiple_checkbox3);

        final ArrayList<String> selection = new ArrayList<String>();
        TextView final_text;
        final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;
        final CheckBox cbBabiyo, cbNapier, cbAxe, cbShaw, cbSickle, cbTimber, cbGun;
        final String[] compiledText = {""};
        final String[] text1 = new String[1];
        final String[] text2 = new String[1];
        Button btnOk = (Button) showDialog.findViewById(R.id.done);
        showDialog.setTitle("Select Items");

        showDialog.setCancelable(true);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);


        editText1 = (EditText) showDialog.findViewById(R.id.editText_1);
        editText2 = (EditText) showDialog.findViewById(R.id.editText_2);

        cbBabiyo = (CheckBox) showDialog.findViewById(R.id.checkbox1);
        cbNapier = (CheckBox) showDialog.findViewById(R.id.checkbox2);

        editText1.setEnabled(false);
        editText2.setEnabled(false);

        cbBabiyo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText1.setEnabled(true);

                } else {
                    editText1.setEnabled(false);
                }
            }
        });
        cbNapier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText2.setEnabled(true);

                } else {
                    editText2.setEnabled(false);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String to_return = "";

                if (cbBabiyo.isChecked()) {
                    to_return = cbBabiyo.getText().toString() + ": " + editText1.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cbNapier.isChecked()) {
                    to_return = cbNapier.getText().toString() + ": " + editText2.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }
                if (cbBabiyo.isChecked() && cbNapier.isChecked()) {
                    to_return = cbBabiyo.getText().toString()+ ": " + editText1.getText().toString() + ", " + cbNapier.getText().toString()
                            + ": " + editText2.getText().toString();
                    Log.i("Return string", to_return);

                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();
                } else {

                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();
                }
            }
        });
//        return displayText;

    }
}
