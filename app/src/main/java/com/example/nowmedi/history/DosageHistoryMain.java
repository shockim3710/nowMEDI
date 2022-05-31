package com.example.nowmedi.history;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AlarmMain;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.mainpage.DosageList;
import com.example.nowmedi.protector.ProtectorManage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DosageHistoryMain extends AppCompatActivity {
    private ArrayList<DosageHistory> arraylist;
    private DosageHistoryAdapter dosageHistoryAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosage_history_main);

        helper = new DBHelper(DosageHistoryMain.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        recyclerView = (RecyclerView)findViewById(R.id.dosage_history_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arraylist = new ArrayList<>();
        dosageHistoryAdapter = new DosageHistoryAdapter(arraylist);
        recyclerView.setAdapter(dosageHistoryAdapter);
        try {
            display_data();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

public void display_data  () throws ParseException {
    helper = new DBHelper(DosageHistoryMain.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT DISTINCT(MEDI_NAME) FROM MEDICINE", null);

        int recordCount = cursor.getCount();

        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");


        for (int i=0; i<recordCount; i++){
            cursor.moveToNext();

            String medi_name = cursor.getString(0);

            Cursor cursor2 = database.rawQuery("SELECT COUNT(c.HISTORY_MEDI_NAME) FROM (SELECT * FROM MEDI_HISTORY " +
                    "WHERE HISTORY_MEDI_NAME ='"+medi_name +"'and HISTORY_TIME IS NOT NULL) AS c", null);
            cursor2.moveToNext();
            int bunja = cursor2.getInt(0);

            cursor2 = database.rawQuery("Select MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE " +
                    "WHERE MEDI_NAME ='"+ medi_name +"'", null);
            cursor2.moveToNext();

            String sdate = cursor2.getString(0);
            String edate = cursor2.getString(1);

            cursor2 = database.rawQuery("Select COUNT(ALARM_ROUTINE) FROM MEDI_ALARM " +
                    "WHERE ALARM_MEDI_NAME ='"+ medi_name +"'", null);
            cursor2.moveToNext();

            int routineCount = cursor2.getInt(0);

            Date startDate = format.parse(sdate);
            Date endDate = format.parse(edate);

            long diff = endDate.getTime() - startDate.getTime();

            TimeUnit time = TimeUnit.DAYS;
            long dateCount = time.convert(diff, TimeUnit.MILLISECONDS);

            long bunmo = (dateCount+1) * routineCount;

            float percent = Math.round(((float) bunja/(float) bunmo) * 100.0f);

            DosageHistory dosageHistory = new DosageHistory((int) percent,R.drawable.medicine,medi_name,
                    String.valueOf(percent),String.valueOf(bunja)+"/",String.valueOf(bunmo));
            arraylist.add(dosageHistory);

        }

        dosageHistoryAdapter.notifyDataSetChanged();

    }

    public void AlarmAddClick(View view) {
        Intent intent = new Intent(DosageHistoryMain.this, AlarmMain.class);
        startActivity(intent);
        DosageHistoryMain.this.finish();
    }

    public void DosageListClick(View view) {
        Intent intent = new Intent(DosageHistoryMain.this, DosageList.class);
        startActivity(intent);
        DosageHistoryMain.this.finish();
        overridePendingTransition(0, 0);
    }

    public void ProtectorClick(View view) {
        Intent intent = new Intent(DosageHistoryMain.this, ProtectorManage.class);
        startActivity(intent);
        DosageHistoryMain.this.finish();
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



