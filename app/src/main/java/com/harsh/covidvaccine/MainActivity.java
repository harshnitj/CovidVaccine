package com.harsh.covidvaccine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.harsh.covidvaccine.adapters.ViewPagerAdapter;
import com.harsh.covidvaccine.fragments.VaccineCenterFragment;
import com.harsh.covidvaccine.fragments.VaccineCertificateFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        tabLayout=(TabLayout) findViewById(R.id.tab_tablayout);
        viewPager=(ViewPager) findViewById(R.id.tab_viewpager);
        setSupportActionBar(toolbar);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hospital);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_certificate);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_about:
                loadAbout();
                return true;
            case R.id.menu_privacy:
                loadPrivacy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadPrivacy() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/vcc-privacypolicy/"));
        startActivity(intent);
    }

    private void loadAbout() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/vcc-privacypolicy/about"));
        startActivity(intent);
    }


}