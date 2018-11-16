package com.maimeng.apigateway.config.cache;

import com.maimeng.apigateway.core.model.PtMenu;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.maimeng.apigateway.common.Constant.CACHE_ROLE_MENU_KEY;


/**
 * @author wuweifeng wrote on 2017/11/3.
 * 角色和menu对应关系的缓存
 */
@Component
public class RoleMenuCache {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据角色查询menu集合的缓存
     *
     * @param roleId
     *         roleId
     * @return 菜单集合
     */
    public List<PtMenu> findMenuByRoleId(Long roleId) {
        Object object = stringRedisTemplate.opsForValue().get(keyOfRole(roleId));
        if (object == null) {
            return null;
        }
        JSONArray jsonArray = JSONUtil.parseArray(object.toString());
        return jsonArray.stream().map(json -> JSONUtil.toBean((JSONObject) json, PtMenu.class)).collect(Collectors
                .toList());
    }

    /**
     * 缓存role的menu信息
     *
     * @param roleId
     *         roleId
     * @param menus
     *         菜单集合
     */
    public void saveMenusByRoleId(Long roleId, List<PtMenu> menus) {
        if (CollectionUtil.isEmpty(menus)) {
            return;
        }
        stringRedisTemplate.opsForValue().set(keyOfRole(roleId), JSONUtil.toJsonStr(menus));
    }

    private String keyOfRole(Long roleId) {
        return CACHE_ROLE_MENU_KEY + "_" + roleId;
    }
}
