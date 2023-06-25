package com.zsy.admin.vos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/4 19:52
 * @desc
 */
@Data
@NoArgsConstructor
public class BlogContentVo {
    private Integer id;

    private String description;

    private String title;

    private String coverImage;

    private String sourceUrl;

    private String content;

    private Long eyeCount=0L;

    private Long likeCount=0L;

    private Long starCount=0L;

    private CategoryVo category;

    private String aiMessage;

    private Set<TagVo> tags=new HashSet<>();

    private TopicVo topic;

    private SimpleUserVo user;

    private boolean markdown;

    private Date createTime;

    private Date updateTime;
}
