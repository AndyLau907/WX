package com.example.wx.msg.in;

import java.util.Map;

public class InImageMsg extends InMsg {
    private String picUrl;
    private String mediaId;

    public InImageMsg(Map<String, String> map) {
        super(map);
        picUrl = map.get("PicUrl");
        mediaId = map.get("MediaId");
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

}
