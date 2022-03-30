package com.example.nowmedi.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.nowmedi.R;

public class AddTimePop extends AppCompatActivity {

    TimePicker mTimePicker;
    int Hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_time_pop);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);



        mTimePicker = (TimePicker) findViewById(R.id.timepicker);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Hour=selectedHour;
                minute=selectedMinute;
            }
        });

    }
    public void mOnSave(View v){
        //데이터 전달하기
        Intent intent = new Intent(this, alarm_main.class);
        intent.putExtra("시간",String.valueOf(Hour));
        intent.putExtra("분",""+minute);
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();

    }





    //확인 버튼 클릭
    public void mOnClose(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기

        return;
    }
}