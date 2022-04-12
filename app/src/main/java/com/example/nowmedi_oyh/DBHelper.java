package com.example.nowmedi_oyh;

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

        String sql_MEDI_HISTORY = "CREATE TABLE if not exists MEDI_HISTORY ("
                + "_id integer primary key autoincrement,"
                + "HISTORY_MEDI_NAME text,"
                + "HISTORY_DATE text,"
                + "HISTORY_TIME text,"
                + "HISTORY_ROUTINE text);";

        db.execSQL(sql_MEDI_HISTORY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_MEDICINE = "DROP TABLE if exists MEDICINE";
        db.execSQL(sql_MEDICINE);

        String sql_MEDI_ALARM = "DROP TABLE if exists MEDI_ALARM";
        db.execSQL(sql_MEDI_ALARM);

        String sql_MEDI_HISTORY = "DROP TABLE if exists MEDI_HISTORY";
        db.execSQL(sql_MEDI_HISTORY);

        onCreate(db);
    }
}
