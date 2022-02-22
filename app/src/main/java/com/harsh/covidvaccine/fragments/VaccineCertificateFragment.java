package com.harsh.covidvaccine.fragments;




import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.harsh.covidvaccine.MainActivity;
import com.harsh.covidvaccine.R;
import com.harsh.covidvaccine.modals.BaseUrls;
import com.harsh.covidvaccine.rest.ApiInterface;
import com.harsh.covidvaccine.rest.RestAdapter;
import com.rajat.pdfviewer.PdfViewerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineCertificateFragment extends Fragment {

    private EditText mobileNumber, mobileOtp, benfId;
    private Button sentOtpBtn, verifyOtpBtn, getCertBtn;
    private CardView mobCard,otpCard, bidCard;
    private Call<JsonObject> apiCallback = null;
    private String txn, token,OTP;
    private ProgressDialog mProgressDialog;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public VaccineCertificateFragment() {
        // Required empty public constructor
    }


    public static VaccineCertificateFragment newInstance(String param1, String param2) {
        VaccineCertificateFragment fragment = new VaccineCertificateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vaccine_certificate, container, false);
        init(view);
        sentOtpBtn.setOnClickListener(view1 -> {
            if(mobileNumber.getText().toString().trim().length()!=10)
                Toast.makeText(getContext(), "Please Enter 10 digit valid Mobile Number ", Toast.LENGTH_LONG).show();
           else{
               if(isNetworkConnected())
                 sentOtp(mobileNumber.getText().toString().trim());
               else
                   Toast.makeText(getContext(), "Check Internet Connection !!!", Toast.LENGTH_SHORT).show();
           }
        });
        verifyOtpBtn.setOnClickListener(view12 -> {
            OTP=mobileOtp.getText().toString().trim();
            verifyOtp(mobileOtp.getText().toString().trim());

        });
        getCertBtn.setOnClickListener(view13 -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(benfId.getWindowToken(), 0);
            Log.e("TEST1","111");
           requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    211);
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("TEST2","222");
        if(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            Log.e("TEST3","333");
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(" Downloading...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
            Log.e("TEST4","444");
            downloadCft();
        }
        else
            Toast.makeText(getContext(), "Please Grant the Permission", Toast.LENGTH_SHORT).show();

    }

    private void downloadCft() {
        getCertBtn.setEnabled(false);
        String bid="";
        try {
             bid=benfId(token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressDialog.show();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer "+token);
        headers.put("Content-Type", "application/pdf");
        Log.d("TAGGMAP",headers.toString());
        ApiInterface apiInterface = RestAdapter.createAPI(BaseUrls.CENTERS_API_URL);
        Call<ResponseBody> call=apiInterface.downloadPdf(bid,headers);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("TAGG34", response.message());
                if(response.isSuccessful()){
                    getCertBtn.setEnabled(false);
                    DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask();
                    downloadFileAsyncTask.execute(response.body().byteStream());

                }
                if (!response.isSuccessful())
                {
                    getCertBtn.setEnabled(true);
                    mProgressDialog.hide();
                    Toast.makeText(getContext(),"Please try later" , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getCertBtn.setEnabled(false);
                Toast.makeText(getContext(),"Please try later" , Toast.LENGTH_SHORT).show();
                Log.d("TAGGERROR","",t);
            }
        });

    }

    private void verifyOtp(String mobileOtp) {
        verifyOtpBtn.setEnabled(false);
        String hashOtp = getSha256Hash(mobileOtp);

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("otp", hashOtp);
        jsonRequest.addProperty("txnId", txn);
        Log.e("TAGG11===>>", jsonRequest.toString());

        ApiInterface apiInterface = RestAdapter.createAPI(BaseUrls.CENTERS_API_URL);
        apiCallback = apiInterface.verifyOtp(jsonRequest);
        apiCallback.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){
                    verifyOtpBtn.setEnabled(false);
                Toast.makeText(getContext(), "OTP Verified", Toast.LENGTH_SHORT).show();
                token = response.body().get("token").getAsString();
                otpCard.setVisibility(View.GONE);
                bidCard.setVisibility(View.VISIBLE);
                }
                Log.e("TAGG123",token);

                if (!response.isSuccessful())
                {
                    verifyOtpBtn.setEnabled(true);
                    Toast.makeText(getContext(),"Please try later" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                verifyOtpBtn.setEnabled(true);
                Log.e("TAGG13", "", t);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sentOtp(String mobileNumber) {
        sentOtpBtn.setEnabled(false);
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("mobile", mobileNumber);
        Log.e("TAGG14===>>", jsonRequest.toString());
        ApiInterface apiInterface = RestAdapter.createAPI(BaseUrls.CENTERS_API_URL);
        apiCallback = apiInterface.sentOtp(jsonRequest);
        apiCallback.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful())
                {
                    sentOtpBtn.setEnabled(true);
                    Toast.makeText(getContext(),"Please try later" , Toast.LENGTH_SHORT).show();
                }
                if(response.isSuccessful()){
                    sentOtpBtn.setEnabled(false);
                Toast.makeText(getContext(), "OTP SENT valid for 3 Minute", Toast.LENGTH_SHORT).show();
                mobCard.setVisibility(View.GONE);
                otpCard.setVisibility(View.VISIBLE);
                mobileOtp.setText("");

                txn=response.body().get("txnId").getAsString();}

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                sentOtpBtn.setEnabled(true);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                Log.e("TAGG16", "", t);
            }
        });

    }

    private void init(View view) {
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        mobileOtp = (EditText) view.findViewById(R.id.mobileOtp);
        benfId = (EditText) view.findViewById(R.id.benfId);
        sentOtpBtn = (Button) view.findViewById(R.id.sentOtpBtn);
        verifyOtpBtn = (Button) view.findViewById(R.id.verifyOtpBtn);
        getCertBtn = (Button) view.findViewById(R.id.getCertBtn);
        otpCard = (CardView) view.findViewById(R.id.otpCard);
        bidCard = (CardView) view.findViewById(R.id.bidCard);
        mobCard=(CardView) view.findViewById(R.id.mobCard);
    }
    boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public static String getSha256Hash(String password) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(password.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }
    private String benfId(String token) throws JSONException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = null;
        JSONObject payload =null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decoder = Base64.getUrlDecoder();
            String header = new String(decoder.decode(chunks[0]));
            String payld = new String(decoder.decode(chunks[1]));
            payload=new JSONObject(payld);

        }
        return payload.getString("beneficiary_reference_id");

    }
    class DownloadFileAsyncTask extends AsyncTask<InputStream, Void, Boolean> {
        private static final String TAG = "TAGG123";
        final String filename = "Vaccine certificate"+OTP+".pdf";

        @Override
        protected Boolean doInBackground(InputStream... params) {
            InputStream inputStream = params[0];
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            OutputStream output = null;
            try {
                output = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    else{
                        Log.d(TAG, "Output stream is null");
                    }
                } catch (IOException e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mProgressDialog.hide();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getPath() + File.separator +
                    filename);

            if(result) {
                Toast.makeText(getContext(), "Downloaded Successfully in DOWNLOAD folder", Toast.LENGTH_SHORT)
                        .show();
                getContext().startActivity(

                        // Opening pdf from assets folder

                        PdfViewerActivity.Companion.launchPdfFromPath(
                                getContext(),
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        .getPath() + File.separator +
                                        filename,
                                "Vaccine Certificate "+OTP,
                                Environment
                                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        .getPath()+"/VCD",
                                false,
                                false
                        )
                );
                bidCard.setVisibility(View.GONE);
                mobCard.setVisibility(View.VISIBLE);
                mobileNumber.setText("");
                sentOtpBtn.setEnabled(true);
                verifyOtpBtn.setEnabled(true);
                getCertBtn.setEnabled(true);

            }
            else
                Toast.makeText(getContext(), " Failed to Download ", Toast.LENGTH_LONG).show();
        }
    }
}
