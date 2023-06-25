package com.zsy.admin.response;

import lombok.Data;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/1/17 9:14
 * @desc
 */
@Data
public class PageInfo<T> {

    private int page;

    private int size;

    private long total;

    private List<T> data;

}
