package com.example.wx.handler;

import com.example.wx.msg.event.InClickEvent;
import com.example.wx.msg.event.InSubscribeEvent;
import com.example.wx.msg.in.InImageMsg;
import com.example.wx.msg.in.InLinkMsg;
import com.example.wx.msg.in.InTextMsg;
import com.example.wx.msg.out.OutArticleMsg;
import com.example.wx.msg.out.OutImgMsg;
import com.example.wx.msg.out.OutMsg;
import com.example.wx.msg.out.OutTextMsg;

public class DefaultHandler extends MsgHandler {

    @Override
    protected OutMsg handlerTextMsg(InTextMsg inTextMsg) {
        OutTextMsg outTextMsg = new OutTextMsg(inTextMsg);
        OutArticleMsg articleMsg = new OutArticleMsg(inTextMsg);
        String content = inTextMsg.getContent().replaceAll(" ", "");
        if (content.equals("1")) {
            //回复商品信息图文消息
            articleMsg.setTitle("本店商品大合集~快来看看吧");
            articleMsg.setDescription("");
            articleMsg.setUrl("http://119.45.120.159/WX/index.html");
            articleMsg.setPicurl("http://119.45.120.159/WX/index.html");
            return articleMsg;
        } else if (content.equals("2")) {
            //回复联系方式 文本消息
            outTextMsg.setContent("暂时没有情报啊~~");
            return outTextMsg;
        } else if (content.equals("3")) {
            //回复淘宝链接 图文消息
            articleMsg.setTitle("淘宝店铺");
            articleMsg.setDescription("暂时没有情报啊~只能给你百度了~");
            articleMsg.setUrl("https://www.baidu.com");
            articleMsg.setPicurl("https://www.baidu.com");
            return articleMsg;
        } else {
            outTextMsg.setContent("~~成都源林花卉期待您的光临~~！\n" +
                    "------------------------------\n" +
                    "1.商品信息展示\n" +
                    "2.联系我们\n" +
                    "3.线上店铺\n" +
                    "回复以上选项选择功能\n");
        }

        return outTextMsg;
    }

    @Override
    protected OutMsg handlerLinkMsg(InLinkMsg inLinkMsg) {
        return null;
    }

    @Override
    protected OutMsg handlerImageMsg(InImageMsg inImageMsg) {
        OutImgMsg outImgMsg = new OutImgMsg(inImageMsg);
        outImgMsg.setMediaId(inImageMsg.getMediaId());
        return outImgMsg;
    }

    @Override
    protected OutMsg handlerClickEvent(InClickEvent inClickEvent) {
        String eventKey = inClickEvent.getEventKey();
        OutMsg outMsg = null;
        if (eventKey.equals("button1")) {
            OutTextMsg outTextMsg = new OutTextMsg(inClickEvent);
            outTextMsg.setMsgType(MsgTypes.TEXT.getType());
            outTextMsg.setContent("抓紧筹备中");
            outMsg = outTextMsg;
        }
        return outMsg;
    }

    @Override
    protected OutMsg handlerViewEvent(InClickEvent inClickEvent) {
        return null;
    }

    //关注公众号时的事件
    @Override
    protected OutMsg handlerSubscribeEvent(InSubscribeEvent inSubscribeEvent) {

        OutTextMsg outTextMsg = new OutTextMsg(inSubscribeEvent);
        outTextMsg.setMsgType(MsgTypes.TEXT.getType());
        outTextMsg.setContent("感谢您的关注,成都源林花卉期待您的光临！\n" +
                "回复选项选择功能：\n" +
                "1.商品信息展示\n" +
                "2.联系我们\n" +
                "3.线上店铺\n" +
                "回复以上选项选择功能\n");
        return outTextMsg;
    }
}
