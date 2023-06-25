package com.zsy.admin.request.admin;

import com.zsy.admin.enums.BlogSortEnum;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/11 17:21
 * @desc
 */
@Data
public class FilterRequest {
    private int page = 1;

    private int cid = -1;

    private BlogSortEnum sort=BlogSortEnum.CREATE;

    private String keyword;

    private Long[] range;

}
