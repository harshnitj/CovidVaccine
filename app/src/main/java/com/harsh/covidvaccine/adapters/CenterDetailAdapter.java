package com.harsh.covidvaccine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.covidvaccine.R;
import com.harsh.covidvaccine.modals.Session;

import java.util.ArrayList;

public class CenterDetailAdapter extends RecyclerView.Adapter<CenterDetailAdapter.CenterDetailViewHolder> {

    ArrayList<Session> list;
    Context context;

    public CenterDetailAdapter(ArrayList<Session> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CenterDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.center_detail_row,parent,false);
        return new CenterDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterDetailViewHolder holder, int position) {
        Session session=list.get(position);
        holder.mDate.setText(session.date+"");
        holder.mCapacity.setText(session.available_capacity+"");
        holder.mVaccine.setText(session.vaccine);
        holder.minAge.setText(session.min_age_limit+"");
        if(session.allow_all_age)
            holder.maxAge.setText("Allowed for all age");
        else
            holder.maxAge.setText(session.max_age_limit+"");
        holder.dose1.setText(session.available_capacity_dose1+"");
        holder.dose2.setText(session.available_capacity_dose2+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CenterDetailViewHolder extends RecyclerView.ViewHolder{
        TextView mDate,mCapacity,mVaccine,minAge,maxAge,dose1,dose2;
        public CenterDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate=(TextView) itemView.findViewById(R.id.mDate);
            mCapacity=(TextView) itemView.findViewById(R.id.mCapacity);
            mVaccine=(TextView) itemView.findViewById(R.id.mVaccine);
            minAge=(TextView) itemView.findViewById(R.id.minAge);
            maxAge=(TextView) itemView.findViewById(R.id.maxAge);
            dose1=(TextView) itemView.findViewById(R.id.dose1);
            dose2=(TextView) itemView.findViewById(R.id.dose2);
        }
    }
}
