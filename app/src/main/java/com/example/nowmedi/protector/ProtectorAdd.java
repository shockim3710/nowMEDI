package com.example.nowmedi.protector;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.mainpage.DosageList;
import com.example.nowmedi.mainpage.MediDetail;

public class ProtectorAdd extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;
    private String addPhoneCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_add);

        EditText editText = (EditText) findViewById(R.id.protector_phone);

// 연락처 입력시 하이픈(-) 자동 입력
        editText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        helper = new DBHelper(ProtectorAdd.this, "newdb.db", null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

    }

    public void CancleClick(View view) {
        Intent intent = new Intent(ProtectorAdd.this, ProtectorManage.class);
        startActivity(intent);
        ProtectorAdd.this.finish();
    }

    public void ProtectorAddClick(View view) {
        EditText protectorName = (EditText)findViewById(R.id.protector_name);
        EditText protectorPhone = (EditText)findViewById(R.id.protector_phone);
        EditText protectorMsg = (EditText)findViewById(R.id.protector_msg);

        String name = protectorName.getText().toString();
        String num = protectorPhone.getText().toString();
        String msg = protectorMsg.getText().toString();

        SQLiteDatabase database = helper.getReadableDatabase();


        if(name.length()==0){
            Toast.makeText(getApplicationContext(),"보호자 이름을 입력해주세요.",Toast.LENGTH_LONG).show();
        } else if(num.length()==0){
            Toast.makeText(getApplicationContext(),"보호자 전화번호를 입력해주세요.",Toast.LENGTH_LONG).show();
        } else if(msg.length()==0){
            Toast.makeText(getApplicationContext(),"전송 문자를 입력해주세요.",Toast.LENGTH_LONG).show();
        } else{

            Cursor addPhone = database.rawQuery("SELECT PROTECTOR_PHONE_NUM FROM PROTECTOR " +
                    "WHERE PROTECTOR_PHONE_NUM = '" + num + "' ", null);

            while (addPhone.moveToNext()) {
                addPhoneCheck = addPhone.getString(0);
            }

            if(num.equals(addPhoneCheck)){
                Toast.makeText(getApplicationContext(),"입력한 전화번호는 이미 저장되어 있습니다.",Toast.LENGTH_LONG).show();
            } else{
                db.execSQL("INSERT INTO PROTECTOR" +
                        "(PROTECTOR_NAME, PROTECTOR_PHONE_NUM, PROTECTOR_MESSAGE) " +
                        "VALUES ('" + name + "', '" + num + "', '" + msg + "');");

                Intent intent = new Intent(ProtectorAdd.this, ProtectorManage.class);
                startActivity(intent);
                ProtectorAdd.this.finish();

                Toast.makeText(ProtectorAdd.this, "보호자가 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void onBackPressed() {
        Intent intent = new Intent(ProtectorAdd.this, ProtectorManage.class);
        startActivity(intent);
        ProtectorAdd.this.finish();
    }


}
