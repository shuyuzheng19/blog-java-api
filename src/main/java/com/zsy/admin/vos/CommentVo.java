package com.zsy.admin.vos;

import com.zsy.admin.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:08
 * @desc
 */
@Data
public class CommentVo {

    private Long id;

    private Long parentId;

    private CommentUserVo user;

    private String address;

    private String content;

    private Integer uid;

    private Integer likes=0;

    private Date createTime;

    private String contentImg;

    private ReplyVo reply;

}
