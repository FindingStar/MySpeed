package com.example.myspeed.ui.message.model;

public class RemindModel {
    private final int index=counter;
    private static int counter;

    private String remindText;
    private String remindTime;


    RemindModel(){
        // test
        remindText=" remindText"+index;
        remindTime=" remind time "+index;
        counter++;
    }

    public String getRemindText() {
        return remindText;
    }

    public void setRemindText(String remindText) {
        this.remindText = remindText;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }
}
