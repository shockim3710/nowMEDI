package com.example.nowmedi.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;
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
    int index= 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        recyclerView = (RecyclerView)findViewById(R.id.rv_addtime);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arraylist = new ArrayList<>();
        HourList = new ArrayList<>();
        MinuteList = new ArrayList<>();
        Date1List = new ArrayList<>();
        Date2List = new ArrayList<>();
        IDList = new ArrayList<>();

        calendarArrayList = new ArrayList<>();
        addedTimeAdapter = new AddTimeAdapter(arraylist);
        recyclerView.setAdapter(addedTimeAdapter);
        this.calendar1 = Calendar.getInstance();

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

                HourList.add(Fullhour);
                MinuteList.add(minute);
                for(int i=0; i<HourList.size();i++){
                    System.out.println(HourList.get(i)+"시"+MinuteList.get(i)+"분");

                }

                Toast.makeText(this, +HourList.get(0)+"시" +MinuteList.get(0)+"분", Toast.LENGTH_SHORT).show();



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

//        this.calendar1.setTime(date1);
//        this.calendar1.set(Calendar.HOUR_OF_DAY, Fullhour);
//        this.calendar1.set(Calendar.MINUTE, minute);
//        this.calendar1.set(Calendar.SECOND, 0);

        // Receiver 설정
        Intent intent = new Intent(this, AlarmReceiver.class);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        String end = format1.format(date2);
        intent.putExtra("end",end);

        // 알람 설정
//        final int id = (int) System.currentTimeMillis();
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar1.getTimeInMillis(),pendingIntent);

        for(int i =0; i<HourList.size();i++) {

            int Lastindex = IDList.size();
            IDList.add(Lastindex);
            System.out.println(Lastindex+"\n");
            Lastindex ++;
            intent.putExtra("id",IDList.get(i));
            this.calendar1.setTime(date1);
            this.calendar1.set(Calendar.HOUR_OF_DAY, HourList.get(i));
            this.calendar1.set(Calendar.MINUTE, MinuteList.get(i));
            this.calendar1.set(Calendar.SECOND, 0);
            calendarArrayList.add(calendar1);
            Date1List.add(date1);
            Date2List.add(date2);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, IDList.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarArrayList.get(i).getTimeInMillis(),pendingIntent);

        }

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Toast.makeText(this, "Alarm : " + format.format(calendar1.getTime()), Toast.LENGTH_LONG).show();

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