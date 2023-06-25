package com.zsy.admin.constants;

/**
 * @author 郑书宇
 * @create 2023/6/4 21:57
 * @desc
 */
public interface Constants {

    //TOKEN过期时间 单位:天
    int TOKEN_EXPIRE=30;

    //接口统一前缀
    String API_PREFIX="/api/v1";

    //每页的博客数量
    int BLOG_PAGE_SIZE=10;

    //每页的专题数量,不是博客列表
    int TOPIC_PAGE_SIZE=16;

    long KB=1024;

    long MB=1024 * 1024;

    long GB=1024 * 1024 * 1024;

    long TB = 1024 * 1024 * 1024 * 1024;

    //推荐博客的数量
    int RECOMMEND_COUNT=4;

    //热门博客的数量
    int HOT_BLOG_COUNT=10;

    //随机博客的数量
    int RANDOM_BLOG_COUNT=10;

    //随机标签的数量
    int RANDOM_TAG_COUNT=20;

    //归档博客每页多少条
    int ARCHIVE_BLOG_PAGE_SIZE=15;

    //用户榜单数量多少条
    int USER_TOP_BLOG_COUNT=10;

    //搜索引擎的INDEX
    String ARTICLE_INDEX_NAME="articles";

    //搜索引擎高亮前缀
    String HIG_PRE="<strong>";

    //搜索引擎高亮后缀
    String HIG_SUFFIX="</strong>";

    //一页多少条评论
    int COMMENT_COUNT=15;

    //一页多少条文件数据
    int FILE_COUNT=15;

}
