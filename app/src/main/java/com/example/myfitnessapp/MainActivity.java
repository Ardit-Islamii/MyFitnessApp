package com.example.myfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.data.database2;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    ProgressBar progressBar;
    Button button;
    TextView goalTextView;
    DatabaseHelper myDB;
    TextView displayResultTextView;
    EditText insertNumberEditText;
    int exercisesDone;
    int numberOfPushupsToBeDone;

    NavigationView navigationView;
    database2 db;
    SimpleDateFormat sdf;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadData();
        progressBar.setMax(numberOfPushupsToBeDone);
        myDB = new DatabaseHelper(this);
        db = new database2(this);
        goalTextView = (TextView) findViewById(R.id.goalTextView);
        button = (Button) findViewById(R.id.button);
        displayResultTextView = (TextView) findViewById(R.id.displayResultsTextView);
        insertNumberEditText = (EditText) findViewById(R.id.insertNumberEditText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setCheckedItem(R.id.nav_home);
        incrementProgress();

        displayTodaysResults();

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Planner.SHARED_PREFS,MODE_PRIVATE);
        numberOfPushupsToBeDone = sharedPreferences.getInt(Planner.DAILY_PUSHUPS, 100);
    }

    @Override
    //Method for when the menu button is clicked. Basically opens up the drawer
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    //At the start of the app, this method gets the progress that you've done for today and displays it on the progressBar and edits the resultTextView
    public void displayTodaysResults() {
        int exerDone = getNumOfExercisesDone();
        double maxResult = progressBar.getMax();
        double percentage = exercisesDone * 100 / maxResult;
        goalTextView.setText("Goal achieved : " + percentage + "%");
        progressBar.setProgress(exerDone);
        displayResultTextView.setText("Pushups done : " + exercisesDone + "/" + progressBar.getMax());
    }
    //Like the method says, it gets the number of exercises done and returns an integer.
    public int getNumOfExercisesDone() {
        exercisesDone = 0;
        Cursor res = myDB.getData();
        long milis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        Date date = new Date(milis);
        String currentDate = sdf.format(date);
        while (res.moveToNext()) {
            long databaseMilis = res.getLong(1);
            Date databaseDate = new Date(databaseMilis);
            String dbdate = sdf.format(databaseDate);
            if (dbdate.contains(currentDate)) {
                exercisesDone += res.getInt(2);
            }
        }
        return exercisesDone;
    }

    //Add button - Increments the progress depending on how many exercises you've done.
    public void incrementProgress() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long systime = System.currentTimeMillis();
                sdf = new SimpleDateFormat("dd MMM");
                String todaysDate = sdf.format(new Date(systime));

                String number = insertNumberEditText.getText().toString();
                if(number.isEmpty()){
                    Toast.makeText(MainActivity.this,"Pleas insert a number",Toast.LENGTH_LONG).show();
                    return;
                }
                int numberInserted = Integer.parseInt(number);
                if(numberInserted < 0){
                    Toast.makeText(MainActivity.this,"You can't insert negative numbers",Toast.LENGTH_LONG).show();
                    return;
                }
                if(numberInserted >= 300){
                    Toast.makeText(MainActivity.this,"You couldn't possibly have done that many pushups now.",Toast.LENGTH_LONG).show();
                }
                boolean isInserted = myDB.insertData(numberInserted);
                db.saveData(todaysDate,systime,numberInserted,number);
                displayTodaysResults();
                insertNumberEditText.setText("");
            }
        });
    }
    @Override // TODO - TRY TO UNDERSTAND THE CODE DOWN.
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
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
    //On navigation items selected, you switch activities.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final Intent intent;
        Handler handler = new Handler();
        switch(menuItem.getItemId()){
            case R.id.nav_settings:
                    intent = new Intent(MainActivity.this,Settings.class);
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
}
