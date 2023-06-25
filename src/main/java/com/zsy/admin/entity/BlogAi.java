package com.zsy.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 郑书宇
 * @create 2023/6/10 11:20
 * @desc
 */
@Data
@Table(name="blog_ai")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BlogAi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}
