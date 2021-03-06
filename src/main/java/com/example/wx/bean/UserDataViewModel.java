package com.example.wx.bean;

import java.text.SimpleDateFormat;

public class UserDataViewModel extends UserData {
    private String startDayStr;
    private String endDayStr;

    public String getStartDayStr() {
        return startDayStr;
    }

    public void setStartDayStr(String startDayStr) {
        this.startDayStr = startDayStr;
    }

    public String getEndDayStr() {
        return endDayStr;
    }

    public void setEndDayStr(String endDayStr) {
        this.endDayStr = endDayStr;
    }

    public void dateToStr(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy.MM.dd");
        startDayStr=sf.format(getStartDay());
        endDayStr=sf.format(getEndDay());
    }
}
