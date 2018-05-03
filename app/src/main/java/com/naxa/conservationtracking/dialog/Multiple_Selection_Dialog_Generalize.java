package com.naxa.conservationtracking.dialog;

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

import com.naxa.conservationtracking.R;

import java.util.ArrayList;

import com.naxa.conservationtracking.forest.IllegalLogging;

/**
 * Created by ramaan on 4/9/2016.
 */
public class Multiple_Selection_Dialog_Generalize {
    public static void multipleChoice_NurseryDetail(Context context, int title, String displayText) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.multiple_checkbox_generalized);


        final ArrayList<String> selection = new ArrayList<String>();
        TextView final_text;
        final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;
        CheckBox cbTruck, cbTractor, cbAxe, cbShaw, cbSickle, cbTimber, cbGun;
        final String[] compiledText = {""};
        final String[] text1 = new String[1];
        final String[] text2 = new String[1];
        final String[] text3 = new String[1];
        final String[] text4 = new String[1];
        final String[] text5 = new String[1];
        final String[] text6 = new String[1];
        final String[] text7 = new String[1];
        Button btnOk = (Button) showDialog.findViewById(R.id.done);
        showDialog.setTitle("Select Items");

        showDialog.setCancelable(true);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);


        editText1 = (EditText) showDialog.findViewById(R.id.editText_1);
        editText2 = (EditText) showDialog.findViewById(R.id.editText_2);
        editText3 = (EditText) showDialog.findViewById(R.id.editText_3);
        editText4 = (EditText) showDialog.findViewById(R.id.editText_4);
        editText5 = (EditText) showDialog.findViewById(R.id.editText_5);
        editText6 = (EditText) showDialog.findViewById(R.id.editText_6);
        editText7 = (EditText) showDialog.findViewById(R.id.editText_7);

        cbTruck = (CheckBox) showDialog.findViewById(R.id.checkbox1);
        cbTractor = (CheckBox) showDialog.findViewById(R.id.checkbox2);
        cbAxe = (CheckBox) showDialog.findViewById(R.id.checkbox3);
        cbShaw = (CheckBox) showDialog.findViewById(R.id.checkbox4);
        cbSickle = (CheckBox) showDialog.findViewById(R.id.checkbox5);
        cbTimber = (CheckBox) showDialog.findViewById(R.id.checkbox6);
        cbGun = (CheckBox) showDialog.findViewById(R.id.checkbox7);

        editText1.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);
        editText7.setEnabled(false);


        cbTruck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    editText1.setEnabled(true);
                    text1[0] = editText1.getText().toString();
                    selection.add("Truck-" + text1[0]);
                    compiledText[0] = compiledText[0] + "Truck-" + text1[0];
                    Log.e("edit text1", text1[0]);

                } else {
                    selection.remove("Truck-" + text1[0]);
                    editText1.setEnabled(false);
                }
            }
        });
        cbTractor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText2.setEnabled(true);
                    text2[0] = editText2.getText().toString();
                    selection.add("Tractor-" + text2[0]);
                    Log.i("edit text2", text2[0]);
                    compiledText[0] = compiledText[0] + "Tractor-" + text2[0];

                } else {
                    selection.remove("Tractor-" + text2[0]);
                    editText2.setEnabled(false);
                }
            }
        });
        cbAxe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText3.setEnabled(true);
                    text3[0] = editText3.getText().toString();
                    selection.add("Axe-" + text3[0]);
                    Log.i("edit text3", text3[0]);
                    compiledText[0] = compiledText[0] + "Axe-" + text3[0];
                } else {
                    selection.remove("Axe-" + text3[0]);
                    editText3.setEnabled(false);
                }
            }
        });

        cbShaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText4.setEnabled(true);
                    text4[0] = editText4.getText().toString();
                    selection.add("Shaw-" + text4[0]);
                    Log.i("edit text4", text4[0]);
                    compiledText[0] = compiledText[0] + "Shaw-" + text4[0];
                } else {
                    selection.remove("Shaw-" + text4[0]);
                    editText4.setEnabled(false);
                }
            }
        });

        cbSickle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText5.setEnabled(true);
                    text5[0] = editText5.getText().toString();
                    selection.add("Sickle-" + text5[0]);
                    Log.i("edit text5", text5[0]);
                    compiledText[0] = compiledText[0] + "Sickle-" + text5[0];
                } else {
                    selection.remove("Sickle-" + text5[0]);
                    editText5.setEnabled(false);
                }
            }
        });

        cbTimber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText6.setEnabled(true);
                    text6[0] = editText6.getText().toString();
                    selection.add("Timber-" + text6[0]);
                    Log.i("edit text6", text6[0]);
                    compiledText[0] = compiledText[0] + "Timber-" + text6[0];
                } else {
                    selection.remove("Timber-" + text6[0]);
                    editText6.setEnabled(false);
                }
            }
        });

        cbGun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText7.setEnabled(true);
                    text7[0] = editText7.getText().toString();
                    selection.add("Gun-" + text7[0]);
                    Log.i("edit text7", text7[0]);
                    compiledText[0] = compiledText[0] + "Gun-" + text7[0];
                } else {
                    selection.remove("Gun-" + text7[0]);
                    editText7.setEnabled(false);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String to_return = "Truck- "+editText1.getText().toString()+", Tractor- "+editText2.getText().toString()
                        +", Axe- "+editText3.getText().toString() + ", Shaw- "+editText4.getText().toString() +
                        ", Sickle- "+editText3.getText().toString() + ", Timber- "+editText3.getText().toString() + ", Gun- "+editText3.getText().toString();
                IllegalLogging.fromMultipleSelectionDialog = to_return;
                IllegalLogging.tvNameAndNumber.setText(to_return);
                showDialog.dismiss();
            }
        });
//        return displayText;

    }
}
