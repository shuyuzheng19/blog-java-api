package com.zsy.admin.constants;

import com.zsy.admin.entity.Role;
import com.zsy.admin.enums.PermissionEnum;
import com.zsy.admin.enums.RoleEnum;
import java.util.List;
import java.util.Set;

/**
 * @author 郑书宇
 * @create 2023/6/3 22:46
 * @desc
 */
public interface UserConstants {

    Role DEFAULT_ROLE=Role.of(RoleEnum.USER.getId());

    List<Role> DEFAULT_ROLES= List.of(
        Role.of(RoleEnum.USER.getId(), "USER","普通用户"),
        Role.of(RoleEnum.ADMIN.getId(),"ADMIN","管理员"),
        Role.of(RoleEnum.SUPER_ADMIN.getId(),"SUPER_ADMIN","超级管理员")
    );
}
