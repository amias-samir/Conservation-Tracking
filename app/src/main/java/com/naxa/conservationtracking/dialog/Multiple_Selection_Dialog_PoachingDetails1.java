package com.naxa.conservationtracking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.naxa.conservationtracking.R;

import java.util.ArrayList;

import com.naxa.conservationtracking.wildlife_trade_control.Poaching;

/**
 * Created by ramaan on 4/9/2016.
 */
public class Multiple_Selection_Dialog_PoachingDetails1 {

    public static void multipleChoice_PoachingDetails1(Context context, int title, String displayText) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.multiple_checkbox_poaching_details1);
        final View layout = View.inflate(context, R.layout.multiple_checkbox_poaching_details1, null);

        final ArrayList<String> selection = new ArrayList<String>();
        final String[] final_text = {""};
        final String[] text = new String[1];
        final String other_items;

        final CheckBox cbGuns, cbSnares, cbKhabarNets, cbLegholes, cbBullets, cbDeadAnimals, cbOthersitems;
//        final String[] compiledText = {""};
//        final String[] text1 = new String[1];
//        final String[] text2 = new String[1];
//        final String[] text3 = new String[1];
//        final String[] text4 = new String[1];
//        final String[] text5 = new String[1];
//        final String[] text6 = new String[1];

        final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;

        final AutoCompleteTextView tvOtherItems;
        editText1 = (EditText) showDialog.findViewById(R.id.guns_no_id);
        editText2 = (EditText) showDialog.findViewById(R.id.snares_no_id);
        editText3 = (EditText) showDialog.findViewById(R.id.khabar_nets_no_id);
        editText4 = (EditText) showDialog.findViewById(R.id.leg_hole_traps_no_id);
        editText5 = (EditText) showDialog.findViewById(R.id.bullets_no_id);
        editText6 = (EditText) showDialog.findViewById(R.id.dead_animal_no_id);
        tvOtherItems = (AutoCompleteTextView) showDialog.findViewById(R.id.poaching_otherItems);

        String text1;
        String text2;
        String text3;
        String text4;
        String text5;
        String text6;

        Button btnOk = (Button) showDialog.findViewById(R.id.done);
        showDialog.setTitle("Select Items");

        showDialog.setCancelable(true);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        cbGuns = (CheckBox) showDialog.findViewById(R.id.checkbox1);
        cbSnares = (CheckBox) showDialog.findViewById(R.id.checkbox2);
        cbKhabarNets = (CheckBox) showDialog.findViewById(R.id.checkbox3);
        cbLegholes = (CheckBox) showDialog.findViewById(R.id.checkbox4);
        cbBullets = (CheckBox) showDialog.findViewById(R.id.checkbox5);
        cbDeadAnimals = (CheckBox) showDialog.findViewById(R.id.checkbox6);
        cbOthersitems = (CheckBox) showDialog.findViewById(R.id.other_items_id);

//        editText1.setEnabled(false);
//        editText2.setEnabled(false);
//        editText3.setEnabled(false);
//        editText4.setEnabled(false);
//        editText5.setEnabled(false);
//        editText6.setEnabled(false);


//        cbGuns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                    editText1.setEnabled(true);
//                    text1[0] = editText1.getText().toString();
//                    selection.add("Guns-" + text1[0]);
//                    compiledText[0] = compiledText[0] + "Guns-" + text1[0];
//                    Log.e("edit text1", text1[0]);
//
//                } else {
//                    selection.remove("Guns-" + text1[0]);
//                    editText1.setEnabled(false);
//                }
//            }
//        });
//        cbSnares.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
//        cbKhabarNets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
//        cbLegholes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
//        cbBullets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText5.setEnabled(true);
//                    text5[0] = editText5.getText().toString();
//                    selection.add("Bullets-" + text5[0]);
//                    Log.i("edit text5", text5[0]);
//                    compiledText[0] = compiledText[0] + "Bullets-" + text5[0];
//                } else {
//                    selection.remove("Bullets-" + text5[0]);
//                    editText5.setEnabled(false);
//                }
//            }
//        });
//
//        cbDeadAnimals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText6.setEnabled(true);
//                    text6[0] = editText6.getText().toString();
//                    selection.add("Dead animal or its body parts-" + text6[0]);
//                    Log.i("edit text6", text6[0]);
//                    compiledText[0] = compiledText[0] + "Dead animal or its body parts-" + text6[0];
//                } else {
//                    selection.remove("Dead animal or its body parts-" + text6[0]);
//                    editText6.setEnabled(false);
//                }
//            }
//        });


//        cbGuns.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText1.setEnabled(true);
////                    text1 = editText1.getText().toString();
//
////                    final_text[0] = final_text[0] + "  " + cbGuns.getText().toString() + ": " + text1.toString();
//                } else {
//                    editText1.setEnabled(false);
//                }
//            }
//        });
//        cbSnares.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText2.setEnabled(true);
////                    text2 = editText2.getText().toString();
////                    final_text[0] = final_text[0] + "  " + cbSnares.getText().toString() + ": " + text2.toString();
//
//                } else {
//                    editText2.setEnabled(false);
//                }
//            }
//        });
//        cbKhabarNets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText3.setEnabled(true);
////                    text3 = editText3.getText().toString();
////                    final_text[0] = final_text[0] + "  " + cbKhabarNets.getText().toString() + ": " + text3.toString();
//                } else {
//                    editText3.setEnabled(false);
//                }
//            }
//        });
//
//        cbLegholes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText4.setEnabled(true);
////                    text4 = editText4.getText().toString();
////                    final_text[0] = final_text[0] + "  " + cbLegholes.getText().toString() + ": " + text4.toString();
//                } else {
//                    editText4.setEnabled(false);
//                }
//            }
//        });
//
//        cbBullets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText5.setEnabled(true);
////                    text5 = editText5.getText().toString();
//
////                    final_text[0] = final_text[0] + "  " + cbBullets.getText().toString() + ": " + text5.toString();
//                } else {
//                    editText5.setEnabled(false);
//                }
//            }
//        });
//
//        cbDeadAnimals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    editText6.setEnabled(true);
////                    final_text[0] = final_text[0] + "  " + cbDeadAnimals.getText().toString() + ": " + text6.toString();
//                } else {
//                    editText6.setEnabled(false);
//                }
//            }
//        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (cbGuns.isChecked() == true) {
                    editText1.setEnabled(true);

                    text[0] = editText1.getText().toString();
                    Log.e("cbGuns", text[0]);
                    final_text[0] = final_text[0] + "  " + (cbGuns.getText().toString() + ": " + text[0]);

                } else {
                    editText1.setEnabled(false);
                }
                if (cbSnares.isChecked() == true) {
                    editText2.setEnabled(true);
                    text[0] = editText2.getText().toString();

                    final_text[0] = final_text[0] + "  " + (cbKhabarNets.getText().toString() + ": " + text[0]);

                } else {
                    editText2.setEnabled(false);
                }
                if (cbKhabarNets.isChecked() == true) {
                    editText3.setEnabled(true);

                    text[0] = editText3.getText().toString();

                    final_text[0] = final_text[0] + "  " + (cbSnares.getText().toString() + ": " + text[0]);

                } else {
                    editText3.setEnabled(false);
                }
                if (cbLegholes.isChecked() == true) {
                    editText4.setEnabled(true);

                    text[0] = editText4.getText().toString();
                    final_text[0] = final_text[0] + "  " + (cbLegholes.getText().toString() + ": " + text[0]);

                } else {
                    editText4.setEnabled(false);
                }
                if (cbBullets.isChecked() == true) {
                    editText5.setEnabled(true);

                    text[0] = editText5.getText().toString();

                    final_text[0] = final_text[0] + "  " + cbBullets.getText().toString() + ": " + text[0];

                } else {
                    editText5.setEnabled(false);
                }
                if (cbDeadAnimals.isChecked() == true) {
                    editText6.setEnabled(true);

                    text[0] = editText6.getText().toString();

                    final_text[0] = final_text[0] + "  " + (cbDeadAnimals.getText().toString() + ": " + text[0]);
//                            + ":  " + other_items;
                } else {
                    editText6.setEnabled(false);
                }

                if (cbDeadAnimals.isChecked() == true) {
                    editText6.setEnabled(true);

                    text[0] = editText6.getText().toString();

                    final_text[0] = final_text[0] + "  " + (cbDeadAnimals.getText().toString() + ": " + text[0]);
//                            + ":  " + other_items;
                } else {
                    editText6.setEnabled(false);
                }


                if (cbOthersitems.isChecked() == true) {
                    text[0] = tvOtherItems.getText().toString();

                    final_text[0] = final_text[0] + "  " + (cbOthersitems.getText().toString() + ": " + text[0]);
//                            + ":  " + other_items;
                }

                Poaching.fromMultipleSelectionDialog = final_text[0];
                Poaching.tvNameAndNumber.setText(final_text[0]);
                showDialog.dismiss();
            }
        });
//        return displayText;

    }
}
