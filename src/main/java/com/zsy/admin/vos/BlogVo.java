package com.zsy.admin.vos;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class BlogVo {

    private Integer id;

    private String title;

    private String desc;

    private String coverImage;

    public BlogVo(Integer id, String title, String description, String coverImage, Date createAt,
                  Integer categoryId, String categoryName,
                  Integer userId, String nickName) {
        this.id = id;
        this.title = title;
        this.desc = description;
        this.coverImage = coverImage;
        this.dateStr = GlobalUtils.dateToStr(createAt);
        this.category=new CategoryVo(categoryId,categoryName);
        this.user=new SimpleUserVo(userId,nickName);
    }

    public BlogVo(Integer id, String title, String description, String coverImage, Date createAt,
                  Integer userId, String nickName) {
        this.id = id;
        this.title = title;
        this.desc = description;
        this.coverImage = coverImage;
        this.dateStr = GlobalUtils.dateToStr(createAt);
        this.user=new SimpleUserVo(userId,nickName);
    }

    private String dateStr;

    private SimpleUserVo user;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private CategoryVo category;

}
