package com.example.nowmedi.alarm;

public class AddTime {

    private int iv_minus;
    private String tv_hour;
    private String tv_minute;
    private String tv_daytime;
    private String ampm;

    public AddTime(int iv_minus, String tv_hour, String tv_minute, String tv_daytime, String ampm) {
        this.iv_minus = iv_minus;
        this.tv_hour = tv_hour;
        this.tv_minute = tv_minute;
        this.tv_daytime = tv_daytime;
        this.ampm = ampm;
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

    public String getTv_daytime() {
        return tv_daytime;
    }

    public void setTv_daytime(String tv_daytime) {
        this.tv_daytime = tv_daytime;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }
}
