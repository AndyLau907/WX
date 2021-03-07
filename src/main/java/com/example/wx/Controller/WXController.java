package com.example.wx.Controller;

import com.example.wx.Utils.ResponseUtil;
import com.example.wx.Utils.SignUtil;
import com.example.wx.Utils.XMLUtil;
import com.example.wx.api.AccessTokenApi;
import com.example.wx.api.IdAndSecretApi;
import com.example.wx.bean.*;
import com.example.wx.handler.DefaultHandler;
import com.example.wx.repository.SignRepository;
import com.example.wx.repository.UserDataRepository;
import com.example.wx.repository.UserRepository;
import com.example.wx.repository.WxGoodsInfoRepository;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class WXController {

    @Autowired
    WxGoodsInfoRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SignRepository signRepository;
    @Autowired
    UserDataRepository userDataRepository;
    @Autowired
    EntityManager em;
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
            result.setMessage("Login failed, please check the network status or account and password!");
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
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setPassword(password);
        user.setGold(100);
        user.setPhone(phone);
        user.setUserName(userName);
        userRepository.save(user);
        result.setValid(true);
        result.setMessage("Registration success!");
        result.setData(user);
        return result;
    }


    @PostMapping("Sign")
    public Result sign(@RequestBody HashMap map) {
        Result result = new Result();

        String id = map.get("userId") == null ? "" : map.get("userId").toString();
        String id2 = map.get("userDataId") == null ? "" : map.get("userDataId").toString();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String s=year+"-"+month+"-"+day;
        List<Sign> list = em.createNativeQuery("select *from sign where user_id='"+id+"' and sign_date='"+s+"'",Sign.class).getResultList();
        if (!list.isEmpty()) {
            result.setMessage("Signed in today, no need to sign again!");
            result.setValid(false);
            return result;
        }
        Sign sign = new Sign();
        sign.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        sign.setUserId(id);
        sign.setUserDataId(id2);
        sign.setSignDate(calendar.getTime());
        signRepository.save(sign);

        //更新金币
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setGold(user.getGold() + 10);
            userRepository.save(user);
            result.setValid(true);
            result.setData(user);
            result.setMessage("Sign in successfully");
        }
        return result;
    }

    @PostMapping("getPlan")
    public Result getPlan(@RequestBody HashMap map) {
        Result result = new Result();
        String id = map.get("userId") == null ? "" : map.get("userId").toString();
        List<UserData> list = userDataRepository.findAllByIsActiveAndUserId(1, id);
        if (list.isEmpty()) {
            result.setValid(false);
        } else {
            List<Sign> signList = signRepository.findAllByUserDataId(list.get(0).getId());
            List<String> signDates = new ArrayList<>();
            for (Sign sign : signList) {
                Date d = sign.getSignDate();
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                signDates.add(c.get(Calendar.YEAR) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.DAY_OF_MONTH));
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            String s=year+"-"+month+"-"+day;
            List<Sign> temp = em.createNativeQuery("select *from sign where user_id='"+id+"' and sign_date='"+s+"'",Sign.class).getResultList();
            boolean isSigned = !temp.isEmpty();
            HashMap<String, Object> data = new HashMap<>();
            UserData ud=list.get(0);
            UserDataViewModel model=new UserDataViewModel();
            model.setUserId(ud.getUserId());
            model.setId(ud.getId());
            model.setEndDay(ud.getEndDay());
            model.setStartDay(ud.getStartDay());
            model.setIsActive(ud.getIsActive());
            model.setDays(ud.getDays());
            model.dateToStr();
            data.put("plan", model);
            data.put("signDates", signDates);
            data.put("isSigned", isSigned);
            result.setData(data);
            result.setValid(true);
        }
        return result;
    }

    @PostMapping("newPlan")
    public Result newPlan(@RequestBody HashMap map) throws ParseException {
        Result result = new Result();
        String id = map.get("userId") == null ? "" : map.get("userId").toString();
        String startDay = map.get("startDay") == null ? "" : map.get("startDay").toString();
        String endDay = map.get("endDay") == null ? "" : map.get("endDay").toString();

        List<UserData> list=userDataRepository.findAllByIsActiveAndUserId(1,id);
        if(!list.isEmpty()){
            result.setValid(false);
            result.setMessage("You already have a plan, you can't create it again!");
            return result;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        Date startDate = sf.parse(startDay);
        Date endDate = sf.parse(endDay);
        int days = (int) (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);

        UserData userData = new UserData();
        userData.setDays(days);
        userData.setEndDay(endDate);
        userData.setStartDay(startDate);
        userData.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        userData.setUserId(id);
        userData.setIsActive(1);
        userDataRepository.save(userData);
        result.setValid(true);
        result.setMessage("Created successfully!");
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
