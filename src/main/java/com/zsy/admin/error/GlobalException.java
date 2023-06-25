package com.zsy.admin.error;

import lombok.Builder;
import lombok.Getter;

/**
 * @author 郑书宇
 * @create 2023/5/4 14:24
 * @desc
 */
@Builder @Getter
public class GlobalException extends RuntimeException{

    private ResultCode code;

    private String message;

    private String url;

}
