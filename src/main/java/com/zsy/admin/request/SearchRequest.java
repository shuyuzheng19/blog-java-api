package com.zsy.admin.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/7 4:26
 * @desc
 */
@Data
@Builder
public class SearchRequest {
    
    private String q; // 查询字符串
    
    private Integer offset; // 要跳过的文档数
    
    private Integer limit; // 返回的最大文档数
    
    private Integer hitsPerPage; // 为一页返回的最大文档数
    
    private Integer page; // 请求特定页面的结果

    private List<String> filter; // 按属性值筛选查询

    private List<String> facets; // 显示每个方面的匹配计数

    private List<String> attributesToRetrieve; // 要在返回的文档中显示的属性

    private List<String> attributesToCrop; // 必须裁剪其值的属性

    private Integer cropLength; // 裁剪值的最大长度（以字为单位）

    private String cropMarker; // 字符串标记裁剪边界

    private List<String> attributesToHighlight; // 突出显示属性中包含的匹配术语

    private String highlightPreTag; // 在突出显示的术语开头插入的字符串

    private String highlightPostTag; // 插入在突出显示的术语末尾的字符串

    private boolean showMatchesPosition; // 返回匹配字词位置

    private List<String> sort; // 按属性值对搜索结果进行排序
    
    private String matchingStrategy; // 用于匹配文档中查询词的策略
}
