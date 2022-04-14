package com.example.nowmedi.alarm;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;

public class AlarmGo extends AppCompatActivity{
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_go);

        // 알람음 재생
        this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
        this.mediaPlayer.start();

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
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        finishAndRemoveTask();


    }


}
