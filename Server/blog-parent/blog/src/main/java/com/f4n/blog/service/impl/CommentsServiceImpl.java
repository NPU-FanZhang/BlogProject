package com.f4n.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.f4n.blog.dao.mapper.CommentsMapper;
import com.f4n.blog.dao.pojo.Comment;
import com.f4n.blog.dao.pojo.SysUser;
import com.f4n.blog.service.CommentsService;
import com.f4n.blog.service.SysUserService;
import com.f4n.blog.utils.UserThreadLocal;
import com.f4n.blog.vo.CommentVo;
import com.f4n.blog.vo.Result;
import com.f4n.blog.vo.UserVo;
import com.f4n.blog.vo.params.CommentParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /*
         *  1.  根据文章id 在 zf_comments 表中 查询 评论列表
         *  2.  根据 查出的作者id 查询作者信息
         *  3.  判断 level = 1 ,再查询子评论 根据评论id 来查询子评论(parentId)
         *  4.
         * */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, id);
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> commentList = commentsMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(commentList);

        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParams commentParams) {
        SysUser sysUser = UserThreadLocal.get();

        Comment comment = new Comment();
        comment.setArticleId(commentParams.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParams.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParams.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParams.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentsMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        /* 作者信息 */
        Long authorId = comment.getAuthorId();
        UserVo UserVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(UserVo);
        /* 子评论 */
        Integer level = comment.getLevel();
        if (level == 1) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentByParentId(id);
            commentVo.setChildren(commentVoList);
        }
        if (level > 1) {
            Long toUid = comment.getToUid();
            UserVo userVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(userVo);

        }


        return commentVo;
    }

    private List<CommentVo> findCommentByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);

        return copyList(commentsMapper.selectList(queryWrapper));

    }
}
