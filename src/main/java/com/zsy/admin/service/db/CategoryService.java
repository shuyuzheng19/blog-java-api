package com.zsy.admin.service.db;

import com.zsy.admin.entity.Category;
import com.zsy.admin.entity.Tag;
import com.zsy.admin.vos.CategoryVo;
import com.zsy.admin.vos.TagVo;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:38
 * @desc
 */
public interface CategoryService {
    Category createCategory(String categoryName);
    List<CategoryVo> getAllCategory();
    List<CategoryVo> getCategoryList();
}
