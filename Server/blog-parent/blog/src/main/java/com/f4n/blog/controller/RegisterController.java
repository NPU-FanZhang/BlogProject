package com.f4n.blog.controller;

import com.f4n.blog.service.LoginService;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private LoginService loginService;

    @PostMapping()
    public Result register(@RequestBody LoginParams loginParams) {
        /*SSO 单点登录 后期可以将登录注册服务单独提出去*/
        return loginService.register(loginParams);

    }
}
