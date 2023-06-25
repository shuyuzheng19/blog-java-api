package com.zsy.admin.request;

import com.zsy.admin.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author 郑书宇
 * @create 2023/6/4 12:04
 * @desc
 */
@Data
public class UserRegisteredRequest {
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 8,max = 16,message = "账号长度不能小于8个并且不能大于16个")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 8,max = 16,message = "密码长度不能小于8个并且不能大于16个")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;

    @NotEmpty(message = "昵称不能为空")
    @Length(max = 20,message = "用户名称不能大于20个字符")
    private String nickName;

    @Pattern(regexp = "^https?://.*\\.(png|jpe?g|gif|svg|ico)$",message = "这不是一个正确图片链接")
    private String icon;

    @NotEmpty(message = "验证码不能为空")
    private String code;

    public User toUser(){
      return User.of(username,password,icon,nickName,email);
    }
}
