package com.example.nowmedi_oyh;

public class DosageDetailSub {

    private  int iv_daytime;
    private  String tv_daytime_detail;
    private  String tv_hour_detail;
    private  String tv_minute_detail;
    private  String tv_dosage_detail;


    public DosageDetailSub(int iv_daytime, String tv_daytime_detail, String tv_hour_detail, String tv_minute_detail, String tv_dosage_detail) {
        this.iv_daytime = iv_daytime;
        this.tv_daytime_detail = tv_daytime_detail;
        this.tv_hour_detail = tv_hour_detail;
        this.tv_minute_detail = tv_minute_detail;
        this.tv_dosage_detail = tv_dosage_detail;
    }

    public int getIv_daytime() {
        return iv_daytime;
    }

    public void setIv_daytime(int iv_daytime) {
        this.iv_daytime = iv_daytime;
    }

    public String getTv_daytime_detail() {
        return tv_daytime_detail;
    }

    public void setTv_daytime_detail(String tv_daytime_detail) {
        this.tv_daytime_detail = tv_daytime_detail;
    }

    public String getTv_hour_detail() {
        return tv_hour_detail;
    }

    public void setTv_hour_detail(String tv_hour_detail) {
        this.tv_hour_detail = tv_hour_detail;
    }

    public String getTv_minute_detail() {
        return tv_minute_detail;
    }

    public void setTv_minute_detail(String tv_minute_detail) {
        this.tv_minute_detail = tv_minute_detail;
    }

    public String getTv_dosage_detail() {
        return tv_dosage_detail;
    }

    public void setTv_dosage_detail(String tv_dosage_detail) {
        this.tv_dosage_detail = tv_dosage_detail;
    }
}
