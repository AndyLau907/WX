package com.example.wx.msg.out;

import com.example.wx.handler.MsgTypes;
import com.example.wx.msg.in.InMsg;

public class OutImgMsg extends OutMsg {

    private String mediaId;

    public OutImgMsg(InMsg inMsg) {
        super(inMsg);
        setMsgType(MsgTypes.IMAGE.getType());
    }

    @Override
    public String toString() {
        String template = "<xml>\n" +
                "  <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "  <CreateTime>12345678</CreateTime>\n" +
                "  <MsgType><![CDATA[image]]></MsgType>\n" +
                "  <Image>\n" +
                "    <MediaId><![CDATA[media_id]]></MediaId>\n" +
                "  </Image>\n" +
                "</xml>";
        String xmlStr = template.replace("toUser", toUserName)
                .replace("fromUser", fromUserName)
                .replace("createTime", createTime)
                .replace("msgType", msgType)
                .replace("MediaId", mediaId);
        return xmlStr;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
