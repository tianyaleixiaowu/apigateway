package com.maimeng.apigateway.core.manager;

import com.maimeng.apigateway.config.cache.UserRoleCache;
import com.maimeng.apigateway.core.model.PtRole;
import com.maimeng.apigateway.core.model.PtUser;
import com.maimeng.apigateway.core.model.PtUserRole;
import com.maimeng.apigateway.core.repository.PtRoleRepository;
import com.maimeng.apigateway.core.repository.PtUserRoleRepository;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuweifeng wrote on 2017/10/26.
 */
@Service
public class PtRoleManager {
    @Resource
    private PtRoleRepository ptRoleRepository;
    @Resource
    private PtUserRoleRepository ptUserRoleRepository;
    @Resource
    private UserRoleCache userRoleCache;

    /**
     * 查询用户的所有role
     *
     * @param userId
     *         userId
     * @return 集合
     */
    private List<PtRole> findByUserId(Long userId) {
        List<PtUserRole> userRoles = ptUserRoleRepository.findByUserId(userId);
        return userRoles.stream().map(userRole -> findByRoleId(userRole.getRoleId())).collect(Collectors
                .toList());
    }

    public PtRole findByRoleId(Long roleId) {
        return ptRoleRepository.getOne(roleId);
    }

    /**
     * 获取用户的roles
     *
     * @param user
     *         user
     * @return 角色集合
     */
    public List<PtRole> findRolesByUser(PtUser user) {
        return findRolesByUser(user.getId());
    }

    /**
     * 获取用户的roles
     *
     * @param userId
     *         userId
     * @return 角色集合
     */
    public List<PtRole> findRolesByUser(Long userId) {
        //从缓存获取
        List<PtRole> roles = userRoleCache.findRolesByUserId(userId);
        if (CollectionUtil.isEmpty(roles)) {
            roles = findByUserId(userId);
            //放入缓存
            userRoleCache.saveUserRolesByUser(userId, roles);
        }
        return roles;
    }

    public String getRoleStrByUser(PtUser user) {
        List<String> roleList = findRolesByUser(user).stream().map(PtRole::getName).collect(Collectors.toList());
        String roleStr = "";
        for (String s : roleList) {
            roleStr += s + ",";
        }
        return roleStr;
    }

}
