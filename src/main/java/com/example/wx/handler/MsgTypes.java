package com.example.wx.handler;

public enum MsgTypes {

    TEXT("text"),
    IMAGE("image"),
    LINK("link"),
    EVENT("event"),
    ARTICLE("news");

    private String type;

    private MsgTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
