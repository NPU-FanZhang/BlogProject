package com.f4n.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.f4n.blog.dao.mapper.ArticleMapper;
import com.f4n.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class ThreadService {

    /* 期望此操作在线程池中执行, 不希望影响其他线程 */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts + 1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        // 设置一个 在多线程下线程安全的设置
        updateWrapper.eq(Article::getViewCounts, viewCounts);
        // update article set viewCount  = 100 where viewCount = ?
        articleMapper.update(articleUpdate,updateWrapper);
        try {
            Thread.sleep(500);
            System.out.println("--------------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
