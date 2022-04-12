package com.example.nowmedi.history;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.mainpage.DosageList;
import com.example.nowmedi.mainpage.MediDetail;

import java.util.ArrayList;

public class DosageDetailMain extends AppCompatActivity {
    TextView textView;
    private DBHelper helper;
    private SQLiteDatabase db;
    private String history_mediname;


    private ArrayList<DosageDetailSuper> arrayList_super;
    private ArrayList<ArrayList<DosageDetailSub>> arrayList_sub;
    private DosageDetailSuperAdapter dosageDetailSuperAdapter;
    private RecyclerView recyclerView_super;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosage_detail);
        Intent intent = getIntent();
        history_mediname = intent.getStringExtra("title");
        Toast.makeText(this, history_mediname, Toast.LENGTH_SHORT).show();
        textView = (TextView)findViewById(R.id.medi_name_detail);
        textView.setText(history_mediname);

        int capicity=100;
        arrayList_sub = new ArrayList<>(capicity);
        arrayList_super = new ArrayList<>();


        recyclerView_super = (RecyclerView)findViewById(R.id.rv_super_detail);
        linearLayoutManager = new LinearLayoutManager(DosageDetailMain.this);
        dosageDetailSuperAdapter = new DosageDetailSuperAdapter(arrayList_super);
        recyclerView_super.setAdapter(dosageDetailSuperAdapter);
        recyclerView_super.setLayoutManager(linearLayoutManager);

        //supertest();
        DBTest();
    }


    public void supertest(){

        for(int i=0; i<3;i++){
            arrayList_sub.add(new ArrayList<>());
        }


        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.morning_medi,
                "오전","6시","30분","복용안함");
        arrayList_sub.get(0).add(dosageDetailSub);

        dosageDetailSub = new DosageDetailSub(R.drawable.morning_medi,
                "오후","8시","10분","복용함");
        arrayList_sub.get(1).add(dosageDetailSub);

        DosageDetailSuper dosageDetailSuper = new DosageDetailSuper("2021년 11월 21일",arrayList_sub.get(0));
        arrayList_super.add(dosageDetailSuper);
        dosageDetailSuper = new DosageDetailSuper("2022년5월1일",arrayList_sub.get(1));
        arrayList_super.add(dosageDetailSuper);
        arrayList_super.add(dosageDetailSuper);

        dosageDetailSuperAdapter.notifyDataSetChanged();

    }

    public void DBTest(){
        ArrayList<String> history_date;
        ArrayList<String> history_time;
        ArrayList<String> history_routine;
        history_date = new ArrayList<>();
        history_time = new ArrayList<>();
        history_routine = new ArrayList<>();

        int count =0;


        helper = new DBHelper(DosageDetailMain.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT DISTINCT(HISTORY_DATE) FROM MEDI_HISTORY " +
                "WHERE HISTORY_MEDI_NAME ='" + history_mediname+ "'", null);

        int recordCount = cursor.getCount();
        //System.out.println("날짜의 갯수는?"+String.valueOf(recordCount));




        for(int i=0; i<recordCount; i++){
            int image_id;
            cursor.moveToNext();
            arrayList_sub.add(new ArrayList<>());
            history_date.add(cursor.getString(0)); // 날짜 추가
            System.out.println("날짜는?"+history_date.get(i));

            //서브 넣기
            Cursor cursor2 = database.rawQuery("SELECT HISTORY_TIME FROM MEDI_HISTORY " +
                    "WHERE HISTORY_MEDI_NAME='"+history_mediname+"'AND HISTORY_DATE='"+history_date.get(i)+"'", null);
            int recordCount2 = cursor2.getCount();
            System.out.println("해당 날짜의 복용 내역의 갯수는"+ String.valueOf(recordCount2));

            Cursor cursor3 = database.rawQuery("SELECT HISTORY_ROUTINE FROM MEDI_HISTORY " +
                    "WHERE HISTORY_MEDI_NAME='"+history_mediname+"'AND HISTORY_DATE='"+history_date.get(i)+"'", null);
            for (int j=0; j<recordCount2; j++){
                cursor2.moveToNext();
                cursor3.moveToNext();

                history_time.add(cursor2.getString(0));
                history_routine.add(cursor3.getString(0));
                System.out.println("루틴은?" + history_routine.get(count) );
                String routine = history_routine.get(count);
                Drawable drawable = getResources().getDrawable(R.drawable.morning_medi);

                //System.out.println("시간은?"+history_time.get(count));
                String time = history_time.get(count);
                if (time != null) {
                    int idx = time.indexOf(":");
                    String hour = time.substring(0,idx);
                    String minute = time.substring(idx+1);
                    System.out.println("시간은:"+hour+"분은"+minute);
                    String ampm;
                    int hour_int = Integer.parseInt(hour);
                    if(hour_int==12) {
                        ampm="오후";
                    }
                    else if(hour_int>12){
                        hour_int%=12;
                        ampm="오후";
                    }
                    else{
                        ampm="오전";
                    }


                    if(routine.equals("아침약")){
                        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.morning_medi,
                                ampm,String.valueOf(hour_int)+"시",minute+"분","복용");
                        arrayList_sub.get(i).add(dosageDetailSub);
                    }
                    else if( routine.equals("점심약") ){
                        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.lunch_medi,
                                ampm,String.valueOf(hour_int)+"시",minute+"분","복용");
                        arrayList_sub.get(i).add(dosageDetailSub);
                    }
                    else {
                        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.dinner_medi,
                                ampm,String.valueOf(hour_int)+"시",minute+"분","복용");
                        arrayList_sub.get(i).add(dosageDetailSub);
                    }



                }
                else{

                    if(routine.equals("아침약")) {
                        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.morning_medi,
                                "", "X", history_time.get(count), "미복용");
                        arrayList_sub.get(i).add(dosageDetailSub);
                    }

                    else if(routine.equals("점심약")) {
                        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.lunch_medi,
                                "", "X", history_time.get(count), "미복용");
                        arrayList_sub.get(i).add(dosageDetailSub);
                    }
                    else{
                        DosageDetailSub dosageDetailSub = new DosageDetailSub(R.drawable.dinner_medi,
                                "", "X", history_time.get(count), "미복용");
                        arrayList_sub.get(i).add(dosageDetailSub);
                    }

                }

                count++;
            }

            DosageDetailSuper dosageDetailSuper = new DosageDetailSuper(history_date.get(i),arrayList_sub.get(i));
            arrayList_super.add(dosageDetailSuper);
            dosageDetailSuperAdapter.notifyDataSetChanged();

        }

    }

    public void BackClick(View view) {
        Intent intent = new Intent(DosageDetailMain.this, DosageHistoryMain.class);
        startActivity(intent);
        DosageDetailMain.this.finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent(DosageDetailMain.this, DosageHistoryMain.class);
        startActivity(intent);
        DosageDetailMain.this.finish();
    }

}