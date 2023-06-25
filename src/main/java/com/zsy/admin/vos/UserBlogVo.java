package com.zsy.admin.vos;

import com.zsy.admin.utils.GlobalUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/4 19:52
 * @desc
 */
@Data
@NoArgsConstructor
public class UserBlogVo {

    private Integer id;

    private String title;

    private String desc;

    private String coverImage;

    private Integer topicId;

    public UserBlogVo(Integer id, String title, String description, String coverImage, Date createAt,
                      Integer categoryId, String categoryName,
                      Integer userId, String nickName,Integer topicId) {
        this.id = id;
        this.title = title;
        this.desc = description;
        this.coverImage = coverImage;
        this.dateStr = GlobalUtils.dateToStr(createAt);
        this.category=new CategoryVo(categoryId,categoryName);
        this.user=new SimpleUserVo(userId,nickName);
        this.topicId=topicId;
    }

    private String dateStr;

    private SimpleUserVo user;

    private CategoryVo category;

}
