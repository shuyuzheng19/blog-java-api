package com.zsy.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 郑书宇
 * @create 2023/6/8 11:39
 * @desc
 */
@Getter
@AllArgsConstructor
public enum UploadTypeEnum {
    AVATAR("avatar"),IMAGES("images"),FILES("files");
    private String type;
}
