package com.zsy.admin.request;

import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/12 21:55
 * @desc
 */
@Data
public class FileRequest {

    private String keyword;

    private String sort = "date";

    private int page = 1;

}
