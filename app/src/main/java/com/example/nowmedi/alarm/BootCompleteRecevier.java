package com.example.nowmedi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nowmedi.database.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BootCompleteRecevier extends BroadcastReceiver {
    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            helper = new DBHelper(context, "newdb.db", null, 1);
            db = helper.getWritableDatabase();
            helper.onCreate(db);

            ArrayList<String> medi_name_list;
            medi_name_list = new ArrayList<>();
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM MEDICINE" , null);
            SimpleDateFormat format_ymd = new SimpleDateFormat("yyyy.MM.dd");

            for(int i=0;i<cursor.getCount();i++) {
                cursor.moveToNext();

                String st_today,st_sday,st_eday,mediname;
                int compare1,compare2;
                Date today = new Date();
                Date sday = new Date();
                Date eday = new Date();

                st_today= format_ymd.format(today);
                st_sday=cursor.getString(4);
                st_eday=cursor.getString(5);
                mediname=cursor.getString(1);

                try {
                    today = format_ymd.parse(st_today);
                    sday = format_ymd.parse(st_sday);
                    eday = format_ymd.parse(st_eday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                compare1 = today.compareTo(sday); //sday1이 eday2보다 크면-(compare=0보다 크면 ok)
                compare2 = today.compareTo(eday); //compare2가 0보다 작으면 ok
                if(compare1>=0 && compare2<=0){
                    medi_name_list.add(mediname);
                }
            }
            cursor.close();

            for(int i =0; i<medi_name_list.size();i++) {
                String mediname=medi_name_list.get(i);
                cursor = database.rawQuery("SELECT * FROM MEDI_ALARM WHERE ALARM_MEDI_NAME ='" + mediname + "'", null);
                for(int j=0;j<cursor.getCount();j++){
                    cursor.moveToNext();
                    int id=cursor.getInt(0);
                    String time=cursor.getString(3);

                    int idx = time.indexOf(":");
                    int hour1= Integer.parseInt(time.substring(0,idx));
                    int minute1 = Integer.parseInt(time.substring(idx+1));

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour1);
                    calendar.set(Calendar.MINUTE, minute1);
                    calendar.set(Calendar.SECOND,0);
                    Intent repeat_intent = new Intent(context, AlarmReceiver.class);
                    repeat_intent.putExtra("id", id);
                    repeat_intent.putExtra("count",0);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                    AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
            cursor.close();
            helper.close();
            db.close();

        }
    }
}
