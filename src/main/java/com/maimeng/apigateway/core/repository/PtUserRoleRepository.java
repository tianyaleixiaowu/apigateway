package com.maimeng.apigateway.core.repository;

import com.maimeng.apigateway.core.model.PtUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuweifeng wrote on 2017/10/26.
 */
public interface PtUserRoleRepository extends JpaRepository<PtUserRole, Long> {
    /**
     * 查询某用户的所有role
     *
     * @param userId
     *         userId
     * @return 集合
     */
    List<PtUserRole> findByUserId(Long userId);

    PtUserRole findByUserIdAndRoleId(Long userId, Long roleId);

    /**
     * 根据roleId查询
     *
     * @param roleId
     *         roleId
     * @return 集合
     */
    List<PtUserRole> findByRoleId(Long roleId);

    /**
     * 删除某个用户的role
     *
     * @param userId
     *         userId
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from PtUserRole where userId = ?1")
    void deleteByUserId(Long userId);
}
