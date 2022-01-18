package com.f4n.blog.service;

import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    /*依据文章ID查询标签列表*/
    /*必须自建XML写SQL来实现多表查询*/
    /*XML 的路径必须和接口的包名一致 */
    List<TagVo> findTagsByArticleId(Long articleId);
    Result hot(int limit);

    Result findAll();

    Result findAllDetail();
}
