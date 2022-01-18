package com.f4n.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.f4n.blog.dao.mapper.TagMapper;
import com.f4n.blog.dao.pojo.Tag;
import com.f4n.blog.service.TagService;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    // tag 和 对应的tag内容还有文章id分布于不同的表
    // 但是mybatisPlus 无法进行多表查询
    @Autowired
    private TagMapper tagMapper;


    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hot(int limit) {
        List<Long> hotTags = tagMapper.findHotsTagsById(limit);
        if (CollectionUtils.isEmpty(hotTags)) {
            return Result.success(Collections.emptyList());
        }
        List<Tag> tagList = tagMapper.findTagsByTagIds(hotTags);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tagList));

    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tagList));
    }

    public List<TagVo> copyList(List<Tag> tagList) {
        ArrayList<TagVo> tagVoArrayList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoArrayList.add(copy(tag));
        }
        return tagVoArrayList;
    }

    public TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }
}
