package com.zsy.admin.service.ty;

/**
 * @author 郑书宇
 * @create 2023/6/7 14:07
 * @desc
 */
public interface LikeService {
    void like(Integer blogId,String ip);
    boolean isLike(Integer blogId,String ip);
}
