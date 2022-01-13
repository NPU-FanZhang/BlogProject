package com.f4n.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    private static final String jwtToken = "123456zf!@##$";

    public static String creatToken(Long userId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtToken)//签发算法,密钥为jwtToken
                .setClaims(claims)//传入Body数据,自行定义 不安全
                .setIssuedAt(new Date())//设置签发日期
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));//一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
