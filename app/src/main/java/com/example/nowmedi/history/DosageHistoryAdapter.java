package com.example.nowmedi.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;
import com.example.nowmedi.database.DBHelper;
import com.example.nowmedi.mainpage.MediDetailAdapter;

import java.util.ArrayList;

public class DosageHistoryAdapter extends RecyclerView.Adapter<DosageHistoryAdapter.CustomViewHolder> {
    private ArrayList<DosageHistory> arrayList;

    public DosageHistoryAdapter(ArrayList<DosageHistory> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dosage_histroy_item,parent,false);
        CustomViewHolder holder =  new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.pb_dosage.setProgress(arrayList.get(position).getPb_dosage());
        holder.iv_medicine.setImageResource(arrayList.get(position).getIv_medicine());
        holder.tv_mediname.setText(arrayList.get(position).getTv_mediname());
        holder.tv_percent.setText(arrayList.get(position).getTv_percent());
        holder.tv_numerator.setText(arrayList.get(position).getTv_numerator());
        holder.tv_denominator.setText(arrayList.get(position).getTv_denominator());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove(holder.getAdapterPosition());
                String curName = holder.tv_mediname.getText().toString();
                //Toast.makeText(v.getContext(), curName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), DosageDetailMain.class);
                intent.putExtra("title",curName);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                holder.itemView.getContext().startActivity(intent);


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DBHelper helper;
                SQLiteDatabase db;
                helper = new DBHelper(view.getContext(), "newdb.db", null, 1);
                db = helper.getWritableDatabase();
                helper.onCreate(db);

                String curName = holder.tv_mediname.getText().toString();


                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(view.getContext())
                        .setTitle("")
                        .setMessage("내역을 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Cursor라는 그릇에 목록을 담아주기
                                Cursor cursor = db.rawQuery("SELECT COUNT(MEDI_NAME) FROM MEDICINE " +
                                        "WHERE MEDI_NAME = '" + curName + "' ", null);

                                //리스트뷰에 목록 채워주는 도구인 adapter준비
                                MediDetailAdapter adapter = new MediDetailAdapter();

                                //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
                                int mediCount = 0;
                                while (cursor.moveToNext()) {
                                    mediCount = cursor.getInt(0);
                                }


                                Toast.makeText(view.getContext(), "내역을 삭제하였습니다.", Toast.LENGTH_SHORT).show();

                                String sql1 = "DELETE FROM  MEDI_HISTORY " +
                                        "WHERE HISTORY_MEDI_NAME = '" + curName + "';";
                                db.execSQL(sql1);
                                remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeRemoved(holder.getAdapterPosition(), arrayList.size());

//                                if (mediCount != 0) {
//                                    Toast.makeText(view.getContext(), "약 알람 목록에 있으므로 삭제하지 못하였습니다.", Toast.LENGTH_SHORT).show();
//
//                                }
//                                else {
//                                    Toast.makeText(view.getContext(), "내역을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
//
//                                    String sql1 = "DELETE FROM  MEDI_HISTORY " +
//                                            "WHERE HISTORY_MEDI_NAME = '" + curName + "';";
//                                    db.execSQL(sql1);
//                                    remove(holder.getAdapterPosition());
//                                    notifyItemRemoved(holder.getAdapterPosition());
//                                    notifyItemRangeRemoved(holder.getAdapterPosition(), arrayList.size());
//
//                                }
                            }

                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(view.getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }); AlertDialog msgDlg = msgBuilder.create();
                msgDlg.show();

                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ProgressBar pb_dosage;
        protected ImageView iv_medicine;
        protected TextView tv_mediname;
        protected TextView tv_percent;
        protected TextView tv_numerator;
        protected TextView tv_denominator;




        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pb_dosage = (ProgressBar) itemView.findViewById(R.id.pb_dosage);
            this.iv_medicine =(ImageView) itemView.findViewById(R.id.iv_medicine);
            this.tv_mediname =(TextView) itemView.findViewById(R.id.tv_mediname);
            this.tv_percent=(TextView) itemView.findViewById(R.id.tv_percent);
            this.tv_numerator =(TextView) itemView.findViewById(R.id.tv_numerator);
            this.tv_denominator=(TextView) itemView.findViewById(R.id.tv_denominator);
        }

    }
}
