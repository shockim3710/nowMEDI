package com.example.nowmedi_oyh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class protector_add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_add);

    }

    public void cancle_click(View view) {
        Intent intent = new Intent(protector_add.this, protector_manage.class);
        startActivity(intent);
        protector_add.this.finish();
    }

}
