package com.example.nowmedi_oyh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medi_history);
    }

    public void ProtectorClick(View view) {
        Intent intent = new Intent(MainActivity.this, protector_manage.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

}