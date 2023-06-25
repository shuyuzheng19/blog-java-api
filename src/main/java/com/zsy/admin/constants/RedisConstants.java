package com.zsy.admin.constants;

/**
 * @author 郑书宇
 * @create 2023/6/4 11:22
 * @desc
 */
public interface RedisConstants {
    //登陆失败
    String LOGIN_FAIL_COUNT="LOGIN-FAIL:";
    //频繁登录失败锁定多久 单位 分钟
    int DEFAULT_LOCK_IP_EXPIRE=5;
    //发送邮箱的键
    String GET_EMAIL_CODE="EMAIL-CODE:";
    //登录图片验证码
    String CAPTCHA_CODE="CAPTCHA-LOGIN-CODE:";
    //缓存用户信息的键
    String USER_INFO="USER-INFO:";
    //缓存用户信息失效时间 单位:分钟
    int USER_INFO_EXPIRE=30;
    //缓存用户的TOKEN
    String USER_TOKEN="USER_TOKEN:";
    //缓存博客详情信息map
    String BLOG_INFO="BLOG-INFO-MAP";
    //缓存推荐博客的KEY
    String RECOMMEND_BLOG="RECOMMEND-BLOG";
    //缓存博客浏览量的MAP
    String BLOG_EYE_COUNT_MAP="BLOG-EYE-COUNT-MAP";
    //缓存博客点赞的MAP
    String BLOG_LIKE_COUNT_MAP="BLOG-LIKE-COUNT-MAP";
    //缓存榜单博客
    String HOT_BLOG="HOT-BLOG";
    //缓存随机标签
    String RANDOM_TAG="RANDOM-TAG-SET";
    //缓存随机博客
    String RANDOM_BLOG="RANDOM-BLOG";
    //缓存分类列表
    String CATEGORY_LIST="CATEGORY-LIST";
    //缓存博客信息配置
    String BLOG_CONFIG="BLOG-CONFIG";
    //缓存博客专题信息
    String TOPIC_MAP="TOPIC-MAP";
    //缓存博客标签信息
    String TAG_MAP="TAG-MAP";
    //存取用户点赞
    String IP_LIKE="IP-LIKE:";
    //用户搜索关键字的数量
    String SEARCH_COUNT="SEARCH-COUNT";
    //存取用户评论的点赞ID
    String USER_LIKE_COMMENT="USER-LIKE-COMMENT:%s:%s";
    //存取用户评论的点赞数量
    String USER_LIKE_COMMENT_COUNT="USER-LIKE-COMMENT-COUNT";
    //保存用户编辑的博客
    String SAVE_EDIT_BLOG="USER-EDIT-BLOG-MAP";
    //保存用户预览的博客
    String SAVE_PREVIEW_BLOG="PREVIEW-BLOG:";
}
