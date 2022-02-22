package com.harsh.covidvaccine.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.covidvaccine.R;
import com.harsh.covidvaccine.activities.CenterDetailActivity;
import com.harsh.covidvaccine.modals.Center;
import com.harsh.covidvaccine.modals.VaccineCenterCallBack;

import java.util.ArrayList;

public class VaccineCentersAdapter extends RecyclerView.Adapter<VaccineCentersAdapter.CentersViewHolder> {
   private Context context;
   private VaccineCenterCallBack centerObject;
    public Context mctx;

    public VaccineCentersAdapter(Context context, VaccineCenterCallBack centerObject) {
        this.context = context;
        this.centerObject = centerObject;
    }

    @NonNull
    @Override
    public CentersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mctx= parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.centers_row,parent,false);
        return new CentersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentersViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ArrayList<Center> centers=centerObject.centers;
            holder.centerName.setText(centers.get(position).name);
            holder.centerAddress.setText(centers.get(position).address);
            holder.centerTiming.setText(centers.get(position).from+" To "+centers.get(position).to);
            holder.centerFee.setText(centers.get(position).fee_type);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CenterDetailActivity.class);
                    intent.putExtra("CENTER_DETAIL", centers.get(position).sessions);
                    intent.putExtra("CENTERNAME",centers.get(position).name);
                    mctx.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return centerObject.centers.size();
    }

    public class CentersViewHolder extends RecyclerView.ViewHolder{
        TextView centerName,centerAddress,centerTiming,centerFee;
        LinearLayout item;
        public CentersViewHolder(@NonNull View itemView) {
            super(itemView);
            centerName=(TextView) itemView.findViewById(R.id.centerName);
            centerAddress=(TextView) itemView.findViewById(R.id.centerAddress);
            centerTiming=(TextView) itemView.findViewById(R.id.centerTiming);
            centerFee=(TextView) itemView.findViewById(R.id.centerFeeType);
            item=(LinearLayout) itemView.findViewById(R.id.item);
        }
    }
}
