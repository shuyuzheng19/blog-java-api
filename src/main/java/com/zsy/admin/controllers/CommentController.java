package com.zsy.admin.controllers;

import com.zsy.admin.constants.Constants;
import com.zsy.admin.entity.Comment;
import com.zsy.admin.request.CommentRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.response.Result;
import com.zsy.admin.service.db.CommentService;
import com.zsy.admin.utils.IpUtils;
import com.zsy.admin.vos.CommentUserVo;
import com.zsy.admin.vos.CommentVo;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:30
 * @desc
 */
@RestController
@RequestMapping(Constants.API_PREFIX+"/comments")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/auth/add_comment")
    public Result addComment(@RequestBody @Valid CommentRequest commentRequest, HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        String ip = IpUtils.getIpAddress(request);
        String address= IpUtils.getIpCity(ip);
        Comment comment = commentRequest.toDo();
        if(StringUtils.isEmpty(address)) {
            comment.setAddress("未知IP");
        }else{
            comment.setAddress(address);
        }
        comment.setUserAgent(userAgent);
        comment.setIp(ip);
        CommentVo commentVo = commentService.addComment(comment);
        return Result.success(commentVo);
    }

    @GetMapping("/auth/like/{id}")
    public Result likeComment(@PathVariable("id") Integer id,Integer blogId){
        commentService.likeComment(blogId,id);
        return Result.success();
    }

    @GetMapping("/comments/{id}")
    public Result getComments(@PathVariable("id") Integer blogId,@RequestParam(defaultValue = "1") Integer page){
        PageInfo<CommentVo> pageInfo = commentService.getCommentPage(page, blogId);
        return Result.success(pageInfo);
    }

    @GetMapping("/auth/user")
    public Result getCommentUser(Integer blogId){
        CommentUserVo currentUserCommentVo = commentService.getCurrentUserCommentVo(blogId);
        return Result.success(currentUserCommentVo);
    }
}
