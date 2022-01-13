package com.f4n.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.f4n.blog.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagsByArticleId(Long articleId);
    List<Long> findHotsTagsById(int limit);
    List<Tag> findTagsByTagIds(List<Long> hotTags);
}
