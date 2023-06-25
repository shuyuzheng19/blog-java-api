package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.request.TopicRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.TopicService;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.vos.SimpleTopicVo;
import com.zsy.admin.vos.TopicVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
/**
 * @author 郑书宇
 * @create 2023/6/5 9:51
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/topics")
public class TopicController {

    @Resource
    private TopicService topicService;

    @PostMapping("/admin/add")
    public Result addTopic(@Valid @RequestBody TopicRequest topicRequest) {
        Integer userId = GlobalUtils.getCurrentUserId();
        SimpleTopicVo topic = topicService.saveTopic(topicRequest.toTopic(userId)).toSimpleTopicVo();
        return Result.success(topic);
    }

    @GetMapping("/list")
    public Result getTopicList(@RequestParam(defaultValue = "1") Integer page){
        PageInfo<TopicVo> pageInfo = topicService.getByPageTopic(page);
        return Result.success(pageInfo);
    }

    @GetMapping("/get/{id}")
    public Result getTopicVo(@PathVariable("id") Integer id){
        return Result.success(topicService.getTopicById(id));
    }

    @GetMapping("/current/list")
    public Result getAllCurrentUserTopic(){
        Integer userId = GlobalUtils.getCurrentUserId();
        return Result.success(topicService.getSimpleTopicVos(userId));
    }

    @GetMapping("/user/{id}/list")
    public Result getUserTopic(@PathVariable("id") Integer userId){
        return Result.success(topicService.getUserTopics(userId));
    }

    @GetMapping("/admin/current/list")
    public Result getCurrentUserTopic(){
        return Result.success(topicService.getCurrentTopics());
    }

    @GetMapping("/{id}/blogs")
    public Result getUserTopic(@PathVariable("id") Integer topicId,@RequestParam(defaultValue = "1")Integer page){
        return Result.success(topicService.getTopicBlog(page,topicId));
    }

    @GetMapping("/{id}/list")
    public Result getAllTopicSimleBlogVo(@PathVariable("id") Integer topicId){
        return Result.success(topicService.findAllSimpleBlog(topicId));
    }

}
