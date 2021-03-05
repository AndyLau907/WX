package com.example.wx.Controller;

import com.example.wx.Utils.ResponseUtil;
import com.example.wx.Utils.SignUtil;
import com.example.wx.Utils.XMLUtil;
import com.example.wx.api.AccessTokenApi;
import com.example.wx.api.IdAndSecretApi;
import com.example.wx.bean.Result;
import com.example.wx.bean.User;
import com.example.wx.bean.WxGoodsInfo;
import com.example.wx.handler.DefaultHandler;
import com.example.wx.repository.UserRepository;
import com.example.wx.repository.WxGoodsInfoRepository;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@RestController
public class WXController {

    @Autowired
    WxGoodsInfoRepository repository;
    @Autowired
    UserRepository userRepository;

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

    @PostMapping("Login")
    public Result login(@RequestBody HashMap map) {
        String userName = map.get("userName") == null ? "" : map.get("userName").toString();
        String password = map.get("password") == null ? "" : map.get("password").toString();
        Result result = new Result();
        List<User> list = userRepository.findAllByUserNameAndPassword(userName, password);
        if (list.isEmpty()) {
            result.setValid(false);
            result.setMessage("Login failed, account or password is wrong!");
        } else {
            result.setValid(true);
            result.setMessage("Login successful!");
            result.setData(list.get(0));
        }
        return result;
    }

    @PostMapping("Register")
    public Result register(@RequestBody HashMap map) {
        String userName = map.get("userName") == null ? "" : map.get("userName").toString();
        String password = map.get("password") == null ? "" : map.get("password").toString();
        String phone = map.get("phone") == null ? "" : map.get("phone").toString();

        Result result = new Result();
        List<User> list = userRepository.findAllByUserName(userName);
        if (!list.isEmpty()) {
            result.setMessage("Registration failed, username already exists!");
            result.setValid(false);
            return result;
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setPassword(password);
        user.setPhone(phone);
        user.setUserName(userName);
        userRepository.save(user);
        result.setValid(true);
        result.setMessage("Registration success!");
        result.setData(user);
        return result;
    }

    @GetMapping("getGoods")
    @ResponseBody
    public String getGoods() {
        List<WxGoodsInfo> list = repository.findAllByOrderByCreateTimeDesc();
        String temp = "<li class=\"list-group-item active\">\n" +
                "    <h4 class=\"list-group-item-heading\">\n" +
                "        ItemTitle\n" +
                "       <small>MinPrice----MaxPrice 元</small>\n" +
                "           NEW\n" +
                "    </h4>\n" +
                "</li>\n" +
                "<li class=\"list-group-item\">\n" +
                "    <img src=\"ImgSrc\"  width=\"100%\">\n" +
                "    <p class=\"list-group-item-text\">\n" +
                "        ItemDesc\n" +
                "    </p>\n" +
                "</li>\n";
        String temp2 = "<span class=\"label label-danger\">新</span>";
        StringBuilder stringBuilder = new StringBuilder();
        String s;
        Date now = new Date();
        Date exDate;
        for (WxGoodsInfo wxGoodsInfo : list) {
            s = temp.replace("ItemTitle", wxGoodsInfo.getGoodsName());
            if (wxGoodsInfo.getMaxPrice() == null || wxGoodsInfo.getMinPrice() == null) {
                s = s.replace("MinPrice----MaxPrice 元", "面议");
            } else {
                s = s.replace("MinPrice", wxGoodsInfo.getMinPrice().toString())
                        .replace("MaxPrice", wxGoodsInfo.getMaxPrice().toString());
            }
            Calendar c = Calendar.getInstance();
            c.setTime(wxGoodsInfo.getCreateTime());
            c.add(Calendar.DAY_OF_MONTH, 30);
            exDate = c.getTime();

            if (exDate.after(now)) {
                s = s.replace("NEW", temp2);
            } else {
                s = s.replace("NEW", "");
            }
            s = s.replace("ImgSrc", wxGoodsInfo.getFilePath())
                    .replace("ItemDesc", wxGoodsInfo.getRemark());
            stringBuilder.append(s);
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
