package com.maimeng.apigateway.model;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author wuweifeng wrote on 2017/10/25.
 * 角色表
 */
@Entity
@Table(name = "pt_role")
public class PtRole extends BaseEntity {

    /**
     * 角色名（admin，level1，level2，level3）
     */
    private String name;
    /**
     * 角色描述（超级管理员，1级客户，2级客户，3级客户）
     */
    private String sign;
    /**
     * 公司id（为0时代表是超级管理员设置的角色，其他的都是各公司自己设置的角色）
     */
    private Long companyId;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PtRole{" +
                "name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                ", companyId=" + companyId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PtRole ptRole = (PtRole) o;

        if (name != null ? !name.equals(ptRole.name) : ptRole.name != null) {
            return false;
        }
        if (sign != null ? !sign.equals(ptRole.sign) : ptRole.sign != null) {
            return false;
        }
        return companyId != null ? companyId.equals(ptRole.companyId) : ptRole.companyId == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        return result;
    }
}
