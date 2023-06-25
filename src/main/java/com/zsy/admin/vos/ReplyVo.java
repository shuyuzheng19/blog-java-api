package com.zsy.admin.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/8 15:07
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyVo {
    private Integer total;
    private List<CommentVo> list;
}
