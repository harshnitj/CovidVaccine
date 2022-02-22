package com.harsh.covidvaccine.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.harsh.covidvaccine.R;
import com.harsh.covidvaccine.adapters.VaccineCentersAdapter;
import com.harsh.covidvaccine.modals.BaseUrls;
import com.harsh.covidvaccine.modals.VaccineCenterCallBack;
import com.harsh.covidvaccine.rest.ApiInterface;
import com.harsh.covidvaccine.rest.RestAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineCentersActivity extends AppCompatActivity {
    private Toolbar toolbar;
  private String pinCode;
  private String date;
  private RecyclerView centersRV;
  private ProgressBar centerPB;
  private Call<VaccineCenterCallBack> apiCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_centers);
        pinCode=getIntent().getStringExtra("PINCODE");
        date=getIntent().getStringExtra("DATE");
        toolbar=(Toolbar)findViewById(R.id.centerTB);
        toolbar.setTitle("PINCODE  "+pinCode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.getNavigationIcon().mutate().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        centerPB=(ProgressBar) findViewById(R.id.centerPB);
        centersRV=(RecyclerView) findViewById(R.id.centersRV);
        if(isNetworkConnected())
          apiCall(pinCode,date);
        else
            Toast.makeText(getApplicationContext(), "Check Internet Connection !!!", Toast.LENGTH_SHORT).show();
    }

    private void apiCall(String pinCode, String date) {
        centerPB.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RestAdapter.createAPI(BaseUrls.CENTERS_API_URL);
        apiCallback=apiInterface.searchVaccineCenters(pinCode, date);
        apiCallback.enqueue(new Callback<VaccineCenterCallBack>() {
            @Override
            public void onResponse(Call<VaccineCenterCallBack> call, Response<VaccineCenterCallBack> response) {
                centerPB.setVisibility(View.GONE);
                Gson gson = new Gson();
                String res = gson.toJson(response.body(), VaccineCenterCallBack.class);
                Log.e("TAGG===>>",res);
                VaccineCentersAdapter adapter=new VaccineCentersAdapter(getApplicationContext(), response.body());
                centersRV.setAdapter(adapter);
                centersRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<VaccineCenterCallBack> call, Throwable t) {
                Log.e("TAGG","",t);
                centerPB.setVisibility(View.GONE);
            }
        });
    }
     boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}