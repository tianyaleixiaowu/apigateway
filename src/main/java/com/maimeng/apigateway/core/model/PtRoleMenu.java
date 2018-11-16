package com.maimeng.apigateway.core.model;

import javax.persistence.Entity;

/**
 * @author wuweifeng wrote on 2017/10/25.
 * 菜单+角色表
 */
@Entity
public class PtRoleMenu extends BaseEntity {
    private Long menuId;
    private Long roleId;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
