package com.example.nowmedi.mainpage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AddTime;
import com.example.nowmedi.alarm.AddTimeAdapter;
import com.example.nowmedi.alarm.AddTimePop;
import com.example.nowmedi.alarm.AlarmReceiver;
import com.example.nowmedi.database.DBHelper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MediDetailModify extends AppCompatActivity {

    int Fullhour,hour,minute;
    String daytime,ampm;
    Date date1,date2,today;
    Calendar calendar1;

    private ArrayList<Integer> IDList;
    private ArrayList<Date> Date1List;
    private ArrayList<Date> Date2List;
    private ArrayList<Calendar> calendarArrayList;
    private ArrayList<AddTime> arraylist;


    private AddTimeAdapter addedTimeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private DBHelper helper;
    private SQLiteDatabase db;

    private EditText et_mediname_detail;
    private EditText et_caution;

    private TextView mediName;
    private String clickMediName;
    private TextView tv_seted_date;

    private ArrayList<String> dbAlarmTimeList;
    private Long mLastClickTime = 0L;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medi_detail_modify);

        //DB ??????
        helper = new DBHelper(MediDetailModify.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        arraylist = new ArrayList<>();
        Date1List = new ArrayList<>();
        Date2List = new ArrayList<>();
        IDList = new ArrayList<>();
        calendarArrayList = new ArrayList<>();

        addedTimeAdapter = new AddTimeAdapter(arraylist);

        this.calendar1 = Calendar.getInstance();

        //?????????????????? ??????
        recyclerView = (RecyclerView)findViewById(R.id.rv_addtime);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addedTimeAdapter);

        mediName = (TextView) findViewById(R.id.medi_name_detail);

        Intent intent = getIntent(); // ????????? Intent??? ?????????
        clickMediName = intent.getStringExtra("mediName");

        mediName.setText(clickMediName);

        //????????? ????????? ?????? ????????????
        EditText editText1 = (EditText) findViewById(R.id.et_mediname_detail);
        EditText editText2 = (EditText) findViewById(R.id.et_caution);
        TextView textView1 = (TextView) findViewById(R.id.tv_seted_date);

        Cursor cursor1 = db.rawQuery("SELECT MEDI_PRODUCT, MEDI_MEMO, MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE " +
                "WHERE MEDI_NAME = '" + clickMediName + "' ", null);

        Cursor cursor2 = db.rawQuery("SELECT ALARM_ROUTINE, ALARM_TIME FROM MEDI_ALARM " +
                "WHERE ALARM_MEDI_NAME = '" + clickMediName + "' ", null);

        while (cursor1.moveToNext()) {
            editText1.setText(cursor1.getString(0));
            editText2.setText(cursor1.getString(1));
            textView1.setText(cursor1.getString(2) + "~" + cursor1.getString(3));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Cursor cursor3 = db.rawQuery("SELECT MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE", null);
        cursor3.moveToFirst();
        String sday = cursor3.getString(0);
        String eday = cursor3.getString(1);
        try {
            date1 = simpleDateFormat.parse(sday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            date2  = simpleDateFormat.parse(eday);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        while (cursor2.moveToNext()) {
            String routine = cursor2.getString(0);
            String time = cursor2.getString(1);

            int idx = time.indexOf(":");


            Fullhour = Integer.parseInt(time.substring(0, idx));
            minute = Integer.parseInt(time.substring(idx+1));
            hour = Fullhour;

            if(Fullhour==12){
                ampm="??????";
            }
            else if(Fullhour>12){
                hour%=12;
                ampm="??????";
            }
            else{
                ampm="??????";
            }

            AddTime addTime = new AddTime(R.drawable.minus_icon, String.valueOf(hour)+"???",
                    String.valueOf(minute)+"???",routine,ampm);
            arraylist.add(addTime);

        }

        addedTimeAdapter.notifyDataSetChanged();


        et_mediname_detail = findViewById(R.id.et_mediname_detail);
        et_caution = findViewById(R.id.et_caution);
        tv_seted_date = findViewById(R.id.tv_seted_date);
    }

    public void TimeSetClick(View v){
        Intent intent = new Intent(this, AddTimePop.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //????????? ??????
                Fullhour = Integer.parseInt(data.getStringExtra("??????"));
                minute = Integer.parseInt(data.getStringExtra("???"));
                daytime = data.getStringExtra("??????");
                hour = Fullhour;

                if(Fullhour==12){
                    ampm="??????";
                }
                else if(Fullhour>12){
                    hour%=12;
                    ampm="??????";
                }
                else{
                    ampm="??????";
                }
                AddTime addtime = new AddTime(R.drawable.minus_icon,String.valueOf(hour)+"???",
                        String.valueOf(minute)+"???",daytime,ampm);
                arraylist.add(addtime);
                addedTimeAdapter.notifyDataSetChanged();
            }

        }
    }


    public void date_set(View v){
        TextView tv_date = findViewById(R.id.tv_seted_date);
        Calendar calendar;
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("???????????? ????????? ???????????????").setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();

        materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener
                (new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                        date1  = new Date();
                        date2  = new Date();
                        today = new Date();

                        String td = simpleDateFormat.format(today);

                        try {
                            today = simpleDateFormat.parse(td);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        date1.setTime(selection.first);
                        date2.setTime(selection.second);

                        if(date1.before(today)){
                            Toast.makeText(MediDetailModify.this, "??????????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String datestring1 = simpleDateFormat.format(date1);
                            String datestring2 = simpleDateFormat.format(date2);
                            tv_date.setText(datestring1 + "~" + datestring2);
                        }
                    }
                });
    }



    public void SetAlarm(View v) throws ParseException {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();


        int is_empty=0;
        TextView tv_date = findViewById(R.id.tv_seted_date);

        if (arraylist.isEmpty()){
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tv_date.getText())){
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
        else if(is_settime_duplicated()){

        }
        else if(is_dbtime_duplicated()){

        }
        else{
            is_empty=1;
        }
        if(is_empty==1){
            delete_leagcy_arlarm();
            MedicineDBModify();
            AlarmDBModify();

            add_alarm();
        }

    }

    public void MedicineDBModify(){
        String startdate,enddate;
        String mediname, product,caution, date;

        product = et_mediname_detail.getText().toString();
        caution = et_caution.getText().toString();

        date = tv_seted_date.getText().toString();

        int count = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        int idx = date.indexOf("~");


        startdate = date.substring(0, idx);
        enddate = date.substring(idx+1);

        db.execSQL("UPDATE MEDICINE SET MEDI_PRODUCT = '" + product + "'"+
                ", MEDI_MEMO = '" + caution + "'"+
                ", MEDI_START_DATE = '" + startdate + "'"+
                ", MEDI_END_DATE = '" + enddate + "'"+
                " WHERE MEDI_NAME = '" + clickMediName + "'");
    }

    public void AlarmDBModify() {

        String mediname, ampm, hour, minute, routine, time;
        int idx, int_hour, nullcheck = 0;

        String sql1 = "DELETE FROM MEDI_ALARM " +
                "WHERE ALARM_MEDI_NAME = '" + clickMediName + "';";

        db.execSQL(sql1);

        for (int i = 0; i < arraylist.size(); i++) {
            ampm = arraylist.get(i).getAmpm();
            hour = arraylist.get(i).getTv_hour();
            minute = arraylist.get(i).getTv_minute();
            routine = arraylist.get(i).getTv_daytime();

            // ?????? , ??? ?????? ????????? ????????????
            idx = hour.indexOf("???");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "??????" && int_hour != 12) {
                int_hour += 12;
                hour = String.valueOf(int_hour);
            } else if (hour.length() < 2) {
                hour = "0" + hour;
            }

            idx = minute.indexOf("???");
            minute = minute.substring(0, idx);
            if (minute.length() < 2) {
                minute = "0" + minute;
            }

            //hh:mm???????????? ?????????
            time = hour + ":" + minute;

            db.execSQL("INSERT INTO MEDI_ALARM" +
                    "(ALARM_MEDI_NAME, ALARM_ROUTINE, ALARM_TIME) " +
                    "VALUES ('" + clickMediName + "', '" + routine + "', '" + time + "');");
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(MediDetailModify.this, MediDetail.class);
        intent.putExtra("mediName", clickMediName);
        startActivity(intent);
        MediDetailModify.this.finish();
        overridePendingTransition(0, 0);

    }

    public void CancleClick(View view) {
        Intent intent = new Intent(MediDetailModify.this, MediDetail.class);
        intent.putExtra("mediName", clickMediName);
        startActivity(intent);
        MediDetailModify.this.finish();
        overridePendingTransition(0, 0);

    }

    public void add_alarm() throws ParseException {

        // Receiver ??????
        Intent intent = new Intent(this, AlarmReceiver.class);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");

        Intent intent2 = new Intent(MediDetailModify.this, MediDetail.class);
        intent2.putExtra("mediName", clickMediName);
        startActivity(intent2);
        MediDetailModify.this.finish();
        Toast.makeText(MediDetailModify.this, "??? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
        overridePendingTransition(0, 0);

        //Dbhelper??? ???????????? ????????? ????????? SQLiteDatabase??? ?????? ????????????
        helper = new DBHelper(MediDetailModify.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor?????? ????????? ????????? ????????????
        Cursor cursor1 = database.rawQuery("SELECT MEDI_NAME FROM MEDICINE WHERE MEDI_NAME='"+clickMediName+"'", null);

        while (cursor1.moveToNext()) {
            String selectName = cursor1.getString(0);

            dbAlarmTimeList = new ArrayList<String>();

            Cursor cursor2 = database.rawQuery("SELECT _id, ALARM_TIME FROM MEDI_ALARM " +
                    "WHERE ALARM_MEDI_NAME = '" + selectName + "' ", null);

            //????????? ???????????? ???????????? adapter??? ?????? list????????? add
            while (cursor2.moveToNext()) {
                IDList.add(cursor2.getInt(0));
                dbAlarmTimeList.add(cursor2.getString(1));

            }

            for(int i =0; i<dbAlarmTimeList.size();i++) {

                int idx = dbAlarmTimeList.get(i).indexOf(":");

                intent.putExtra("id",IDList.get(i));

                this.calendar1.setTime(date1);
                this.calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dbAlarmTimeList.get(i).substring(0, idx)));
                this.calendar1.set(Calendar.MINUTE, Integer.parseInt(dbAlarmTimeList.get(i).substring(idx+1)));
                this.calendar1.set(Calendar.SECOND, 0);

                calendarArrayList.add(calendar1);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, IDList.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarArrayList.get(i).getTimeInMillis(),pendingIntent);

            }
        }
    }

    public boolean is_settime_duplicated(){
        String ampm,hour,minute,time1,time2;
        int idx,int_hour,int_minute,compare,snoozing_time=16;
        boolean is_duplicated=false;
        ArrayList <Calendar> calendarArrayList;
        calendarArrayList = new ArrayList<>();
        SimpleDateFormat hh_mmformat = new SimpleDateFormat("HH:mm");

        for (int i =0; i<arraylist.size();i++ ) {
            ampm = arraylist.get(i).getAmpm();
            hour = arraylist.get(i).getTv_hour();
            minute = arraylist.get(i).getTv_minute();

            // ??????, ??? ?????? ????????? ????????????
            idx = hour.indexOf("???");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "??????" && int_hour != 12) {
                int_hour += 12;
            }
            idx = minute.indexOf("???");
            minute = minute.substring(0, idx);
            int_minute=Integer.parseInt(minute);

            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.HOUR_OF_DAY,int_hour);
            calendar.set(Calendar.MINUTE,int_minute);
            calendar.set(Calendar.SECOND,0);
            calendarArrayList.add(calendar);
        }

        for (int i =0; i<calendarArrayList.size();i++ ){
            Calendar calendar = calendarArrayList.get(i);

            for (int j =i+1;j<calendarArrayList.size();j++){

                Calendar calendar2 = calendarArrayList.get(j);
                time1 = hh_mmformat.format(calendar.getTime());

                // +15????????? ?????? ??????
                for(int k=0; k<snoozing_time;k++) {
                    compare=calendar.compareTo(calendar2);
                    if (compare == 0) {
                        time2 = hh_mmformat.format(calendar2.getTime());
                        Toast.makeText(this, "??????????????? ?????? ??????" + time1 + "??? " + time2 + "??? ???????????????.", Toast.LENGTH_SHORT).show();
                        is_duplicated = true;
                    }
                    calendar.add(Calendar.MINUTE,1);
                }

                calendar.add(Calendar.MINUTE,-snoozing_time);

                // -15????????? ?????? ??????
                for(int k=0; k<snoozing_time;k++) {
                    compare=calendar.compareTo(calendar2);
                    if (compare == 0) {
                        time2 = hh_mmformat.format(calendar2.getTime());
                        Toast.makeText(this, "??????????????? ?????? ??????" + time1 + "??? " + time2 + "??? ???????????????.", Toast.LENGTH_SHORT).show();
                        is_duplicated = true;
                    }
                    calendar.add(Calendar.MINUTE,-1);
                }
            }

        }
        return is_duplicated;
    }

    public boolean is_dbtime_duplicated(){

        String ampm,hour,minute,time;
        int idx,int_hour,int_minute,snooze_time=16;
        boolean is_duplicated=false;

        ArrayList <String> Alarm_medi_name;
        Alarm_medi_name = new ArrayList<>();

        ArrayList <String> startday_list;
        startday_list = new ArrayList<>();

        ArrayList <String> endday_list;
        endday_list = new ArrayList<>();


        for (int i =0; i<arraylist.size();i++ ) {
            ampm = arraylist.get(i).getAmpm();
            hour = arraylist.get(i).getTv_hour();
            minute = arraylist.get(i).getTv_minute();

            // ??????, ??? ?????? ????????? ????????????
            idx = hour.indexOf("???");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "??????" && int_hour != 12) {
                int_hour += 12;
            }
            idx = minute.indexOf("???");
            minute = minute.substring(0, idx);
            int_minute=Integer.parseInt(minute);

            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.HOUR_OF_DAY,int_hour);
            calendar.set(Calendar.MINUTE,int_minute);
            calendar.set(Calendar.SECOND,0);
            SimpleDateFormat hh_mmformat = new SimpleDateFormat("HH:mm");

            //????????? 15???
            for(int j=0; j<snooze_time;j++){

                time = hh_mmformat.format(calendar.getTime());
                String duplicated_alarm_name=null;
                String mediname;
                helper = new DBHelper(this, "newdb.db", null, 1);
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE ALARM_TIME ='"+ time+
                        "' AND ALARM_MEDI_NAME !='"+ clickMediName+"'" , null);

                if(cursor != null && cursor.moveToFirst()){
                    duplicated_alarm_name = cursor.getString(0);
                    cursor.close();
                }
                if(duplicated_alarm_name != null) {
                    if(!Alarm_medi_name.contains(duplicated_alarm_name)){
                        Alarm_medi_name.add(duplicated_alarm_name);
                    }
                    Toast.makeText(this, "????????? ????????? "+Alarm_medi_name.get(Alarm_medi_name.size()-1)+" ???????????? "+time+"??? ?????? ??? ????????????. ??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    is_duplicated = true;
                }
                calendar.add(Calendar.MINUTE,1);
            }

            //?????? 15???
            calendar.add(Calendar.MINUTE,-snooze_time);
            for(int j=0; j<snooze_time;j++) {
                time = hh_mmformat.format(calendar.getTime());


                String duplicated_alarm_name = null;
                String mediname;
                helper = new DBHelper(this, "newdb.db", null, 1);
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE ALARM_TIME ='"+ time+
                        "' AND ALARM_MEDI_NAME !='"+ clickMediName+"'", null);

                if (cursor != null && cursor.moveToFirst()) {
                    duplicated_alarm_name = cursor.getString(0);
                    cursor.close();
                }
                if (duplicated_alarm_name != null) {
                    if (!Alarm_medi_name.contains(duplicated_alarm_name)) {
                        Alarm_medi_name.add(duplicated_alarm_name);
                    }
                    Toast.makeText(this, "????????? ?????????" + Alarm_medi_name.get(Alarm_medi_name.size() - 1) + "??? ????????????" + time + "??? ?????? ??? ????????????. ??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    is_duplicated = true;
                }
                calendar.add(Calendar.MINUTE, -1);
            }
        }

        int count=0;
        for(int i =0; i<Alarm_medi_name.size();i++){

            String medi_name=Alarm_medi_name.get(i);

            helper = new DBHelper(this, "newdb.db", null, 1);
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT MEDI_START_DATE FROM MEDICINE WHERE MEDI_NAME ='"+ medi_name+ "'" , null);
            cursor.moveToNext();
            startday_list.add(cursor.getString(0));
            cursor.close();

            cursor = database.rawQuery("SELECT MEDI_END_DATE FROM MEDICINE WHERE MEDI_NAME ='"+ medi_name+ "'" , null);
            cursor.moveToNext();
            endday_list.add(cursor.getString(0));
            cursor.close();

            SimpleDateFormat format_ymd = new SimpleDateFormat("yyyy.MM.dd");
            String st_sday1,st_eday1,st_sday2,st_eday2;
            int compare1,compare2;
            st_sday1= format_ymd.format(date1);
            st_eday1= format_ymd.format(date2);
            st_sday2=startday_list.get(i);
            st_eday2=endday_list.get(i);
            Date sday1 = new Date();
            Date eday1 = new Date();
            Date sday2 = new Date();
            Date eday2 = new Date();
            try {
                sday1 = format_ymd.parse(st_sday1);
                eday1 = format_ymd.parse(st_eday1);
                sday2 = format_ymd.parse(st_sday2);
                eday2 = format_ymd.parse(st_eday2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            compare1 = sday1.compareTo(eday2); //sday1??? eday2?????? ??????-(compare=0?????? ?????? ok)
            compare2 = eday1.compareTo(sday2); //compare2??? 0?????? ????????? ok

            if(compare1>0){
                count++;
            }
            else if(compare2<0){
                count++;
            }

            if(count==Alarm_medi_name.size()){
                is_duplicated = false;
            }

        }

        return is_duplicated;
    }

    public void delete_leagcy_arlarm(){
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT _id FROM MEDI_ALARM WHERE ALARM_MEDI_NAME ='"+ clickMediName+ "'" , null);
        for(int idx=0;idx<cursor.getCount();idx++){
            cursor.moveToNext();
            int id=cursor.getInt(0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(getApplicationContext(),
                    AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            alarmManager.cancel(pendingIntent);
        }
    }
}