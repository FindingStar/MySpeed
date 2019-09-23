package com.example.myspeed.ui.message.model;

public class Conversation {
    private String time;
    private String nickName;
    private String mes;

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
