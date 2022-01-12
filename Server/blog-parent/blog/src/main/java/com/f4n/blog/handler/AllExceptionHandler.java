package com.f4n.blog.handler;

import com.f4n.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// ControllerAdvice 对加了@Controller注解的方法进行拦截处理 AOP实现

@ControllerAdvice
public class AllExceptionHandler {
    // 进行异常处理, 处理Exception 异常
    @ExceptionHandler(Exception.class)
    @ResponseBody// 返回Json数据
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }
}
