package com.naxa.conservationtrackingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.naxa.conservationtrackingapp.R;
import java.util.List;
import com.naxa.conservationtrackingapp.model.SpeciesNaNumModel;

/**
 * Created by Susan on 1/2/2017.
 */
public class RecyclerListAdapterSpeciesNN extends RecyclerView.Adapter<RecyclerListAdapterSpeciesNN.MyHolder> {

    private List<SpeciesNaNumModel> data;

    // create constructor to innitilize context and data sent from MainActivity
    public RecyclerListAdapterSpeciesNN(List<SpeciesNaNumModel> data){
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.species_row, parent, false);

        return new MyHolder(itemView);
    }

    // Bind data
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        SpeciesNaNumModel current = data.get(position);
        holder.textSpeciesName.setText(current.species_10m);
        holder.textSpeciesNumber.setText(current.gbh_10m);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{

        TextView textSpeciesName;
        TextView textSpeciesNumber;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textSpeciesName = (TextView) itemView.findViewById(R.id.tv_species_name);
            textSpeciesNumber = (TextView) itemView.findViewById(R.id.tv_species_number);

        }

    }

}