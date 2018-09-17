package com.naxa.conservationtrackingapp.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by ramaan on 2/6/2016.
 */
public class StaticListOfCoordinates {
    static ArrayList<LatLng> list = new ArrayList<LatLng>();
    public static void setList(ArrayList<LatLng>  listToAdd){
        list = listToAdd ;
    }
    public static ArrayList<LatLng> getList(){
        return list ;
    }
}
