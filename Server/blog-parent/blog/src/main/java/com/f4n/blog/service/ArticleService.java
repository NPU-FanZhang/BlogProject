package com.f4n.blog.service;

import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.PageParams;

public interface ArticleService {
    /*分页查询文章列表*/
    Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);
    Result newArticle(int limit);

    Result listArchives();
}
