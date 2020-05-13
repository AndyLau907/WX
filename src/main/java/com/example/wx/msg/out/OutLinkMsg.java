package com.example.wx.msg.out;

import com.example.wx.handler.MsgTypes;
import com.example.wx.msg.in.InMsg;

public class OutLinkMsg extends OutMsg {

    private String url;
    private String title;
    private String description;

    public OutLinkMsg(InMsg inMsg) {
        super(inMsg);
        this.msgType = MsgTypes.LINK.getType();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
