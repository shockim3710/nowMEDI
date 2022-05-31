package com.example.nowmedi.history;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;

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
                String curName = holder.tv_mediname.getText().toString();

                Intent intent = new Intent(holder.itemView.getContext(), DosageDetailMain.class);
                intent.putExtra("title",curName);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                holder.itemView.getContext().startActivity(intent);

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
