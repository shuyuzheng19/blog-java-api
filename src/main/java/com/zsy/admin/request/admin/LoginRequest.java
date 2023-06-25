package com.zsy.admin.request.admin;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/9 10:55
 * @desc
 */
@Data
public class LoginRequest {
    @NotEmpty(message = "账户不能为空")
    private String account;

    @NotEmpty(message = "密码不能为空")
    private String password;
}
