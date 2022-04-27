package com.example.nowmedi.mainpage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;
import com.example.nowmedi.alarm.AddTime;
import com.example.nowmedi.alarm.AddTimeAdapter;
import com.example.nowmedi.alarm.AddTimePop;
import com.example.nowmedi.alarm.AlarmReceiver;
import com.example.nowmedi.database.DBHelper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MediDetailModify extends AppCompatActivity {

    int Fullhour,hour,minute;
    String daytime,ampm;
    Date date1,date2;
    Calendar calendar1;
    int count=0;


    private ArrayList<Integer> HourList;
    private ArrayList<Integer> MinuteList;
    private ArrayList<Integer> IDList;
    private ArrayList<Date> Date1List;
    private ArrayList<Date> Date2List;
    private ArrayList<Calendar> calendarArrayList;
    private ArrayList<AddTime> arraylist;


    private AddTimeAdapter addedTimeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private DBHelper helper;
    private SQLiteDatabase db;

    private EditText et_mediname_detail;
    private EditText et_caution;

    private TextView mediName;
    private String clickMediName;
    private TextView tv_seted_date;

    int index= 0;
    int medicine_db_lock=0,alarm_db_lock=0,alarm_lock=0;

    private ArrayList<String> dbAlarmTimeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medi_detail_modify);

        //DB 생성
        helper = new DBHelper(MediDetailModify.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);


        arraylist = new ArrayList<>();
        Date1List = new ArrayList<>();
        Date2List = new ArrayList<>();
        IDList = new ArrayList<>();
        calendarArrayList = new ArrayList<>();

        addedTimeAdapter = new AddTimeAdapter(arraylist);

        this.calendar1 = Calendar.getInstance();

        //리사이클러뷰 설정
        recyclerView = (RecyclerView)findViewById(R.id.rv_addtime);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addedTimeAdapter);

        mediName = (TextView) findViewById(R.id.medi_name_detail);

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        clickMediName = intent.getStringExtra("mediName");

        mediName.setText(clickMediName);


        //에디트 텍스트 정보 가져오기
        EditText editText1 = (EditText) findViewById(R.id.et_mediname_detail);
        EditText editText2 = (EditText) findViewById(R.id.et_caution);
        TextView textView1 = (TextView) findViewById(R.id.tv_seted_date);

        Cursor cursor1 = db.rawQuery("SELECT MEDI_PRODUCT, MEDI_MEMO, MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE " +
                "WHERE MEDI_NAME = '" + clickMediName + "' ", null);

        Cursor cursor2 = db.rawQuery("SELECT ALARM_ROUTINE, ALARM_TIME FROM MEDI_ALARM " +
                "WHERE ALARM_MEDI_NAME = '" + clickMediName + "' ", null);

        while (cursor1.moveToNext()) {
            editText1.setText(cursor1.getString(0));
            editText2.setText(cursor1.getString(1));
            textView1.setText(cursor1.getString(2) + "~" + cursor1.getString(3));
        }

        while (cursor2.moveToNext()) {
            String routine = cursor2.getString(0);
            String time = cursor2.getString(1);

            int idx = time.indexOf(":");


            Fullhour = Integer.parseInt(time.substring(0, idx));
            minute = Integer.parseInt(time.substring(idx+1));
            hour = Fullhour;

            if(Fullhour==12){
                ampm="오후";
            }
            else if(Fullhour>12){
                hour%=12;
                ampm="오후";
            }
            else{
                ampm="오전";
            }

            AddTime addTime = new AddTime(R.drawable.minus_icon, String.valueOf(hour)+"시",
                    String.valueOf(minute)+"분",routine,ampm);
            arraylist.add(addTime);

        }

        addedTimeAdapter.notifyDataSetChanged();


        et_mediname_detail = findViewById(R.id.et_mediname_detail);
        et_caution = findViewById(R.id.et_caution);
        tv_seted_date = findViewById(R.id.tv_seted_date);



    }


    public void TimeSetClick(View v){
        Intent intent = new Intent(this, AddTimePop.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                Fullhour = Integer.parseInt(data.getStringExtra("시간"));
                minute = Integer.parseInt(data.getStringExtra("분"));
                daytime = data.getStringExtra("하루");
                hour = Fullhour;

                if(Fullhour==12){
                    ampm="오후";
                }
                else if(Fullhour>12){
                    hour%=12;
                    ampm="오후";
                }
                else{
                    ampm="오전";
                }
                AddTime addtime = new AddTime(R.drawable.minus_icon,String.valueOf(hour)+"시",
                        String.valueOf(minute)+"분",daytime,ampm);
                arraylist.add(addtime);
                addedTimeAdapter.notifyDataSetChanged();
            }

        }
    }


    public void date_set(View v){
        TextView tv_date = findViewById(R.id.tv_seted_date);
        Calendar calendar;
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("원하시는 기간을 선택하세요").setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();

        materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener
                (new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                        date1  = new Date();
                        date2  = new Date();
//                        String today = simpleDateFormat.format(date2);
//                        Toast.makeText(alarm_main.this, ""+date2, Toast.LENGTH_SHORT).show();

                        date1.setTime(selection.first);
                        if(date1.before(date2)){

                        }
                        date2.setTime(selection.second);

                        String datestring1 = simpleDateFormat.format(date1);
                        String datestring2 = simpleDateFormat.format(date2);

                        tv_date.setText(datestring1 + "~" + datestring2);
                    }
                });
    }



    public void SetAlarm(View v) throws ParseException {
        int is_empty=0;
        TextView tv_date = findViewById(R.id.tv_seted_date);

        if (arraylist.isEmpty()){
            Toast.makeText(this, "복용 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tv_date.getText())){
            Toast.makeText(this, "복용 기간을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
//        else if(TextUtils.isEmpty(et_mediname_detail.getText())){
//            Toast.makeText(this, "약제품명 입력하세요", Toast.LENGTH_SHORT).show();
//        }
//        else if(TextUtils.isEmpty(et_caution.getText())){
//            Toast.makeText(this, "주의사항을 입력하세요", Toast.LENGTH_SHORT).show();
//        }
        else{
            is_empty=1;
        }

        if(is_empty==1){
            MedicineDBModify();
            AlarmDBModify();


            //        this.calendar1.setTime(date1);
//        this.calendar1.set(Calendar.HOUR_OF_DAY, Fullhour);
//        this.calendar1.set(Calendar.MINUTE, minute);
//        this.calendar1.set(Calendar.SECOND, 0);

            // Receiver 설정
            Intent intent = new Intent(this, AlarmReceiver.class);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");

            Intent intent2 = new Intent(MediDetailModify.this, MediDetail.class);
            intent2.putExtra("mediName", clickMediName);
            startActivity(intent2);
            MediDetailModify.this.finish();
            Toast.makeText(MediDetailModify.this, "약 알람이 수정되었습니다.", Toast.LENGTH_SHORT).show();
            overridePendingTransition(0, 0);


            // 알람 설정
//        final int id = (int) System.currentTimeMillis();
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar1.getTimeInMillis(),pendingIntent);





            //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
            helper = new DBHelper(MediDetailModify.this, "newdb.db", null, 1);
            SQLiteDatabase database = helper.getReadableDatabase();



            //Cursor라는 그릇에 목록을 담아주기
            Cursor cursor1 = database.rawQuery("SELECT MEDI_NAME, MEDI_START_DATE, MEDI_END_DATE FROM MEDICINE", null);

            //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
            while (cursor1.moveToNext()) {
                String selectName = cursor1.getString(0);
                String selectDate1 = cursor1.getString(1);
                String selectDate2 = cursor1.getString(2);

                dbAlarmTimeList = new ArrayList<String>();

                Date dbStartDate = format1.parse(selectDate1);
                Date dbEndDate = format1.parse(selectDate2);

                System.out.println("dbStartDate = " + dbStartDate);

                Cursor cursor2 = database.rawQuery("SELECT _id, ALARM_TIME FROM MEDI_ALARM " +
                        "WHERE ALARM_MEDI_NAME = '" + selectName + "' ", null);

                //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
                while (cursor2.moveToNext()) {
                    IDList.add(cursor2.getInt(0));
                    dbAlarmTimeList.add(cursor2.getString(1));

                }
                System.out.println("dbAlarmTimeList = " + dbAlarmTimeList);


                for(int i =0; i<dbAlarmTimeList.size();i++) {

                    int idx = dbAlarmTimeList.get(i).indexOf(":");

//                    int Lastindex = IDList.size();
//                    IDList.add(Lastindex);
//                    System.out.println("Lastindex = " + Lastindex);
//                    Lastindex ++;
                    intent.putExtra("id",IDList.get(i));

                    this.calendar1.setTime(dbStartDate);
                    this.calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dbAlarmTimeList.get(i).substring(0, idx)));
                    this.calendar1.set(Calendar.MINUTE, Integer.parseInt(dbAlarmTimeList.get(i).substring(idx+1)));
                    this.calendar1.set(Calendar.SECOND, 0);

                    calendarArrayList.add(calendar1);
                    Date1List.add(dbStartDate);
                    Date2List.add(dbEndDate);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, IDList.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarArrayList.get(i).getTimeInMillis(),pendingIntent);

                }

            }


            // Toast 보여주기 (알람 시간 표시)
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Toast.makeText(this, "Alarm : " + format.format(calendar1.getTime()), Toast.LENGTH_LONG).show();

        }






    }

    public void MedicineDBModify(){
        String startdate,enddate;
        String mediname, product,caution, date;


//        mediname = et_mediname.getText().toString();
        product = et_mediname_detail.getText().toString();
        caution = et_caution.getText().toString();

        date = tv_seted_date.getText().toString();

        int count=0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
//        startdate=simpleDateFormat.format(date1);
//        enddate=simpleDateFormat.format(date2);
        int idx = date.indexOf("~");


        startdate = date.substring(0, idx);
        enddate = date.substring(idx+1);

        //약이름, 제품명, 메모 저장

//        Toast.makeText(this, "약이름은:"+mediname  +"\n약상세이름은:"+product +"\n주의사항은:"+ caution +
////                "\n시작일은:"+startdate+"\n종료일은"+enddate, Toast.LENGTH_SHORT).show();


        db.execSQL("UPDATE MEDICINE SET MEDI_PRODUCT = '" + product + "'"+
                ", MEDI_MEMO = '" + caution + "'"+
                ", MEDI_START_DATE = '" + startdate + "'"+
                ", MEDI_END_DATE = '" + enddate + "'"+
                " WHERE MEDI_NAME = '" + clickMediName + "'");
    }

    public void AlarmDBModify() {
        String mediname, ampm, hour, minute, routine, time;
        int idx, int_hour, nullcheck = 0;
//        for(int i=0;i<arraylist.size();i++){}

        String sql1 = "DELETE FROM MEDI_ALARM " +
                "WHERE ALARM_MEDI_NAME = '" + clickMediName + "';";

        db.execSQL(sql1);

        for (int i = 0; i < arraylist.size(); i++) {

//            mediname = et_mediname.getText().toString();
            ampm = arraylist.get(i).getAmpm();
            hour = arraylist.get(i).getTv_hour();
            minute = arraylist.get(i).getTv_minute();
            routine = arraylist.get(i).getTv_daytime();

            System.out.println("arraylist1111 = " + routine);


            // 시간 , 분 원래 값으로 복구하기
            idx = hour.indexOf("시");
            hour = hour.substring(0, idx);
            int_hour = Integer.parseInt(hour);

            if (ampm == "오후" && int_hour != 12) {
                int_hour += 12;
                hour = String.valueOf(int_hour);
            } else if (hour.length() < 2) {
                hour = "0" + hour;
            }

            idx = minute.indexOf("분");
            minute = minute.substring(0, idx);
            if (minute.length() < 2) {
                minute = "0" + minute;
            }


            //hh:mm형식으로 만들기
            time = hour + ":" + minute;
//            System.out.println("약이름은:" + mediname + "  루틴은:" + routine + " 시간은" + time);



            db.execSQL("INSERT INTO MEDI_ALARM" +
                    "(ALARM_MEDI_NAME, ALARM_ROUTINE, ALARM_TIME) " +
                    "VALUES ('" + clickMediName + "', '" + routine + "', '" + time + "');");


        }
    }





    public void onBackPressed() {
        Intent intent = new Intent(MediDetailModify.this, MediDetail.class);
        intent.putExtra("mediName", clickMediName);
        startActivity(intent);
        MediDetailModify.this.finish();
        overridePendingTransition(0, 0);

    }

    public void CancleClick(View view) {
        Intent intent = new Intent(MediDetailModify.this, MediDetail.class);
        intent.putExtra("mediName", clickMediName);
        startActivity(intent);
        MediDetailModify.this.finish();
        overridePendingTransition(0, 0);

    }
}