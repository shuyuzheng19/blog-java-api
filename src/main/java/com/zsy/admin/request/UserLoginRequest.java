package com.zsy.admin.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/4 13:12
 * @desc
 */
@Data
public class UserLoginRequest {

    @NotEmpty(message = "账号不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "验证码不能为空")
    private String code;

}
