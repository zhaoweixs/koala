package com.koala.uaa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.koala.uaa.entity.UserInfo;
import com.koala.uaa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin.liveconnect.SecurityContextHelper;

import java.util.List;

/**
 * ToDO
 *
 * @author zhaowei
 * @date 2021/2/19 11:06
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test")
    public Object test(){
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<UserInfo>();
        List<UserInfo> userInfoList = userMapper.selectList(queryWrapper);
        return userInfoList;
    }

    @GetMapping("/test2")
    public Object test2(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

}
