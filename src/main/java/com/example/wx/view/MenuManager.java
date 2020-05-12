package com.example.wx.view;

import com.example.wx.api.AccessTokenApi;
import com.example.wx.api.IdAndSecretApi;
import com.example.wx.api.MenuApi;
import com.example.wx.bean.AccessToken;
import com.example.wx.ui.*;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;

public class MenuManager {

    public static String createMenu() throws IOException {

        ViewButton viewButton = new ViewButton();
        viewButton.setName("商品展示");
        viewButton.setType("viewFlowers");
        viewButton.setUrl("http://www.86pm25.com/city/beijing.html");
        ClickButton clickButton = new ClickButton();
        clickButton.setKey("getPhoneNumber");
        clickButton.setName("联系我们");
        clickButton.setType("click");
        ComplexButton complexButton = new ComplexButton();
        complexButton.setName("信息");
        complexButton.setSub_button(new Button[]{clickButton, viewButton});

        ClickButton clickButton2 = new ClickButton();
        clickButton2.setName("网上店铺");
        clickButton2.setType("click");
        clickButton2.setKey("getShop");
        /*
        ViewButton viewButton1 = new ViewButton();
        viewButton1.setName("个人网站");
        viewButton1.setType("view");
        viewButton1.setUrl("http://www.erlie.cc");
        ComplexButton complexButton1 = new ComplexButton();
        complexButton1.setName("关于作者");
        complexButton1.setSub_button(new Button[] {viewButton1});*/
        Menu menu = new Menu();
        menu.setButton(new Button[]{complexButton, clickButton2});
        AccessToken accessToken;
        accessToken = AccessTokenApi.getAccessToken(IdAndSecretApi.appID, IdAndSecretApi.appSecret);
        String token = accessToken.getAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(menu);
        return MenuApi.createMenu(token, json);
    }
}
