package com.f4n.blog.controller;

import com.f4n.blog.service.TagService;
import com.f4n.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result listHotTags() {
        int limit = 5;
        return tagService.hot(limit);
    }

    @GetMapping()
    public Result findAll() {
        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail() {
        return tagService.findAllDetail();
    }

}
