package com.f4n.blog.service;

import com.f4n.blog.vo.CategoryVo;
import com.f4n.blog.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result findAllDetailById(Long id);
}
