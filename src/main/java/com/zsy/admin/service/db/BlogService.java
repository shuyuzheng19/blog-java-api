package com.zsy.admin.service.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zsy.admin.entity.Blog;
import com.zsy.admin.entity.BlogAi;
import com.zsy.admin.request.BlogRequest;
import com.zsy.admin.request.admin.FilterRequest;
import com.zsy.admin.vos.ArchiveBlogVo;
import com.zsy.admin.vos.BlogContentVo;
import com.zsy.admin.vos.BlogVo;
import com.zsy.admin.request.BlogPageRequest;
import com.zsy.admin.response.PageInfo;
import com.zsy.admin.vos.SimpleBlogVo;
import com.zsy.admin.vos.admin.BlogVoA;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/4 19:39
 * @desc
 */
public interface BlogService {
    //查询博客分页信息
    PageInfo<BlogVo> paginateAndSortQueries(BlogPageRequest pageRequest);
    //通过博客ID获取博客详情
    BlogContentVo getBlog(Integer id) throws JsonProcessingException;
    //添加一个新的博客
    Blog saveBlog(Blog blog);
    //修改博客
    Blog updateBlog(Integer blogId,BlogRequest blog);
    //删除某个博客
    int deleteBlog(Integer userId,List<Integer> ids);
    //保存用户写的博客
    void saveEditBlog(String content);
    //保存预览博客
    String savePreviewBlog(String content,String uuid);
    //获取用户写的博客
    String getSaveEditBlog();
    //获取预览
    String getSavePreviewBlog(String uuid);
    //获取推荐博客内容
    List<SimpleBlogVo> getRecommendBlog();
    //获取热门博客
    List<SimpleBlogVo> getHotBlogs();
    //获取随机博客
    Set<SimpleBlogVo> getRandomBlog();
    //获取两个日期之间的博客
    PageInfo<ArchiveBlogVo> getBetBetweenBlog(int page,Date start,Date end);
    //获取用户的博客
    PageInfo<BlogVo> getUseBlog(Integer page,Integer userId);
    //获取用户的榜单
    List<SimpleBlogVo> getUserTopBlog(Integer userId);

    BlogAi aiToDb(Integer bid,BlogAi blogAi);

    /**博客后台管理相关**/
    PageInfo<BlogVoA> findConditionQuery(Integer userId,FilterRequest filterRequest);
}
