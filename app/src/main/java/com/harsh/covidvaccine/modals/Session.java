package com.harsh.covidvaccine.modals;

import java.io.Serializable;
import java.util.ArrayList;

public class Session implements Serializable {
    public String session_id;
    public String date;
    public int available_capacity;
    public int min_age_limit;
    public boolean allow_all_age;
    public String vaccine;
    public ArrayList<String> slots;
    public int available_capacity_dose1;
    public int available_capacity_dose2;
    public int max_age_limit;
}
