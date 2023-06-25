package com.zsy.admin.error;

/**
 * @author 郑书宇
 * @create 2023/5/4 14:26
 * @desc
 */
public enum ResultCode {
    SUCCESS_CODE(200),
    FAIL_CODE(10001),
    PARAMS_CODE(10002),
    NOT_FOUNT_CODE(10003),
    ERROR_CODE(500)
    ;

    private int code;

    ResultCode(int code){
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
