package com.example.myfitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.TimeUnit;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fitness.db";
    public static final String DATABASE_TABLE = "exercises";
    public static final String COL_ID = "ID";
    public static final String COL_DATETIME = "DATE";
    public static final String COL_EXERCISES_DONE = "EXERCISES_DONE";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("create table " + DATABASE_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE INT, EXERCISES_DONE INT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }
    //Method for inserting.
    public boolean insertData(int col_exercises_done){
        long milis = System.currentTimeMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATETIME, milis);
        contentValues.put(COL_EXERCISES_DONE,col_exercises_done);
        long result = db.insert(DATABASE_TABLE,null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    //Method for getting the entire database data.
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE,null);
        return res;

    }
    //Method for wiping out database.
    /*public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,null,null);
        db.close();
    }*/
}
