package com.maimeng.apigateway.config.cache;

import com.maimeng.apigateway.model.PtRole;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author wuweifeng wrote on 2017/10/27.
 * 用户角色信息缓存
 */
@Component
public class UserRoleCache {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 用户角色
     */
    String CACHE_USER_ROLE_KEY = "user_role_key";
    /**
     * 权限保存10个小时，redis存储的user权限
     */
    int CACHE_USER_ROLE_EXPIE = 10;

    /**
     * 根据userId获取缓存的角色
     *
     * @param userId
     *         userId
     * @return 角色集合
     */
    public List<PtRole> findRolesByUserId(Long userId) {
        Object object = stringRedisTemplate.opsForValue().get(roleKeyOfUserId(userId));
        JSONArray jsonArray = JSONUtil.parseArray(object.toString());
        return jsonArray.stream().map(json -> JSONUtil.toBean((JSONObject) json, PtRole.class)).collect(Collectors
                .toList());
    }

    /**
     * 缓存用户的所有role
     *
     * @param userId
     *         userId
     * @param roles
     *         角色集合
     */
    public void saveUserRolesByUser(Long userId, List<PtRole> roles) {
        if (CollectionUtil.isEmpty(roles)) {
            return;
        }
        stringRedisTemplate.opsForValue().set(roleKeyOfUserId(userId), JSONUtil.toJsonStr
                        (roles),
                CACHE_USER_ROLE_EXPIE, TimeUnit.HOURS);
    }

    private void remove(Long userId) {
        stringRedisTemplate.delete(roleKeyOfUserId(userId));
    }

    private String roleKeyOfUserId(Long userId) {
        return CACHE_USER_ROLE_KEY + "_" + userId;
    }
}
