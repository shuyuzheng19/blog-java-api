package com.zsy.admin.vos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SimpleBlogVo {

    private Integer id;

    private String title;

    public SimpleBlogVo(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String coverImage;

}
