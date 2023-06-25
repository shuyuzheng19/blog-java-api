package com.zsy.admin.entity;

import com.zsy.admin.vos.CommentVo;
import com.zsy.admin.vos.ReplyVo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:08
 * @desc
 */
@Data
@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @OneToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    private User user;

    private String ip;

    private String address;

    private String content;

    private Integer likes=0;

    private Date createTime;

    private Integer blogId;

    private String userAgent;

    private String contentImg;

    @OneToMany(fetch = FetchType.EAGER,targetEntity = Comment.class,cascade = CascadeType.ALL)
    private List<Comment> reply=new ArrayList<>();


    public CommentVo toCommentVo(){
        CommentVo commentVo=new CommentVo();
        commentVo.setId(id);
        commentVo.setParentId(parentId);
        if(user!=null && user.getUsername()!=null){
            commentVo.setUser(user.toCommentUserVo());
        }
        commentVo.setUid(user.getId());
        commentVo.setAddress(address);
        commentVo.setContent(content);
        commentVo.setLikes(likes);
        commentVo.setCreateTime(createTime);
        commentVo.setContentImg(contentImg);
        if(reply!=null && reply.size()>0){
            ReplyVo replyVo=new ReplyVo();
            replyVo.setTotal(reply.size());
            List<CommentVo> replyList = reply.stream().map(comment-> comment.toCommentVo()).collect(Collectors.toList());
            replyVo.setList(replyList);
            commentVo.setReply(replyVo);
        }
        return commentVo;
    }
}
