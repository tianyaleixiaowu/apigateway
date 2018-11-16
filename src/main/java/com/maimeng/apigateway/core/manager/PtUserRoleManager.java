package com.maimeng.apigateway.core.manager;

import com.maimeng.apigateway.core.model.PtUserRole;
import com.maimeng.apigateway.core.repository.PtUserRoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wuweifeng wrote on 2017/10/31.
 */
@Service
public class PtUserRoleManager {
    @Resource
    private PtUserRoleRepository ptUserRoleRepository;


    /**
     * 根据roleId查询
     *
     * @param roleId roleId
     * @return 集合
     */
    public List<PtUserRole> findByRoleId(Long roleId) {
        return ptUserRoleRepository.findByRoleId(roleId);
    }


    /**
     * 根据userId查找角色
     *
     * @param userId userId
     * @return List
     */
    public List<PtUserRole> findByUserId(Long userId) {
        return ptUserRoleRepository.findByUserId(userId);
    }
}
