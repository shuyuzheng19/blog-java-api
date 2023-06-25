package com.zsy.admin.vos.admin;

import com.zsy.admin.vos.CategoryVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/9 18:55
 * @desc
 */
@Data
@NoArgsConstructor
public class BlogVoA {

    private int id;
    private String title;
    private String description;
    private String coverImage;
    private Long eyeCount;
    private Long likeCount;
    private CategoryVo category;
    private boolean markdown;
    private Date createAt;
    private Date updateAt;

    public BlogVoA(int id, String title, String description, String coverImage, Long eyeCount, Long likeCount, boolean markdown, Date createAt, Date updateAt, Integer categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.eyeCount = eyeCount;
        this.likeCount = likeCount;
        this.category = new CategoryVo(categoryId,categoryName);
        this.markdown = markdown;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
