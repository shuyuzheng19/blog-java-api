package com.zsy.admin.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/8 18:11
 * @desc
 */
@Data
public class ContactMeRequest {

    @NotEmpty(message = "请输入你的名字")
    private String name;

    @NotEmpty(message = "请输入你的邮箱")
    @Email(message = "这不是一个正确的邮箱格式")
    private String email;

    @NotEmpty(message = "请输入你的主题")
    private String subject;

    @NotEmpty(message = "请输入你的留言")
    private String content;

}
