package com.example.nowmedi_oyh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class protector_manage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_manage);

    }

    public void Add_ProtectorClick(View view) {
        Intent intent = new Intent(protector_manage.this, protector_add.class);
        startActivity(intent);
        protector_manage.this.finish();
    }

    public void DosageHistoryClick(View view) {
        Intent intent = new Intent(protector_manage.this, dosage_history.class);
        startActivity(intent);
        protector_manage.this.finish();
    }


}
