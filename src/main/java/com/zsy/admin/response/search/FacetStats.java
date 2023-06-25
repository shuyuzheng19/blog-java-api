package com.zsy.admin.response.search;

import lombok.Data;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/7 4:38
 * @desc
 */
@Data
public class FacetStats {
    private String q;

    private List<String> facets;
}
