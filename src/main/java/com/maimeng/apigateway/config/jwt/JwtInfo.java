package com.maimeng.apigateway.config.jwt;

/**
 * Created by ace on 2017/9/10.
 */
public class JwtInfo {
    private String userId;
    private String name;
    private Long createTime;

    public JwtInfo(String userId, String name, Long createTime) {
        this.userId = userId;
        this.name = name;
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "JwtInfo{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
