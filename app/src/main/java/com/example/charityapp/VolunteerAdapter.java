package com.example.charityapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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

        nameTxt.setText(name);
        hoursTxt.setText("Total Hours: " + hours);
    }
}
