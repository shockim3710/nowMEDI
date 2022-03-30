package com.example.nowmedi.mainpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nowmedi.R;

public class MediDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medi_detail);
    }


    void showDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MediDetail.this)
                .setTitle("")
                .setMessage("약을 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MediDetail.this, "약을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MediDetail.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }); AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    public void MediDeleteClick(View view) {
        showDialog();
    }



    public void BackClick(View view) {
        Intent intent = new Intent(MediDetail.this, DosageList.class);
        startActivity(intent);
        MediDetail.this.finish();
    }
}
