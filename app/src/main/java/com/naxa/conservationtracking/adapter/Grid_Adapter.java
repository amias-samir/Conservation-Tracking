package com.naxa.conservationtracking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naxa.conservationtracking.R;

import java.util.ArrayList;
import java.util.List;

import com.naxa.conservationtracking.model.Grid_Model;

import Utls.SharedPreferenceUtils;


/**
 * Created by user1 on 11/30/2015.
 */
public class Grid_Adapter extends RecyclerView.Adapter<Grid_Adapter.ViewHolder>  {
    List<Grid_Model> mItems;

    SharedPreferenceUtils sharedPreferenceUtils;

    String[] colorcode = {
            "#388e3c","#3f51b5",
            "#c0392b","#ff5722",
            "#10b5ff","#607d8b",
            "#78bf26","#00796b",
            "#6b6b6b","#795548"};

    public Grid_Adapter(Context context) {
        super();

        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);
        if(sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.KEY_IS_USER_LOGGED_IN, false)){
            mItems = new ArrayList<Grid_Model>();
            Grid_Model species = new Grid_Model();

            species = new Grid_Model();
            species.setName("Forest");
            species.setThumbnail(R.mipmap.forest);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Wildlife Management and Monitoring");
            species.setThumbnail(R.mipmap.wmm);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Wildlife Trade Control");
            species.setThumbnail(R.mipmap.wildlifetrade_control);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Human Wildlife Conflict Management");
            species.setThumbnail(R.mipmap.conflict);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Climate Change");
            species.setThumbnail(R.mipmap.climate);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Community Support");
            species.setThumbnail(R.mipmap.community_support);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Conservation Education");
            species.setThumbnail(R.mipmap.conservation_education);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Wildlife Monitoring Techniques");
            species.setThumbnail(R.mipmap.monitoring_techniquies);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Saved Forms");
            species.setThumbnail(R.mipmap.saved_forms);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("General Form");
            species.setThumbnail(R.mipmap.general_form);
            mItems.add(species);
        }else {
            mItems = new ArrayList<Grid_Model>();
            Grid_Model species = new Grid_Model();

            species = new Grid_Model();
            species.setName("Forest");
            species.setThumbnail(R.mipmap.forest);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Wildlife Management and Monitoring");
            species.setThumbnail(R.mipmap.wmm);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Wildlife Trade Control");
            species.setThumbnail(R.mipmap.wildlifetrade_control);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Human Wildlife Conflict Management");
            species.setThumbnail(R.mipmap.conflict);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("Saved Forms");
            species.setThumbnail(R.mipmap.saved_forms);
            mItems.add(species);

            species = new Grid_Model();
            species.setName("General Form");
            species.setThumbnail(R.mipmap.general_form);
            mItems.add(species);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_grid, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Grid_Model nature = mItems.get(i);
        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
        viewHolder.cardViewGridView.setCardBackgroundColor(Color.parseColor(colorcode[i]));
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView tvspecies;
        public CardView cardViewGridView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView)itemView.findViewById(R.id.tv_species);
            cardViewGridView = (CardView) itemView.findViewById(R.id.cardview_grd);
        }
    }
}
