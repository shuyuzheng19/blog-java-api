package com.zsy.admin.vos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/7 22:24
 * @desc
 */
@Data
public class CommentUserVo {
    private String username;
    private String avatar;
    private Integer level=0;
    private String homeLink;
    @JsonInclude
    private Set<Integer> likeIds;
}
