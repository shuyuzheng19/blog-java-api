package com.zsy.admin.enums;

/**
 * @author 郑书宇
 * @create 2023/6/4 22:19
 * @desc
 */
public enum BlogSortEnum {
    CREATE("createAt"),
    UPDATE("updateAt"),
    EYE("eyeCount"),
    LIKE("likeCount"),
    BACK("createAt")
    ;

    private String field;

    BlogSortEnum(String field){
        this.field=field;
    }

    public String getField() {
        return field;
    }
}
