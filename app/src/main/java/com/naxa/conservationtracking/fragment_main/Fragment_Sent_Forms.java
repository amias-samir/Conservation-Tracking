package com.naxa.conservationtracking.fragment_main;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naxa.conservationtracking.activities.General_Form;
import com.naxa.conservationtracking.R;

import java.util.ArrayList;
import java.util.List;

import com.naxa.conservationtracking.adapter.GridSpacingItemDecorator;
import com.naxa.conservationtracking.adapter.RecyclerItemClickListener;
import com.naxa.conservationtracking.adapter.Sent_Forms_Adapter;
import com.naxa.conservationtracking.climate_change.BiogasDetail;
import com.naxa.conservationtracking.climate_change.ICSDetails;
import com.naxa.conservationtracking.community_support.CapacityBuilding;
import com.naxa.conservationtracking.community_support.IGA;
import com.naxa.conservationtracking.community_support.InstitutionalSupport;
import com.naxa.conservationtracking.community_support.RevolvingFund;
import com.naxa.conservationtracking.community_support.SkillBasedTraining;
import com.naxa.conservationtracking.community_support.TALCooperativeMicroFinance;
import com.naxa.conservationtracking.conservation_education.ConservationEducationAwareness;
import com.naxa.conservationtracking.conservation_education.EcoClubDatabase;
import com.naxa.conservationtracking.conservation_education.EcoClubSupport;
import com.naxa.conservationtracking.database.DataBaseConserVationTracking;
import com.naxa.conservationtracking.dialog.Default_DIalog;
import com.naxa.conservationtracking.forest.Cf_Detail;
import com.naxa.conservationtracking.forest.ForestEncroachmentStatus;
import com.naxa.conservationtracking.forest.ForestFire;
import com.naxa.conservationtracking.forest.ForestIntegLivestocManagement;
import com.naxa.conservationtracking.forest.ForestProctection;
import com.naxa.conservationtracking.forest.Forest_E_EvictionStatus;
import com.naxa.conservationtracking.forest.IllegalLogging;
import com.naxa.conservationtracking.forest.NurseryDetails;
import com.naxa.conservationtracking.forest.PlantationDetail;
import com.naxa.conservationtracking.human_wildlife_conflict_management.AlternativeCropSupport;
import com.naxa.conservationtracking.human_wildlife_conflict_management.CropDepredation;
import com.naxa.conservationtracking.human_wildlife_conflict_management.HWCEndowmentFund;
import com.naxa.conservationtracking.human_wildlife_conflict_management.HWCMitigationSupport;
import com.naxa.conservationtracking.human_wildlife_conflict_management.HumanCasualty;
import com.naxa.conservationtracking.human_wildlife_conflict_management.InfrastructureDamage;
import com.naxa.conservationtracking.human_wildlife_conflict_management.LivestockDepredation;
import com.naxa.conservationtracking.model.SavedFormParameters;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.GharialAndMuggerMonitoringAnthropogenicParameters;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.GharialAndMuggerMonitoringBiologicalParameters;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.HumanDisturbance;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.SpeciesOccupancySurvey;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.TigerPreyBaseMonitoring;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.TigerTrappingDetail;
import com.naxa.conservationtracking.wildlife_monitoring_techniques.VegetationAndInvasiveSpeciesSampling;
import com.naxa.conservationtracking.wildlife_trade_control.AntiPoachingInfrastructure;
import com.naxa.conservationtracking.wildlife_trade_control.AntipoachingSupport;
import com.naxa.conservationtracking.wildlife_trade_control.CBAPOStatus;
import com.naxa.conservationtracking.wildlife_trade_control.Poaching;
import com.naxa.conservationtracking.wmm.EmergencyRescueRelease;
import com.naxa.conservationtracking.wmm.GrasslandManagement;
import com.naxa.conservationtracking.wmm.InvasiveSpeciesManagement;
import com.naxa.conservationtracking.wmm.WetlandManagement;
import com.naxa.conservationtracking.wmm.WildlifeMortalityDetails;
import com.naxa.conservationtracking.wmm.WildlifeSightingDetails;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Sent_Forms extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static List<SavedFormParameters> resultCur = new ArrayList<>();
    Sent_Forms_Adapter ca;
    Context context = getActivity() ;

    public Fragment_Sent_Forms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment__sent__form_list, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.NewsList);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecorator(1, 5, true));
        createList();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                alert_editlist(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {


            }
        }));

        return rootview;
    }

    //-------------------------------Method Dialog Box List for << REPORT DETAIL, SEND and DELETE >>-----------------------------------//
    protected void alert_editlist(final int position) {

        // TODO Auto-generated method stub
        final CharSequence[] items = {"Open", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Action");

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (items[item] == "Open") {
                    String id = resultCur.get(position).formId;
                    String jSon = resultCur.get(position).jSON;
                    String photo = resultCur.get(position).photo;
                    String gps = resultCur.get(position).gps;
                    String status = resultCur.get(position).status;
                    loadForm(id, jSon, photo, gps, status);

                } else if (items[item] == "Delete") {
                    DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;

                    final Dialog showDialog = new Dialog(getActivity());
                    showDialog.setContentView(R.layout.delete_dialog);
                    TextView tvDisplay = (TextView) showDialog.findViewById(R.id.textViewDefaultDialog);
                    Button btnOk = (Button) showDialog.findViewById(R.id.button_delete);
                    Button cancle = (Button) showDialog.findViewById(R.id.button_cancle);
                    showDialog.setTitle("Are You Sure ??");
                    tvDisplay.setText("Are you sure you want to delete the data ??");
                    showDialog.setCancelable(true);
                    showDialog.show();
                    showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

                    btnOk.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub


                            DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(getActivity());
                            dataBaseConserVationTracking.open();
                            int id = (int) dataBaseConserVationTracking.updateTable_DeleteFlag(resultCur.get(position).dbId);
//                Toast.makeText(getActivity() ,resultCur.get(position).date+ " Long Clicked "+id , Toast.LENGTH_SHORT ).show();
                            dataBaseConserVationTracking.close();
                            showDialog.dismiss();
                            createList();
                        }
                    });
                    cancle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            showDialog.dismiss();
                        }
                    });

                }
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showDeleteDialog(final int position) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(getActivity());
        showDialog.setContentView(R.layout.delete_dialog);
        TextView tvDisplay = (TextView) showDialog.findViewById(R.id.textViewDefaultDialog);
        Button btnOk = (Button) showDialog.findViewById(R.id.button_delete);
        Button cancle = (Button) showDialog.findViewById(R.id.button_cancle);
        showDialog.setCancelable(true);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(getActivity());
                dataBaseConserVationTracking.open();
                int id = (int) dataBaseConserVationTracking.updateTable_DeleteFlag(resultCur.get(position).dbId);
//                Toast.makeText(getActivity() ,resultCur.get(position).date+ " Long Clicked "+id , Toast.LENGTH_SHORT ).show();
                dataBaseConserVationTracking.close();
                showDialog.dismiss();
                createList();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog.dismiss();
            }
        });

    }

    public void loadForm(String formId,String jsonData ,String photo ,  String gps, String status){
        switch (formId){
            case "1" :
                Intent intent1 = new Intent(getActivity(), Cf_Detail.class);
                intent1.putExtra("JSON1", jsonData);
                intent1.putExtra("photo" , photo);
                intent1.putExtra("gps" , gps) ;
                intent1.putExtra("status" , status) ;
                startActivity(intent1);
                break;
            case "2" :
                Intent intent2 = new Intent(getActivity(), NurseryDetails.class);
                intent2.putExtra("JSON1", jsonData);
                intent2.putExtra("photo" , photo);
                intent2.putExtra("gps" , gps) ;
                intent2.putExtra("status" , status) ;
                startActivity(intent2);
                break;
            case "3" :
                Intent intent3 = new Intent(getActivity(), PlantationDetail.class);
                intent3.putExtra("JSON1", jsonData);
                intent3.putExtra("photo" , photo);
                intent3.putExtra("gps" , gps) ;
                intent3.putExtra("status" , status) ;
                startActivity(intent3);
                break;
            case "4" :
                Intent intent4 = new Intent(getActivity(), ForestProctection.class);
                intent4.putExtra("JSON1", jsonData);
                intent4.putExtra("photo" , photo);
                intent4.putExtra("gps" , gps) ;
                intent4.putExtra("status" , status) ;
                startActivity(intent4);
                break;
            case "5" :
                Intent intent5 = new Intent(getActivity(), ForestIntegLivestocManagement.class);
                intent5.putExtra("JSON1", jsonData);
                intent5.putExtra("photo" , photo);
                intent5.putExtra("status" , status) ;
                startActivity(intent5);
                break;
            case "6" :
                Intent intent6 = new Intent(getActivity(), ForestEncroachmentStatus.class);
                intent6.putExtra("JSON1", jsonData);
                intent6.putExtra("photo" , photo);
                intent6.putExtra("gps" , gps) ;
                intent6.putExtra("status" , status) ;
                startActivity(intent6);
                break;
            case "7" :
                Intent intent7 = new Intent(getActivity(), Forest_E_EvictionStatus.class);
                intent7.putExtra("JSON1", jsonData);
                intent7.putExtra("photo" , photo);
                intent7.putExtra("gps" , gps) ;
                intent7.putExtra("status" , status) ;
                startActivity(intent7);
                break;
            case "8" :
                Intent intent8 = new Intent(getActivity(), ForestFire.class);
                intent8.putExtra("JSON1", jsonData);
                intent8.putExtra("photo" , photo);
                intent8.putExtra("gps" , gps) ;
                intent8.putExtra("status" , status) ;
                startActivity(intent8);
                break;
            case "9" :
                Intent intent9 = new Intent(getActivity(), IllegalLogging.class);
                intent9.putExtra("JSON1", jsonData);
                intent9.putExtra("photo" , photo);
                intent9.putExtra("gps" , gps) ;
                intent9.putExtra("status" , status) ;;
                startActivity(intent9);
                break;
            case "21" :
                Intent intentto = new Intent(getActivity(), EmergencyRescueRelease.class);
                intentto.putExtra("JSON1", jsonData);
                intentto.putExtra("photo", photo);
                intentto.putExtra("gps" , gps) ;
                intentto.putExtra("status" , status) ;
                startActivity(intentto);
                break;
            case "22" :
                Intent intentt = new Intent(getActivity(), GrasslandManagement.class);
                intentt.putExtra("JSON1", jsonData);
                intentt.putExtra("photo", photo);
                intentt.putExtra("gps" , gps) ;
                intentt.putExtra("status" , status) ;
                startActivity(intentt);
                break;
            case "23" :
                Intent intentth = new Intent(getActivity(), InvasiveSpeciesManagement.class);
                intentth.putExtra("JSON1", jsonData);
                intentth.putExtra("photo", photo);
                intentth.putExtra("gps" , gps) ;
                intentth.putExtra("status" , status) ;
                startActivity(intentth);
                break;
            case "24" :
                Intent intentf = new Intent(getActivity(), WetlandManagement.class);
                intentf.putExtra("JSON1", jsonData);
                intentf.putExtra("photo", photo);
                intentf.putExtra("status" , status) ;
                startActivity(intentf);
                break;
            case "25" :
                Intent intentfi = new Intent(getActivity(), WildlifeMortalityDetails.class);
                intentfi.putExtra("JSON1", jsonData);
                intentfi.putExtra("photo", photo);
                intentfi.putExtra("gps" , gps) ;
                intentfi.putExtra("status" , status) ;
                startActivity(intentfi);
                break;
            case "26" :
                Intent intente = new Intent(getActivity(), WildlifeSightingDetails.class);
                intente.putExtra("JSON1", jsonData);
                intente.putExtra("photo", photo);
                intente.putExtra("gps" , gps) ;
                intente.putExtra("status" , status) ;
                startActivity(intente);
                break;

            case "31" :
                Intent intent16 = new Intent(getActivity(), Poaching.class);
                intent16.putExtra("JSON1", jsonData);
                intent16.putExtra("photo", photo);
                intent16.putExtra("gps" , gps) ;
                intent16.putExtra("status" , status) ;
                startActivity(intent16);
                break;

            case "32" :
                Intent intent17 = new Intent(getActivity(), CBAPOStatus.class);
                intent17.putExtra("JSON1", jsonData);
                intent17.putExtra("photo" , photo);
                intent17.putExtra("gps" , gps) ;
                intent17.putExtra("status" , status) ;
                startActivity(intent17);
                break;

            case "33" :
                Intent intent18 = new Intent(getActivity(), AntiPoachingInfrastructure.class);
                intent18.putExtra("JSON1", jsonData);
                intent18.putExtra("photo" , photo);
                intent18.putExtra("gps" , gps) ;
                intent18.putExtra("status" , status) ;
                startActivity(intent18);
                break;
            case "34" :
                Intent intent19 = new Intent(getActivity(), AntipoachingSupport.class);
                intent19.putExtra("JSON1", jsonData);
                intent19.putExtra("photo", photo);
                intent19.putExtra("gps" , gps) ;
                intent19.putExtra("status" , status) ;
                startActivity(intent19);
                break;

            case "41" :
                Intent intent20 = new Intent(getActivity(), HumanCasualty.class);
                intent20.putExtra("JSON1", jsonData);
                intent20.putExtra("photo", photo);
                intent20.putExtra("gps" , gps) ;
                intent20.putExtra("status" , status) ;
                startActivity(intent20);
                break;

            case "42" :
                Intent intent21 = new Intent(getActivity(), CropDepredation.class);
                intent21.putExtra("JSON1", jsonData);
                intent21.putExtra("photo", photo);
                intent21.putExtra("gps" , gps) ;
                intent21.putExtra("status" , status) ;
                startActivity(intent21);
                break;

            case "43" :
                Intent intent22 = new Intent(getActivity(), LivestockDepredation.class);
                intent22.putExtra("JSON1", jsonData);
                intent22.putExtra("photo", photo);
                intent22.putExtra("gps" , gps) ;
                intent22.putExtra("status" , status) ;
                startActivity(intent22);
                break;

            case "44" :
                Intent intent23 = new Intent(getActivity(), InfrastructureDamage.class);
                intent23.putExtra("JSON1", jsonData);
                intent23.putExtra("photo", photo);
                intent23.putExtra("gps" , gps) ;
                intent23.putExtra("status" , status) ;
                startActivity(intent23);
                break;

            case "45" :
                Intent intent24 = new Intent(getActivity(), AlternativeCropSupport.class);
                intent24.putExtra("JSON1", jsonData);
                intent24.putExtra("photo", photo);
                intent24.putExtra("gps" , gps) ;
                intent24.putExtra("status" , status) ;
                startActivity(intent24);
                break;

            case "46" :
                Intent intent25 = new Intent(getActivity(), HWCEndowmentFund.class);
                intent25.putExtra("JSON1", jsonData);
                intent25.putExtra("photo", photo);
                intent25.putExtra("gps" , gps) ;
                intent25.putExtra("status" , status) ;
                startActivity(intent25);
                break;

            case "47" :
                Intent intent26 = new Intent(getActivity(), HWCMitigationSupport.class);
                intent26.putExtra("JSON1", jsonData);
                intent26.putExtra("photo", photo);
                intent26.putExtra("gps" , gps) ;
                intent26.putExtra("status" , status) ;
                startActivity(intent26);
                break;
            case "51" :
                Intent intent51 = new Intent(getActivity(), BiogasDetail.class);
                intent51.putExtra("JSON1", jsonData);
                intent51.putExtra("photo", photo);
                intent51.putExtra("gps" , gps) ;
                intent51.putExtra("status" , status) ;
                startActivity(intent51);
                break;
            case "52" :
                Intent intent52 = new Intent(getActivity(), ICSDetails.class);
                intent52.putExtra("JSON1", jsonData);
                intent52.putExtra("photo", photo);
                intent52.putExtra("gps", gps) ;
                intent52.putExtra("status" , status) ;
                startActivity(intent52);
                break;
            case "61" :
                Intent intent61 = new Intent(getActivity(), InstitutionalSupport.class);
                intent61.putExtra("JSON1", jsonData);
                intent61.putExtra("photo", photo);
                intent61.putExtra("gps", gps) ;
                intent61.putExtra("status" , status) ;
                startActivity(intent61);
                break;
            case "62" :
                Intent intent62 = new Intent(getActivity(), IGA.class);
                intent62.putExtra("JSON1", jsonData);
                intent62.putExtra("photo", photo);
                intent62.putExtra("gps", gps) ;
                intent62.putExtra("status" , status) ;
                startActivity(intent62);
                break;
            case "63" :
                Intent intent63 = new Intent(getActivity(), SkillBasedTraining.class);
                intent63.putExtra("JSON1", jsonData);
                intent63.putExtra("photo", photo);
                intent63.putExtra("gps", gps) ;
                intent63.putExtra("status" , status) ;
                startActivity(intent63);
                break;
            case "64" :
                Intent intent64 = new Intent(getActivity(), TALCooperativeMicroFinance.class);
                intent64.putExtra("JSON1", jsonData);
                intent64.putExtra("photo", photo);
                intent64.putExtra("gps", gps) ;
                intent64.putExtra("status" , status) ;
                startActivity(intent64);
                break;
            case "65" :
                Intent intent65 = new Intent(getActivity(), RevolvingFund.class);
                intent65.putExtra("JSON1", jsonData);
                intent65.putExtra("photo", photo);
                intent65.putExtra("gps", gps) ;
                intent65.putExtra("status" , status) ;
                startActivity(intent65);
                break;
            case "66" :
                Intent intent66 = new Intent(getActivity(), CapacityBuilding.class);
                intent66.putExtra("JSON1", jsonData);
                intent66.putExtra("photo", photo);
                intent66.putExtra("gps", gps) ;
                intent66.putExtra("status" , status) ;
                startActivity(intent66);
                break;
            case "71" :
                Intent intent71 = new Intent(getActivity(), ConservationEducationAwareness.class);
                intent71.putExtra("JSON1", jsonData);
                intent71.putExtra("photo", photo);
                intent71.putExtra("gps", gps) ;
                intent71.putExtra("status" , status) ;
                startActivity(intent71);
                break;
            case "72" :
                Intent intent72 = new Intent(getActivity(), EcoClubDatabase.class);
                intent72.putExtra("JSON1", jsonData);
                intent72.putExtra("photo", photo);
                intent72.putExtra("gps", gps) ;
                intent72.putExtra("status" , status) ;
                startActivity(intent72);
                break;
            case "73" :
                Intent intent73 = new Intent(getActivity(), EcoClubSupport.class);
                intent73.putExtra("JSON1", jsonData);
                intent73.putExtra("photo", photo);
                intent73.putExtra("gps", gps) ;
                intent73.putExtra("status" , status) ;
                startActivity(intent73);
                break;

            case "81" :
                Intent intent81 = new Intent(getActivity(), GharialAndMuggerMonitoringBiologicalParameters.class);
                intent81.putExtra("JSON1", jsonData);
                intent81.putExtra("gps", gps) ;
                intent81.putExtra("status" , status) ;
                startActivity(intent81);
                break;

            case "82" :
                Intent intent82 = new Intent(getActivity(), GharialAndMuggerMonitoringAnthropogenicParameters.class);
                intent82.putExtra("JSON1", jsonData);
                intent82.putExtra("gps", gps) ;
                intent82.putExtra("status" , status) ;;
                startActivity(intent82);
                break;

            case "83" :
                Intent intent83 = new Intent(getActivity(), SpeciesOccupancySurvey.class);
                intent83.putExtra("JSON1", jsonData);
                intent83.putExtra("gps", gps) ;
                intent83.putExtra("status" , status) ;
                startActivity(intent83);
                break;
            case "84" :
                Intent intent84 = new Intent(getActivity(), HumanDisturbance.class);
                intent84.putExtra("JSON1", jsonData);
                intent84.putExtra("photo", photo);
                intent84.putExtra("gps", gps) ;
                intent84.putExtra("status" , status) ;
                startActivity(intent84);
                break;
            case "85" :
                Intent intent85 = new Intent(getActivity(), TigerTrappingDetail.class);
                intent85.putExtra("JSON1", jsonData);
                intent85.putExtra("photo", photo);
                intent85.putExtra("gps", gps) ;
                intent85.putExtra("status" , status) ;
                startActivity(intent85);
                break;
            case "86" :
                Intent intent86 = new Intent(getActivity(), TigerPreyBaseMonitoring.class);
                intent86.putExtra("JSON1", jsonData);
                intent86.putExtra("photo", photo);
                intent86.putExtra("gps", gps) ;
                intent86.putExtra("status" , status) ;
                startActivity(intent86);
                break;
            case "87" :
                Intent intent87 = new Intent(getActivity(), VegetationAndInvasiveSpeciesSampling.class);
                intent87.putExtra("JSON1", jsonData);
                intent87.putExtra("photo", photo);
                intent87.putExtra("gps", gps) ;
                intent87.putExtra("status" , status) ;
                startActivity(intent87);
                break;

            case "100" :
                Intent intent100 = new Intent(getActivity(), General_Form.class);
                intent100.putExtra("JSON1", jsonData);
                intent100.putExtra("photo", photo);
                intent100.putExtra("gps", gps) ;
                intent100.putExtra("status" , status) ;
                startActivity(intent100);
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
//        Single_String_Title newData1 = new Single_String_Title();
//        newData1.title = "CF Detail";
//        resultCur.add(newData1);
        DataBaseConserVationTracking dataBaseConserVationTracking = new DataBaseConserVationTracking(getActivity());
        dataBaseConserVationTracking.open();
        boolean isTableEmpty = dataBaseConserVationTracking.is_TABLE_MAIN_Empty();
        if(isTableEmpty){
            Default_DIalog.showDefaultDialog(getActivity() , R.string.app_name , "No data Saved ");
        }else{
            int count = dataBaseConserVationTracking.returnTotalNoOf_TABLE_MAIN_NUM();
            for(int i=count ; i>=1 ; i--) {
//                String[] data = dataBaseConserVationTracking.return_Data_TABLE_MAIN(i);
                String[] data = dataBaseConserVationTracking.return_Data_ID(i);
                SavedFormParameters savedData = new SavedFormParameters();
                Log.e("DATA" , "08 "+data[8] +" one: "+ data[1]+" two: "+data[2]);
//                savedData.dbId = data[0];
                savedData.formId = data[0];
                savedData.formName = data[1];
                savedData.date = data[2];
                savedData.jSON = data[3];
                savedData.gps = data[4] ;
                savedData.photo = data[5];
                savedData.status = data[6];
                savedData.deletedStatus = data[7];
                savedData.dbId = data[8];

                if(data[7].equals("0")) {

                    if(data[6].equals("Sent")) {
                        resultCur.add(savedData);
                    }
                }

            }
        }
        fillTable();
    }

    public void fillTable() {
        Log.e("FILLTABLE", "INSIDE FILL TABLE");
        ca = new Sent_Forms_Adapter(resultCur);
        recyclerView.setAdapter(ca);
        ca.notifyDataSetChanged();

        Log.e("FILLTABLE", "AFTER FILL TABLE");
//        CheckValues.setValue();
    }

}


