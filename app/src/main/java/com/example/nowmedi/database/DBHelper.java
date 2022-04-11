package com.example.nowmedi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_MEDICINE = "CREATE TABLE if not exists MEDICINE ("
                + "_id integer primary key autoincrement,"
                + "MEDI_NAME text,"
                + "MEDI_PRODUCT text,"
                + "MEDI_MEMO text,"
                + "MEDI_START_DATE text,"
                + "MEDI_END_DATE text);";

        db.execSQL(sql_MEDICINE);

        String sql_MEDI_ALARM = "CREATE TABLE if not exists MEDI_ALARM ("
                + "_id integer primary key autoincrement,"
                + "ALARM_MEDI_NAME text,"
                + "ALARM_ROUTINE text,"
                + "ALARM_TIME text);";

        db.execSQL(sql_MEDI_ALARM);

        String sql_PROTECTOR = "CREATE TABLE if not exists PROTECTOR ("
                + "_id integer primary key autoincrement,"
                + "PROTECTOR_NAME text,"
                + "PROTECTOR_PHONE_NUM text,"
                + "PROTECTOR_MESSAGE text);";

        db.execSQL(sql_PROTECTOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_MEDICINE = "DROP TABLE if exists MEDICINE";
        db.execSQL(sql_MEDICINE);

        String sql_MEDI_ALARM = "DROP TABLE if exists MEDI_ALARM";
        db.execSQL(sql_MEDI_ALARM);

        String sql_PROTECTOR = "DROP TABLE if exists PROTECTOR";
        db.execSQL(sql_PROTECTOR);

        onCreate(db);
    }
}