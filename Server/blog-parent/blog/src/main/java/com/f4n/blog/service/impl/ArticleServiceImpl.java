package com.f4n.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.f4n.blog.dao.dos.Archives;
import com.f4n.blog.dao.mapper.ArticleBodyMapper;
import com.f4n.blog.dao.mapper.ArticleMapper;
import com.f4n.blog.dao.mapper.ArticleTagMapper;
import com.f4n.blog.dao.pojo.Article;
import com.f4n.blog.dao.pojo.ArticleBody;
import com.f4n.blog.dao.pojo.ArticleTag;
import com.f4n.blog.dao.pojo.SysUser;
import com.f4n.blog.service.*;
import com.f4n.blog.utils.UserThreadLocal;
import com.f4n.blog.vo.ArticleBodyVo;
import com.f4n.blog.vo.ArticleVo;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.TagVo;
import com.f4n.blog.vo.params.ArticleParams;
import com.f4n.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(),
                pageParams.getYear(), pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));

    }

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /*分页查询*/
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//        if (pageParams.getTagId() != null) {
//            /* 加入标签的条件查询
//             * 但Article 表中并没有tagId字段
//             * 因为文章和标签是一对多的,所以需要多表查询 用in的条件来实现
//             * */
//            List<Long> articleIdList = new ArrayList<>();
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0) {
//                queryWrapper.in(Article::getId, articleIdList);
//            }
//        }
//        // 是否置顶 进行排序
//        // 倒序排列
//        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        // 不能直接返回
//        // 再进行一次数据处理,处理成前端需要的数据类型
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//
//
//        return Result.success(articleVoList);
//    }

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

    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /*
         *  1.  根据文章ID查找文章内容
         *  2.  文章的内容和相关的标签信息需要联表查询
         *
         * */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);

        /*  因为文章有阅读次数的统计, 所以看一次文章, 增加一次相应的阅读量,但是这样有一定的问题.
         *  查看完文章之后, 应该直接返回数据,如果这个时候进行更新操作, 更新会增加写锁, 阻塞的其他读操作,会降低性能
         *  所以更新增加了接口的耗时.
         *  如果更新出问题, 也不能影响查看文章的操作.
         *  -- --------------
         *  这个时候可以 给更新操作一个单独的线程,和主线程就无关了
         *  通过 线程池 实现
         *  -- -------------
         * */
        threadService.updateArticleViewCount(articleMapper, article);

        return Result.success(articleVo);
    }

    /*发布文章*/
    @Override
    public Result publish(ArticleParams articleParams) {
        /*
         *  1.  发布文章 构建 Article 对象
         *  2.  分析 Article 对象参数
         *  3.  获取作者id时(就是登录的人) 需要将服务加入拦截器,完成登录拦截
         *  4.  需要将对应的标签 插入到关联列表当中
         *  5.  文章id通过 先给数据库插一条文章,然后再获取其id(id是由数据库生成的)
         *  6.  添加文章内容 body
         * */
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        //可以直接赋值的属性
        article.setAuthorId(sysUser.getId());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setTitle(articleParams.getTitle());
        article.setSummary(articleParams.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParams.getCategory().getId()));
        // 下面更新相关的关联表
        // insert后主键会自动set到实体的ID字段,所以后面只需要getId就行.
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParams.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        // insert后主键会自动set到实体的ID字段,所以后面只需要getId就行.
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());

        articleMapper.updateById(article);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", article.getId().toString());
        return Result.success(hashMap);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        ArrayList<ArticleVo> articleVoArrayList = new ArrayList<>();
        for (Article record : records) {
            articleVoArrayList.add(copy(record, true, true, false, false));
        }
        return articleVoArrayList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody,
                                     boolean isCategory) {
        ArrayList<ArticleVo> articleVoArrayList = new ArrayList<>();
        for (Article record : records) {
            articleVoArrayList.add(copy(record, true, true, isBody, isCategory));
        }
        return articleVoArrayList;
    }


    @Autowired
    private CategoryService categoryService;

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody,
                           boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
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
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
//        System.out.println("----------------");
//        System.out.println(articleVo);
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
