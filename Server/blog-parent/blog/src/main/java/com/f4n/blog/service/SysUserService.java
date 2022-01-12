package com.f4n.blog.service;

import com.f4n.blog.dao.pojo.SysUser;

public interface SysUserService {
    SysUser findUserById(Long id);
}
