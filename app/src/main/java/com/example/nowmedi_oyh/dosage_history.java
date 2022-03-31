package com.example.nowmedi_oyh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class dosage_history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosage_history);

    }

    public void ProtectorClick(View view) {
        Intent intent = new Intent(dosage_history.this, protector_manage.class);
        startActivity(intent);
        dosage_history.this.finish();
    }



}
