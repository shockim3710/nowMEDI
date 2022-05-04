package com.example.nowmedi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.history.DosageHistoryMain;
import com.example.nowmedi.mainpage.DosageList;
import com.example.nowmedi.mainpage.MediDetail;
import com.example.nowmedi.protector.ProtectorAdd;
import com.example.nowmedi.protector.ProtectorManage;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AlarmMain extends AppCompatActivity {
    int Fullhour,hour,minute;
    String daytime,ampm;
    Date date1,date2,today;
    Calendar calendar1;
    int count=0;

    private Long mLastClickTime = 0L;


//    private ArrayList<Integer> HourList;
//    private ArrayList<Integer> MinuteList;
//    private ArrayList<Integer> IDList;
//    private ArrayList<Date> Date1List;
//    private ArrayList<Date> Date2List;
//    private ArrayList<Calendar> calendarArrayList;

    private ArrayList<AddTime> arraylist;
    private AddTimeAdapter addedTimeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private DBHelper helper;
    private SQLiteDatabase db;

    private EditText et_mediname;
    private EditText et_mediname_detail;
    private EditText et_caution;

    int index= 0;
    int medicine_db_lock=0,alarm_db_lock=0,alarm_lock=0;

    private ArrayList<String> dbAlarmTimeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);




        arraylist = new ArrayList<>();
//        Date1List = new ArrayList<>();
//        Date2List = new ArrayList<>();
//        IDList = new ArrayList<>();
//        calendarArrayList = new ArrayList<>();

        addedTimeAdapter = new AddTimeAdapter(arraylist);

        this.calendar1 = Calendar.getInstance();

        //리사이클러뷰 설정
        recyclerView = (RecyclerView)findViewById(R.id.rv_addtime);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addedTimeAdapter);

        //에디트 텍스트 초기화
        et_mediname = findViewById(R.id.et_mediname);
        et_mediname_detail = findViewById(R.id.et_mediname_detail);
        et_caution = findViewById(R.id.et_caution);


        //DB 생성
        helper = new DBHelper(AlarmMain.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

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
                //데이터 받기
                Fullhour = Integer.parseInt(data.getStringExtra("시간"));
                minute = Integer.parseInt(data.getStringExtra("분"));
                daytime = data.getStringExtra("하루");
                hour = Fullhour;

                if(Fullhour==12){
                    ampm="오후";
                }
                else if(Fullhour>12){
                    hour%=12;
                    ampm="오후";
                }
                else{
                    ampm="오전";
                }
                AddTime addtime = new AddTime(R.drawable.minus_icon,String.valueOf(hour)+"시",
                        String.valueOf(minute)+"분",daytime,ampm);
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
                .setTitleText("원하시는 기간을 선택하세요").setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();

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
                            Toast.makeText(AlarmMain.this, "시작날짜는 당일부터 가능합니다.", Toast.LENGTH_SHORT).show();
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

        if(TextUtils.isEmpty(et_mediname.getText())){
            Toast.makeText(this, "약 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if (arraylist.isEmpty()){
            Toast.makeText(this, "복용 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tv_date.getText())){
            Toast.makeText(this, "복용 기간을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(is_name_duplicated()){

        }
        else if(is_settime_duploicated()){

        }
        else if(is_dbtime_duplicated()){

        }

        else{
            is_empty=1;
        }

        if(is_empty==1){
            TESTALARM();
            MedicineDBAdd();
            AlarmDBAdd();
            //setall_arlam();
//            helper.close();
//            db.close();

            Intent intent2 = new Intent(AlarmMain.this, DosageList.class);
            startActivity(intent2);
            AlarmMain.this.finish();
            Toast.makeText(AlarmMain.this, "약 알람이 추가되었습니다.", Toast.LENGTH_SHORT).show();


        }
    }

    public void MedicineDBAdd(){
        String startdate,enddate;
        String mediname, product,caution;


        mediname = et_mediname.getText().toString();
        product = et_mediname_detail.getText().toString();
        caution = et_caution.getText().toString();
        int count=0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        startdate=simpleDateFormat.format(date1);
        enddate=simpleDateFormat.format(date2);

        //약이름, 제품명, 메모 저장

//        Toast.makeText(this, "약이름은:"+mediname  +"\n약상세이름은:"+product +"\n주의사항은:"+ caution +
//                "\n시작일은:"+startdate+"\n종료일은"+enddate, Toast.LENGTH_SHORT).show();


        db.execSQL("INSERT INTO MEDICINE" +
                "(MEDI_NAME, MEDI_PRODUCT, MEDI_MEMO, " +
                "MEDI_START_DATE, MEDI_END_DATE) " +
                "VALUES ('" + mediname + "', '" + product + "', '" + caution + "', " +
                "'" + startdate + "', '" + enddate + "');");



    }

    public void AlarmDBAdd(){
        String mediname,ampm,hour,minute,routine,time;
        int idx,int_hour,nullcheck=0;
//        for(int i=0;i<arraylist.size();i++){}

        for(int i =0; i<arraylist.size();i++) {

            mediname = et_mediname.getText().toString();
            ampm = arraylist.get(i).getAmpm();
            hour = arraylist.get(i).getTv_hour();
            minute = arraylist.get(i).getTv_minute();
            routine = arraylist.get(i).getTv_daytime();

            // 시간 , 분 원래 값으로 복구하기
            idx = hour.indexOf("시");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "오후" && int_hour != 12) {
                int_hour += 12;
                hour = String.valueOf(int_hour);
            }
            else if(hour.length()<2){
                hour= "0"+hour;
            }

            idx = minute.indexOf("분");
            minute = minute.substring(0, idx);
            if(minute.length()<2){
                minute= "0"+minute;
            }


            //hh:mm형식으로 만들기
            time = hour + ":" + minute;
            System.out.println("약이름은:" + mediname + "  루틴은:" + routine + " 시간은"+time);

            db.execSQL("INSERT INTO MEDI_ALARM" +
                    "(ALARM_MEDI_NAME, ALARM_ROUTINE, ALARM_TIME) " +
                    "VALUES ('" + mediname + "', '" + routine + "', '" + time + "');");


        }

    }


    public boolean is_name_duplicated(){
        String is_empty=null;
        String mediname = et_mediname.getText().toString();
        helper = new DBHelper(AlarmMain.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT MEDI_NAME FROM MEDICINE WHERE MEDI_NAME ='"+ mediname+ "'" , null);

        if(cursor != null && cursor.moveToFirst()){
            is_empty = cursor.getString(0);
            cursor.close();
        }

        if(is_empty != null) {
            Toast.makeText(this, "입력한 이름은 이미 저장되어 있습니다.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            // 중복된 이름이 없을경우 false를 반환시켜 통과
            return false;
        }


    }



    public boolean is_settime_duploicated(){
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

            // 시간 , 분 원래 값으로 복구하기
            idx = hour.indexOf("시");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "오후" && int_hour != 12) {
                int_hour += 12;
            }
            idx = minute.indexOf("분");
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

                // +15분까지 중복 방지
                for(int k=0; k<snoozing_time;k++) {
                    compare=calendar.compareTo(calendar2);
                    if (compare == 0) {
                        time2 = hh_mmformat.format(calendar2.getTime());
                        Toast.makeText(this, "등록하고자 하는 시간" + time1 + "이 " + time2 + "와 중첩됩니다. ", Toast.LENGTH_SHORT).show();
                        is_duplicated = true;
                        break;
                    }
                    calendar.add(Calendar.MINUTE,1);
                }

                calendar.add(Calendar.MINUTE,-snoozing_time);

                // -15분까지 중복 방지
                for(int k=0; k<snoozing_time;k++) {
                    compare=calendar.compareTo(calendar2);
                    if (compare == 0) {
                        time2 = hh_mmformat.format(calendar2.getTime());
                        Toast.makeText(this, "등록하고자 하는 시간" + time1 + "이 " + time2 + "와 중첩됩니다. ", Toast.LENGTH_SHORT).show();
                        is_duplicated = true;
                        break;
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

            // 시간 , 분 원래 값으로 복구하기
            idx = hour.indexOf("시");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "오후" && int_hour != 12) {
                int_hour += 12;
            }
            idx = minute.indexOf("분");
            minute = minute.substring(0, idx);
            int_minute=Integer.parseInt(minute);

            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.set(Calendar.HOUR_OF_DAY,int_hour);
            calendar.set(Calendar.MINUTE,int_minute);
            calendar.set(Calendar.SECOND,0);
            SimpleDateFormat hh_mmformat = new SimpleDateFormat("HH:mm");

            //앞으로 15분
            for(int j=0; j<snooze_time;j++){

                time = hh_mmformat.format(calendar.getTime());
                String duplicated_alarm_name=null;
                String mediname;
                helper = new DBHelper(AlarmMain.this, "newdb.db", null, 1);
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE ALARM_TIME ='"+ time+ "'" , null);

                if(cursor != null && cursor.moveToFirst()){
                    duplicated_alarm_name = cursor.getString(0);
                    cursor.close();
                }
                if(duplicated_alarm_name != null) {
                    if(!Alarm_medi_name.contains(duplicated_alarm_name)){
                        Alarm_medi_name.add(duplicated_alarm_name);
                    }
                    Toast.makeText(this, "선택한 시간은"+Alarm_medi_name.get(Alarm_medi_name.size()-1)+"약 알람시간"+time+"과 겹칠 수 있습니다. 다른시간을 선택하세요.", Toast.LENGTH_SHORT).show();
                    is_duplicated = true;
                }
                calendar.add(Calendar.MINUTE,1);
            }

            //뒤로 15분
            calendar.add(Calendar.MINUTE,-snooze_time);
            for(int j=0; j<snooze_time;j++) {
                time = hh_mmformat.format(calendar.getTime());


                String duplicated_alarm_name = null;
                String mediname;
                helper = new DBHelper(AlarmMain.this, "newdb.db", null, 1);
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE ALARM_TIME ='" + time + "'", null);

                if (cursor != null && cursor.moveToFirst()) {
                    duplicated_alarm_name = cursor.getString(0);
                    cursor.close();
                }
                if (duplicated_alarm_name != null) {
                    if (!Alarm_medi_name.contains(duplicated_alarm_name)) {
                        Alarm_medi_name.add(duplicated_alarm_name);
                    }
                    Toast.makeText(this, "선택한 시간은" + Alarm_medi_name.get(Alarm_medi_name.size() - 1) + "약 알람시간" + time + "과 겹칠 수 있습니다. 다른시간을 선택하세요.", Toast.LENGTH_SHORT).show();
                    is_duplicated = true;
                }
                calendar.add(Calendar.MINUTE, -1);
            }
        }


        int count=0;
        for(int i =0; i<Alarm_medi_name.size();i++){

            String medi_name=Alarm_medi_name.get(i);

            helper = new DBHelper(AlarmMain.this, "newdb.db", null, 1);
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
            compare1 = sday1.compareTo(eday2);//sday1이 eday2보다 크면-(compare=0보다 크면 ok)
            compare2 = eday1.compareTo(sday2);//compare2가 0보다 작으면 ok

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




    public void TESTALARM(){

        String ampm,hour,minute,routine,time;
        int idx,id=0,int_hour,int_minute,last_index=1,count=0;

        helper = new DBHelper(AlarmMain.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT seq FROM sqlite_sequence WHERE name='MEDI_ALARM"+"'" , null);

        if(cursor != null && cursor.moveToFirst()){
            last_index = cursor.getInt(0);

            id=last_index;
//            Toast.makeText(this, "id="+id, Toast.LENGTH_SHORT).show();
            cursor.close();
        }



        for (int i =0; i<arraylist.size();i++ ){
            id++;
            ampm = arraylist.get(i).getAmpm();
            hour = arraylist.get(i).getTv_hour();
            minute = arraylist.get(i).getTv_minute();
            routine = arraylist.get(i).getTv_daytime();

            idx = hour.indexOf("시");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            //시간 가져오기
            if (ampm == "오후" && int_hour != 12) {
                int_hour += 12;
            }

            //분 가져오기
            idx = minute.indexOf("분");
            minute = minute.substring(0, idx);
            int_minute = Integer.parseInt(minute);

            this.calendar1.setTime(date1);
            this.calendar1.set(Calendar.HOUR_OF_DAY, int_hour);
            this.calendar1.set(Calendar.MINUTE,int_minute );
            this.calendar1.set(Calendar.SECOND, 0);

            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("id",id);
            intent.putExtra("count",count);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(),pendingIntent);
        }
        Toast.makeText(this, "id="+id, Toast.LENGTH_SHORT).show();

    }


    public void setall_arlam(){

        ArrayList <String> medi_name_list;
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
            System.out.println("약이름은"+mediname+" 오늘은"+ st_today+" 시작일은"+st_sday+" 종료일"+st_eday);

            try {
                today = format_ymd.parse(st_today);
                sday = format_ymd.parse(st_sday);
                eday = format_ymd.parse(st_eday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            compare1 = today.compareTo(sday);//sday1이 eday2보다 크면-(compare=0보다 크면 ok)
            compare2 = today.compareTo(eday);//compare2가 0보다 작으면 ok
            if(compare1>=0 && compare2<=0){
                medi_name_list.add(mediname);
//                System.out.println("추가된약은"+mediname);
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

                System.out.println("알람에 추가할약은"+mediname+" id는"+id+"시간은"+time);
                int idx = time.indexOf(":");
                int hour1= Integer.parseInt(time.substring(0,idx));
                int minute1 = Integer.parseInt(time.substring(idx+1));

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour1);
                calendar.set(Calendar.MINUTE, minute1);
                calendar.set(Calendar.SECOND,0);
                Intent repeat_intent = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        cursor.close();
    }






    public void Canclealarm(int id){
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(AlarmMain.this, DosageList.class);
        startActivity(intent);
        AlarmMain.this.finish();
    }

    public void CancleClick(View view) {
        Intent intent = new Intent(AlarmMain.this, DosageList.class);
        startActivity(intent);
        AlarmMain.this.finish();
    }


}