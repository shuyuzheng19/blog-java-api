package com.zsy.admin.vos;

import com.zsy.admin.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 郑书宇
 * @create 2023/6/4 17:18
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private Integer id;

    private String nickName;

    private String icon;

    private UserStatusEnum status;

    private String role;
}
