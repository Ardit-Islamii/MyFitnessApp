package com.example.myfitnessapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class database2 extends SQLiteOpenHelper {
    public database2(Context context) {
        super(context, constant2.DATABASE_NAME, null, constant2.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + constant2.TABLE_NAME + " (" +
                constant2.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                constant2.DATE + " STRING, " +
                constant2.SYSTIME + " INTEGER, " +
                constant2.NUMOFEXERCISES + " INTEGER, " +
                constant2.yAxis + " STRING)";
                sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + constant2.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void saveData(String date, long systime, int numofexerises, String yaxis){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(constant2.DATE,date);
        values.put(constant2.SYSTIME, systime);
        values.put(constant2.NUMOFEXERCISES, numofexerises);
        values.put(constant2.yAxis,yaxis);

        db.insert(constant2.TABLE_NAME,null,values);
    }
    public ArrayList<String> queryXData(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> xData = new ArrayList<>();
        String query = "SELECT " + constant2.DATE + " FROM " + constant2.TABLE_NAME + " GROUP BY " + constant2.DATE;

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
        String query = "SELECT SUM(" + constant2.yAxis + ")" + " FROM " + constant2.TABLE_NAME + " WHERE " + constant2.yAxis + " IS NOT NULL " + " GROUP BY " + constant2.DATE
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
        Cursor res = db.rawQuery("select * from " + constant2.TABLE_NAME,null);
        return res;

    }
}
