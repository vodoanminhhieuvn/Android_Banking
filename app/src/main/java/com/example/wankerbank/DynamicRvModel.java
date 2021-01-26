package com.example.wankerbank;

public class DynamicRvModel {

    public DynamicRvModel(String type, String date, String fund) {
        this.type = type;
        this.date = date;
        this.fund = fund;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getFund() {
        return fund;
    }

    String type, date, fund;
}
