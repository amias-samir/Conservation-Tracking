package com.naxa.conservationtracking.fragment_main;

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

import com.naxa.conservationtracking.R;

import java.util.ArrayList;
import java.util.List;

import com.naxa.conservationtracking.adapter.GridSpacingItemDecorator;
import com.naxa.conservationtracking.adapter.RecyclerListAdapter7;
import com.naxa.conservationtracking.model.Single_String_Title;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.GharialAndMuggerMonitoringAnthropogenicParameters;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.GharialAndMuggerMonitoringBiologicalParameters;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.HumanDisturbance;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.ScatCollectionDetailsActivity;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.SnowLeopardPreyBaseMonitoringActivity;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.SpeciesOccupancySurvey;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.TigerTrappingDetail;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.TigerPreyBaseMonitoring;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.VegetationAndInvasiveSpeciesSampling;

/**
 * Created by ramaan on 1/5/2016.
 */
public class Fragment_WildlifeMonitoringTechniques extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<Single_String_Title> resultCur = new ArrayList<>();
    String urll;
    ProgressDialog mProgressDlg;
    String catId, link , name;
    RecyclerListAdapter7 ca;

    public Fragment_WildlifeMonitoringTechniques() {

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
        switch (position){
            case 0 :
                startActivity(new Intent(getActivity() , SpeciesOccupancySurvey.class));
                break;
            case 1 :
                startActivity(new Intent(getActivity() , HumanDisturbance.class));
                break;
            case 2 :
                startActivity(new Intent(getActivity() , TigerTrappingDetail.class));
                break;
            case 3 :
                startActivity(new Intent(getActivity() , TigerPreyBaseMonitoring.class));
                break;
            case 4 :
                startActivity(new Intent(getActivity() , VegetationAndInvasiveSpeciesSampling.class));
                break;
            case 5 :
                startActivity(new Intent(getActivity() , GharialAndMuggerMonitoringBiologicalParameters.class));
                break;
            case 6 :
                startActivity(new Intent(getActivity() , GharialAndMuggerMonitoringAnthropogenicParameters.class));
                break;
            case 7 :
                startActivity(new Intent(getActivity() , ScatCollectionDetailsActivity.class));
                break;
            case 8 :
                startActivity(new Intent(getActivity() , SnowLeopardPreyBaseMonitoringActivity.class));
                break;

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
        resultCur.clear();
        Single_String_Title newData1 = new Single_String_Title();
        newData1.title = "Species Occupancy Survey";
        resultCur.add(newData1);
        Single_String_Title newData2 = new Single_String_Title();
        newData2.title = "Human Disturbance Details";
        resultCur.add(newData2);
        Single_String_Title newData3 = new Single_String_Title();
        newData3.title = "Camera Trapping Details";
        resultCur.add(newData3);
        Single_String_Title newData4 = new Single_String_Title();
        newData4.title = "Tiger Prey Base Monitoring";
        resultCur.add(newData4);
        Single_String_Title newData5 = new Single_String_Title();
        newData5.title = "Vegetation And Invasive Species Sample";
        resultCur.add(newData5);
        Single_String_Title newData6 = new Single_String_Title();
        newData6.title = "Crocodile Monitoring Details";
        resultCur.add(newData6);
        Single_String_Title newData7 = new Single_String_Title();
        newData7.title = "Crocodile Anthropogenic Details";
        resultCur.add(newData7);
        Single_String_Title newData8 = new Single_String_Title();
        newData8.title = "Scat Collection Details";
        resultCur.add(newData8);
        Single_String_Title newData9 = new Single_String_Title();
        newData9.title = "Snow Leopard Prey base monitoring";
        resultCur.add(newData9);
        fillTable();
    }

    public void fillTable() {
        Log.e("FILLTABLE", "INSIDE FILL TABLE");
        ca = new RecyclerListAdapter7(resultCur);
        recyclerView.setAdapter(ca);
        Log.e("FILLTABLE", "AFTER FILL TABLE");
//        CheckValues.setValue();
    }

}
