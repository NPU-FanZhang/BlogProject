package com.f4n.blog.service.impl;

import com.f4n.blog.dao.mapper.SysUserMapper;
import com.f4n.blog.dao.pojo.SysUser;
import com.f4n.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser findUserById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("NULL");
        }
        return sysUser;
    }
}
