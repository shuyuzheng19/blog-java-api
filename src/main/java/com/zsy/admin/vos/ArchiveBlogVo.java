package com.zsy.admin.vos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/5 23:18
 * @desc
 */
@Data
@AllArgsConstructor
public class ArchiveBlogVo {
    private Integer id;

    private String title;

    private String desc;

    private Date create;
}
