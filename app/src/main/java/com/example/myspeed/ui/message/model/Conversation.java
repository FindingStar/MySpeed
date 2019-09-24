package com.example.myspeed.ui.message.model;

public class Conversation {
    private final int index=counter;
    private static int counter;

    private String time;
    private String nickName;
    private String mes;


    Conversation(){
        // test
        time=" time "+index;
        nickName=" nick name "+index;
        mes=" mes "+index;
        counter++;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getTime() {
        return time;
    }

    public String getNickName() {
        return nickName;
    }

    public String getMes() {
        return mes;
    }

}
