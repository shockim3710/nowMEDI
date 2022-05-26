package com.example.nowmedi.alarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.nowmedi.R;
import com.example.nowmedi.database.DBHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class AlarmGo extends AppCompatActivity{
    private final int RECIEVE_MESSAGE = 1;
    private MediaPlayer mediaPlayer;
    int id,count,routine;
    private DBHelper helper;
    private SQLiteDatabase db;
    private TextView tv_alarm_count,tv_alarm_message,tv_alarm_day,tv_alarm_ampm,tv_alarm_time;
    private Long mLastClickTime = 0L;


    TextView mTvBluetoothStatus;
    TextView mTvReceiveData;
    TextView mTvSendData;
    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnConnect;
    Button mBtnSendData;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_READ = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        count = intent.getIntExtra("count",0);
        setContentView(R.layout.alarm_go);
        create_screen();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if(count<2) {
            //Toast.makeText(this, "id="+id+" count="+count, Toast.LENGTH_LONG).show();
            count++;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND,0);
            Intent repeat_intent = new Intent(this, AlarmReceiver.class);
            repeat_intent.putExtra("id", id);
            repeat_intent.putExtra("count",count);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            //알람 설정
            setVolumeControlStream (AudioManager.STREAM_MUSIC);
            AudioManager audio = null;
            audio = (AudioManager) getSystemService(this.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC,
                    (int)(audio.getStreamVolume(AudioManager.STREAM_MUSIC) + 1), // 10씩 늘림, 알람볼륨조절
                    0);

            // 알람음 재생
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
            this.mediaPlayer.setVolume(1.0f,1.0f);
            this.mediaPlayer.start();
            this.mediaPlayer.setLooping(true);

        }
        else{
            Add_Nextday_alarm();
            IS_NOT_CLICKED();
            finishAndRemoveTask();
            // 내역 db 추가 해야됨
        }


        find_routine();

        try {
            BluetoothMedicineBox();

        } catch (IOException e) {
            e.printStackTrace();
        }


//        final Handler handler = new Handler(Looper.getMainLooper());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // 코드 작성
//                    Toast.makeText(AlarmGo.this, "5초", Toast.LENGTH_SHORT).show();
//                    handler.postDelayed(this,5000);
//                }
//            }, 0, 5000);
//        }

//        Timer timer =  new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//                Toast.makeText(AlarmGo.this, "aa", Toast.LENGTH_SHORT).show();
//            }
//        };
//        timer.schedule(timerTask,0,5000);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.mBluetoothHandler != null){
            mBluetoothHandler.removeMessages(0);
        }
        if (this.mThreadConnectedBluetooth != null) {
            mThreadConnectedBluetooth.cancel();
        }
        // MediaPlayer release
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }



    public void alarm_close(View v) throws IOException {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        BluetoothMedicineBox();




//        Add_Nextday_alarm(); //다음날 알람 추가
//
//        if (this.mediaPlayer.isPlaying()) { //소리 끄기
//            this.mediaPlayer.stop();
//            this.mediaPlayer.release();
//            this.mediaPlayer = null;
//        }
//        IS_CLICKED(); //내역 저장
//        finishAndRemoveTask();
    }


    public void IS_CLICKED(){
        String mediName, mediRoutine;
        Date today= new Date();
        String date, time;

        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        Cursor cursor2 = database.rawQuery("SELECT ALARM_ROUTINE FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);

        cursor1.moveToNext();
        cursor2.moveToNext();

        mediName=cursor1.getString(0);
        mediRoutine=cursor2.getString(0);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = simpleDateFormat.format(today);

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        time = simpleDateFormat1.format(today);


        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
                +"('"+ mediName + "','" +date +"','"+time+"','"+ mediRoutine+"')"
        );

    }

    public void IS_NOT_CLICKED(){
        String mediName, mediRoutine;
        Date today= new Date();
        String date;

        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        Cursor cursor2 = database.rawQuery("SELECT ALARM_ROUTINE FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);

        cursor1.moveToNext();
        cursor2.moveToNext();

        mediName=cursor1.getString(0);
        mediRoutine=cursor2.getString(0);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = simpleDateFormat.format(today);


        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        db.execSQL("INSERT INTO MEDI_HISTORY (HISTORY_MEDI_NAME, HISTORY_DATE, HISTORY_TIME, HISTORY_ROUTINE) VALUES"
                +"('"+ mediName + "','" +date +"',"+"NULL"+",'"+ mediRoutine+"')"
        );
    }

    public void Add_Nextday_alarm(){
        String st_time;
        int idx=0,hour=0,minute=0;
        count=0;
        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_TIME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        cursor1.moveToNext();
        st_time=cursor1.getString(0);

        idx = st_time.indexOf(":");
        hour= Integer.parseInt(st_time.substring(0,idx));
        minute = Integer.parseInt(st_time.substring(idx+1));


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        Intent repeat_intent = new Intent(this, AlarmReceiver.class);
        repeat_intent.putExtra("id", id);
        repeat_intent.putExtra("count",count);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, repeat_intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void create_screen(){
        tv_alarm_count=(TextView) findViewById(R.id.tv_alarm_count);
        tv_alarm_message=(TextView) findViewById(R.id.tv_alarm_message);
        tv_alarm_day=(TextView) findViewById(R.id.tv_alarm_day);
        tv_alarm_ampm=(TextView) findViewById(R.id.tv_alarm_ampm);
        tv_alarm_time=(TextView) findViewById(R.id.tv_alarm_time);

        String count,message,day,ampm,time;
        count="1";message="테스트";day="1월 2일";ampm="오전";time="10:25";

        count=String.valueOf(this.count)+"번째 알람";
        //알람 횟수 출력


        String mediName;



        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_MEDI_NAME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        cursor1.moveToNext();


        mediName=cursor1.getString(0);
        message = mediName +" 드셔야할 시간입니다.";
        cursor1.close();
        // 알람 문구 출력


        Date today= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        String date,week;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일");
        date = simpleDateFormat.format(today);
        week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.KOREAN);

        day= date +" "+ week;

        cursor1 = database.rawQuery("SELECT ALARM_TIME FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        cursor1.moveToNext();
        time = cursor1.getString(0);
        cursor1.close();


        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        try {
            today=simpleDateFormat2.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("hh:mm");
        time = simpleDateFormat3.format(today);

        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("a");
        ampm = simpleDateFormat4.format(today);

        tv_alarm_count.setText(count);
        tv_alarm_message.setText(message);
        tv_alarm_day.setText(day);
        tv_alarm_ampm.setText(ampm);
        tv_alarm_time.setText(time);

    }





    public void BluetoothMedicineBox() throws IOException {



        if (mBluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }



                mBluetoothHandler = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        if (msg.what == BT_MESSAGE_READ) {
                            String readMessage = null;
                            try {
                                readMessage = new String((byte[]) msg.obj, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            mTvReceiveData.setText(readMessage);
                        }
                    }
                };




                connectSelectedDevice("Medicine_Box");

                if(mBluetoothSocket == null) {
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
                    mBluetoothSocket.connect();
                    mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
                    mThreadConnectedBluetooth.start();
                    mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
                }


                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("1");
                    System.out.println("LED가 켜집니다.");

                    mThreadConnectedBluetooth.write(Integer.toString(routine));


                    mBluetoothHandler = new Handler() {
                        public void handleMessage(android.os.Message msg) {
                            if (msg.what == BT_MESSAGE_READ) {
                                String readMessage = null;

                                byte[] readBuf = (byte[]) msg.obj;
                                readMessage = new String(readBuf, 0, msg.arg1);
//                                try {
//
//                                    readMessage = new String((byte[]) msg.obj, "UTF-8");
//
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
                                System.out.println("버튼을 눌렀습니다.!!!!!!!!!!!" + readMessage);
                                mThreadConnectedBluetooth.write("0");
                                System.out.println("LED가 꺼집니다.");
                            }
                        }
                    };






//                    mBluetoothHandler = new Handler() {
//                        public void handleMessage(android.os.Message msg) {
//                            if (msg.what == BT_MESSAGE_READ) {
//                                String readMessage = null;
//                                try {
//                                    readMessage = new String((byte[]) msg.obj, "UTF-8");
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//                                System.out.println("버튼을 눌렀습니다.");
//
//                                if(readMessage == "2") {
//                                    mThreadConnectedBluetooth.write("0");
//                                    System.out.println("LED가 꺼집니다.");
//                                }
//
//                            }
//                        }
//                    };












                }





//                // The Handler that gets information back from the BluetoothChatService
//                final Handler mHandler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        switch (msg.what) {
//                            case MESSAGE_READ:
//                                byte[] readBuf = (byte[]) msg.obj;
//                                String strIncom = new String(readBuf, 0, msg.arg1);
//
//                                if (strIncom.equals("2")) {
//                                    mThreadConnectedBluetooth.write("0");
//                                }
//
//                                //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
//                                break;
//                        }
//                    }
//                };






            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }



    }



    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }


    void connectSelectedDevice(String selectedDeviceName) {
//        boolean checkBluetoothName = false;

        for (BluetoothDevice tempDevice : mPairedDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
//                checkBluetoothName = true;
//                System.out.println("tempDevice = " + tempDevice);

                break;
            }


        }


        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
//            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }

//        System.out.println("checkBluetoothName = " + checkBluetoothName);
//
//        if(checkBluetoothName == false) {
//            Toast.makeText(getApplicationContext(), "메디슨 박스와 연결되지 않았습니다. 스마트폰에 등록되어 있는지 메디슨 박스의 블루투스를 확인해주세요.", Toast.LENGTH_LONG).show();
//            System.out.println("메디슨 박스와 연결되지 않았습니다. 스마트폰에 등록되어 있는지 메디슨 박스의 블루투스를 확인해주세요.");
//        }
//        else {
//            try {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
//                mBluetoothSocket.connect();
//                mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
//                mThreadConnectedBluetooth.start();
//                mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
//            } catch (IOException e) {
////            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
    }








    public void find_routine(){
        helper = new DBHelper(AlarmGo.this, "newdb.db", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor1 = database.rawQuery("SELECT ALARM_ROUTINE FROM MEDI_ALARM WHERE _id ='"+ id+ "'" , null);
        cursor1.moveToNext();
        String rt=cursor1.getString(0);

        if(rt.equals("아침약")) {
            routine=3;
        }
        else if(rt.equals("점심약")){
            routine=4;
        }
        else if(rt.equals("저녁약")){
            routine=5;
        }
        cursor1.close();
    }



}

