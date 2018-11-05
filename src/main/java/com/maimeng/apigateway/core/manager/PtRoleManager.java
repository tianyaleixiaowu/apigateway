package com.maimeng.apigateway.manager;

import com.maimeng.apigateway.config.cache.UserRoleCache;
import com.maimeng.apigateway.model.PtRole;
import com.maimeng.apigateway.model.PtUser;
import com.maimeng.apigateway.model.PtUserRole;
import com.maimeng.apigateway.repository.PtRoleRepository;
import com.maimeng.apigateway.repository.PtUserRoleRepository;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.joda.time.DateTime;
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
    public List<PtRole> findByUserId(Long userId) {
        List<PtUserRole> userRoles = ptUserRoleRepository.findByUserId(userId);
        return userRoles.stream().map(userRole -> findByRoleId(userRole.getRoleId())).collect(Collectors
                .toList());
    }

    public String findManagerRoleName(Long userId) {
        return findByUserId(userId).get(0).getName();
    }

    public Long findManagerRoleId(Long userId) {
        return findByUserId(userId).get(0).getId();
    }

    public PtRole findByRoleId(Long roleId) {
        return ptRoleRepository.getOne(roleId);
    }

    /**
     * 添加一个role
     *
     * @param ptRole
     *         ptRole
     * @return ptRole
     */
    public PtRole add(PtRole ptRole) {
        ptRole.setCreateTime(DateTime.now().toDate());
        ptRole.setUpdateTime(DateTime.now().toDate());
        return ptRoleRepository.save(ptRole);
    }

    public PtRole update(PtRole ptRole) {
        ptRole.setUpdateTime(DateTime.now().toDate());
        return ptRoleRepository.save(ptRole);
    }


    /**
     * 获取用户的roles
     *
     * @param user
     *         user
     * @return 角色集合
     */
    public List<PtRole> findRolesByUser(PtUser user) {
        //从缓存获取
        List<PtRole> roles = userRoleCache.findRolesByUserId(user.getId());
        if (CollectionUtil.isEmpty(roles)) {
            roles = findByUserId(user.getId());
            //放入缓存
            userRoleCache.saveUserRolesByUser(user.getId(), roles);
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
