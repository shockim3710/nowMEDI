package com.example.nowmedi_oyh;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DosageDetailSubAdapter extends RecyclerView.Adapter<DosageDetailSubAdapter.SubItemViewHolder>{
    private ArrayList<DosageDetailSub> arraylist;

    DosageDetailSubAdapter(ArrayList<DosageDetailSub> arraylist) {
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dosage_detail_item2, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int position) {
        DosageDetailSub dosageDetailSub = arraylist.get(position);
        subItemViewHolder.iv_daytime.setImageResource(dosageDetailSub.getIv_daytime());
        subItemViewHolder.tv_daytime_detail.setText(dosageDetailSub.getTv_daytime_detail());
        subItemViewHolder.tv_hour_detail.setText(dosageDetailSub.getTv_hour_detail());
        subItemViewHolder.tv_minute_detail.setText(dosageDetailSub.getTv_minute_detail());
        subItemViewHolder.tv_dosage_detail.setText(dosageDetailSub.getTv_dosage_detail());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_daytime;
        private TextView tv_daytime_detail;
        private TextView tv_hour_detail;
        private TextView tv_minute_detail;
        private TextView tv_dosage_detail;


        SubItemViewHolder(View itemView) {
            super(itemView);
            iv_daytime = itemView.findViewById(R.id.iv_daytime_detail);
            tv_daytime_detail = itemView.findViewById(R.id.tv_daytime_detail);
            tv_hour_detail = itemView.findViewById(R.id.tv_hour_detail);
            tv_minute_detail = itemView.findViewById(R.id.tv_minute_detail);
            tv_dosage_detail = itemView.findViewById(R.id.tv_dosage_detail);
        }
    }


}
