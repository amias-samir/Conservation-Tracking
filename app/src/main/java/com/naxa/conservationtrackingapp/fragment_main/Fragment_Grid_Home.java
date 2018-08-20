package com.naxa.conservationtrackingapp.fragment_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.naxa.conservationtrackingapp.activities.General_Form;
import com.naxa.conservationtrackingapp.MainActivity;
import com.naxa.conservationtrackingapp.R;
import com.naxa.conservationtrackingapp.activities.SavedFormsActivity;

import com.naxa.conservationtrackingapp.adapter.GridSpacingItemDecorator;
import com.naxa.conservationtrackingapp.adapter.Grid_Adapter;

import Utls.SharedPreferenceUtils;

/**
 * Created by user1 on 11/30/2015.
 */
public class Fragment_Grid_Home extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
//    ConnectonDetector connectonDetector;

    public Fragment_Grid_Home() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_list_view, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.NewsList);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spanCount = 2;
        int spacing = 5;
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecorator(spanCount, spacing, includeEdge));

        mAdapter = new Grid_Adapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildPosition(child);
                    callFragment(position);
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

    private void callFragment(int position) {

        Fragment mFragment = null;
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        String title = null;
        int color = R.color.teal;
//        connectonDetector = new ConnectonDettor.isConnectedToInternet();
        switch (position) {
            case 0:
                mFragment = new Fragment_Forest();
                title = "Forest";
                color = R.color.forest;
                break;
            case 1:
                mFragment = new Fragment_WMM();
                title = "Wildlife Management and Monitoring";
                color = R.color.wmm;
                break;
            case 2:
                mFragment = new Fragment_Wildlife_Trade_Control();
                title = "Wildlife Trade Control";
                color = R.color.wild_life_trade_control;
                break;
            case 3:
                mFragment = new Fragment_Human_Wildlife_Conflict_Management();
                title = "Human Wildlife Conflict Management";
                color = R.color.human_wildLife_conflict;
                break;
            case 4:
                mFragment = new Fragment_ClimateChange();
                title = "Climate Change";
                color = R.color.climate_change;
                break;
            case 5:
                mFragment = new Fragment_CommunitySupport();
                title = "Community Support";
                color = R.color.community_support;
                break;
            case 6:
                mFragment = new Fragment_Conservation_Education();
                title = "Conservation Education";
                color = R.color.conservation_education;
                break;
            case 7:
                mFragment = new Fragment_WildlifeMonitoringTechniques();
                title = "WildLife Monitoring Techniques";
                color = R.color.wildlife_monitoring_technique;
                break;
            case 8:
                startActivity(new Intent(getActivity(), SavedFormsActivity.class));
//                mFragment = new Fragment_SavedForms();
//                title = "Saved Forms";
//                color = R.color.saved_form;
                break;
            case 9:
                startActivity(new Intent(getActivity(), General_Form.class));
                break;

        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.container, mFragment).commit();
        }
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((MainActivity) getActivity()).getToolbar().setBackgroundResource(color);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.home);
        ((MainActivity) getActivity()).getToolbar().setBackgroundResource(R.color.teal);
    }
}
