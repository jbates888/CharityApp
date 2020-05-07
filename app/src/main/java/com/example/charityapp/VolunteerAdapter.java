package com.example.charityapp;

import android.content.Context;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @description Class that sets all data for volunteer recycle adapter
 *
 * @author AJ Thut
 * @date_created 02/14/20
 * @date_modified 05/01/20
 */
public class VolunteerAdapter extends RecyclerView.ViewHolder{
    View view;

    public VolunteerAdapter(@NonNull View itemView) {
        //set parent it item view
        super(itemView);
        view = itemView;
    }

    public void setView(Context context, String name, int hours){
        //set each text view to their id
        TextView nameTxt = view.findViewById(R.id.volname_txt);
        TextView hoursTxt = view.findViewById(R.id.hours_txt);
        //setting text for each text view
        nameTxt.setText(name);
        hoursTxt.setText("Total Hours: " + hours);
    }
}
