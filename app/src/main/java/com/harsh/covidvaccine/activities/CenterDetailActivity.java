package com.harsh.covidvaccine.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.harsh.covidvaccine.R;
import com.harsh.covidvaccine.adapters.CenterDetailAdapter;
import com.harsh.covidvaccine.modals.Session;

import java.util.ArrayList;

public class CenterDetailActivity extends AppCompatActivity {
    private RecyclerView centerDetailRV;
    private Toolbar toolbar;
    private ProgressBar centerDPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_detail);
        ArrayList<Session> sessions= (ArrayList<Session>) getIntent().getSerializableExtra("CENTER_DETAIL");
        Log.d("TAGG",sessions.size()+"");
        String centerName=getIntent().getStringExtra("CENTERNAME");
        toolbar=(Toolbar)findViewById(R.id.centerDTB);
        toolbar.setTitle(centerName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.getNavigationIcon().mutate().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        centerDetailRV=(RecyclerView) findViewById(R.id.centerDetailRV);
        CenterDetailAdapter adapter=new CenterDetailAdapter(sessions,getApplicationContext());
        centerDetailRV.setAdapter(adapter);
        centerDetailRV.setLayoutManager(new LinearLayoutManager(this));
    }
}