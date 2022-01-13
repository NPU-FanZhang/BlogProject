package com.f4n.blog.handler;

import com.alibaba.fastjson.JSON;
import com.f4n.blog.dao.pojo.SysUser;
import com.f4n.blog.service.LoginService;
import com.f4n.blog.vo.ErrorCode;
import com.f4n.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        /*该方法在 Controller 方法（Handler） 之前执行*/
        /*
         *  1.  需要判断 请求的接口路径 是否为 HandlerMethod 如果不是就放过(可能访问资源的)
         *  2.  判断 Token是否为空,如果为空则未登录
         *  3.  如果 Token 不为空, 登录验证 token
         *  4.  认证成功则放行.
         * */

        if (!(handler instanceof HandlerMethod)) {
            // handler 可能是 RequestResourceHandler
            // Springboot 程序访问静态资源默认去 classpath 下的 static 目录去寻找
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        if (StringUtils.isBlank(token)) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 验证成功,放行
        return true;
    }
}