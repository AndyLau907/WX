package com.example.wx.Controller;

import com.example.wx.Utils.ResponseUtil;
import com.example.wx.Utils.SignUtil;
import com.example.wx.Utils.XMLUtil;
import com.example.wx.api.AccessTokenApi;
import com.example.wx.api.IdAndSecretApi;
import com.example.wx.handler.DefaultHandler;
import org.dom4j.DocumentException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RestController
public class WXController {

    @GetMapping(value = "check")
    public String getUserName(@RequestParam(name = "signature") String signature,
                              @RequestParam(name = "timestamp") String timestamp,
                              @RequestParam(name = "nonce") String nonce,
                              @RequestParam(name = "echostr") String echostr) {
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        throw new RuntimeException("非法请求");
    }

    @PostMapping("check")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Map<String, String> map = XMLUtil.xml2Map(request);
        PrintWriter out = response.getWriter();
        DefaultHandler defaultHandler = new DefaultHandler();
        String msg = defaultHandler.handler(map);
        out.println(msg);
        out.close();
    }
    @GetMapping("getGoods")
    @ResponseBody
    public String getGoods() {
        return "sssssssssssssssssssss";
    }
    @GetMapping("get")
    @ResponseBody
    public String getImages() {
        String url="https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
        JSONObject json=new JSONObject();
        try {
            json.put("type","image");
            json.put("offset",0);
            json.put("count","20");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            url=url.replaceAll("ACCESS_TOKEN",AccessTokenApi.getAccessToken(IdAndSecretApi.appID,IdAndSecretApi.appSecret).getAccessToken());
            return ResponseUtil.sendPost(url,json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed";
    }

}
