package com.harsh.covidvaccine.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.harsh.covidvaccine.R;
import com.harsh.covidvaccine.activities.VaccineCentersActivity;

import java.util.Calendar;


public class VaccineCenterFragment extends Fragment {
   private EditText pinCode;
   private TextView date;
   private Button btnSearch;
   private String PinCode;
   private String Date;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public VaccineCenterFragment() {
        // Required empty public constructor
    }


    public static VaccineCenterFragment newInstance(String param1, String param2) {
        VaccineCenterFragment fragment = new VaccineCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_vaccine_center, container, false);
        init(view);

        date.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pinCode.getWindowToken(), 0);
            getDate();
        });
        btnSearch.setOnClickListener(v -> {
            btnSearch.setEnabled(false);
            PinCode=pinCode.getText().toString();
            if(PinCode.length()!=6)
                Toast.makeText(getContext(), "Please Enter 6 digit valid PIN Code ", Toast.LENGTH_LONG).show();

            else if(Date==null)
            {
                Toast.makeText(getContext(), "Please Enter Date for Slots", Toast.LENGTH_LONG).show();
            }
           else{
            Intent intent=new Intent(getContext(),VaccineCentersActivity.class);
            intent.putExtra("PINCODE",PinCode);
            intent.putExtra("DATE",Date);
            getContext().startActivity(intent);
            Log.d("TAGG", PinCode+Date);}
            btnSearch.setEnabled(true);
        });
        return view;
    }
    public void init(View view) {

        pinCode=(EditText) view.findViewById(R.id.pinCode);
        date=(TextView) view.findViewById(R.id.date);
        btnSearch=(Button) view.findViewById(R.id.btnSearch);
    }
    public void getDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(getActivity(),
                datePickerListener,
                year,
                month,
                day).show();
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String dd, mm;
            if (selectedDay < 10) {
                dd = "0" + selectedDay;
            } else
                dd = "" + selectedDay;
            if (selectedMonth < 10)
                mm = "0" + (selectedMonth + 1);
            else
                mm = "" + (selectedMonth + 1);

            Date = dd + "-" + mm + "-" + selectedYear;
            date.setText(Date);

        }
    };
}