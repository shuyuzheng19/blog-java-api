package com.zsy.admin.service.db;

import com.zsy.admin.entity.Tag;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.vos.TagVo;

import java.util.List;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:38
 * @desc
 */
public interface TagService {
    Tag createTag(String tagName);
    List<TagVo> getAllTag();
    Set<TagVo> getRandomTag();
    PageInfo<BlogVo> getTagBlogs(int page,Integer tagId);
    Tag getTagInfo(Integer tagId);
}
