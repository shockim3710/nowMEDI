package com.example.nowmedi_oyh;

import java.util.ArrayList;
import java.util.List;

public class DosageDetailSuper {
    private String tv_history_date;
    private ArrayList<DosageDetailSub> subItemList;

    public DosageDetailSuper(String tv_history_date, ArrayList<DosageDetailSub> subItemList) {
        this.tv_history_date = tv_history_date;
        this.subItemList = subItemList;
    }

    public String getTv_history_date() {
        return tv_history_date;
    }

    public void setTv_history_date(String tv_history_date) {
        this.tv_history_date = tv_history_date;
    }

    public ArrayList<DosageDetailSub> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(ArrayList<DosageDetailSub> subItemList) {
        this.subItemList = subItemList;
    }
}
