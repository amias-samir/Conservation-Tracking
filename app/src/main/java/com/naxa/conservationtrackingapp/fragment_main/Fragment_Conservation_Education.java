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
import com.naxa.conservationtrackingapp.adapter.RecyclerListAdapter6;
import com.naxa.conservationtrackingapp.conservation_education.ConservationEducationAwareness;
import com.naxa.conservationtrackingapp.conservation_education.EcoClubDatabase;
import com.naxa.conservationtrackingapp.conservation_education.EcoClubSupport;
import com.naxa.conservationtrackingapp.model.Single_String_Title;

/**
 * Created by ramaan on 1/5/2016.
 */
public class Fragment_Conservation_Education extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<Single_String_Title> resultCur = new ArrayList<>();
    String urll;
    ProgressDialog mProgressDlg;
    String catId, link , name;
    RecyclerListAdapter6 ca;

    public Fragment_Conservation_Education() {

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
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), EcoClubDatabase.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), EcoClubSupport.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), ConservationEducationAwareness.class));
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
        newData1.title = "Eco-Club Database";
        resultCur.add(newData1);
        Single_String_Title newData2 = new Single_String_Title();
        newData2.title = "Eco-Club Support";
        resultCur.add(newData2);
        Single_String_Title newData3 = new Single_String_Title();
        newData3.title = "Conservation Education Awareness";
        resultCur.add(newData3);


        fillTable();
    }

    public void fillTable() {
        Log.e("FILLTABLE", "INSIDE FILL TABLE");
        ca = new RecyclerListAdapter6(resultCur);
        recyclerView.setAdapter(ca);
        Log.e("FILLTABLE", "AFTER FILL TABLE");
//        CheckValues.setValue();
    }

}
