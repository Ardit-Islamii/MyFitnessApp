package com.example.myfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
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


public class Data extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Button todayButton;
    Button showAllButton;
    Button infoButton;
    DatabaseHelper myDB;
    final static String EXTRA_DATA = "com.example.myfitnessapp.EXTRA_DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        myDB = new DatabaseHelper(this);
        toolbarMethod();
        todayButton = findViewById(R.id.todayButton);
        showAllButton = findViewById(R.id.showAllButton);
        infoButton = findViewById(R.id.infoButton);
        showTodaysResults();
        showOnClick();
        infoButtonMethod();
    }
    //Info button method - displays interesting things like the amount of exercises done overall
    // - the highest amount of push ups done in one session etc.
    public void infoButtonMethod(){
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int allExercises = 0;
                int todaysExercises = 0;
                int highestNumberOfPushups = 0;
                double averageExercisesDone = 0;
                double averageExercisesForToday = 0;
                int countAll = 0;
                int countToday = 0;
                String Date = "";
                Cursor res = myDB.getData();
                String todaysDate = (new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
                while(res.moveToNext()) {
                    allExercises += res.getInt(2);
                    countAll++;
                    if (res.getInt(2) >= highestNumberOfPushups) {
                        long milis = res.getLong(1);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy hh:mm a");
                        Date date = new Date(milis);
                        Date = sdf.format(date);
                        highestNumberOfPushups = res.getInt(2);
                    }
                    long databaseMiliseconds = res.getLong(1);
                    String databaseDate = (new SimpleDateFormat("dd/MM/yyyy").format(new Date(databaseMiliseconds)));
                    if(databaseDate.contains(todaysDate)){
                        todaysExercises += res.getInt(2);
                        countToday++;
                    }
                }
                try {
                    averageExercisesDone = allExercises / countAll;
                    if(countToday == 0){
                        averageExercisesForToday = 0;
                    }else {
                        averageExercisesForToday = todaysExercises / countToday;
                    }

                }catch(NumberFormatException e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(Data.this,DatabaseActivity.class);
                String data = "The total amount of pushups done since the creation of this app : " + allExercises +
                        "\n\nThe highest amount of pushups done through one session : " + highestNumberOfPushups + "\nOn " + Date +
                        "\n\nThe average number of your exercises so far is : " + averageExercisesDone +
                        "\n\nThe average number of your exercises today is : " + averageExercisesForToday;
                intent.putExtra(EXTRA_DATA,data);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }



    //Basically sets up the toolbar at the start of the settings activity
    public void toolbarMethod(){
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data");
        navigationView = findViewById(R.id.nav_view3);
        drawerLayout = findViewById(R.id.drawer_layout3);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setCheckedItem(R.id.nav_data);
    }
    //Show button - Opens up another activity that displays the entire database of the exercises done from start to finish.
    public void showOnClick() {
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDB.getData();
                StringBuffer buffer = new StringBuffer();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
                while (res.moveToNext()) {
                    long milis = res.getLong(1);
                    Date resultDate = new Date(milis);
                    String date = sdf.format(resultDate);
                    buffer.append("Date : " + date + "\n");
                    buffer.append("Exercises done : " + res.getInt(2) + "\n\n");
                }
                String allData = buffer.toString();
                Intent intent = new Intent(Data.this,DatabaseActivity.class);
                intent.putExtra(EXTRA_DATA, allData);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    //Today button - Displays todays exercises and the time they were done at with an AlertDialog
    public void showTodaysResults() {
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDB.getData();
                boolean dataInserted = false;
                long milis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
                Date date = new Date(milis);
                String currentDate = sdf.format(date);
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    long databaseMilis = res.getLong(1);
                    Date databaseDate = new Date(databaseMilis);
                    String databaseCurrentDate = sdf.format(databaseDate);
                    if (databaseCurrentDate.contains(currentDate)) {
                        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss a");
                        String hoursAndMinutes = sdf2.format(new Date(databaseMilis));
                        buffer.append("At : " + hoursAndMinutes + " were done " + res.getInt(2) + " pushups\n\n");
                    }
                }
                if (buffer == null) {
                    showMessage("Error", "No exercises were done for today");
                    return;
                }
                String message = buffer.toString();
                showMessage("Today", message);
            }
        });
    }
    //Part of the Today button process
    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
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
            case R.id.nav_settings:
                intent = new Intent(this,Settings.class);
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
