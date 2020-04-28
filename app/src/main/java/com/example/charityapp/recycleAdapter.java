package com.example.charityapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @description holder for the recycle viewer on the users main screen which displays all events
 *
 * @authors Jack Bates
 * @date_created
 * @date_modified
 */
public class recycleAdapter extends RecyclerView.ViewHolder{
    //class for the holder which shows the event in the recycle viewer on the main screens for each user

    View view;

    public recycleAdapter(@NonNull View itemView) {
        //set parent it item view
        super(itemView);
        view = itemView;
    }

    public void setView(Context context, String name, String program, String date, String time){
        //set each text view to their id
        TextView nameTxt = view.findViewById(R.id.name_txt);
        TextView programTxt = view.findViewById(R.id.program_txt);
        TextView dateTxt = view.findViewById(R.id.date_txt);
        TextView timeTxt = view.findViewById(R.id.time_txt);
        //set each text field tot he value passed in
        nameTxt.setText(name);
        programTxt.setText(program);
        dateTxt.setText(date);
        timeTxt.setText(time);

    }



}