package com.zsy.admin.response.search;

import lombok.Data;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/7 4:27
 * @desc
 */
@Data
public class SearchResponse<T> {
    private List<T> hits; // 查询结果
    private int offset; // 跳过的文档数
    private int limit; // 要携带的文件数量
    private int estimatedTotalHits; // 估计匹配总数
    private int totalHits; // 详尽的匹配总数
    private int totalPages; // 详尽的搜索结果页总数
    private int hitsPerPage; // 每页上的结果数
    private int page; // 当前搜索结果页
    private FacetDistribution facetDistribution; // 给定分面的分布
    private FacetStats facetStats; // 每个方面的数字最小值和最大值
    private int processingTimeMs; // 查询的处理时间
    private String query; // 发起响应的查询
}
