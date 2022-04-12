package com.example.nowmedi_oyh;

public class DosageHistory {
    private int pb_dosage;
    private int iv_medicine;
    private String tv_mediname;
    private String tv_percent;
    private String tv_numerator;
    private String tv_denominator;

    public DosageHistory(int pb_dosage, int iv_medicine, String tv_mediname, String tv_percent, String tv_numerator, String tv_denominator) {
        this.pb_dosage = pb_dosage;
        this.iv_medicine = iv_medicine;
        this.tv_mediname = tv_mediname;
        this.tv_percent = tv_percent;
        this.tv_numerator = tv_numerator;
        this.tv_denominator = tv_denominator;
    }


    public int getPb_dosage() {
        return pb_dosage;
    }

    public void setPb_dosage(int pb_dosage) {
        this.pb_dosage = pb_dosage;
    }

    public int getIv_medicine() {
        return iv_medicine;
    }

    public void setIv_medicine(int iv_medicine) {
        this.iv_medicine = iv_medicine;
    }

    public String getTv_mediname() {
        return tv_mediname;
    }

    public void setTv_mediname(String tv_mediname) {
        this.tv_mediname = tv_mediname;
    }

    public String getTv_percent() {
        return tv_percent;
    }

    public void setTv_percent(String tv_percent) {
        this.tv_percent = tv_percent;
    }

    public String getTv_numerator() {
        return tv_numerator;
    }

    public void setTv_numerator(String tv_numerator) {
        this.tv_numerator = tv_numerator;
    }

    public String getTv_denominator() {
        return tv_denominator;
    }

    public void setTv_denominator(String tv_denominator) {
        this.tv_denominator = tv_denominator;
    }
}
