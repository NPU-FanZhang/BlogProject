package com.f4n.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.f4n.blog.dao.dos.Archives;
import com.f4n.blog.dao.mapper.ArticleMapper;
import com.f4n.blog.dao.pojo.Article;
import com.f4n.blog.service.ArticleService;
import com.f4n.blog.service.SysUserService;
import com.f4n.blog.service.TagService;
import com.f4n.blog.vo.ArticleVo;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result listArticle(PageParams pageParams) {
        /*分页查询*/
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        // 是否置顶 进行排序
        // 倒序排列
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);

        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        // 不能直接返回
        // 再进行一次数据处理,处理成前端需要的数据类型
        List<ArticleVo> articleVoList = copyList(records, true, true);


        return Result.success(articleVoList);
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 浏览量
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        /* select id,title from zf_article order by view_counts desc limit 5 */
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 浏览量
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        /* select id,title from zf_article order by view_counts desc limit 5 */
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        System.out.println("-------tagService---------");
        System.out.println(archivesList);
        return Result.success(archivesList);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        ArrayList<ArticleVo> articleVoArrayList = new ArrayList<>();
        for (Article record : records) {
            articleVoArrayList.add(copy(record, true, true));
        }
        return articleVoArrayList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 不是所有的article都需要作者信息和标签信息
        if (isTag) {
            Long articleId = article.getId();
//            System.out.println("-------tagService---------");
//            System.out.println(tagService.findTagsByArticleId(articleId));
            articleVo.setTags(tagService.findTagsByArticleId(articleId));

        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
//        System.out.println("----------------");
//        System.out.println(articleVo);
        return articleVo;
    }
}
