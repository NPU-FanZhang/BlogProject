package com.f4n.blog.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.f4n.blog.mapper")
public class MybatisPlusConfig {
}
