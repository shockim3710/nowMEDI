package com.example.nowmedi.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.example.nowmedi.R;
import com.example.nowmedi.mainpage.DosageList;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class alarm_main extends AppCompatActivity {
    int hour,minute;
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
                String result = data.getStringExtra("시간");
                String result2 = data.getStringExtra("분");
                AddTime addtime = new AddTime(R.drawable.minus_icon,String.valueOf(result)+"시",String.valueOf(result2)+"분");
                arraylist.add(addtime);
                addedTimeAdapter.notifyDataSetChanged();
            }
        }
    }



}