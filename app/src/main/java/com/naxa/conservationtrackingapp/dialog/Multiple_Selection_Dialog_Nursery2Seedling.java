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
public class Multiple_Selection_Dialog_Nursery2Seedling {

    public static void multipleChoice_NurseryDetail(Context context, int title, String displayText) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.multiple_checkbox_nurseryseedling);

        final ArrayList<String> selection = new ArrayList<String>();
        TextView final_text;
        final EditText
                editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText8, editText9, editText10,
                editText11, editText12, editText13, editText14, editText15, editText16, editText17, editText18, editText19, editText20,
                editText21, editText22, editText23, editText24, editText25, editText26, editText27, editText28, editText29, editText30,
                editText31, editText32, editText33, editText34, editText35, editText36, editText37, editText38, editText39, editText40;


        final CheckBox
                cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10,
                cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19, cb20,
                cb21, cb22, cb23, cb24, cb25, cb26, cb27, cb28, cb29, cb30,
                cb31, cb32, cb33, cb34, cb35, cb36, cb37, cb38, cb39, cb40;


        final String[] compiledText = {""};
        final String[] text1 = new String[1];
        final String[] text2 = new String[1];
        final String[] text3 = new String[1];
        final String[] text4 = new String[1];
        final String[] text5 = new String[1];
        final String[] text6 = new String[1];
        final String[] text7 = new String[1];
        final String[] text8 = new String[1];
        final String[] text9 = new String[1];
        final String[] text10 = new String[1];
        final String[] text12 = new String[1];
        final String[] text13 = new String[1];
        final String[] text14 = new String[1];
        final String[] text15 = new String[1];
        final String[] text16 = new String[1];
        final String[] text17 = new String[1];
        final String[] text18 = new String[1];
        final String[] text19 = new String[1];
        final String[] text20 = new String[1];
        final String[] text21 = new String[1];
        final String[] text22 = new String[1];
        final String[] text23 = new String[1];
        final String[] text24 = new String[1];
        final String[] text25 = new String[1];
        final String[] text26 = new String[1];
        final String[] text27 = new String[1];
        final String[] text28 = new String[1];
        final String[] text29 = new String[1];
        final String[] text30 = new String[1];
        final String[] text31 = new String[1];
        final String[] text32 = new String[1];
        final String[] text33 = new String[1];
        final String[] text34 = new String[1];
        final String[] text35 = new String[1];
        final String[] text36 = new String[1];
        final String[] text37 = new String[1];
        final String[] text38 = new String[1];
        final String[] text39 = new String[1];
        final String[] text40 = new String[1];


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
        editText8 = (EditText) showDialog.findViewById(R.id.editText_8);
        editText9 = (EditText) showDialog.findViewById(R.id.editText_9);
        editText10 = (EditText) showDialog.findViewById(R.id.editText_10);
        editText11 = (EditText) showDialog.findViewById(R.id.editText_11);
        editText12 = (EditText) showDialog.findViewById(R.id.editText_12);
        editText13 = (EditText) showDialog.findViewById(R.id.editText_13);
        editText14 = (EditText) showDialog.findViewById(R.id.editText_14);
        editText15 = (EditText) showDialog.findViewById(R.id.editText_15);
        editText16 = (EditText) showDialog.findViewById(R.id.editText_16);
        editText17 = (EditText) showDialog.findViewById(R.id.editText_17);
        editText18 = (EditText) showDialog.findViewById(R.id.editText_18);
        editText19 = (EditText) showDialog.findViewById(R.id.editText_19);
        editText20 = (EditText) showDialog.findViewById(R.id.editText_20);
        editText21 = (EditText) showDialog.findViewById(R.id.editText_21);
        editText22 = (EditText) showDialog.findViewById(R.id.editText_22);
        editText23 = (EditText) showDialog.findViewById(R.id.editText_23);
        editText24 = (EditText) showDialog.findViewById(R.id.editText_24);
        editText25 = (EditText) showDialog.findViewById(R.id.editText_25);
        editText26 = (EditText) showDialog.findViewById(R.id.editText_26);
        editText27 = (EditText) showDialog.findViewById(R.id.editText_27);
        editText28 = (EditText) showDialog.findViewById(R.id.editText_28);
        editText29 = (EditText) showDialog.findViewById(R.id.editText_29);
        editText30 = (EditText) showDialog.findViewById(R.id.editText_30);
        editText31 = (EditText) showDialog.findViewById(R.id.editText_31);
        editText32 = (EditText) showDialog.findViewById(R.id.editText_32);
        editText33 = (EditText) showDialog.findViewById(R.id.editText_33);
        editText34 = (EditText) showDialog.findViewById(R.id.editText_34);
        editText35 = (EditText) showDialog.findViewById(R.id.editText_35);
        editText36 = (EditText) showDialog.findViewById(R.id.editText_36);
        editText37 = (EditText) showDialog.findViewById(R.id.editText_37);
        editText38 = (EditText) showDialog.findViewById(R.id.editText_38);
        editText39 = (EditText) showDialog.findViewById(R.id.editText_39);
        editText40 = (EditText) showDialog.findViewById(R.id.editText_40);

        cb1 = (CheckBox) showDialog.findViewById(R.id.checkbox1);
        cb2 = (CheckBox) showDialog.findViewById(R.id.checkbox2);
        cb3 = (CheckBox) showDialog.findViewById(R.id.checkbox3);
        cb4 = (CheckBox) showDialog.findViewById(R.id.checkbox4);
        cb5 = (CheckBox) showDialog.findViewById(R.id.checkbox5);
        cb6 = (CheckBox) showDialog.findViewById(R.id.checkbox6);
        cb7 = (CheckBox) showDialog.findViewById(R.id.checkbox7);
        cb8 = (CheckBox) showDialog.findViewById(R.id.checkbox8);
        cb9 = (CheckBox) showDialog.findViewById(R.id.checkbox9);
        cb10 = (CheckBox) showDialog.findViewById(R.id.checkbox10);
        cb11 = (CheckBox) showDialog.findViewById(R.id.checkbox11);
        cb12 = (CheckBox) showDialog.findViewById(R.id.checkbox12);
        cb13 = (CheckBox) showDialog.findViewById(R.id.checkbox13);
        cb14 = (CheckBox) showDialog.findViewById(R.id.checkbox14);
        cb15 = (CheckBox) showDialog.findViewById(R.id.checkbox15);
        cb16 = (CheckBox) showDialog.findViewById(R.id.checkbox16);
        cb17 = (CheckBox) showDialog.findViewById(R.id.checkbox17);
        cb18 = (CheckBox) showDialog.findViewById(R.id.checkbox18);
        cb19 = (CheckBox) showDialog.findViewById(R.id.checkbox19);
        cb20 = (CheckBox) showDialog.findViewById(R.id.checkbox20);
        cb21 = (CheckBox) showDialog.findViewById(R.id.checkbox21);
        cb22 = (CheckBox) showDialog.findViewById(R.id.checkbox22);
        cb23 = (CheckBox) showDialog.findViewById(R.id.checkbox23);
        cb24 = (CheckBox) showDialog.findViewById(R.id.checkbox24);
        cb25 = (CheckBox) showDialog.findViewById(R.id.checkbox25);
        cb26 = (CheckBox) showDialog.findViewById(R.id.checkbox26);
        cb27 = (CheckBox) showDialog.findViewById(R.id.checkbox27);
        cb28 = (CheckBox) showDialog.findViewById(R.id.checkbox28);
        cb29 = (CheckBox) showDialog.findViewById(R.id.checkbox29);
        cb30 = (CheckBox) showDialog.findViewById(R.id.checkbox30);
        cb31 = (CheckBox) showDialog.findViewById(R.id.checkbox31);
        cb32 = (CheckBox) showDialog.findViewById(R.id.checkbox32);
        cb33 = (CheckBox) showDialog.findViewById(R.id.checkbox33);
        cb34 = (CheckBox) showDialog.findViewById(R.id.checkbox34);
        cb35 = (CheckBox) showDialog.findViewById(R.id.checkbox35);
        cb36 = (CheckBox) showDialog.findViewById(R.id.checkbox36);
        cb37 = (CheckBox) showDialog.findViewById(R.id.checkbox37);
        cb38 = (CheckBox) showDialog.findViewById(R.id.checkbox38);
        cb39 = (CheckBox) showDialog.findViewById(R.id.checkbox39);
        cb40 = (CheckBox) showDialog.findViewById(R.id.checkbox40);

        editText1.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);
        editText7.setEnabled(false);
        editText8.setEnabled(false);
        editText9.setEnabled(false);
        editText10.setEnabled(false);
        editText11.setEnabled(false);
        editText12.setEnabled(false);
        editText13.setEnabled(false);
        editText14.setEnabled(false);
        editText15.setEnabled(false);
        editText16.setEnabled(false);
        editText17.setEnabled(false);
        editText18.setEnabled(false);
        editText19.setEnabled(false);
        editText20.setEnabled(false);
        editText21.setEnabled(false);
        editText22.setEnabled(false);
        editText23.setEnabled(false);
        editText24.setEnabled(false);
        editText25.setEnabled(false);
        editText26.setEnabled(false);
        editText27.setEnabled(false);
        editText28.setEnabled(false);
        editText29.setEnabled(false);
        editText30.setEnabled(false);
        editText31.setEnabled(false);
        editText32.setEnabled(false);
        editText33.setEnabled(false);
        editText34.setEnabled(false);
        editText35.setEnabled(false);
        editText36.setEnabled(false);
        editText37.setEnabled(false);
        editText38.setEnabled(false);
        editText39.setEnabled(false);
        editText40.setEnabled(false);

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText1.setEnabled(true);

                } else {
                    editText1.setEnabled(false);
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText2.setEnabled(true);

                } else {
                    editText2.setEnabled(false);
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText3.setEnabled(true);

                } else {
                    editText3.setEnabled(false);
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText4.setEnabled(true);

                } else {
                    editText4.setEnabled(false);
                }
            }
        });
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText5.setEnabled(true);

                } else {
                    editText5.setEnabled(false);
                }
            }
        });
        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText6.setEnabled(true);

                } else {
                    editText6.setEnabled(false);
                }
            }
        });
        cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText7.setEnabled(true);

                } else {
                    editText7.setEnabled(false);
                }
            }
        });
        cb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText8.setEnabled(true);

                } else {
                    editText8.setEnabled(false);
                }
            }
        });
        cb9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText9.setEnabled(true);

                } else {
                    editText9.setEnabled(false);
                }
            }
        });
        cb10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText10.setEnabled(true);

                } else {
                    editText10.setEnabled(false);
                }
            }
        });
//
        cb11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText11.setEnabled(true);

                } else {
                    editText11.setEnabled(false);
                }
            }
        });
        cb12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText12.setEnabled(true);

                } else {
                    editText12.setEnabled(false);
                }
            }
        });
        cb13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText13.setEnabled(true);

                } else {
                    editText13.setEnabled(false);
                }
            }
        });
        cb14.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText14.setEnabled(true);

                } else {
                    editText14.setEnabled(false);
                }
            }
        });
        cb15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText15.setEnabled(true);

                } else {
                    editText15.setEnabled(false);
                }
            }
        });
        cb16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText16.setEnabled(true);

                } else {
                    editText16.setEnabled(false);
                }
            }
        });
        cb17.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText17.setEnabled(true);

                } else {
                    editText17.setEnabled(false);
                }
            }
        });
        cb18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText18.setEnabled(true);

                } else {
                    editText18.setEnabled(false);
                }
            }
        });
        cb19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText19.setEnabled(true);

                } else {
                    editText19.setEnabled(false);
                }
            }
        });
        cb20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText20.setEnabled(true);

                } else {
                    editText20.setEnabled(false);
                }
            }
        });
//
        cb21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText21.setEnabled(true);

                } else {
                    editText21.setEnabled(false);
                }
            }
        });
        cb22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText22.setEnabled(true);

                } else {
                    editText22.setEnabled(false);
                }
            }
        });
        cb23.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText23.setEnabled(true);

                } else {
                    editText23.setEnabled(false);
                }
            }
        });
        cb24.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText24.setEnabled(true);

                } else {
                    editText24.setEnabled(false);
                }
            }
        });
        cb25.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText25.setEnabled(true);

                } else {
                    editText25.setEnabled(false);
                }
            }
        });
        cb26.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText26.setEnabled(true);

                } else {
                    editText26.setEnabled(false);
                }
            }
        });
        cb27.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText27.setEnabled(true);

                } else {
                    editText27.setEnabled(false);
                }
            }
        });
        cb28.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText28.setEnabled(true);

                } else {
                    editText28.setEnabled(false);
                }
            }
        });
        cb29.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText29.setEnabled(true);

                } else {
                    editText29.setEnabled(false);
                }
            }
        });
        cb30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText30.setEnabled(true);

                } else {
                    editText30.setEnabled(false);
                }
            }
        });
//
        cb31.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText31.setEnabled(true);

                } else {
                    editText31.setEnabled(false);
                }
            }
        });
        cb32.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText32.setEnabled(true);

                } else {
                    editText32.setEnabled(false);
                }
            }
        });
        cb33.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText33.setEnabled(true);

                } else {
                    editText33.setEnabled(false);
                }
            }
        });
        cb34.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText34.setEnabled(true);

                } else {
                    editText34.setEnabled(false);
                }
            }
        });
        cb35.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText35.setEnabled(true);

                } else {
                    editText35.setEnabled(false);
                }
            }
        });
        cb36.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText36.setEnabled(true);

                } else {
                    editText36.setEnabled(false);
                }
            }
        });
        cb37.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText37.setEnabled(true);

                } else {
                    editText37.setEnabled(false);
                }
            }
        });
        cb38.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText38.setEnabled(true);

                } else {
                    editText38.setEnabled(false);
                }
            }
        });
        cb39.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText39.setEnabled(true);

                } else {
                    editText39.setEnabled(false);
                }
            }
        });
        cb40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText40.setEnabled(true);

                } else {
                    editText40.setEnabled(false);
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String to_return = "";

                if (cb1.isChecked()) {
                    to_return = cb1.getText().toString() + ": " + editText1.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb2.isChecked()) {
                    to_return = cb2.getText().toString() + ": " + editText2.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb3.isChecked()) {
                    to_return = cb3.getText().toString() + ": " + editText3.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb4.isChecked()) {
                    to_return = cb4.getText().toString() + ": " + editText4.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb5.isChecked()) {
                    to_return = cb5.getText().toString() + ": " + editText5.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb6.isChecked()) {
                    to_return = cb6.getText().toString() + ": " + editText6.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb7.isChecked()) {
                    to_return = cb7.getText().toString() + ": " + editText7.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb8.isChecked()) {
                    to_return = cb8.getText().toString() + ": " + editText8.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb9.isChecked()) {
                    to_return = cb9.getText().toString() + ": " + editText9.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb10.isChecked()) {
                    to_return = cb10.getText().toString() + ": " + editText10.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }
//---------------------------------------------------------------------------------------------------
                if (cb11.isChecked()) {
                    to_return = cb11.getText().toString() + ": " + editText11.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb12.isChecked()) {
                    to_return = cb12.getText().toString() + ": " + editText12.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb13.isChecked()) {
                    to_return = cb13.getText().toString() + ": " + editText13.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb14.isChecked()) {
                    to_return = cb14.getText().toString() + ": " + editText14.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb15.isChecked()) {
                    to_return = cb15.getText().toString() + ": " + editText15.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb16.isChecked()) {
                    to_return = cb16.getText().toString() + ": " + editText16.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb17.isChecked()) {
                    to_return = cb17.getText().toString() + ": " + editText17.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb18.isChecked()) {
                    to_return = cb18.getText().toString() + ": " + editText18.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb19.isChecked()) {
                    to_return = cb19.getText().toString() + ": " + editText19.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb20.isChecked()) {
                    to_return = cb20.getText().toString() + ": " + editText20.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }
//---------------------------------------------------------------------------------------------------
                if (cb21.isChecked()) {
                    to_return = cb21.getText().toString() + ": " + editText21.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb22.isChecked()) {
                    to_return = cb22.getText().toString() + ": " + editText22.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb23.isChecked()) {
                    to_return = cb23.getText().toString() + ": " + editText23.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb24.isChecked()) {
                    to_return = cb24.getText().toString() + ": " + editText24.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb25.isChecked()) {
                    to_return = cb25.getText().toString() + ": " + editText25.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb6.isChecked()) {
                    to_return = cb26.getText().toString() + ": " + editText26.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb27.isChecked()) {
                    to_return = cb27.getText().toString() + ": " + editText27.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb28.isChecked()) {
                    to_return = cb28.getText().toString() + ": " + editText28.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb29.isChecked()) {
                    to_return = cb29.getText().toString() + ": " + editText29.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb30.isChecked()) {
                    to_return = cb30.getText().toString() + ": " + editText30.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }
//----------------------------------------------------------------------------------------------------
                if (cb31.isChecked()) {
                    to_return = cb31.getText().toString() + ": " + editText31.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb32.isChecked()) {
                    to_return = cb32.getText().toString() + ": " + editText32.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb33.isChecked()) {
                    to_return = cb33.getText().toString() + ": " + editText33.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                } else if (cb34.isChecked()) {
                    to_return = cb34.getText().toString() + ": " + editText34.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb35.isChecked()) {
                    to_return = cb35.getText().toString() + ": " + editText35.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb36.isChecked()) {
                    to_return = cb36.getText().toString() + ": " + editText36.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb37.isChecked()) {
                    to_return = cb37.getText().toString() + ": " + editText37.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb38.isChecked()) {
                    to_return = cb38.getText().toString() + ": " + editText38.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb39.isChecked()) {
                    to_return = cb39.getText().toString() + ": " + editText39.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }else if (cb10.isChecked()) {
                    to_return = cb40.getText().toString() + ": " + editText40.getText().toString();
                    NurseryDetails.fromMultipleSelectionDialog = to_return;
                    NurseryDetails.tvNameAndNumber.setText(to_return);
                    showDialog.dismiss();

                }

                if (cb1.isChecked() && cb2.isChecked()) {
                    to_return = cb1.getText().toString() + ": " + editText1.getText().toString() + ", " + cb2.getText().toString()
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
