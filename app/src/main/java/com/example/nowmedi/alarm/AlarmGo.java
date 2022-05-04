package com.example.nowmedi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;
import com.example.nowmedi.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AlarmGo extends AppCompatActivity{
    private MediaPlayer mediaPlayer;
    int id,count;
    private DBHelper helper;
    private SQLiteDatabase db;
    private TextView tv_alarm_count,tv_alarm_message,tv_alarm_day,tv_alarm_ampm,tv_alarm_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        count = intent.getIntExtra("count",0);


        if(count<2) {
            //Toast.makeText(this, "id="+id+" count="+count, Toast.LENGTH_LONG).show();
            count++;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND,0);
            Intent repeat_intent = new Intent(this, AlarmReceiver.class);
            repeat_intent.putExtra("id", id);
            repeat_intent.putExtra("count",count);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            // 알람음 재생
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
            this.mediaPlayer.start();

        }
        else{
            Add_Nextday_alarm();
            IS_NOT_CLICKED();
            finishAndRemoveTask();
            // 내역 db 추가 해야됨
        }

        setContentView(R.layout.alarm_go);
        create_screen();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer release
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }

    }



    public void alarm_close(View v){
        Add_Nextday_alarm();

        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        IS_CLICKED();
        finishAndRemoveTask();
    }


    public void IS_CLICKED(){
        String mediName, mediRoutine;
        Date today= new Date();
        String date, time;

        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        Cursor cursor2 = database.rawQuery("SELECT ALARM_ROUTINE FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);

        cursor1.moveToNext();
        cursor2.moveToNext();

        mediName=cursor1.getString(0);
        mediRoutine=cursor2.getString(0);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = simpleDateFormat.format(today);

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        time = simpleDateFormat1.format(today);


        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
                +"('"+ mediName + "','" +date +"','"+time+"','"+ mediRoutine+"')"
        );

    }

    public void IS_NOT_CLICKED(){
        String mediName, mediRoutine;
        Date today= new Date();
        String date;

        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        Cursor cursor2 = database.rawQuery("SELECT ALARM_ROUTINE FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);

        cursor1.moveToNext();
        cursor2.moveToNext();

        mediName=cursor1.getString(0);
        mediRoutine=cursor2.getString(0);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = simpleDateFormat.format(today);


        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
                +"('"+ mediName + "','" +date +"',"+"NULL"+",'"+ mediRoutine+"')"
        );
    }

    public void Add_Nextday_alarm(){
        String st_time;
        int idx=0,hour=0,minute=0;
        count=0;
        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_TIME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
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
        Intent repeat_intent = new Intent(this, AlarmReceiver.class);
        repeat_intent.putExtra("id", id);
        repeat_intent.putExtra("count",count);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void create_screen(){
        tv_alarm_count=(TextView) findViewById(R.id.tv_alarm_count);
        tv_alarm_message=(TextView) findViewById(R.id.tv_alarm_message);
        tv_alarm_day=(TextView) findViewById(R.id.tv_alarm_day);
        tv_alarm_ampm=(TextView) findViewById(R.id.tv_alarm_ampm);
        tv_alarm_time=(TextView) findViewById(R.id.tv_alarm_time);

        String count,message,day,ampm,time;
        count="1";message="테스트";day="1월 2일";ampm="오전";time="10:25";

        count=String.valueOf(this.count)+"번째 알람";
        //알람 횟수 출력


        String mediName;



        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);

        cursor1.moveToNext();

        mediName=cursor1.getString(0);
        message = mediName +" 드셔야할 시간입니다.";
        // 알람 문구 출력

        Date today= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        String date,week;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");
        date = simpleDateFormat.format(today);
        week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREAN);

        day= date +" "+ week;


        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm");
        time = simpleDateFormat2.format(today);

        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("a");
        ampm = simpleDateFormat3.format(today);

        tv_alarm_count.setText(count);
        tv_alarm_message.setText(message);
        tv_alarm_day.setText(day);
        tv_alarm_ampm.setText(ampm);
        tv_alarm_time.setText(time);

    }



}

