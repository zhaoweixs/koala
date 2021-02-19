package com.koala.rs.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public Object test(){
        return "test";
    }

    @GetMapping("/welcome")
    public String welcome(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Welcome " + authentication.getName();
    }

}
