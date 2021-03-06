package com.naxa.conservationtrackingapp.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naxa.conservationtrackingapp.R;

import java.util.List;

import com.naxa.conservationtrackingapp.model.Single_String_Title;

/**
 * Created by user1 on 12/22/2015.
 */
public class RecyclerListAdapter1 extends RecyclerView.Adapter<RecyclerListAdapter1.ContactViewHolder> {

    private List<Single_String_Title> colorList;

    public RecyclerListAdapter1(List<Single_String_Title> cList) {
        this.colorList = cList;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Single_String_Title ci = colorList.get(i);
        contactViewHolder.vName.setText(ci.title);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_single_item_wmm, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected CardView cardView;

        public ContactViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.textViewSms);
//            cardView = (CardView) v.findViewById(R.id.cardview);
        }
    }

}
