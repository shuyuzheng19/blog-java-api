package com.zsy.admin.enums;

/**
 * @author 郑书宇
 * @create 2023/6/3 23:15
 * @desc
 */
public enum RoleEnum {
    USER(1),ADMIN(2),SUPER_ADMIN(3);
    private int id;
    RoleEnum(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }
}
