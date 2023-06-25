package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.entity.Tag;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.TagService;
import com.zsy.admin.vos.TagVo;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:51
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/tags")
public class TagController {

    @Resource
    private TagService tagService;

    @PostMapping("/admin/add")
    public Result addTag(String name){
        if(StringUtils.isEmpty(name) || name.length()>15){
            return Result.fail("标签名称为空 或者 名称长度大于15");
        }
        TagVo tag = tagService.createTag(name).toVo();
        return Result.success(tag);
    }

    @GetMapping("/list")
    public Result getAllTags(){
        return Result.success(tagService.getAllTag());
    }

    @GetMapping("/get/{id}")
    public Result getTagInfo(@PathVariable("id") Integer id){
        return Result.success(tagService.getTagInfo(id).toVo());
    }

    @GetMapping("/{id}/blogs")
    public Result getTagBlogs(@PathVariable("id") Integer id,@RequestParam(defaultValue = "1") Integer page){
        return Result.success(tagService.getTagBlogs(page,id));
    }

    @GetMapping("/random")
    public Result getRandomTag(){
        return Result.success(tagService.getRandomTag());
    }
}
