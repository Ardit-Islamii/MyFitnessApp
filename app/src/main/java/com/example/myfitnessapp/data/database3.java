package com.example.myfitnessapp.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class database3 extends SQLiteOpenHelper {
    public database3(Context context) {
        super(context, constant3.DATABASE_NAME, null, constant3.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + constant3.TABLE_NAME + " (" +
                constant3.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                constant3.DATE + " STRING, " +
                constant3.SYSTIME + " INTEGER, " +
                constant3.NUMOFEXERCISES + " INTEGER, " +
                constant3.yAxis + " STRING)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + constant3.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void saveData(String date, long systime, int numofexerises, String yaxis){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(constant3.DATE,date);
        values.put(constant3.SYSTIME, systime);
        values.put(constant3.NUMOFEXERCISES, numofexerises);
        values.put(constant3.yAxis,yaxis);

        db.insert(constant3.TABLE_NAME,null,values);
    }
    public ArrayList<String> queryXData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> xData = new ArrayList<>();
        String query = "SELECT " + constant3.DATE + " FROM " + constant3.TABLE_NAME + " GROUP BY " + constant3.DATE;

        Cursor res = db.rawQuery(query,null);

        for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()){
            xData.add(res.getString(0));
        }
        res.close();
        return xData;
    }
    public ArrayList<String> queryYData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> yData = new ArrayList<>();
        String query = "SELECT SUM(" + constant3.yAxis + ")" + " FROM " + constant3.TABLE_NAME + " WHERE " + constant3.yAxis + " IS NOT NULL " + " GROUP BY " + constant3.DATE
                + " ORDER BY strftime(" + "'%s'" + "," + "'now'" +  ") DESC";

        Cursor res = db.rawQuery(query,null);

        for(res.moveToFirst(); !res.isAfterLast(); res.moveToNext()){
            yData.add(res.getString(0));
        }
        res.close();
        return yData;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + constant3.TABLE_NAME,null);
        return res;

    }
}
