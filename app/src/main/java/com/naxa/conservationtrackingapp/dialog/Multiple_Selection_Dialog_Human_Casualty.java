package com.naxa.conservationtrackingapp.dialog;

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
import android.widget.RelativeLayout;

import com.naxa.conservationtrackingapp.R;

import java.util.ArrayList;

import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.HumanCasualty;

/**
 * Created by Samir on 3/8/2017.
 */

public class Multiple_Selection_Dialog_Human_Casualty {
    public static void MultipleChoice_HumanCasualty(Context context, int title, String displayText) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    int width = metrics.widthPixels;
    int height = metrics.heightPixels;


    final Dialog showDialog = new Dialog(context);
    showDialog.setContentView(R.layout.multiple_checkbox_human_casualty);
    final View layout = View.inflate(context, R.layout.multiple_checkbox_human_casualty, null);

    final ArrayList<String> selection = new ArrayList<String>();
    final String[] final_text = {""};
//    final String[] text = new String[1];
    final String other_items;

    final CheckBox cbMassControl, cbHospitalTransfer, cbFirstAid, cbOthersSpecify;

    final RelativeLayout rlOthers;


    final EditText editText1, editText2, editText3, editText4, editText5, editText6, editText7;

    final AutoCompleteTextView tvOtherItems;
    tvOtherItems = (AutoCompleteTextView) showDialog.findViewById(R.id.human_casualty_otherItems);

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

    cbMassControl = (CheckBox) showDialog.findViewById(R.id.checkbox1);
    cbHospitalTransfer = (CheckBox) showDialog.findViewById(R.id.checkbox2);
    cbFirstAid = (CheckBox) showDialog.findViewById(R.id.checkbox3);
//    cbOthersSpecify = (CheckBox) showDialog.findViewById(R.id.checkbox4);


    rlOthers = (RelativeLayout) showDialog. findViewById(R.id.othersLayout);




    btnOk.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (cbMassControl.isChecked() == true) {
                Log.e("cbMassControl","human casualty");
                final_text[0] = final_text[0] + "  " + (cbMassControl.getText().toString() );

            }
            if (cbHospitalTransfer.isChecked() == true) {

                final_text[0] = final_text[0] + " , " + (cbHospitalTransfer.getText().toString());

            }
            if (cbFirstAid.isChecked() == true) {

                final_text[0] = final_text[0] + " , " + (cbFirstAid.getText().toString()  );

            }


                final_text[0] = final_text[0] + " , " + (tvOtherItems.getText().toString()  );




            HumanCasualty.fromMultipleSelectionDialog = final_text[0];
            HumanCasualty.tvFacilationSupport.setText(final_text[0]);
            showDialog.dismiss();
        }
    });

}
}
