package com.zsy.admin.vos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zsy.admin.utils.GlobalUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/4 17:28
 * @desc
 */
@Data
@NoArgsConstructor
public class TopicVo {

    private Integer id;

    private String name;

    private String description;

    private String cover;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private SimpleUserVo user;

    public TopicVo(Integer id, String name, String description, String cover,Integer userId,String nickName,Date createAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cover = cover;
        this.user=new SimpleUserVo(userId,nickName);
        this.dateStr= GlobalUtils.dateToStr(createAt);
    }

    public TopicVo(Integer id, String name, String description, String cover) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cover = cover;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String dateStr;
}
