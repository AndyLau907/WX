package com.example.wx.Controller;

import com.example.wx.Utils.SignUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WXController {

    @GetMapping(value ="check")
    public  String getUserName(@RequestParam(name = "signature") String signature,
                               @RequestParam(name = "timestamp") String timestamp,
                               @RequestParam(name = "nonce") String nonce,
                               @RequestParam(name = "echostr") String echostr){
        if(SignUtil.checkSignature(signature,timestamp,nonce)){
            return echostr;
        }
        throw new RuntimeException("非法请求");
    }
    @GetMapping(value ="test")
    public  String get(){
        return "666";
    }
}
