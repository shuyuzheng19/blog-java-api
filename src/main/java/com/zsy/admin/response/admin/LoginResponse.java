package com.zsy.admin.response.admin;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/9 11:25
 * @desc
 */
@Data @Builder
public class LoginResponse {
    private String account;
    private Integer uid;
    private String avatar;
    private Date lastLoginTime;
}
