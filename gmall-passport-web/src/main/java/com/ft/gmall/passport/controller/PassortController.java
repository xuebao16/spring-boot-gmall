package com.ft.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.ft.gmall.annotaions.LoginRequire;
import com.ft.gmall.user.bean.UmsMember;
import com.ft.gmall.user.service.UserService;
import com.ft.gmall.util.JwtUtil;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PassortController {
    @Reference(version = "1.0.0")
    UserService userService;

    @RequestMapping("/index")
    String indexPage(String returnUrl,  Model model){
        model.addAttribute("originUrl", returnUrl);
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    String login(UmsMember userInfo, String originUrl, HttpServletRequest request){
        UmsMember user = userService.login(userInfo);

        if(user == null){
            return "failed";
        }

        String userId = user.getId();
        String nickName = user.getNickName();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberId", userId);
        map.put("nickName", nickName);

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();
            if (StringUtils.isBlank(ip))
                ip = "127.0.0.1";
        }

        String token = JwtUtil.encode("2020gmall0418", map, ip);

        userService.addUserToken(token, userId);
        return token;
    }

    @RequestMapping("/verify")
    @ResponseBody
    String verify(String token, String currentIp, HttpServletRequest request){
        Map<String, String> map = new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "2020gmall0418", currentIp);
        if(decode!=null){
            map.put("status","success");
            map.put("memberId",(String)decode.get("memberId"));
            map.put("nickname",(String)decode.get("nickname"));
        }else{
            map.put("status","fail");
        }

        return JSON.toJSONString(map);
    }
}
