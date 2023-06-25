package com.zsy.admin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/7 14:02
 * @desc
 */
@Data
@Entity
@Table(name="blog_likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer blogId;

    @Column(name="ip",nullable = false)
    private String ip;

    private Date likeDate;
}
