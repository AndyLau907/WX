package com.example.wx.handler;

import com.example.wx.msg.event.InClickEvent;
import com.example.wx.msg.event.InSubscribeEvent;
import com.example.wx.msg.in.InImageMsg;
import com.example.wx.msg.in.InLinkMsg;
import com.example.wx.msg.in.InTextMsg;
import com.example.wx.msg.out.OutImgMsg;
import com.example.wx.msg.out.OutMsg;
import com.example.wx.msg.out.OutTextMsg;

public class DefaultHandler extends MsgHandler{

    @Override
    protected OutMsg handlerTextMsg(InTextMsg inTextMsg) {
        OutTextMsg outTextMsg = new OutTextMsg(inTextMsg);
        outTextMsg.setContent("感谢您的留言，欢迎提供更好的意见或建议。（请勿反复回消息噢~因为我只是自动回复啦~）");
        return outTextMsg;
    }

    @Override
    protected OutMsg handlerLinkMsg(InLinkMsg inLinkMsg) {
        return null;
    }

    @Override
    protected OutMsg handlerImageMsg(InImageMsg inImageMsg) {
        OutImgMsg outImgMsg=new OutImgMsg(inImageMsg);
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
        outTextMsg.setContent("感谢您的关注,成都源林花卉期待您的光临！");
        return outTextMsg;
    }
}
