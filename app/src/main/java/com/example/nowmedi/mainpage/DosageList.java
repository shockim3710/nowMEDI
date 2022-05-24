package com.example.nowmedi.mainpage;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AlarmMain;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.history.DosageHistoryMain;
import com.example.nowmedi.protector.ProtectorManage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DosageList extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;
    private ListView morningList;
    private ListView lunchList;
    private ListView dinnerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosage_list);

        SimpleDateFormat dateFormat = new SimpleDateFormat(" MM월 dd일");

        Date currentTime = new Date();
        String todayDate = dateFormat.format(currentTime);

        TextView textView3 = (TextView) findViewById(R.id.today_date) ;
        textView3.setText(todayDate);

        helper = new DBHelper(DosageList.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);


        morningList = (ListView)findViewById(R.id.morning_listview);
        lunchList = (ListView)findViewById(R.id.lunch_listview);
        dinnerList = (ListView)findViewById(R.id.dinner_listview);

        try {
            dosageMorningList();
            dosageLunchList();
            dosageDinnerList();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT


                    },
                    1);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH

                    },
                    1);
        }





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&!Settings.canDrawOverlays(this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()));
            startActivity(intent);
        }



    }


    void dosageMorningList() throws ParseException {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(DosageList.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM MEDI_ALARM WHERE ALARM_ROUTINE = '아침약' ORDER BY ALARM_TIME", null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        DosageListAdapter adapter = new DosageListAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);

            Cursor date = database.rawQuery("SELECT MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE " +
                    "WHERE MEDI_NAME = '" + name + "' ", null);

            while (date.moveToNext()) {
                String start_dt = date.getString(0);
                String end_dt = date.getString(1);

                // 오늘이 복용기간내에 있는지 판별
                if(PeriodDateCalculate(start_dt, end_dt) == true) {
                    adapter.addItemToList(cursor.getString(1), cursor.getString(3));
                }
            }

        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        morningList.setAdapter(adapter);
    }


    void dosageLunchList() throws ParseException {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(DosageList.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM MEDI_ALARM WHERE ALARM_ROUTINE = '점심약' ORDER BY ALARM_TIME", null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        DosageListAdapter adapter = new DosageListAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);

            Cursor date = database.rawQuery("SELECT MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE " +
                    "WHERE MEDI_NAME = '" + name + "' ", null);

            while (date.moveToNext()) {
                String start_dt = date.getString(0);
                String end_dt = date.getString(1);

                // 오늘이 복용기간내에 있는지 판별
                if(PeriodDateCalculate(start_dt, end_dt) == true) {
                    adapter.addItemToList(cursor.getString(1), cursor.getString(3));
                }
            }

        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        lunchList.setAdapter(adapter);
    }


    void dosageDinnerList() throws ParseException {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(DosageList.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM MEDI_ALARM WHERE ALARM_ROUTINE = '저녁약' ORDER BY ALARM_TIME", null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        DosageListAdapter adapter = new DosageListAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);

            Cursor date = database.rawQuery("SELECT MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE " +
                    "WHERE MEDI_NAME = '" + name + "' ", null);

            while (date.moveToNext()) {
                String start_dt = date.getString(0);
                String end_dt = date.getString(1);

                // 오늘이 복용기간내에 있는지 판별
                if(PeriodDateCalculate(start_dt, end_dt) == true) {
                    adapter.addItemToList(cursor.getString(1), cursor.getString(3));
                }
            }

        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        dinnerList.setAdapter(adapter);
    }



    // 오늘이 복용기간내에 있는지 판별
    boolean PeriodDateCalculate(String start_dt, String end_dt) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        boolean result = false;

        Date currentTime = new Date();
        String date = format.format(currentTime);

        Date startDate = format.parse(start_dt);
        Date endDate = format.parse(end_dt);
        Date todate = format.parse(date);

        int compare1 = todate.compareTo(startDate); //크다(1), 같다(0), 작다(-1)
        int compare2 = endDate.compareTo(todate);

        if(compare1 >= 0 && compare2>=0 ) {
            result = true;
        }

        return result;
    }



    public void AlarmAddClick(View view) {
        Intent intent = new Intent(DosageList.this, AlarmMain.class);
        startActivity(intent);
        DosageList.this.finish();

//        String name = "삭제할 약";
//        String product = "테스트zzzz";
//        String memo = "주의해야한다ㅋㅋㅋ";
//        String startdate = "2022.04.01";
//        String enddate = "2022.09.09";
//
//        String routine = "아침약";
//        String time = "09:00";
//
//        String name1 = "홍길동";
//        String num = "01011112222";
//        String msg = "약을 복용하지 않았습니다. 확인해주세요";

//        db.execSQL("INSERT INTO PROTECTOR" +
//                "(PROTECTOR_NAME, PROTECTOR_PHONE_NUM, PROTECTOR_MESSAGE) " +
//                "VALUES ('" + name1 + "', '" + num + "', '" + msg + "');");

//        db.execSQL("INSERT INTO MEDICINE" +
//                "(MEDI_NAME, MEDI_PRODUCT, MEDI_MEMO, " +
//                "MEDI_START_DATE, MEDI_END_DATE) " +
//                "VALUES ('" + name + "', '" + product + "', '" + memo + "', " +
//                "'" + startdate + "', '" + enddate + "');");
//
//        db.execSQL("INSERT INTO MEDI_ALARM" +
//                "(ALARM_MEDI_NAME, ALARM_ROUTINE, ALARM_TIME) " +
//                "VALUES ('" + name + "', '" + routine + "', '" + time + "');");
//
//        Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();

    }


    public void CalendarClick(View view) {
        Intent intent = new Intent(DosageList.this, DosageCalendarList.class);
        startActivity(intent);
        DosageList.this.finish();
        overridePendingTransition(0, 0); //애니메이션 없애기
    }

    public void DosageHistoryClick(View view) {
        Intent intent = new Intent(DosageList.this, DosageHistoryMain.class);
        startActivity(intent);
        DosageList.this.finish();
        overridePendingTransition(0, 0); //애니메이션 없애기
    }

    public void ProtectorClick(View view) {
        Intent intent = new Intent(DosageList.this, ProtectorManage.class);
        startActivity(intent);
        DosageList.this.finish();
        overridePendingTransition(0, 0); //애니메이션 없애기
    }



    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500)
            finish();
        toast.cancel();

    }

}