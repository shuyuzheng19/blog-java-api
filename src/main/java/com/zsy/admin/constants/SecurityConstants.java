package com.zsy.admin.constants;

import com.zsy.admin.utils.GlobalUtils;

import java.util.List;

/**
 * @author 郑书宇
 * @create 2023/6/4 0:01
 * @desc
 */
public interface SecurityConstants {

    List<String> WHITE_URL=List.of(
        GlobalUtils.ofApiUrl("user","/login"),
        GlobalUtils.ofApiUrl("user","/registered"),
        GlobalUtils.ofApiUrl("user","/send_mail"),
        GlobalUtils.ofApiUrl("user","/captcha"),
        GlobalUtils.ofApiUrl("*","/public/**")
    );

}
