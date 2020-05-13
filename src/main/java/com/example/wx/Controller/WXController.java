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
        String temp = "<li class=\"list-group-item active\">\n" +
                "        <h4 class=\"list-group-item-heading\">\n" +
                "            ItemTitle\n" +
                "        </h4>\n" +
                "    </li>\n" +
                "    <li class=\"list-group-item\">\n" +
                "        <img src=\"ImgSrc\" height=\"250px\" width=\"100%\">\n" +
                "        <p class=\"list-group-item-text\">\n" +
                "            ItemDesc\n" +
                "        </p>\n" +
                "    </li>";
        String desc = "Java是一门面向对象编程语言，不仅吸收了C++语言的各种优点，" +
                "还摒弃了C++里难以理解的多继承、指针等概念，因此Java语言具有功能强大和简单易用两个特征。" +
                "Java语言作为静态面向对象编程语言的代表，极好地实现了面向对象理论，允许程序员以优雅的思维方式进行复杂的编程";
        String title = "Java";
        String price = "100-200元";
        String path = "./images/img#.jpg";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            stringBuilder.append(temp.replace("ItemTitle", title + "-" + price)
                    .replace("ImgSrc", path.replace("#", (i + 1) + ""))
                    .replace("ItemDesc", desc));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @GetMapping("get")
    @ResponseBody
    public String getImages() {
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
        JSONObject json = new JSONObject();
        try {
            json.put("type", "image");
            json.put("offset", 0);
            json.put("count", "20");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            url = url.replaceAll("ACCESS_TOKEN", AccessTokenApi.getAccessToken(IdAndSecretApi.appID, IdAndSecretApi.appSecret).getAccessToken());
            return ResponseUtil.sendPost(url, json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed";
    }

}
