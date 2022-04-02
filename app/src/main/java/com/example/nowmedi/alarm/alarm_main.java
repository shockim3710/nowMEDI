package com.example.nowmedi.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.nowmedi.R;
import com.example.nowmedi.mainpage.DosageList;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class alarm_main extends AppCompatActivity {
    int Fullhour,hour,minute;
    String daytime,ampm;
    private ArrayList<AddTime> arraylist;
    private AddTimeAdapter addedTimeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        recyclerView = (RecyclerView)findViewById(R.id.rv_addtime);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arraylist = new ArrayList<>();
        addedTimeAdapter = new AddTimeAdapter(arraylist);
        recyclerView.setAdapter(addedTimeAdapter);

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


                AddTime addtime = new AddTime(R.drawable.minus_icon,String.valueOf(hour)+"시",String.valueOf(minute)+"분",daytime,ampm );
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
                        Date date1  = new Date();
                        Date date2  = new Date();

                        date1.setTime(selection.first);
                        date2.setTime(selection.second);


                        String datestring1 = simpleDateFormat.format(date1);
                        String datestring2 = simpleDateFormat.format(date2);


                        tv_date.setText(datestring1 + "~" + datestring2);
                    }
                });
    }



}