package com.f4n.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.f4n.blog.dao.pojo.SysUser;
import com.f4n.blog.service.LoginService;
import com.f4n.blog.service.SysUserService;
import com.f4n.blog.utils.JWTUtils;
import com.f4n.blog.vo.ErrorCode;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "zf!@##$";

    @Override
    public Result login(LoginParams loginParams) {
        /*
         *  1. 检查参数是否合法
         *  2. 根据用户名秘密去user表中查询 是否存在
         *  3. 如果不存在 登录失败
         *  4. 如果存在,使用jwt 生成token 返回给前端
         *  5. token 放入redis当中, 在redis中保存token:user信息,设置过期时间
         *     (登录验证时,先认证token字符串是否合法,再去redis中认证用户是否存在)
         * */

        /*1*/
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        /*2*/
        password = DigestUtils.md5Hex(password + salt);
        System.out.println("======2============");
        System.out.println(password);
        SysUser sysUser = sysUserService.findUser(account, password);

        /*3*/
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),
                    ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        /*4*/
        String token = JWTUtils.creatToken(sysUser.getId());
        /*5*/
        System.out.println("=====3=============");
        System.out.println(token);
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1,
                TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) return null;
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) return null;
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) return null;
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    /*退出登录*/
    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success("");
    }

    @Override
    public Result register(LoginParams loginParams) {
        /*
         *  1.  判断注册参数是否合法
         *  2.  判断账户是否存在,存在则返回账户已经存在
         *  3.  不存在,注册账户
         *  4.  生成token
         *  5.  将token 存入Redis并返回
         *  6.  加入事务,一旦注册过程出错则回滚
         * */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        String token = JWTUtils.creatToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
