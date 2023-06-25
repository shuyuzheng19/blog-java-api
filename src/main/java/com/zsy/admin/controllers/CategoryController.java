package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.CategoryService;
import com.zsy.admin.service.db.TagService;
import com.zsy.admin.vos.CategoryVo;
import com.zsy.admin.vos.TagVo;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:51
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/admin/add")
    public Result addCategory(String name){
        if(StringUtils.isEmpty(name) || name.length()>15){
            return Result.fail("分类名称为空 或者 名称长度大于15");
        }
        CategoryVo category = categoryService.createCategory(name).toVo();
        return Result.success(category);
    }

    @GetMapping("/list")
    public Result getAllTags(){
        return Result.success(categoryService.getCategoryList());
    }

}
