package com.example.clean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public MyDBHelper(Context context, String dbName){
        //나의 데이터베이스를 생성한것이다. 기존에 내용이 있으면
        // 다시 만들지 않는다.
        super(context, dbName, null, 1);
        this.context = context;
    }

    //테이블을 생성한다.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table myTBL(nickname char(20) primary key, picture blob, name char(20), gender" +
                " char(20), age char(20));");
        sqLiteDatabase.execSQL("create table toDoListTBL(image BLOB, spaceName char, toDoName char, date char, time char," +
                " mon char, tus char, wen char, tur char, fri char, sat char, sun char, alarm integer, clear integer);");
        sqLiteDatabase.execSQL("create table todayListTBL(pk_fullName char primary key, t_today char, t_spaceName char, t_toDoName char, t_date char, t_week char, t_time char, " +
                " t_alarm integer, t_clear integer, t_clearImage BLOB);");
    }

    //테이블을 삭제한다.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists myTBL");
        sqLiteDatabase.execSQL("drop table if exists toDoListTBL;");
        sqLiteDatabase.execSQL("drop table if exists toDayListTBL;");

        onCreate(sqLiteDatabase);
    }

}