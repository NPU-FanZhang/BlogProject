package com.f4n.blog.service;

import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.params.CommentParams;

public interface CommentsService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParams commentParams);
}
