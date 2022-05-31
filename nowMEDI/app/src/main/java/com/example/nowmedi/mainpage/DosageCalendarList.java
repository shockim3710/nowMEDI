package com.example.nowmedi.mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AlarmMain;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.history.DosageHistoryMain;
import com.example.nowmedi.protector.ProtectorManage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DosageCalendarList extends AppCompatActivity {

    private CalendarView calendarView;

    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat(" yyyy년 M월 d일");

    Date currentTime = new Date();
    String date = dateFormat1.format(currentTime);
    String todayDate = dateFormat2.format(currentTime);

    DBHelper helper;
    SQLiteDatabase db;
    ListView calendarList;
    String clickDate = date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosage_calendar_list);

        final TextView textView1 = findViewById(R.id.select_date);

        CalendarView calendarView = findViewById(R.id.calendarView);

        textView1.setText(todayDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                textView1.setText(String.format(" %d년 %d월 %d일", year, month, dayOfMonth));
                clickDate = String.format("%d.%d.%d", year, month, dayOfMonth);

                try {
                    dosageCalendarList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        calendarList = (ListView)findViewById(R.id.select_listview);

        try {
            dosageCalendarList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        helper = new DBHelper(DosageCalendarList.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);
    }

    void dosageCalendarList() throws ParseException {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(DosageCalendarList.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM MEDI_ALARM ORDER BY ALARM_TIME", null);

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

                // 선택날짜가 복용기간내에 있는지 판별
                if(PeriodDateCalculate(start_dt, end_dt) == true) {
                    adapter.addItemToList(cursor.getString(1), cursor.getString(3));
                }
            }

        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        calendarList.setAdapter(adapter);
    }


    // 선택날짜가 복용기간내에 있는지 판별
    boolean PeriodDateCalculate(String start_dt, String end_dt) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        boolean result = false;

        Date startDate = format.parse(start_dt);
        Date endDate = format.parse(end_dt);
        Date todate = format.parse(clickDate);

        int compare1 = todate.compareTo(startDate); //크다(1), 같다(0), 작다(-1)
        int compare2 = endDate.compareTo(todate);

        if(compare1 >= 0 && compare2>=0 ) {
            result = true;
        }

        return result;
    }

    public void ListClick(View view) {
        Intent intent = new Intent(DosageCalendarList.this, DosageList.class);
        startActivity(intent);
        DosageCalendarList.this.finish();
        overridePendingTransition(0, 0);
    }

    public void AlarmAddClick(View view) {
        Intent intent = new Intent(DosageCalendarList.this, AlarmMain.class);
        startActivity(intent);
        DosageCalendarList.this.finish();

    }

    public void DosageHistoryClick(View view) {
        Intent intent = new Intent(DosageCalendarList.this, DosageHistoryMain.class);
        startActivity(intent);
        DosageCalendarList.this.finish();
        overridePendingTransition(0, 0);
    }

    public void ProtectorClick(View view) {
        Intent intent = new Intent(DosageCalendarList.this, ProtectorManage.class);
        startActivity(intent);
        DosageCalendarList.this.finish();
        overridePendingTransition(0, 0);
    }

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;
    @Override
    public void onBackPressed() {
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