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
//import com.naxa.conservationtracking.forest.ForestIntegLivestocManagement;
//
///**
// * Created by ramaan on 4/9/2016.
// */
//public class Multiple_Selection_Dialog1 {
//    public static void multipleChoice_FILM(Context context, int title, String displayText) {
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//
//        final Dialog showDialog = new Dialog(context);
//        showDialog.setContentView(R.layout.multiple_checkbox1);
//
//        final ArrayList<String> selection = new ArrayList<String>();
//        TextView final_text;
//        final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;
//        CheckBox cbTruck, cbTractor, cbAxe, cbShaw, cbSickle, cbTimber, cbGun;
//        final String[] compiledText = {""};
//        final String[] text1 = new String[1];
//        final String[] text2 = new String[1];
//        final String[] text3 = new String[1];
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
//
//        cbTruck = (CheckBox) showDialog.findViewById(R.id.checkbox1);
//        cbTractor = (CheckBox) showDialog.findViewById(R.id.checkbox2);
//        cbAxe = (CheckBox) showDialog.findViewById(R.id.checkbox3);
//
//        editText1.setEnabled(false);
//        editText2.setEnabled(false);
//        editText3.setEnabled(false);
//
//        cbTruck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                    editText1.setEnabled(true);
//                    text1[0] = editText1.getText().toString();
//                    selection.add("Cement Fund Support-" + text1[0]);
//                    compiledText[0] = compiledText[0] + "Cement Fund Support-" + text1[0];
//                    Log.e("edit text1", text1[0]);
//
//                } else {
//                    selection.remove("Cement Fund Support-" + text1[0]);
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
//                    selection.add("Artificial Insemination-" + text2[0]);
//                    Log.i("edit text2", text2[0]);
//                    compiledText[0] = compiledText[0] + "Artificial Insemination-" + text2[0];
//
//                } else {
//                    selection.remove("Artificial Insemination-" + text2[0]);
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
//                    selection.add("Productive Cattle Breed-" + text3[0]);
//                    Log.i("edit text3", text3[0]);
//                    compiledText[0] = compiledText[0] + "Productive Cattle Breed-" + text3[0];
//                } else {
//                    selection.remove("Productive Cattle Breed-" + text3[0]);
//                    editText3.setEnabled(false);
//                }
//            }
//        });
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                String to_return = "Cement Fund Support- "+editText1.getText().toString()+", Artificial Insemination- "+editText2.getText().toString()
//                        +", Productive Cattle Breed- "+editText3.getText().toString();
//                ForestIntegLivestocManagement.fromMultipleSelectionDialog = to_return;
//                ForestIntegLivestocManagement.tvNameAndNumber.setText(to_return);
//                showDialog.dismiss();
//            }
//        });
////        return displayText;
//
//    }
//}
