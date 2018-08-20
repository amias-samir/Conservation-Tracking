package com.naxa.conservationtrackingapp.fragment_main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.naxa.conservationtrackingapp.R;

import java.util.ArrayList;
import java.util.List;

import com.naxa.conservationtrackingapp.adapter.GridSpacingItemDecorator;
import com.naxa.conservationtrackingapp.adapter.RecyclerListAdapter3;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.AlternativeCropSupport;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.CropDepredation;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.EWSEquipmentStatusRecordingActivity;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.ElephantActivityRecordingCSUActivity;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.HWCEndowmentFund;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.HWCMitigationSupport;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.HumanCasualty;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.InfrastructureDamage;
import com.naxa.conservationtrackingapp.human_wildlife_conflict_management.LivestockDepredation;
import com.naxa.conservationtrackingapp.model.Single_String_Title;

import Utls.SharedPreferenceUtils;

/**
 * Created by ramaan on 1/5/2016.
 */
public class Fragment_Human_Wildlife_Conflict_Management extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<Single_String_Title> resultCur = new ArrayList<>();
    String urll;
    ProgressDialog mProgressDlg;
    String catId, link , name;
    RecyclerListAdapter3 ca;

    SharedPreferenceUtils sharedPreferenceUtils;

    public Fragment_Human_Wildlife_Conflict_Management() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_list, container, false);
        // Inflate the layout for this fragment
        sharedPreferenceUtils = new SharedPreferenceUtils(getActivity());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.NewsList);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorator(1, 5, true));
        createList();
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildPosition(child);
                    loadForm(position);
                    String title = resultCur.get(position).title;
//                    String detail = resultCur.get(position).instruction;
//
//                    Intent intent = new Intent(getActivity(), Saahitya_Detail.class);
//                    intent.putExtra("ARTICLETITLE", title);
//                    intent.putExtra("ARTICLEDETAIL", detail);
//                    intent.putExtra("ARTICLEAUTHOR", "");
//                    intent.putExtra("ARTICLESOURCE","");
//                    intent.putExtra("TITLE", getString(R.string.cook_book));
//
//                    startActivity(intent);
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public void loadForm(int position){
        if(sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.KEY_IS_USER_LOGGED_IN, false)) {
            switch (position) {
                case 0:
                    startActivity(new Intent(getActivity(), HumanCasualty.class));
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), CropDepredation.class));
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), LivestockDepredation.class));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), InfrastructureDamage.class));
                    break;

                case 4:
                    startActivity(new Intent(getActivity(), AlternativeCropSupport.class));
                    break;

                case 5:
                    startActivity(new Intent(getActivity(), HWCEndowmentFund.class));
                    break;

                case 6:
                    startActivity(new Intent(getActivity(), HWCMitigationSupport.class));
                    break;

                case 7:
                    startActivity(new Intent(getActivity(), EWSEquipmentStatusRecordingActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(getActivity(), ElephantActivityRecordingCSUActivity.class));
                    break;
            }
        }else {
            switch (position) {
                case 0:
                    startActivity(new Intent(getActivity(), HumanCasualty.class));
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), CropDepredation.class));
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), LivestockDepredation.class));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), InfrastructureDamage.class));
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void createList() {
        if(sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.KEY_IS_USER_LOGGED_IN, false)) {
            resultCur.clear();
            Single_String_Title newData1 = new Single_String_Title();
            newData1.title = "Human Casualty";
            resultCur.add(newData1);
            Single_String_Title newData2 = new Single_String_Title();
            newData2.title = "Crop Depredation";
            resultCur.add(newData2);
            Single_String_Title newData3 = new Single_String_Title();
            newData3.title = "Livestock Depredation";
            resultCur.add(newData3);
            Single_String_Title newData4 = new Single_String_Title();
            newData4.title = "Infrastructure Damage";
            resultCur.add(newData4);
            Single_String_Title newData5 = new Single_String_Title();
            newData5.title = "Alternative Crop Support";
            resultCur.add(newData5);
            Single_String_Title newData6 = new Single_String_Title();
            newData6.title = "HWC Endowment Fund";
            resultCur.add(newData6);
            Single_String_Title newData7 = new Single_String_Title();
            newData7.title = "HWC Mitigation Support";
            resultCur.add(newData7);

            Single_String_Title newData8 = new Single_String_Title();
            newData8.title = "EWS Equipment Status Recording";
            resultCur.add(newData8);

            Single_String_Title newData9 = new Single_String_Title();
            newData9.title = "Elephant Activity Recording";
            resultCur.add(newData9);

            fillTable();
        }else {
            resultCur.clear();
            Single_String_Title newData1 = new Single_String_Title();
            newData1.title = "Human Casualty";
            resultCur.add(newData1);
            Single_String_Title newData2 = new Single_String_Title();
            newData2.title = "Crop Depredation";
            resultCur.add(newData2);
            Single_String_Title newData3 = new Single_String_Title();
            newData3.title = "Livestock Depredation";
            resultCur.add(newData3);
            Single_String_Title newData4 = new Single_String_Title();
            newData4.title = "Infrastructure Damage";
            resultCur.add(newData4);

            fillTable();
        }
    }

    public void fillTable() {
        Log.e("FILLTABLE", "INSIDE FILL TABLE");
        ca = new RecyclerListAdapter3(resultCur);
        recyclerView.setAdapter(ca);
        Log.e("FILLTABLE", "AFTER FILL TABLE");
//        CheckValues.setValue();
    }

}
