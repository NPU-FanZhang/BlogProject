package com.f4n.blog.service;

import com.f4n.blog.dao.pojo.SysUser;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.LoginParams;

public interface LoginService {
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParams loginParams);
}
