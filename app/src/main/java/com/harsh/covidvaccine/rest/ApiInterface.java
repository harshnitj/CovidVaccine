package com.harsh.covidvaccine.rest;

import com.google.gson.JsonObject;
import com.harsh.covidvaccine.modals.VaccineCenterCallBack;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiInterface {

    @GET("api/v2/appointment/sessions/public/calendarByPin")
    Call<VaccineCenterCallBack> searchVaccineCenters (@Query("pincode") String pinCode, @Query("date") String date);

    @POST("api/v2/auth/public/generateOTP")
    Call<JsonObject> sentOtp(@Body JsonObject requestBody);

    @POST("api/v2/auth/public/confirmOTP")
    Call<JsonObject> verifyOtp(@Body JsonObject requestBody);

    @GET("api/v2/registration/certificate/public/download")
    @Streaming
    Call<ResponseBody> downloadPdf(@Query("beneficiary_reference_id") String bid,@HeaderMap Map<String, String> headers);
}
