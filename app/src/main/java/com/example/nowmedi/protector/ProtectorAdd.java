package com.example.nowmedi.protector;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_add);

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

        db.execSQL("INSERT INTO PROTECTOR" +
                "(PROTECTOR_NAME, PROTECTOR_PHONE_NUM, PROTECTOR_MESSAGE) " +
                "VALUES ('" + name + "', '" + num + "', '" + msg + "');");

        Intent intent = new Intent(ProtectorAdd.this, ProtectorManage.class);
        startActivity(intent);
        ProtectorAdd.this.finish();

        Toast.makeText(ProtectorAdd.this, "보호자가 추가되었습니다.", Toast.LENGTH_SHORT).show();
    }


    public void onBackPressed() {
        Intent intent = new Intent(ProtectorAdd.this, ProtectorManage.class);
        startActivity(intent);
        ProtectorAdd.this.finish();
    }


}
