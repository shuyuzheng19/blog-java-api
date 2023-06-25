package com.zsy.admin.enums;

/**
 * @author 郑书宇
 * @create 2023/6/3 23:15
 * @desc
 */
public enum PermissionEnum {
    SELECT(1),CREATE(2),UPDATE(3),DELETE(4);
    private int id;
    PermissionEnum(int id){
        this.id=id;
    }
    public int getId() {
        return id;
    }
}
