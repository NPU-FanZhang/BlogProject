package com.f4n.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.f4n.blog.dao.dos.Archives;
import com.f4n.blog.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();
}
