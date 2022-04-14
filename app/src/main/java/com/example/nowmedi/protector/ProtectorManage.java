package com.example.nowmedi.protector;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AddTime;
import com.example.nowmedi.alarm.AlarmMain;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.history.DosageHistoryMain;
import com.example.nowmedi.mainpage.DosageCalendarList;
import com.example.nowmedi.mainpage.DosageList;
import com.example.nowmedi.mainpage.DosageListAdapter;
import com.example.nowmedi.mainpage.MediDetail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class ProtectorManage extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;
    private ListView protectorList;
    private String clickProtectorPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_manage);

        protectorList = (ListView)findViewById(R.id.protector_listview);

        helper = new DBHelper(ProtectorManage.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        showProtectorList();
        protectorDelete();

    }

    void protectorDelete() {

        protectorList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(ProtectorManage.this)
                        .setTitle("")
                        .setMessage("보호자를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProtectorListAdapterData item = (ProtectorListAdapterData) parent.getItemAtPosition(position);
                                clickProtectorPhone = item.getProtector_phone();

                                Toast.makeText(ProtectorManage.this, "보호자를 삭제하였습니다.", Toast.LENGTH_SHORT).show();

                                String sql = "DELETE FROM PROTECTOR " +
                                        "WHERE PROTECTOR_PHONE_NUM = '" + clickProtectorPhone + "';";

                                db.execSQL(sql);
                                showProtectorList();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ProtectorManage.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }); AlertDialog msgDlg = msgBuilder.create();
                msgDlg.show();

                return true;
            }
        });


    }



    void showProtectorList() {
        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        helper = new DBHelper(ProtectorManage.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM PROTECTOR", null);

        //리스트뷰에 목록 채워주는 도구인 adapter준비
        ProtectorListAdapter adapter = new ProtectorListAdapter();

        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while (cursor.moveToNext()) {
            adapter.addItemToList(cursor.getString(1), cursor.getString(2));
        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        protectorList.setAdapter(adapter);
    }


    public void AlarmAddClick(View view) {
        Intent intent = new Intent(ProtectorManage.this, AlarmMain.class);
        startActivity(intent);
        ProtectorManage.this.finish();
    }


    public void Add_ProtectorClick(View view) {
        Intent intent = new Intent(ProtectorManage.this, ProtectorAdd.class);
        startActivity(intent);
        ProtectorManage.this.finish();
    }

    public void DosageHistoryClick(View view) {
        Intent intent = new Intent(ProtectorManage.this, DosageHistoryMain.class);
        startActivity(intent);
        ProtectorManage.this.finish();
    }

    public void DosageListClick(View view) {
        Intent intent = new Intent(ProtectorManage.this, DosageList.class);
        startActivity(intent);
        ProtectorManage.this.finish();
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
