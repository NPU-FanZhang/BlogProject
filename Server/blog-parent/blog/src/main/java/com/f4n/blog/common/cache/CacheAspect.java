package com.f4n.blog.common.cache;


import com.alibaba.fastjson.JSON;
import com.f4n.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;


/* aop  定义一个切面  切面确定了 切点和通知的关系 */
@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /*切点定义*/
    @Pointcut("@annotation(com.f4n.blog.common.cache.Cache)")
    public void pt() {
    }

    /*  环绕通知
     *  在方法前后进行增强*/
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            Signature signature = pjp.getSignature();
            //类名
            String className = pjp.getTarget().getClass().getSimpleName();
            // 调用的方法名
            String methodName = signature.getName();
            Class[] parameterTypes = new Class[pjp.getArgs().length];
            Object[] args = pjp.getArgs();
            // 参数
            String params = "";

            // 把参数装载起来
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params += JSON.toJSONString(args[i]);
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                // 加密 防止过长
                params = DigestUtils.md5Hex(params);
            }

            //
            Method method = pjp.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            // 获取cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            // 缓存过期时间 和 名称
            long expire = annotation.expire();
            String name = annotation.name();
            // 先从redis获取
            String redisKey = name + "::" + className + "::" + methodName + "::" + params;
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            /* 如果该方法在缓存中 ,直接从缓存中取..*/
            if (StringUtils.isNotEmpty(redisValue)) {
                log.info("{},{},走了缓存", className, methodName);
                /* 这里解析有问题 , Long 等数据类型 返回的精度不够 */
                /* 将 Long 转换为 String */
                return JSON.parseObject(redisValue, Result.class);
            }
            /* 如果没在缓存中,需要运行一遍再加入缓存 */
            Object proceed = pjp.proceed();
            redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(proceed),
                    Duration.ofMillis(expire));

            log.info("{},{},存入缓存", className, methodName);

            return proceed;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(-999, "系统错误");

    }
}
