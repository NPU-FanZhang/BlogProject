package com.f4n.blog.controller;

import com.f4n.blog.service.CategoryService;
import com.f4n.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public Result Categories() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result CategoryDetail() {
        return categoryService.findAllDetail();
    }
    @GetMapping("detail/{id}")
    public Result CategoryDetailById(@PathVariable Long id) {
        return categoryService.findAllDetailById(id);
    }
}
