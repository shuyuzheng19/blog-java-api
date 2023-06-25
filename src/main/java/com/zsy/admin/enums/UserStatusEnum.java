package com.zsy.admin.enums;

/**
 * @author 郑书宇
 * @create 2023/6/3 22:22
 * @desc
 */

public enum UserStatusEnum {
    ACTIVE, //用户账号激活 表示该账号正常
    DISABLED,//账号已被封禁
    LOCK,//用户频繁输错密码或其他原因进行封禁账号
}
