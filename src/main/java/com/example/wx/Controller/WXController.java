package com.example.wx.Controller;

import com.example.wx.Utils.SignUtil;
import com.example.wx.Utils.XMLUtil;
import com.example.wx.handler.DefaultHandler;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
