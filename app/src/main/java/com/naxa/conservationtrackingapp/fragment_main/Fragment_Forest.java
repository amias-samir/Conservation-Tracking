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

import com.naxa.conservationtrackingapp.forest.Cf_Detail;

import com.naxa.conservationtrackingapp.forest.ForestEncroachmentStatus;
import com.naxa.conservationtrackingapp.forest.ForestFire;
import com.naxa.conservationtrackingapp.forest.ForestIntegLivestocManagement;
import com.naxa.conservationtrackingapp.forest.ForestProctection;
import com.naxa.conservationtrackingapp.forest.Forest_E_EvictionStatus;
import com.naxa.conservationtrackingapp.forest.IllegalLogging;
import com.naxa.conservationtrackingapp.forest.NurseryDetails;
import com.naxa.conservationtrackingapp.forest.PlantationDetail;
import com.naxa.conservationtrackingapp.R;

import java.util.ArrayList;
import java.util.List;

import com.naxa.conservationtrackingapp.adapter.GridSpacingItemDecorator;
import com.naxa.conservationtrackingapp.adapter.RecyclerListAdapter;
import com.naxa.conservationtrackingapp.model.Single_String_Title;

import Utls.SharedPreferenceUtils;

/**
 * Created by ramaan on 1/5/2016.
 */
public class Fragment_Forest extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<Single_String_Title> resultCur = new ArrayList<>();
    String urll;
    ProgressDialog mProgressDlg;
    String catId, link , name;
    RecyclerListAdapter ca;

    SharedPreferenceUtils sharedPreferenceUtils ;

    public Fragment_Forest() {

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
                    startActivity(new Intent(getActivity(), Cf_Detail.class));
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), NurseryDetails.class));
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), PlantationDetail.class));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), ForestProctection.class));
                    break;
                case 4:
                    startActivity(new Intent(getActivity(), ForestIntegLivestocManagement.class));
                    break;
                case 5:
                    startActivity(new Intent(getActivity(), ForestEncroachmentStatus.class));
                    break;
                case 6:
                    startActivity(new Intent(getActivity(), Forest_E_EvictionStatus.class));
                    break;
                case 7:
                    startActivity(new Intent(getActivity(), ForestFire.class));
                    break;
                case 8:
                    startActivity(new Intent(getActivity(), IllegalLogging.class));
                    break;
            }
        }else {
            switch (position) {
                case 0:
                    startActivity(new Intent(getActivity(), ForestEncroachmentStatus.class));
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), Forest_E_EvictionStatus.class));
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), ForestFire.class));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), IllegalLogging.class));
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
            newData1.title = "CF Details";
            resultCur.add(newData1);
            Single_String_Title newData2 = new Single_String_Title();
            newData2.title = "Nursery Details";
            resultCur.add(newData2);
            Single_String_Title newData3 = new Single_String_Title();
            newData3.title = "Plantation Details";
            resultCur.add(newData3);
            Single_String_Title newData4 = new Single_String_Title();
            newData4.title = "Forest Protection";
            resultCur.add(newData4);
            Single_String_Title newData5 = new Single_String_Title();
            newData5.title = "Integrated Livestock Management";
            resultCur.add(newData5);
            Single_String_Title newData6 = new Single_String_Title();
            newData6.title = "Encroachment Status";
            resultCur.add(newData6);
            Single_String_Title newData7 = new Single_String_Title();
            newData7.title = "Encroachment Eviction Status";
            resultCur.add(newData7);
            Single_String_Title newData8 = new Single_String_Title();
            newData8.title = "Forest Fire";
            resultCur.add(newData8);
            Single_String_Title newData9 = new Single_String_Title();
            newData9.title = "Illegal Logging";
            resultCur.add(newData9);
            fillTable();
        }else {
            resultCur.clear();
            Single_String_Title newData6 = new Single_String_Title();
            newData6.title = "Encroachment Status";
            resultCur.add(newData6);
            Single_String_Title newData7 = new Single_String_Title();
            newData7.title = "Encroachment Eviction Status";
            resultCur.add(newData7);
            Single_String_Title newData8 = new Single_String_Title();
            newData8.title = "Forest Fire";
            resultCur.add(newData8);
            Single_String_Title newData9 = new Single_String_Title();
            newData9.title = "Illegal Logging";
            resultCur.add(newData9);
            fillTable();
        }

    }

    public void fillTable() {
        Log.e("FILLTABLE", "INSIDE FILL TABLE");
        ca = new RecyclerListAdapter(resultCur);
        recyclerView.setAdapter(ca);
        Log.e("FILLTABLE", "AFTER FILL TABLE");
//        CheckValues.setValue();
    }

}
