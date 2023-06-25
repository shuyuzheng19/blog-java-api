package com.zsy.admin.vos;

import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/5 15:16
 * @desc
 */
@Data
public class FileVo {
    private int id;
    private String name;
    private String dateStr;
    private String suffix;
    private String sizeStr;
    private String md5;
    private String url;
}
