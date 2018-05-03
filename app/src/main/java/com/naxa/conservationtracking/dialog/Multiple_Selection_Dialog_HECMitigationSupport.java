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

import com.naxa.conservationtracking.human_wildlife_conflict_management.HWCMitigationSupport;

/**
 * Created by ramaan on 4/9/2016.
 */
public class Multiple_Selection_Dialog_HECMitigationSupport {

//   public static Constants constants;

    public static String  hwcEndomentGPSPointKey = "" , hwcEndomentGPSTrakingKey = "" ;


    public static void multipleChoice_MitigationSupport(Context context, int title, String displayText) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.multiple_checkbox_mitigationsupport);



        final ArrayList<String> selection = new ArrayList<String>();
        TextView final_text;
        final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7, editText8, editText9;
       final CheckBox cbTruck, cbTractor, cbAxe, cbShaw, cbSickle, cbTimber, cbGun, cbWatch, cbMeshWire;
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

        cbTruck = (CheckBox) showDialog.findViewById(R.id.checkbox1);
        cbTractor = (CheckBox) showDialog.findViewById(R.id.checkbox2);
        cbAxe = (CheckBox) showDialog.findViewById(R.id.checkbox3);
        cbShaw = (CheckBox) showDialog.findViewById(R.id.checkbox4);
        cbSickle = (CheckBox) showDialog.findViewById(R.id.checkbox5);
        cbTimber = (CheckBox) showDialog.findViewById(R.id.checkbox6);
        cbGun = (CheckBox) showDialog.findViewById(R.id.checkbox7);
        cbWatch = (CheckBox) showDialog.findViewById(R.id.checkbox8);
        cbMeshWire = (CheckBox) showDialog.findViewById(R.id.checkbox9);

        editText1.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);
        editText7.setEnabled(false);
        editText8.setEnabled(false);
        editText9.setEnabled(false);

        cbTruck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    HWCMitigationSupport.gpsPointBoolean = "false";
                    HWCMitigationSupport.gpsTrackingBoolean = "true";

                    hwcEndomentGPSPointKey = "false";
                    hwcEndomentGPSTrakingKey = "true";




                    editText1.setEnabled(true);
                    text1[0] = editText1.getText().toString();
                    selection.add("Electric-" + text1[0]);
                    compiledText[0] = compiledText[0] + "Electric-" + text1[0];
                    Log.e("edit text1", text1[0]);

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);



                } else {
                    selection.remove("Electric-" + text1[0]);
                    editText1.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });
        cbTractor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    HWCMitigationSupport.gpsPointBoolean = "false";
                    HWCMitigationSupport.gpsTrackingBoolean = "true";

                    hwcEndomentGPSPointKey = "false";
                    hwcEndomentGPSTrakingKey = "true";


                    editText2.setEnabled(true);
                    text2[0] = editText2.getText().toString();
                    selection.add("Solar-" + text2[0]);
                    Log.i("edit text2", text2[0]);
                    compiledText[0] = compiledText[0] + "Solar-" + text2[0];

                    cbTruck.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Solar-" + text2[0]);
                    editText2.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });
        cbAxe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    HWCMitigationSupport.gpsPointBoolean = "false";
                    HWCMitigationSupport.gpsTrackingBoolean = "true";

                    hwcEndomentGPSPointKey = "false";
                    hwcEndomentGPSTrakingKey = "true";


                    editText3.setEnabled(true);
                    text3[0] = editText3.getText().toString();
                    selection.add("Bio Fencing-" + text3[0]);
                    Log.i("edit text3", text3[0]);
                    compiledText[0] = compiledText[0] + "Bio Fencing-" + text3[0];

                    cbTractor.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Bio Fencing-" + text3[0]);
                    editText3.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });

        cbShaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    HWCMitigationSupport.gpsPointBoolean = "true";
                    HWCMitigationSupport.gpsTrackingBoolean = "false";

                    hwcEndomentGPSPointKey = "true";
                    hwcEndomentGPSTrakingKey = "false";


                    editText4.setEnabled(true);
                    text4[0] = editText4.getText().toString();
                    selection.add("Machan-" + text4[0]);
                    Log.i("edit text4", text4[0]);
                    compiledText[0] = compiledText[0] + "Machan-" + text4[0];

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Machan-" + text4[0]);
                    editText4.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });

        cbSickle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    HWCMitigationSupport.gpsPointBoolean = "false";
                    HWCMitigationSupport.gpsTrackingBoolean = "true";

                    hwcEndomentGPSPointKey = "false";
                    hwcEndomentGPSTrakingKey = "true";


                    editText5.setEnabled(true);
                    text5[0] = editText5.getText().toString();
                    selection.add("Trench-" + text5[0]);
                    Log.i("edit text5", text5[0]);
                    compiledText[0] = compiledText[0] + "Trench-" + text5[0];

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Trench-" + text5[0]);
                    editText5.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });

        cbTimber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    HWCMitigationSupport.gpsPointBoolean = "true";
                    HWCMitigationSupport.gpsTrackingBoolean = "false";

                    hwcEndomentGPSPointKey = "true";
                    hwcEndomentGPSTrakingKey = "false";

                    editText6.setEnabled(true);
                    text6[0] = editText6.getText().toString();
                    selection.add("Grain Storage-" + text6[0]);
                    Log.i("edit text6", text6[0]);
                    compiledText[0] = compiledText[0] + "Grain Storage-" + text6[0];

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Grain Storage-" + text6[0]);
                    editText6.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });

        cbGun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    HWCMitigationSupport.gpsPointBoolean = "true";
                    HWCMitigationSupport.gpsTrackingBoolean = "false";

                    hwcEndomentGPSPointKey = "true";
                    hwcEndomentGPSTrakingKey = "false";

                    editText7.setEnabled(true);
                    text7[0] = editText7.getText().toString();
                    selection.add("Improved Corral-" + text7[0]);
                    Log.i("edit text7", text7[0]);
                    compiledText[0] = compiledText[0] + "Improved Corral-" + text7[0];

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbWatch.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Improved Corral-" + text7[0]);
                    editText7.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });

        cbWatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    HWCMitigationSupport.gpsPointBoolean = "true";
                    HWCMitigationSupport.gpsTrackingBoolean = "false";

                    hwcEndomentGPSPointKey = "true";
                    hwcEndomentGPSTrakingKey = "false";

                    editText8.setEnabled(true);
                    text8[0] = editText8.getText().toString();
                    selection.add("Watch Tower-" + text8[0]);
                    Log.i("edit text7", text8[0]);
                    compiledText[0] = compiledText[0] + "Watch Tower-" + text8[0];

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbMeshWire.setEnabled(false);


                } else {
                    selection.remove("Watch Tower-" + text8[0]);
                    editText8.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });


        cbMeshWire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    HWCMitigationSupport.gpsPointBoolean = "false";
                    HWCMitigationSupport.gpsTrackingBoolean = "true";

                    hwcEndomentGPSPointKey = "false";
                    hwcEndomentGPSTrakingKey = "true";

                    editText9.setEnabled(true);
                    text9[0] = editText9.getText().toString();
                    selection.add("Mesh Wire-" + text9[0]);
                    Log.i("edit text9", text9[0]);
                    compiledText[0] = compiledText[0] + "Mesh Wire-" + text9[0];

                    cbTractor.setEnabled(false);
                    cbAxe.setEnabled(false);
                    cbShaw.setEnabled(false);
                    cbSickle.setEnabled(false);
                    cbTimber.setEnabled(false);
                    cbGun.setEnabled(false);
                    cbTruck.setEnabled(false);
                    cbWatch.setEnabled(false);


                } else {
                    selection.remove("Mesh Wire-" + text9[0]);
                    editText9.setEnabled(false);

                    cbTruck.setEnabled(true);
                    cbTractor.setEnabled(true);
                    cbAxe.setEnabled(true);
                    cbShaw.setEnabled(true);
                    cbSickle.setEnabled(true);
                    cbTimber.setEnabled(true);
                    cbGun.setEnabled(true);
                    cbWatch.setEnabled(true);
                    cbMeshWire.setEnabled(true);

                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String to_return = "Electric- "+editText1.getText().toString()+", Solar- "+editText2.getText().toString()
                        +", Bio Fencing- "+editText3.getText().toString() + ", Machan- "+editText4.getText().toString() +
                        ", Trench- "+editText5.getText().toString() + ", Grain Storage- "+editText6.getText().toString()
                        + ", Improved Corral- "+editText7.getText().toString() + ", Watch Tower- "+editText8.getText().toString()
                        + ", Mesh Wire- "+editText9.getText().toString();
                HWCMitigationSupport.fromMultipleSelectionDialog = to_return;
                HWCMitigationSupport.tvNameAndNumber.setText(to_return);

//                HWCMitigationSupport.gpsPointBoolean = hwcEndomentGPSPointKey;
//                HWCMitigationSupport.gpsTrackingBoolean = hwcEndomentGPSTrakingKey;

                Log.e("tracking toggle", "mitigation support "+ hwcEndomentGPSTrakingKey);
                Log.e("tracking toggle point", "mitigation support "+ hwcEndomentGPSPointKey );

                Log.e("MITIGATION_SUPPORT", "onCreate: "+ Multiple_Selection_Dialog_HECMitigationSupport.hwcEndomentGPSTrakingKey );
                if(hwcEndomentGPSPointKey.equals("true")){
                    HWCMitigationSupport.cvGpsPoint.setVisibility(View.VISIBLE);
                    HWCMitigationSupport.cvGpsTracking.setVisibility(View.INVISIBLE);
                }

                if(hwcEndomentGPSTrakingKey.equals("true")){
                    HWCMitigationSupport.cvGpsTracking.setVisibility(View.VISIBLE);
                    HWCMitigationSupport.cvGpsPoint.setVisibility(View.INVISIBLE);
                }

                showDialog.dismiss();
            }
        });
//        return displayText;

    }
}
