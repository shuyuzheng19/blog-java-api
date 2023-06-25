package com.zsy.admin.service.search;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.vos.SearchBlog;

import java.util.List;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/6 23:29
 * @desc
 */
public interface SearchBlogService {
    void saveBlog(String json);
    void dropIndex();
    void deleteAllDocument();
    void createIndex();
    Set<String> getHotSearchKeyWord();
    PageInfo<SearchBlog> countSearch(String keyword,Integer page);
    List<SearchBlog> similarBlog(String keyword,Integer id);
    PageInfo<SearchBlog> search(int page, String keyword);
}
