package com.example.nowmedi.mainpage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AlarmReceiver;
import com.example.nowmedi.database.DBHelper;

public class MediDetail extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;

    private TextView mediName;
    private TextView mediStartDate;
    private TextView mediEndDate;
    private TextView mediProduct;
    private TextView mediMemo;
    private String clickMediName;

    private ListView AlarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medi_detail);

        mediName = (TextView) findViewById(R.id.medi_name_detail);
        mediStartDate = (TextView) findViewById(R.id.medi_start_date_detail);
        mediEndDate = (TextView) findViewById(R.id.medi_end_date_detail);
        mediProduct = (TextView) findViewById(R.id.medi_product_detail);
        mediMemo = (TextView) findViewById(R.id.medi_memo_detail);

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        clickMediName = intent.getStringExtra("mediName");

        mediName.setText(clickMediName);

        helper = new DBHelper(MediDetail.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        AlarmList = (ListView)findViewById(R.id.medi_alarm_detail_listview);

        mediDetailAlarmList();
        showMediDetail();
    }

    void mediDetailAlarmList(){
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(MediDetail.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM MEDI_ALARM " +
                "WHERE ALARM_MEDI_NAME = '" + clickMediName + "' ", null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        MediDetailAdapter adapter = new MediDetailAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while (cursor.moveToNext()) {
            adapter.addItemToList(cursor.getString(2), cursor.getString(3));
            }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        AlarmList.setAdapter(adapter);

    }

    void showMediDetail() {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(MediDetail.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor1 = database.rawQuery("SELECT * FROM MEDICINE " +
                "WHERE MEDI_NAME = '" + clickMediName + "' ", null);

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while (cursor1.moveToNext()) {
            mediProduct.setText(cursor1.getString(2));
            mediMemo.setText(cursor1.getString(3));
            mediStartDate.setText(cursor1.getString(4));
            mediEndDate.setText(cursor1.getString(5));
        }

    }

    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MediDetail.this)
                .setTitle("")
                .setMessage("약을 삭제하시겠습니까? 약 내역도 같이 삭제됩니다.")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MediDetail.this, DosageList.class);
                        startActivity(intent);
                        MediDetail.this.finish();

                        // 알람 취소
                        SQLiteDatabase database = helper.getReadableDatabase();
                        Cursor cursor = database.rawQuery("SELECT _id FROM MEDI_ALARM WHERE ALARM_MEDI_NAME ='"+ clickMediName+ "'" , null);
                        for(int idx=0;idx<cursor.getCount();idx++){
                            cursor.moveToNext();
                            int id=cursor.getInt(0);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent myIntent = new Intent(getApplicationContext(),
                                    AlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                    getApplicationContext(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

                            alarmManager.cancel(pendingIntent);
                        }

                        db = helper.getWritableDatabase();
                        Toast.makeText(MediDetail.this, "약 알람을 삭제하였습니다.", Toast.LENGTH_SHORT).show();

                        String sql1 = "DELETE FROM MEDICINE " +
                                "WHERE MEDI_NAME = '" + clickMediName + "';";

                        String sql2 = "DELETE FROM MEDI_ALARM " +
                                "WHERE ALARM_MEDI_NAME = '" + clickMediName + "';";

                        String sql3 = "DELETE FROM MEDI_HISTORY " +
                                "WHERE HISTORY_MEDI_NAME = '" + clickMediName + "';";

                        db.execSQL(sql1);
                        db.execSQL(sql2);
                        db.execSQL(sql3);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MediDetail.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }); AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    public void MediDeleteClick(View view) {
        showDialog();
    }

    public void MediModifyClick(View view) {
        Intent intent = new Intent(MediDetail.this, MediDetailModify.class);
        intent.putExtra("mediName", clickMediName);
        startActivity(intent);
        MediDetail.this.finish();
        overridePendingTransition(0, 0);
    }

    public void BackClick(View view) {
        Intent intent = new Intent(MediDetail.this, DosageList.class);
        startActivity(intent);
        MediDetail.this.finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent(MediDetail.this, DosageList.class);
        startActivity(intent);
        MediDetail.this.finish();
    }
}
