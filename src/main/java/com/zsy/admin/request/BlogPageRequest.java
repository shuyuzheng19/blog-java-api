package com.zsy.admin.request;

import com.zsy.admin.enums.BlogSortEnum;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/4 22:09
 * @desc
 */
@Data
public class BlogPageRequest {

    private int page = 1;

    private int cid = -1;

    private BlogSortEnum sort=BlogSortEnum.CREATE;

}
