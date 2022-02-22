package com.harsh.covidvaccine.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.harsh.covidvaccine.fragments.VaccineCenterFragment;
import com.harsh.covidvaccine.fragments.VaccineCertificateFragment;

public class ViewPagerAdapter
        extends FragmentPagerAdapter {

    public ViewPagerAdapter(
            @NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new VaccineCenterFragment();
        else if (position == 1)
            fragment = new VaccineCertificateFragment();


        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "CENTERS";
        else if (position == 1)
            title = "CERTIFICATE";
        return title;
    }
}
