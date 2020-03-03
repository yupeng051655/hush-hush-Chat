package com.example.hushhushchat;

public class Messages {


    private String data, msg, name, time;

    public Messages(){

    }

    public Messages(String data, String msg, String name, String time) {
        this.data = data;
        this.msg = msg;
        this.name = name;
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
