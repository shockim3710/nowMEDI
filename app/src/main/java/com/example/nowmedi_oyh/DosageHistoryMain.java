package com.example.nowmedi_oyh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        display_data();

    }

    public void Show_Dosage_detail(View v){

//        String name = "당뇨병약";
//        String date = "2022.04.01";
//        String time = "09:00";
//        String route = "아침약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        name = "당뇨병약";
//        date = "2022.04.01";
//        time = "12:00";
//        route = "점심약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"',"+"NULL"+",'"+ route+"')"
//        );
//
//        name = "당뇨병약";
//        date = "2022.04.01";
//        time = "18:00";
//        route = "저녁약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        name = "당뇨병약";
//        date = "2022.04.02";
//        time = "09:00";
//        route = "아침약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"',"+"NULL"+",'"+ route+"')"
//        );
//
//        name = "당뇨병약";
//        date = "2022.04.02";
//        time = "12:00";
//        route = "점심약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        name = "당뇨병약";
//        date = "2022.04.02";
//        time = "18:00";
//        route = "저녁약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        name = "당뇨병약";
//        date = "2022.04.03";
//        time = "9:00";
//        route = "아침약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        name = "혈압약";
//        date = "2022.04.01";
//        time = "9:00";
//        route = "아침약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        name = "혈압약";
//        date = "2022.04.02";
//        time = "9:00";
//        route = "아침약";
//        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
//                +"('"+ name + "','" +date +"','"+time+"','"+ route+"')"
//        );
//
//        display_data();
//        dosageHistoryAdapter.notifyDataSetChanged();

    }
public void display_data  (){
    helper = new DBHelper(DosageHistoryMain.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT DISTINCT(HISTORY_MEDI_NAME) FROM MEDI_HISTORY", null);

        int recordCount = cursor.getCount();

        for (int i=0; i<recordCount; i++){
            cursor.moveToNext();

            String medi_name = cursor.getString(0);

            Cursor cursor2 = database.rawQuery("SELECT COUNT(c.HISTORY_MEDI_NAME) FROM (SELECT * FROM MEDI_HISTORY " +
                    "WHERE HISTORY_MEDI_NAME ='"+medi_name +"'and HISTORY_TIME IS NOT NULL) AS c", null);
            cursor2.moveToNext();
            int bunja = cursor2.getInt(0);
            System.out.println("분자는?"+bunja);

            cursor2 = database.rawQuery("Select count(c.HISTORY_MEDI_NAME) FROM (SELECT * FROM MEDI_HISTORY "+
                    "WHERE HISTORY_MEDI_NAME ='"+ medi_name +"')AS c", null);
            cursor2.moveToNext();
            int bunmo = cursor2.getInt(0);
            System.out.println("분모는?"+bunmo);

            float percent = Math.round(((float) bunja/(float) bunmo) * 100.0f);

            DosageHistory dosageHistory = new DosageHistory((int) percent,R.drawable.medicine,medi_name,
                    String.valueOf(percent),String.valueOf(bunja)+"/",String.valueOf(bunmo));
            arraylist.add(dosageHistory);

        }

        dosageHistoryAdapter.notifyDataSetChanged();

    }
}



