package com.example.myfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myfitnessapp.data.database2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    BarChart barChart;
    database2 db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        navigationView = findViewById(R.id.nav_view2);
        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setCheckedItem(R.id.nav_settings);
        barChart = findViewById(R.id.barchart2);
        addDataToGraph();
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

    }


    public void addDataToGraph(){
        db = new database2(this);
        final ArrayList<BarEntry> yVals = new ArrayList<>();
        final ArrayList<String>yData = db.queryYData();

        for(int i = 0; i < db.queryYData().size();i++){
            BarEntry barEntry = new BarEntry(i,Float.parseFloat(db.queryYData().get(i)));
            yVals.add(barEntry);
        }
        final ArrayList<String> xVals = new ArrayList<>();
        final ArrayList<String> xData = db.queryXData();

        for(int i = 0; i< db.queryXData().size(); i++){
            xVals.add(xData.get(i));
        }
        BarDataSet dataSet = new BarDataSet(yVals,"Example Graph");

        ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
        dataSets1.add(dataSet);
        BarData data = new BarData(dataSets1);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));

        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.isCenterAxisLabelsEnabled();
        xAxis.setGranularityEnabled(true);
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinValue(0f);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        barChart.setVisibleXRangeMaximum(6);
        barChart.setMaxVisibleValueCount(5);
        barChart.setFitBars(true);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final Intent intent;
        Handler handler = new Handler();
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                intent = new Intent(this,MainActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                },500);
                break;
            case R.id.nav_data:
                intent = new Intent(this,Data.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                },500);
                break;
            case R.id.nav_planner:
                intent = new Intent(this,Planner.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                },500);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    //Opens up the drawer on the hamburger button click
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
