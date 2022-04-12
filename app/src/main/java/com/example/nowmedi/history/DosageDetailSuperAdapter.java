package com.example.nowmedi.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nowmedi.R;

import java.util.ArrayList;

public class DosageDetailSuperAdapter  extends RecyclerView.Adapter<DosageDetailSuperAdapter.ItemViewHolder>{

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<DosageDetailSuper> arrayList;

    public DosageDetailSuperAdapter(ArrayList<DosageDetailSuper> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dosage_detail_item1, viewGroup, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DosageDetailSuper dosageDetailSuper = arrayList.get(position);
        holder.tv_history_date.setText(dosageDetailSuper.getTv_history_date());

        // 자식 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rv_sub_detail.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(dosageDetailSuper.getSubItemList().size());

        // 자식 어답터 설정
        DosageDetailSubAdapter dosageDetailSubAdapter = new DosageDetailSubAdapter(dosageDetailSuper.getSubItemList());

        holder.rv_sub_detail.setLayoutManager(layoutManager);
        holder.rv_sub_detail.setAdapter(dosageDetailSubAdapter);
        holder.rv_sub_detail.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_history_date;
        private RecyclerView rv_sub_detail;

        ItemViewHolder(View itemView) {
            super(itemView);
            // 부모 타이틀
            tv_history_date = itemView.findViewById(R.id.tv_history_date);
            // 자식아이템 영역
            rv_sub_detail = itemView.findViewById(R.id.rv_sub_detail);
        }
    }
}
