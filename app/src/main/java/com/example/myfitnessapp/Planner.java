package com.example.myfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Planner extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    EditText editText;
    Button saveButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DAILY_PUSHUPS = "dailyPushups";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Planner");
        navigationView = findViewById(R.id.nav_view4);
        drawerLayout = findViewById(R.id.drawer_layout4);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setCheckedItem(R.id.nav_planner);


        saveButton = findViewById(R.id.dailyButton);
        editText = findViewById(R.id.editTextPlanner);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });


    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(DAILY_PUSHUPS,Integer.parseInt(editText.getText().toString()));

        editor.apply();

        Toast.makeText(this, "Number of daily pushups has been changed to " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final Intent intent;
        Handler handler = new Handler();
        switch(menuItem.getItemId()){
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
