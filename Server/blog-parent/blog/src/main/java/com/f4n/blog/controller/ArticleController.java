package com.f4n.blog.controller;

import com.f4n.blog.service.ArticleService;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("articles")
public class ArticleController {
    /*
     * 首页的文章列表
     * */
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }

    @PostMapping("hot")
    public Result hotArticle() {
        int limit = 3;
        return articleService.hotArticle(limit);
    }
    @PostMapping("new")
    public Result newArticle() {
        int limit = 3;
        return articleService.newArticle(limit);
    }
    /*文章归档*/
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

}
