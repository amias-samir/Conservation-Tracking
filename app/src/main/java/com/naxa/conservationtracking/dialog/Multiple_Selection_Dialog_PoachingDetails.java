//package com.naxa.conservationtracking.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.naxa.conservationtracking.R;
//
//import java.util.ArrayList;
//
//import com.naxa.conservationtracking.wildlife_trade_control.Poaching;
//
///**
// * Created by ramaan on 4/9/2016.
// */
//public class Multiple_Selection_Dialog_PoachingDetails {
//    public static void multipleChoice_PoachingDetails(Context context, int title, String displayText) {
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//
//        final Dialog showDialog = new Dialog(context);
//        showDialog.setContentView(R.layout.multiple_checkbox_poaching_details);
//
//        final ArrayList<String> selection = new ArrayList<String>();
//        TextView final_text;
//        final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;
//        CheckBox cbTruck, cbTractor, cbAxe, cbShaw, cbSickle, cbTimber, cbGun;
//        final String[] compiledText = {""};
//        final String[] text1 = new String[1];
//        final String[] text2 = new String[1];
//        final String[] text3 = new String[1];
//        final String[] text4 = new String[1];
//        final String[] text5 = new String[1];
//        Button btnOk = (Button) showDialog.findViewById(R.id.done);
//        showDialog.setTitle("Select Items");
//
//        showDialog.setCancelable(true);
//        showDialog.show();
//        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//
//        editText1 = (EditText) showDialog.findViewById(R.id.editText_1);
//        editText2 = (EditText) showDialog.findViewById(R.id.editText_2);
//        editText3 = (EditText) showDialog.findViewById(R.id.editText_3);
//        editText4 = (EditText) showDialog.findViewById(R.id.editText_4);
//        editText5 = (EditText) showDialog.findViewById(R.id.editText_5);
//
//        cbTruck = (CheckBox) showDialog.findViewById(R.id.checkbox1);
//        cbTractor = (CheckBox) showDialog.findViewById(R.id.checkbox2);
//        cbAxe = (CheckBox) showDialog.findViewById(R.id.checkbox3);
//        cbShaw = (CheckBox) showDialog.findViewById(R.id.checkbox4);
//        cbSickle = (CheckBox) showDialog.findViewById(R.id.checkbox5);
//
//        editText1.setEnabled(false);
//        editText2.setEnabled(false);
//        editText3.setEnabled(false);
//        editText4.setEnabled(false);
//        editText5.setEnabled(false);
//
//        cbTruck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                    editText1.setEnabled(true);
//                    text1[0] = editText1.getText().toString();
//                    selection.add("Gun Shot-" + text1[0]);
//                    compiledText[0] = compiledText[0] + "Gun Shot-" + text1[0];
//                    Log.e("edit text1", text1[0]);
//
//                } else {
//                    selection.remove("Gun Shot-" + text1[0]);
//                    editText1.setEnabled(false);
//                }
//            }
//        });
//        cbTractor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText2.setEnabled(true);
//                    text2[0] = editText2.getText().toString();
//                    selection.add("Snares-" + text2[0]);
//                    Log.i("edit text2", text2[0]);
//                    compiledText[0] = compiledText[0] + "Snares-" + text2[0];
//
//                } else {
//                    selection.remove("Snares-" + text2[0]);
//                    editText2.setEnabled(false);
//                }
//            }
//        });
//        cbAxe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText3.setEnabled(true);
//                    text3[0] = editText3.getText().toString();
//                    selection.add("Khabar nets-" + text3[0]);
//                    Log.i("edit text3", text3[0]);
//                    compiledText[0] = compiledText[0] + "Khabar nets-" + text3[0];
//                } else {
//                    selection.remove("Khabar nets-" + text3[0]);
//                    editText3.setEnabled(false);
//                }
//            }
//        });
//
//        cbShaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText4.setEnabled(true);
//                    text4[0] = editText4.getText().toString();
//                    selection.add("Leg hole traps-" + text4[0]);
//                    Log.i("edit text4", text4[0]);
//                    compiledText[0] = compiledText[0] + "Leg hole traps-" + text4[0];
//                } else {
//                    selection.remove("Leg hole traps-" + text4[0]);
//                    editText4.setEnabled(false);
//                }
//            }
//        });
//
//        cbSickle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText5.setEnabled(true);
//                    text5[0] = editText5.getText().toString();
//                    selection.add("Poisoning-" + text5[0]);
//                    Log.i("edit text5", text5[0]);
//                    compiledText[0] = compiledText[0] + "Poisoning-" + text5[0];
//                } else {
//                    selection.remove("Poisoning-" + text5[0]);
//                    editText5.setEnabled(false);
//                }
//            }
//        });
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                String to_return = "Gun Shot- "+editText1.getText().toString()+", Snares- "+editText2.getText().toString()
//                        +", Khabar nets- "+editText3.getText().toString() + ", Leg hole traps- "+editText4.getText().toString() +
//                        ", Poisoning- "+editText5.getText().toString();
//                Poaching.fromMultipleSelectionDialog = to_return;
//                Poaching.tvMethodNameAndNumber.setText(to_return);
//                showDialog.dismiss();
//            }
//        });
////        return displayText;
//
//    }
//}
