package com.example.nowmedi.alarm;

public class AddTime {

    private int iv_minus;
    private String tv_hour;
    private String tv_minute;

    public AddTime(int iv_minus, String tv_hour, String tv_minute) {
        this.iv_minus = iv_minus;
        this.tv_hour = tv_hour;
        this.tv_minute = tv_minute;
    }

    public int getIv_minus() {
        return iv_minus;
    }

    public void setIv_minus(int iv_minus) {
        this.iv_minus = iv_minus;
    }

    public String getTv_hour() {
        return tv_hour;
    }

    public void setTv_hour(String tv_hour) {
        this.tv_hour = tv_hour;
    }

    public String getTv_minute() {
        return tv_minute;
    }

    public void setTv_minute(String tv_minute) {
        this.tv_minute = tv_minute;
    }
}
