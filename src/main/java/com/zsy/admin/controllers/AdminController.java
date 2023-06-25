package com.zsy.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zsy.admin.constants.Constants;
import com.zsy.admin.entity.Blog;
import com.zsy.admin.entity.User;
import com.zsy.admin.enums.RoleEnum;
import com.zsy.admin.request.BlogRequest;
import com.zsy.admin.request.admin.FilterRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.BlogService;
import com.zsy.admin.service.ty.SuperAdminService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.vos.BlogContentVo;
import com.zsy.admin.vos.CategoryVo;
import com.zsy.admin.vos.TagVo;
import com.zsy.admin.vos.TopicVo;
import com.zsy.admin.vos.admin.BlogVoA;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 郑书宇
 * @create 2023/6/5 9:51
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/admin")
public class AdminController {

    @Resource
    private SuperAdminService adminService;

    @Resource
    private BlogService blogService;

    @PostMapping("/super/recommend")
    public Result setRecommend(@RequestBody List<Integer> ids){
        adminService.setRecommendBlog(ids);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteBlog(@RequestBody List<Integer> ids){
        if(ids==null || ids.size()==0){
            return Result.fail("还有没选中博客哦");
        }
        User currentUser = GlobalUtils.getCurrentUser();
        String roleName = currentUser.getRole().getRoleName();
        if(roleName.equals(RoleEnum.SUPER_ADMIN.name())) {
            return Result.success(blogService.deleteBlog(-1,ids));
        }else if(roleName.equals(RoleEnum.ADMIN.name())) {
            return Result.success(blogService.deleteBlog(currentUser.getId(),ids));
        }else{
            return Result.fail("没有权限");
        }
    }

    @PostMapping("/update_blog/{id}")
    public Result setRecommend(@PathVariable("id") Integer id,@RequestBody BlogRequest blogRequest){
        Blog blog = blogService.updateBlog(id, blogRequest);
        return Result.success(blog.getId());
    }

    @PostMapping("/super/update_blog/{id}")
    public Result setRecommend(@PathVariable("id") Integer id,Integer userId,@RequestBody BlogRequest blogRequest){
        Blog blog = blogRequest.toBlogDo(userId);
        blog.setId(id);
        blogService.saveBlog(blog);
        return Result.success();
    }

    @GetMapping("/edit/{id}")
    public Result getEditBlog(@PathVariable("id") Integer id) throws JsonProcessingException {
        BlogContentVo blog = blogService.getBlog(id);
        User currentUser = GlobalUtils.getCurrentUser();
        if(currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN.name()) && !blog.getUser().getId().equals(currentUser.getId())){
            return Result.fail("你没有权限修改别人的博客,只能改自己的");
        }
        BlogRequest blogRequest=new BlogRequest();
        blogRequest.setTitle(blog.getTitle());
        blogRequest.setDescription(blog.getDescription());
        blogRequest.setContent(blog.getContent());
        blogRequest.setMarkdown(blog.isMarkdown());
        blogRequest.setSourceUrl(blog.getSourceUrl());
        blogRequest.setCoverImage(blog.getCoverImage());
        Set<TagVo> tags = blog.getTags();
        if(tags.size()>0) {
            blogRequest.setTags(tags.stream().map(tag->tag.getId()).collect(Collectors.toSet()));
        }else{
            blogRequest.setTags(new HashSet<>());
        }
        TopicVo topic = blog.getTopic();
        if(topic!=null){
            blogRequest.setTopic(topic.getId());
        }
        CategoryVo category = blog.getCategory();
        if(category!=null){
            blogRequest.setCategory(category.getId());
        }
        return Result.success(blogRequest);
    }

    @PostMapping("/blog/me")
    public Result getMeBlog(@RequestBody FilterRequest filterRequest){
        Integer userId = GlobalUtils.getCurrentUserId();
        PageInfo<BlogVoA> result = blogService.findConditionQuery(userId,filterRequest);
        return Result.success(result);
    }

}
