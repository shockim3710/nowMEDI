package com.example.nowmedi.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TimePicker;

import com.example.nowmedi.R;

public class AddTimePop extends AppCompatActivity {

    TimePicker mTimePicker;
    int Hour,minute;
    String Daytime="아침약";
    ImageButton ib_morning,ib_afternoon,ib_night;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_time_pop);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        ib_morning=(ImageButton)findViewById(R.id.ib_morning);
        ib_afternoon=(ImageButton)findViewById(R.id.ib_afternoon);
        ib_night=(ImageButton)findViewById(R.id.ib_night);
        ib_morning.setBackgroundColor(Color.parseColor("#FFF48F8F"));


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
        intent.putExtra("하루", Daytime);
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    public void mOnMorning(View v){
        Daytime="아침약";

        ib_morning.setBackgroundColor(Color.parseColor("#FFF48F8F"));
        ib_afternoon.setBackgroundColor(Color.parseColor("#FFDEDE"));
        ib_night.setBackgroundColor(Color.parseColor("#FFDEDE"));

    }

    public void mOnAfternoon(View v){

        Daytime="점심약";
        ib_morning.setBackgroundColor(Color.parseColor("#FFDEDE"));
        ib_afternoon.setBackgroundColor(Color.parseColor("#FFF48F8F"));
        ib_night.setBackgroundColor(Color.parseColor("#FFDEDE"));

    }

    public void mOnNight(View v){

        Daytime="저녁약";
        ib_morning.setBackgroundColor(Color.parseColor("#FFDEDE"));
        ib_afternoon.setBackgroundColor(Color.parseColor("#FFDEDE"));
        ib_night.setBackgroundColor(Color.parseColor("#FFF48F8F"));

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