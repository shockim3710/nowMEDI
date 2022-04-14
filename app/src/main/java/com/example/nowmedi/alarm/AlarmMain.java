package com.example.nowmedi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AlarmMain extends AppCompatActivity {
    int Fullhour,hour,minute;
    String daytime,ampm;
    Date date1,date2;
    Calendar calendar1;
    int count=0;


    private ArrayList<Integer> HourList;
    private ArrayList<Integer> MinuteList;
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

    private EditText et_mediname;
    private EditText et_mediname_detail;
    private EditText et_caution;

    int index= 0;
    int medicine_db_lock=0,alarm_db_lock=0,alarm_lock=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);




        arraylist = new ArrayList<>();
        Date1List = new ArrayList<>();
        Date2List = new ArrayList<>();
        IDList = new ArrayList<>();
        calendarArrayList = new ArrayList<>();

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
//                        String today = simpleDateFormat.format(date2);
//                        Toast.makeText(alarm_main.this, ""+date2, Toast.LENGTH_SHORT).show();

                        date1.setTime(selection.first);
                        if(date1.before(date2)){

                        }
                        date2.setTime(selection.second);

                        String datestring1 = simpleDateFormat.format(date1);
                        String datestring2 = simpleDateFormat.format(date2);

                        tv_date.setText(datestring1 + "~" + datestring2);
                    }
                });
    }
    public void SetAlarm(View v){
        int is_empty=0;
        TextView tv_date = findViewById(R.id.tv_seted_date);


        if(TextUtils.isEmpty(et_mediname.getText())){
            Toast.makeText(this, "약이름을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        else if (arraylist.isEmpty()){
            Toast.makeText(this, "시간을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tv_date.getText())){
            Toast.makeText(this, "복용기간을 입력하세요", Toast.LENGTH_SHORT).show();
        }
//        else if(TextUtils.isEmpty(et_mediname_detail.getText())){
//            Toast.makeText(this, "약제품명 입력하세요", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(et_caution.getText())){
//            Toast.makeText(this, "주의사항을 입력하세요", Toast.LENGTH_SHORT).show();
//        }
        else{
            is_empty=1;
        }

        if(is_empty==1){
            MedicineDBAdd();
            AlarmDBAdd();

        }



        // Receiver 설정
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
//        String end = format1.format(date2);
//        intent.putExtra("end",end);

        // 알람 설정

//        for(int i =0; i<HourList.size();i++) {
//
//            int Lastindex = IDList.size();
//            IDList.add(Lastindex);
//            System.out.println(Lastindex+"\n");
//            Lastindex ++;
//            intent.putExtra("id",IDList.get(i));
//            this.calendar1.setTime(date1);
//            this.calendar1.set(Calendar.HOUR_OF_DAY, HourList.get(i));
//            this.calendar1.set(Calendar.MINUTE, MinuteList.get(i));
//            this.calendar1.set(Calendar.SECOND, 0);
//            calendarArrayList.add(calendar1);
//            Date1List.add(date1);
//            Date2List.add(date2);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, IDList.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarArrayList.get(i).getTimeInMillis(),pendingIntent);
////            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarArrayList.get(i).getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
//
//        }

        // Toast 보여주기 (알람 시간 표시)
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Toast.makeText(this, "Alarm : " + format.format(calendar1.getTime()), Toast.LENGTH_LONG).show();


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

        Toast.makeText(this, "약이름은:"+mediname  +"\n약상세이름은:"+product +"\n주의사항은:"+ caution +
                "\n시작일은:"+startdate+"\n종료일은"+enddate, Toast.LENGTH_SHORT).show();


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