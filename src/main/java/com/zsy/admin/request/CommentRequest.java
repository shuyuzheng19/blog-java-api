package com.zsy.admin.request;

import com.zsy.admin.entity.Comment;
import com.zsy.admin.entity.User;
import com.zsy.admin.utils.GlobalUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:28
 * @desc
 */
@Data
public class CommentRequest {

    @NotNull(message = "博客ID不能为空")
    private Integer blogId;

    private Long parentId;

    @NotEmpty(message = "评论不能为空")
    private String content;

    private String contentImg;

    public Comment toDo(){
        Comment comment=new Comment();
        comment.setParentId(parentId);
        comment.setBlogId(blogId);
        comment.setContent(content);
        comment.setLikes(0);
        comment.setCreateTime(new Date());
        comment.setContentImg(contentImg);
        return comment;
    }
}
