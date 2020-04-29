package com.example.charityapp;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class DonorAdapter extends RecyclerView.ViewHolder{
    View view;

    public DonorAdapter(@NonNull View itemView) {
        //set parent it item view
        super(itemView);
        view = itemView;
    }

    public void setView(Context context, String name, int hours, int amount){
        //set each text view to their id
        TextView nameTxt = view.findViewById(R.id.donorname_txt);
        TextView hoursTxt = view.findViewById(R.id.donorhours_txt);
        TextView donatetxt = view.findViewById(R.id.donations_txt);

        nameTxt.setText(name);
        hoursTxt.setText("Total Hours: " + hours);
        donatetxt.setText("Total Donations: $" + amount);
    }

}
