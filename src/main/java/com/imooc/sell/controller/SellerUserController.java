package com.imooc.sell.controller;

import com.imooc.sell.constant.CookieConstant;
import com.imooc.sell.constant.RedisConstant;
import com.imooc.sell.dataobject.SellerInfo;
import com.imooc.sell.enums.ResultEnum;
import com.imooc.sell.service.SellerService;
import com.imooc.sell.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              @RequestParam("password") String pwd,
                              HttpServletResponse response,
                              Map<String ,Object>map){

        //1. openid 去和数据库里的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if (sellerInfo == null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMsg());
            map.put("url","/sell");
            return new ModelAndView("common/error",map);
        }
        if (sellerInfo.getPassword().equals(pwd)){
            //2.设置token至redis
            String token = UUID.randomUUID().toString();
            Integer expire = RedisConstant.EXPIRE;
            stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openid,expire,TimeUnit.SECONDS); //redis key+ value + 过期时间

            //3.设置token至cookie
            CookieUtil.set(response,CookieConstant.TOKEN,token,expire);

            return new ModelAndView("redirect:/seller/order/list");
        }
        map.put("msg", ResultEnum.LOGIN_FAIL.getMsg());
        map.put("url","/sell");
        return new ModelAndView("common/error",map);
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletResponse response,
                       HttpServletRequest request,
                       Map<String ,Object> map){
        //1、从cookie里面查询
        Cookie cookie = CookieUtil.get(request,CookieConstant.TOKEN);
        if (cookie != null){
            //2、清除redis
            System.out.println(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue())));
            stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        }
        //3、清楚cookie
        CookieUtil.set(response,CookieConstant.TOKEN,null,0);

        map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMsg());
        map.put("url","/sell");

        return new ModelAndView("common/success",map);
    }
}
