package com.zsy.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zsy.admin.error.ResultCode;
import lombok.Builder;
import lombok.Data;

/**
 * @author 郑书宇
 * @create 2023/6/4 1:40
 * @desc
 */
@Data
@Builder
public class Result {
    private int code;

    private String message;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Object data;

    public static Result success(){
        return Result.builder()
                .code(ResultCode.SUCCESS_CODE.getCode())
                .message("成功").build();
    }

    public static Result success(Object data){
        return Result.builder()
                .code(ResultCode.SUCCESS_CODE.getCode())
                .message("成功")
                .data(data)
                .build();
    }

    public static Result fail(){
        return Result.builder()
                .code(ResultCode.FAIL_CODE.getCode())
                .message("失败").build();
    }

    public static Result fail(String message){
        return Result.builder()
                .code(ResultCode.FAIL_CODE.getCode())
                .message(message).build();
    }

    public static Result fail(int code,String message){
        return Result.builder()
                .code(code)
                .message(message)
                .build();
    }
}
