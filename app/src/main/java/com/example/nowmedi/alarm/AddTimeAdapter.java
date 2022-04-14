package com.example.nowmedi.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;

import java.util.ArrayList;

public class AddTimeAdapter extends RecyclerView.Adapter<AddTimeAdapter.CustomViewHolder> {

    private ArrayList<AddTime> arrayList;

    public AddTimeAdapter(ArrayList<AddTime> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addtime_list,parent,false);
        CustomViewHolder holder =  new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.iv_minus.setImageResource(arrayList.get(position).getIv_minus());
        holder.tv_Hour.setText(arrayList.get(position).getTv_hour());
        holder.tv_minute.setText(arrayList.get(position).getTv_minute());
        holder.tv_daytime.setText(arrayList.get(position).getTv_daytime());
        holder.tv_ampm.setText(arrayList.get(position).getAmpm());

        holder.iv_minus.setTag(position);
        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
            }
        })
        ;


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
        protected ImageView iv_minus;
        protected TextView tv_Hour;
        protected TextView tv_minute;
        protected TextView tv_daytime;
        protected TextView tv_ampm;





        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_minus =(ImageView) itemView.findViewById(R.id.iv_minus);
            this.tv_Hour =(TextView) itemView.findViewById(R.id.tv_hour);
            this.tv_minute=(TextView) itemView.findViewById(R.id.tv_minute);
            this.tv_daytime=(TextView) itemView.findViewById(R.id.tv_daytime);
            this.tv_ampm=(TextView) itemView.findViewById(R.id.tv_ampm);
        }

    }
}
