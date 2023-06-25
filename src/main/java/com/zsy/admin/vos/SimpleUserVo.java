package com.zsy.admin.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 郑书宇
 * @create 2023/6/5 11:55
 * @desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserVo {
    private Integer id;

    private String nickName;
}
