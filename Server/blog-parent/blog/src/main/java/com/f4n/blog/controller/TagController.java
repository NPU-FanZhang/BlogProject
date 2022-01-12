package com.f4n.blog.controller;

import com.f4n.blog.service.TagService;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
