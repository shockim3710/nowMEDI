package com.example.nowmedi.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Date today = new Date();
        Date enddate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String td = format.format(today);
        String ed =intent.getStringExtra("end");
        int id = intent.getIntExtra("id",0);



        try {
            enddate = format.parse(ed);
            today = format.parse(td);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        int compare1 = today.compareTo(enddate);


        Toast.makeText(context, "today"+ td +"\nendday"+ ed +"compare" +compare1+ "id"+id, Toast.LENGTH_LONG).show();

      if(compare1>0){
           return;
      }





        Intent sIntent = new Intent(context, AlarmService.class);
        // Oreo(26) 버전 이후부터는 Background 에서 실행을 금지하기 때문에 Foreground 에서 실행해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(sIntent);
        } else {
            context.startService(sIntent);
        }
    }
}
