package com.f4n.blog.controller;

import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.PageParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("article")
public class ArticleController {
    /*
     * 首页的文章列表
     * */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams) {

        return  Result.success();
    }
}
