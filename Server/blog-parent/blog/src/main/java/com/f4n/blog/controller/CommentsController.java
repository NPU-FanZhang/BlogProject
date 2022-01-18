package com.f4n.blog.controller;

import com.f4n.blog.service.CommentsService;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.CommentParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    public CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id) {
        return commentsService.commentsByArticleId(id);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParams commentParams) {
        return commentsService.comment(commentParams);
    }
}
