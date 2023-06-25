package com.zsy.admin.service.ty;

/**
 * @author 郑书宇
 * @create 2023/6/5 19:23
 * @desc
 */
public interface CountService {
    //博客浏览量+1 并返回浏览量
    Long increaseInView(Long defaultCount,Integer blogId);
    //博客点赞+1 并返回点赞量
    Long increaseLikeView(Integer blogId);
    //获取点赞数量
    Integer getLikeCount(Integer blogId);
}
