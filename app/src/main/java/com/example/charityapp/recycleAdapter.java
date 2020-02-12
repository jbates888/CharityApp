package com.example.charityapp;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recycleAdapter extends RecyclerView.ViewHolder{

    View view;

    public recycleAdapter(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setView(Context context, String name, String program, String date, String time){
        TextView nameTxt = view.findViewById(R.id.name_txt);
        TextView programTxt = view.findViewById(R.id.program_txt);
        TextView dateTxt = view.findViewById(R.id.date_txt);
        TextView timeTxt = view.findViewById(R.id.time_txt);

        nameTxt.setText(name);
        programTxt.setText(program);
        dateTxt.setText(date);
        timeTxt.setText(time);

    }
}