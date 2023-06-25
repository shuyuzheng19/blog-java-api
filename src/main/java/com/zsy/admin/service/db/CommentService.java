package com.zsy.admin.service.db;

import com.zsy.admin.entity.Comment;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.vos.CommentUserVo;
import com.zsy.admin.vos.CommentVo;

import java.util.List;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:23
 * @desc
 */
public interface CommentService {
    CommentVo addComment(Comment comment);
    void likeComment(Integer blogId,Integer id);
    CommentUserVo getCurrentUserCommentVo(Integer blogId);
    Set<Integer> getCurrentUserBlogLikeCommentId(Integer blogId,Integer userId);
    PageInfo<CommentVo> getCommentPage(int page,Integer blogId);
    List<Integer> getCommentLikeCount(List<String> id);
}
