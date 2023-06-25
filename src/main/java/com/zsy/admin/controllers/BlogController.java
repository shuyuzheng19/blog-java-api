package com.zsy.admin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zsy.admin.constants.Constants;
import com.zsy.admin.entity.Blog;
import com.zsy.admin.entity.BlogAi;
import com.zsy.admin.request.BlogRequest;
import com.zsy.admin.service.search.SearchBlogService;
import com.zsy.admin.service.ty.CountService;
import com.zsy.admin.service.ty.LikeService;
import com.zsy.admin.utils.*;
import com.zsy.admin.vos.*;
import com.zsy.admin.request.BlogPageRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.BlogService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.*;
import java.util.*;

/**
 * @author 郑书宇
 * @create 2023/6/4 21:55
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/blog")
@Slf4j
public class BlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private CountService countService;

    @Resource
    private SearchBlogService searchBlogService;

    @Resource
    private LikeService likeService;

    @Resource
    private ChatGptUtils chatGptUtils;


    @GetMapping("/list")
    public Result getBlogList(@ModelAttribute BlogPageRequest pageRequest){
        PageInfo<BlogVo> blogs = blogService.paginateAndSortQueries(pageRequest);
        return Result.success(blogs);
    }

    @GetMapping(value = "/admin/chat", produces = "text/event-stream;charset=UTF-8")
    public SseEmitter chat(String message,HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("UTF-8");

        return chatGptUtils.chat(message,false);
    }

    @PostMapping("/admin/gpt_token")
    public Result resetToken(@RequestBody Map<String,String> map){
        String token = map.get("token");
        if(StringUtils.isEmpty(token)) {
            return Result.fail("TOKEN不能为空");
        }else{
            chatGptUtils.resetToken(token);
            return Result.success();
        }
    }

    @GetMapping("/search")
    public Result search(String keyword,@RequestParam(defaultValue = "1")Integer page){
        PageInfo<SearchBlog> result = searchBlogService.search(page, keyword);
        return Result.success(result);
    }

    public static void main(String[] args) throws IOException {

    }

    @GetMapping("/search2")
    public Result searchCount(String keyword,@RequestParam(defaultValue = "1")Integer page){
        PageInfo<SearchBlog> result = searchBlogService.countSearch(keyword,page);
        return Result.success(result);
    }

    @GetMapping("/hot_keyword")
    public Result hotKeywords(){
        Set<String> hotKeywords = searchBlogService.getHotSearchKeyWord();
        return Result.success(hotKeywords);
    }

    @GetMapping("/similar")
    public Result similarBlog(String keyword,@RequestParam(defaultValue = "-1") Integer blogId){
        List<SearchBlog> blogs = searchBlogService.similarBlog( keyword,blogId);
        return Result.success(blogs);
    }


    @GetMapping("/is_like/{id}")
    public Result isLike(@PathVariable("id") Integer id,HttpServletRequest request){
        boolean like = likeService.isLike(id, IpUtils.getIpAddress(request));
        return Result.success(like);
    }

    @GetMapping("/like/{id}")
    public Result like(@PathVariable("id") Integer id,HttpServletRequest request){
        likeService.like(id, IpUtils.getIpAddress(request));
        Long count = countService.increaseLikeView(id);
        return Result.success(count);
    }


    @GetMapping("/get/{id}")
    public Result getBlog(@PathVariable Integer id, HttpServletRequest request) throws JsonProcessingException {
//        String userAgent = request.getHeader("User-Agent");
//        log.info("博客ID:{} IP:{} 浏览器:{} 设备:{}",id,
//                IpUtils.getIpAddress(request),
//                UserAgentUtil.parseBrowser(userAgent),
//                UserAgentUtil.parseDevice(userAgent)
//        );
        BlogContentVo blog = blogService.getBlog(id);
        Long eyeCount = countService.increaseInView(blog.getEyeCount(),blog.getId());
        blog.setEyeCount(eyeCount);
        Integer likeCount = countService.getLikeCount(blog.getId());
        if(likeCount!=null && likeCount>0){
            blog.setLikeCount((long) likeCount);
        }
        return Result.success(blog);
    }

    @PostMapping("/admin/ai/{id}")
    public Result generateAi(@PathVariable("id")Integer id,@RequestBody Map<String,String> map){
        String content = map.get("content");
        String ai = chatGptUtils.sendMessage(content);
        BlogAi blogAi = blogService.aiToDb(id, new BlogAi(null, ai));
        return Result.success(blogAi);
    }

    @PostMapping("/admin/save_edit")
    public Result saveEditBlog(@RequestBody Map<String,String> map){
        String content = map.get("content");
        if(StringUtils.isEmpty(content)){
            return Result.fail("内容不能为空!");
        }
        blogService.saveEditBlog(content);
        return Result.success();
    }

    @GetMapping("/admin/get_edit")
    public Result saveEditBlog(){
        String content=blogService.getSaveEditBlog();
        return Result.success(content);
    }


    @PostMapping("/admin/preview_save")
    public Result savePreviewBlog(@RequestBody Map<String,String> map){
        String content = map.get("content");
        String uuid = map.get("uuid");
        if(StringUtils.isEmpty(content) || StringUtils.isEmpty(uuid)){
            return Result.fail("内容不能为空!");
        }
        blogService.savePreviewBlog(content,uuid);
        return Result.success();
    }

    @GetMapping("/get_preview")
    public Result getSavePreviewBlog(String id){
        if(StringUtils.isEmpty(id)){
            return Result.fail("ID不能为空!");
        }
        String content = blogService.getSavePreviewBlog(id);
        if(StringUtils.isEmpty(content)){
            return Result.fail("找不到要预览的博客");
        }

        return Result.success(content);
    }


    @PostMapping("/admin/save")
    public Result saveBlog(@Valid @RequestBody BlogRequest blogRequest){
        Integer currentUserId = GlobalUtils.getCurrentUserId();
        Blog blog = blogService.saveBlog(blogRequest.toBlogDo(currentUserId));
        searchBlogService.saveBlog(JsonUtils.objectToJson(List.of(new SearchBlog(blog.getId(),blog.getTitle(),blog.getDescription()))));
        return Result.success(blog.getId());
    }

    @GetMapping("/range")
    public Result rangeDateBlog(@RequestParam(defaultValue = "1") Integer page,long start,long end){
        PageInfo<ArchiveBlogVo> pageInfo=blogService.getBetBetweenBlog(page,new Date(start),new Date(end));
        return Result.success(pageInfo);
    }

    @GetMapping("/user/{id}")
    public Result getUserBlog(@RequestParam(defaultValue = "1") Integer page,@PathVariable("id") Integer id){
        PageInfo<BlogVo> pageInfo=blogService.getUseBlog(page,id);
        return Result.success(pageInfo);
    }

    @GetMapping("/user/top/{id}")
    public Result getUserTopBlog(@PathVariable("id") Integer id){
        List<SimpleBlogVo> pageInfo=blogService.getUserTopBlog(id);
        return Result.success(pageInfo);
    }

    @GetMapping("/recommend")
    public Result getRecommend(){
        return Result.success(blogService.getRecommendBlog());
    }

    @GetMapping("/hots")
    public Result getHotBlog(){
        return Result.success(blogService.getHotBlogs());
    }

    @GetMapping("/random")
    public Result getRandomBlog(){
        return Result.success(blogService.getRandomBlog());
    }


}
