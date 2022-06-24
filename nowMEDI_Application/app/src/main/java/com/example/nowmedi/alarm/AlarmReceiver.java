package com.example.nowmedi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.Toast;

import com.example.nowmedi.database.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private DBHelper helper;
    private SQLiteDatabase db;
    private int id1;
    private int day_compare,time_compare=0;
    private  Context context1;
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id",0);
        int count = intent.getIntExtra("count",0);
        id1=id;
        context1=context;
        IS_DATE_OVER();
        if(day_compare>0){
          return;
        }
        if(count==0){
            IS_Past_time();
        }
        if(time_compare>0){
            Add_Nextday_alarm();
            return;
        }

        Intent sIntent = new Intent(context, AlarmService.class);
        sIntent.putExtra("id",id);
        sIntent.putExtra("count",count);

        // Oreo(26) 버전 이후부터는 Background 에서 실행을 금지하기 때문에 Foreground 에서 실행해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(sIntent);
        } else {
            context.startService(sIntent);
        }
    }

    public void IS_DATE_OVER(){
        String mediName,st_end_date;

        helper = new DBHelper(context1, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id1+ "'" , null);
        cursor.moveToNext();
        mediName=cursor.getString(0);
        cursor = database.rawQuery("SELECT MEDI_END_DATE FROM MEDICINE WHERE MEDI_NAME  ='"+ mediName+ "'" , null);
        cursor.moveToNext();
        st_end_date = cursor.getString(0);

         Date today = new Date();
         Date enddate = new Date();
         SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
         String st_today = format.format(today);

        try {
            enddate = format.parse(st_end_date);
            today = format.parse(st_today);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        day_compare = today.compareTo(enddate);

    }
    public void IS_Past_time(){

        int idx=0,hour=0,minute=0,today_hour,today_minute;
        String st_time,st_today_time;
        helper = new DBHelper(context1, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT ALARM_TIME FROM MEDI_ALARM WHERE _id ='"+ id1+ "'" , null);
        cursor.moveToNext();
        st_time=cursor.getString(0);

        idx = st_time.indexOf(":");
        hour= Integer.parseInt(st_time.substring(0,idx));
        minute = Integer.parseInt(st_time.substring(idx+1));

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        st_today_time = format.format(today);

        idx = st_today_time.indexOf(":");
        today_hour= Integer.parseInt(st_today_time.substring(0,idx));
        today_minute = Integer.parseInt(st_today_time.substring(idx+1));

        if(today_hour>hour){
            time_compare=1;

        }
        else if(today_minute>minute){
            time_compare=1;

        }

    }

    public void Add_Nextday_alarm(){
        String st_time;
        int idx=0,hour=0,minute=0;
        int count=0;
        helper = new DBHelper(context1, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_TIME FROM MEDI_ALARM WHERE _id ='"+ id1+ "'" , null);
        cursor1.moveToNext();
        st_time=cursor1.getString(0);

        idx = st_time.indexOf(":");
        hour= Integer.parseInt(st_time.substring(0,idx));
        minute = Integer.parseInt(st_time.substring(idx+1));


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        Intent repeat_intent = new Intent(context1, AlarmReceiver.class);
        repeat_intent.putExtra("id", id1);
        repeat_intent.putExtra("count",count);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context1, id1, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context1.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

}
